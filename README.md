PubSubMessenger
=====

`PubSubMessenger` simplify communications from ViewModel/Fragment to Activity/Fragment.

## Usage
### Message
A message needs to implement `PubSubMessage`.
```kotlin
data class SampleMessage(val param1: String, val param2: String): PubSubMessage
```

### In ViewModel or Fragment, publish the message
You can call `publishMessage` in ViewModel or Fragment.
```kotlin
publishMessage(SampleMessage("Please, open next screen!", "Please, receive params!"))
```

### In Activity or Fragment, subscribe the message
You can call `subscribeMessage` in Activity or Fragment.
```kotlin
subscribeMessage<SampleMessage> { message ->
    Log.d(TAG, "Receive $message")
}
```

## Notice
− `PubSubMessenger` uses `launchWhenStarted`, therefore `lifecycle.currentState` is least at STARTED.
− `PubSubMessenger` dose not survive Activity's recreation by process.

## Gradle

[![](https://jitpack.io/v/wada811/PubSubMessenger.svg)](https://jitpack.io/#wada811/PubSubMessenger)

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.wada811:PubSubMessenger:x.y.z'
}
```

## License

Copyright (C) 2020 wada811

Licensed under the Apache License, Version 2.0
