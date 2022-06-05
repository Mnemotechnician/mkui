plugins {
	kotlin("jvm") version "1.6.10"
	id("org.jetbrains.dokka") version "1.6.10"

	`java-library` //todo: why is this added
	`maven-publish`
}

repositories {
	mavenCentral()
	maven("https://www.jitpack.io")
}

val mindustryVersion = "master-SNAPSHOT"

dependencies {
	compileOnly("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	compileOnly("com.github.Anuken.Mindustry:core:$mindustryVersion")

	compileOnly(kotlin("stdlib-jdk8"))
}

tasks.withType<Jar> {
	archiveFileName.set("MKUI.jar")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "com.github.mnemotechnician"
			artifactId = "mkui"
			version = "1.0"

			from(components["java"])
		}
	}
}

