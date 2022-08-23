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

val mindustryVersion = "v135"

dependencies {
	compileOnly(kotlin("stdlib-jdk8"))
	compileOnly("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	compileOnly("com.github.Anuken.Mindustry:core:$mindustryVersion")

	testImplementation(kotlin("test"))
	testImplementation("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	testImplementation("com.github.Anuken.Mindustry:core:$mindustryVersion")
}

tasks.withType<Jar> {
	archiveFileName.set("MKUI.jar")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "com.github.mnemotechnician"
			artifactId = "mkui"
			version = "v1.2"

			from(components["java"])
		}
	}
}

tasks.test {
	useJUnitPlatform()
}
