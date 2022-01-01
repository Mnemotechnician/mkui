# Mindustry Kotlin Ui Lib
An utility library for mindustry mods.

Provides a set of inline extension functions that allow you to build mindustry ui in an idiomatic & optimized way.
Also provides some utility extension functions that arc lacks.

May not be compatible with other (non-mindustry) arc projects.

***Note: this library will only work with mods written in kotlin!***

# Adding this dependency
In your `build.gradle` file:
* add `maven { url("https://jitpack.io") }` to the `repositories {}` block 
(it should already be here if you're using the official mod template)
* add `implementation "com.github.mnemotechnician:MKUI:TAG"` (replace `TAG` with the the latest tag in this repo) to the `dependencies {}` block

example of a `build.gradle` file:
```groovy
//... some other code

repositories {
	mavenCentral()
	maven { url("https://jitpack.io") }
}

dependencies {
	compileOnly "com.github.Anuken.Arc:arc-core:$mindustryVersion"   //these two lines should
	compileOnly "com.github.Anuken.Mindustry:core:$mindustryVersion" //already be here
	implementation "com.github.mnemotechnician:mkui:snapshot-3"
}

//... other code
```

## in case you're using kotlin dls:
Ignore this paragraph if you're using the official kotlin mod template
* use `maven("https://jitpack.io")` in the first step
* use `implementation("com.github.mnemotechnician:MKUI:TAG")` in the second step