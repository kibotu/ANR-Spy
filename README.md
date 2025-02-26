### Android ANR Spy [![Maven Central Version](https://img.shields.io/maven-central/v/net.kibotu/ANR-Spy)](https://central.sonatype.com/artifact/net.kibotu/ANR-Spy) [![](https://jitpack.io/v/kibotu/ANR-Spy.svg)](https://jitpack.io/#kibotu/ANR-Spy) [![Android CI](https://github.com/kibotu/ANR-Spy/actions/workflows/android.yml/badge.svg)](https://github.com/kibotu/ANR-Spy/actions/workflows/android.yml) [![API](https://img.shields.io/badge/Min%20API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21) [![API](https://img.shields.io/badge/Target%20API-35%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=35) [![API](https://img.shields.io/badge/Java-17-brightgreen.svg?style=flat)](https://www.oracle.com/java/technologies/javase/17all-relnotes.html) [![Gradle Version](https://img.shields.io/badge/gradle-8.12-green.svg)](https://docs.gradle.org/current/release-notes) [![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-green.svg)](https://kotlinlang.org/)
Android ANR Spy is the most simplest library that helps android developers to detect ANRs.

### What is Android ANR (Application Not Responding)
when a developer do most heavy jobs on UI thread (more than 5 seconds usually) and UI thread still receieve more request/events for doing a task then Android system raises ANR message. This is extremely bad effect on your app and may lead to the failure of your business.
### Android ANR does matter
Google recommends/suggests your app on play store. If your app raises too many ANRs then your app will be ranked down

### Android ANR Durations
1. Normal on UI Thread in any activity = 5 secs
2. BroadCast = 10 sec
3. Service = 20 sec

## Android ANR Spy Library

# How to install

## MavenCentral

```groovy 
allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation 'net.kibotu:ANR-Spy:{latest-version}'
}

```

## Jitpack

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.kibotu:ANR-Spy:{latest-version}'
}
```

## Usage

### Start ANR Detector, e.g. in `MainActivity.onCreate`
```kotlin
startSpying {
    shouldThrowException = true // default false
    timeout = 5000L // time limit to detect ANR
    onWait {
        //Total blocking time of main thread. 
        //Can be used for doing any action e.g. if blocked time is more than 5 seconds then 
        //restart the app to avoid raising ANR message because it will lead to down rank your app.
        Log.e(TAG, "Waited: $it")
    }
    onAnrDetected {
        // Is triggered when ANR is detected
        Log.e(TAG, "$it")
    }
}
```

### License
<pre>
Copyright 2023 Jan Rabe

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>