plugins {
	id("org.jetbrains.kotlin.jvm") version "1.6.0"

	`java-library` //todo: why is this added
	`maven-publish`
}

repositories {
	mavenCentral()
	maven("https://www.jitpack.io")
}

val mindustryVersion = "v135"

dependencies {
	compileOnly("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	compileOnly("com.github.Anuken.Mindustry:core:$mindustryVersion")

	compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))
	compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	testImplementation("org.jetbrains.kotlin:kotlin-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
	testImplementation("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	testImplementation("com.github.Anuken.Mindustry:core:$mindustryVersion")
}

tasks.withType<Jar> {
	archiveFileName.set("MKUI.jar")
	
	//from(configurations.runtimeClasspath.map { if (it.isDirectory) it else zipTree(it) })
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "com.github.mnemotechnician"
			artifactId = "mkui"
			version = "0.1"

			from(components["java"])
		}
	}
}

