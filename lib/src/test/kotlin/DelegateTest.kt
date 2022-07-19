package com.github.mnemotechnician.mkui

import arc.Core
import com.github.mnemotechnician.mkui.delegates.bundle
import com.github.mnemotechnician.mkui.delegates.dynamicBundle
import kotlin.test.*

class DelegateTest {
	val testPrefix = "amogus"

	@Test
	fun `bundle delegates return the right values`() {
		val expected = "Hello, World!"

		Core.bundle.properties.apply {
			put("$testPrefix.hello-world", expected)
			put("$testPrefix.hello-world-subs", "$1, $2!")
			put("$testPrefix.hello-world-lazy-subs", "$1, $2!")
		}

		// normal
		val helloWorld by bundle(testPrefix)
		assertEquals(expected, helloWorld)
		assertEquals(expected, helloWorld, "Caching error") // correct caching test

		// dynamic
		val helloWorldSubs by dynamicBundle(testPrefix, { "Hello" }, { "World" })
		assertEquals(expected, helloWorldSubs)
		assertEquals(expected, helloWorldSubs, "Caching error")

		// lazy dynamic
		var doReturn = false
		val helloWorldLazySubs by dynamicBundle({ testPrefix.takeIf { doReturn } }, { "Hello" }, { "World" })
		assertEquals(helloWorldLazySubs, "<uninitialised: helloWorldLazySubs>")
		doReturn = true
		assertEquals(helloWorldLazySubs, expected)

		// non-existent
		val definitelyNonExistentBundleEntry by bundle("$testPrefix-")
		assertEquals(definitelyNonExistentBundleEntry, "<invalid: $testPrefix-definitelyNonExistentBundleEntry>")
	}
}
