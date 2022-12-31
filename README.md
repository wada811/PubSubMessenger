PubSubMessenger
===============

`PubSubMessenger` is a Pub/Sub pattern library from ViewModel/Fragment to Activity/Fragment.

![PubSubMessenger](https://github.com/wada811/PubSubMessenger/blob/master/docs/PubSubMessenger-flow-chart.png)

## Overview
- `PubSubMessenger` is a Pub/Sub pattern library.
  - Messaging from ViewModel to Activity/Fragment. For example, Navigation, Dialog, SnackBar and so on.
  - Messaging from Fragment to Activity/Fragment. For example, Fragment callback, DialogFragment callback.
- `PubSubMessenger` is Lifecycle-aware like LiveData.
  - You can subscribe to messages at least Lifecycle.State.STARTED.
  - If you publish a message when lifecycle isn't at least STARTED, the message is suspend until at least STARTED.
  - A message consumed automatically when you observed unlike LiveData, [SingleLiveEvent and LiveData\<Event>](https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150).
  - You can subscribe to messages multiple times unlike LiveData and SingleLiveEvent.
  - You cannot use to communication with another Activity. There is risk of losing the message because of killing Activity by OS.
- `PubSubMessenger` restricts where you publish and where you subscribe for preventing chaos by like EventBus.
  - You can publish in ViewModel or Fragment.
  - You can subscribe in Activity or Fragment.

## Usage
### Define a message
The message needs to implement `PubSubMessage`.
```kotlin
class SampleMessage(val param: String): PubSubMessage
```

### Publish a message
You can call `pubSubMessenger.publish(message)` in ViewModel or Fragment.
```kotlin
pubSubMessenger.publish(SampleMessage("You can pass parameters via the message"))
```

### Subscribe to messages
You can call `pubSubMessenger.subscribe<PubSubMessage> { }` in Activity or Fragment.
```kotlin
pubSubMessenger.subscribe<SampleMessage> { message ->
    Log.d(TAG, "Receive $message")
}
```

## Gradle

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.wada811.pubsubmessenger/pubsubmessenger/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.wada811.pubsubmessenger/pubsubmessenger)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'com.wada811.pubsubmessenger:pubsubmessenger:x.y.z'
}
```

## License

Copyright (C) 2020 wada811

Licensed under the Apache License, Version 2.0
