import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.8.0"
	id("org.jetbrains.dokka") version "1.7.20"

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

	testImplementation(kotlin("test"))
	testImplementation("com.github.Anuken.Arc:arc-core:$mindustryVersion")
	testImplementation("com.github.Anuken.Mindustry:core:$mindustryVersion")
}

tasks.jar {
	archiveFileName.set("MKUI.jar")
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<JavaCompile> {
	sourceCompatibility = "1.8"
	targetCompatibility = "1.8"
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
		freeCompilerArgs += arrayOf(
			"-Xcontext-receivers"
		)
	}
}

val javadocJar by tasks.creating(Jar::class) {
	from(tasks.dokkaJavadoc.get().outputs)
	archiveClassifier.set("javadoc")
}

val sourceJar by tasks.creating(Jar::class) {
	from(sourceSets["main"].allSource)
	archiveClassifier.set("sources")
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "com.github.mnemotechnician"
			artifactId = "mkui"
			version = "v1.2.2"

			from(components["java"])
			artifact(javadocJar)
			artifact(sourceJar)
		}
	}
}
