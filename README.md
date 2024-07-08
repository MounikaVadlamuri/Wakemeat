# Wake me @ Project
This Android mobile app rings alarms based on the user's current location.

## Features
- Rings alarms based on the user's current location
- [Insert other features of your app]

## Best Practices Used
### Layouting
Used spacing_**** dimensions for layouting, margins, and paddings instead of hard-coded values for a consistent look-and-feel and easier style and layout changes.
### Strings
Named strings with keys that resemble namespaces to bring context and avoid ambiguity.
Avoided writing string values in all uppercase and instead used normal text conventions (e.g., capitalizing the first character).
Used the textAllCaps attribute on a TextView to display strings in all caps if needed.

### View Hierarchy
Avoided a deep hierarchy of views by using a flat layout structure.

### Colors and Styles
Defined a color palette in colors.xml and referenced it in styles.xml to reflect color usage.
Separated underlying colors and style usage by defining an additional color resource file that references the color palette.
Used styles to define the color and usage properties of views.

### Dimensions
Treated dimens.xml like colors.xml by defining a "palette" of typical spacing and font sizes.
Used dimens.xml to define font sizes, typical spacing between views, and typical sizes of views.

###  Gradle
Followed Gradle's documentation for building and signing the app.
Defined signingConfigs for the release build in the app's build.gradle file.

## Getting Started
[Insert instructions on how to build and run the app]

## License
This is an academic project only, but all rights are reserved by the code owners. The code present here cannot be used without approval its developers.