# Satin Forge

A lightweight Forge library for OpenGL shader usage.

---

## Adding Satin to your project

You can add the library by inserting the following in your `build.gradle`:

```gradle
repositories {
     maven {
        name = 'ConstructLegacy'
        url = 'https://repo.constructlegacy.ru/public'
        content {
            includeGroup 'com.github.dima_dencep.mods'
        }
    }
}

dependencies {
    modImplementation "com.github.dima_dencep.mods:satin-forge:${satin_version}"
    // Include Satin as a Jar-in-Jar dependency (optional)
    include "com.github.dima_dencep.mods:satin-forge:${satin_version}"
}
```

You can then add the library version to your `gradle.properties`file:

```properties
# Satin library
satin_version = 1.x.y
```

You can find the current version of Satin in the [releases](https://github.com/dima-dencep/Satin-Forge/releases) tab of the repository on Github.

## Full documentation
This [repository's wiki](https://github.com/Ladysnake/Satin/wiki) provides documentation to write and use shaders with Satin API.
