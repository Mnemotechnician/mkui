# MaKe UI - mindustry ui library.
MK UI is a a library that provides many useful ui-related features for other mindustry mods.
This includes:
- DSL UI builders
- Dialog construction functions
- New extension functions and properties for some ui elements
- New delegates that allow you to easily interact with the settings/translation bundle
- New UI elements

You can view the documentation here: [mnemotechnician.github.io/mkui](https://mnemotechnician.github.io/mkui/)

# Note
Most of the features providen by this library are made exclusively for kotlin mods.

This library shouldn't be used by java mods as it requires the kotlin stdlib as a runtime dependency.

# Adding this dependency
In your `build.gradle` file:
* add `maven { url("https://jitpack.io") }` to the `repositories {}` block 
(it should already be here if you're using a mod template)
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
	implementation "com.github.mnemotechnician:mkui:v1.1"
}

//... other code
```

## Adding this dependency with kotlin gradle dsl
The steps are the same, but you must:

* use `maven("https://jitpack.io")` in the first step
* use `implementation("com.github.mnemotechnician", "mkui", "TAG")` in the second step

# Features

## DSL UI builders
The domain-specific-language provided by this library allows you
to easily and conveniently create complex and functional UI.
For example, the below code fragment creates a "hello world"
dialog with a single button that shows the text "yay" when clicked.

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
* Window + WindowManager (self-explanatory)
* TablePager (allows you to display content in tabs)
* ToggleButton
* MenuButton

## Delegates
There are two kinds of delegates MKUI provides: setting delegate and bundle delegate.

The former allows you to delegate a property to a setting, e.g.:
```kotlin
val myInterestingSetting by setting(0, "my-prefix")
```
This delegates to a setting with the name "my-prefix.my-interesting-setting".
Zero is the default value which is used if the setting is not found or its type is not `Int`.

Another example:
```kotlin
val anotherBoringSetting by setting(true, "my-prefix")
```
This one delegates to "my-prefix.my-boring-setting", has a boolean type and true as the default value.

---------------------

The latter allows you to delegate a property to a bundle, e.g.:
```kotlin
val myBundleEntry by bundle("my-prefix")
```
This delegates to a bundle with the name "my-prefix.my-bundle-entry".
