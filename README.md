# Mindustry Kotlin Ui Lib
A ui library for mindustry mods written in kotlin.

Provides some useful stuff, such as inline ui construction functions, utility functions, new ui classes, etc.

You can view the documentation here: [mnemotechnician.github.io/mkui](https://mnemotechnician.github.io/mkui/)

# Update: breaking change
MKUI has is no longer an experimental library.
Starting from MKUI 1.0, there are several major changes:
- The package structure was changed significantly.
- The versioning system has been changed. Its version will no longer be just a meaningless integer number.

# Note
Most of features provided by this library are supposed to be used in kotlin mods, as they have little to no usage in java (it lacks many features providen by kotlin)

If you (somewhy) want to use this library in a java mod, you will have to include the whole kotlin stdlib as an implementation dependency.

# Adding this dependency
In your `build.gradle` file:
* add `maven { url("https://jitpack.io") }` to the `repositories {}` block 
(it should already be here if you're using the official mod template)
* add `implementation "com.github.mnemotechnician:MKUI:TAG"` (replace `TAG` with the the latest tag in this repo) to the `dependencies {}` block

Note: you can go to the [releases tab](https://github.com/mnemotechnician/mkui/releases) and use the name of the latest release as the tag. 

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
	implementation "com.github.mnemotechnician:mkui:v1.0"
}

//... other code
```

## in case you're using kotlin dls:
Ignore this paragraph if you're using the official kotlin mod template
* use `maven("https://jitpack.io")` in the first step
* use `implementation("com.github.mnemotechnician:MKUI:TAG")` in the second step

# Features

## Simplier and more readable tree-styled ui construction
For example, with this short and easy code you can create a BaseDialog with a "hello world" label
and a button that shows an info dialog when clicked.

```kotlin
import com.github.mnemotechnician.mkui.extensions.dsl.*

createBaseDialog(addCloseButton = true) {
	addLabel("hello world!").row()
	
	addTable {
		textButton("Clean and readable!", wrap = false) { Vars.ui.showInfo("Yay!") }
	}
}.show()
```

## New ui classes
* Window and WindowManager
* TablePager
* More coming in future

## Inline functions instead of java lambdas
While having no significant performance impact (due to how inefficient element allocation is),
this approach has many advantages, such as enabling your code to return from a whole tree of
nested inline functions, reference anything outside the lambdas, etc.
