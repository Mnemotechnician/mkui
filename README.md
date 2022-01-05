# Mindustry Kotlin Ui Lib
An ui library for mindustry mods.

Provides some useful stuff, such as inline ui construction functions, utility functions, new ui classes, etc.

May not be compatible with other (non-mindustry) arc projects.

***Note: this library will only work with mods written in kotlin!***

Messy documentation: [mnemotechnician.github.io/mkui](https://mnemotechnician.github.io/mkui/)

# Adding this dependency
In your `build.gradle` file:
* add `maven { url("https://jitpack.io") }` to the `repositories {}` block 
(it should already be here if you're using the official mod template)
* add `implementation "com.github.mnemotechnician:MKUI:TAG"` (replace `TAG` with the the latest tag in this repo) to the `dependencies {}` block

Note: you can go to the releases tab and use the name of the latest release as the tag. These tags are usually just plain numbers.

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