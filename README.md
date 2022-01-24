# Mindustry Kotlin Ui Lib
An ui library for mindustry kotlin mods.

Provides some useful stuff, such as inline ui construction functions, utility functions, new ui classes, etc.

May not be compatible with other (non-mindustry) arc projects.

Messy documentation: [mnemotechnician.github.io/mkui](https://mnemotechnician.github.io/mkui/)

# Note
Most of features provided by this library are supposed to be used in kotlin mods, as they have little to no usage in java (it lacks many features providen by kotlin)

If you (somewhy) want to use this library in a java mod, you will have to include the whole kotlin stdlib as an implementation dependency.

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
	implementation "com.github.mnemotechnician:mkui:15"
}

//... other code
```

## in case you're using kotlin dls:
Ignore this paragraph if you're using the official kotlin mod template
* use `maven("https://jitpack.io")` in the first step
* use `implementation("com.github.mnemotechnician:MKUI:TAG")` in the second step

# Features

## Simplier and more readable tree-styled ui construction

```kotlin
cont.addTable {
	addLabel("hello world!")
	
	addTable {
		textButton("Clean and readable!", wrap = false) { Vars.ui.showInfo("Yay!") }
	}
}
```

## New ui classes
* Abstract class Window and a WindowManager
* TablePager
* More coming soon

## More optimized
The original ui construction functions use literal lambdas. That approach is slow and inefficient.

MKUI, on the other hand, uses inline functions with lambda arguments.
Unlike normal functions, they don't cause any performance impact as they and their arguments are inlined at the call place.