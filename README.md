[![Build Status](https://travis-ci.org/brunodles/IpGetter.svg?branch=release)](https://travis-ci.org/brunodles/IpGetter)
# IpGetter Plugin
The IpGetter is a plugin which will help us to get the Ip of the current machine to use on a build.gradle.

You may ask, why do I need that? The answer is simple, you need that to replace your *API URL* for your computer
address during development stage, to point to a MockApi or a *under development api*. That's the main idea.

# How to use

## BuildTypes, do you know it?

First you may need to know how to use [build types](https://developer.android.com/studio/build/build-variants.html).
This will help you to change the *API URL* depending on the *build type*.
There's no need to check it with a if like that `if (BuildConfig.DEBUG)`
or `String API_URL = BuildConfig.DEBUG? "192.168.10.42:3000" : "http://my-real-api.com"`.
Man, this is ugly, stop doing that.

## Plugin setup?

You can choose the wanted *network interface* you want to use to get the ip from.

To do that you need to create a new file in your *root project path*. `rootPath/ip.properties`.

Inside this file you just can create two properties.
* `api.local_ip_interface` will be used to define the *network interface*
* `api.local_port` will be used to define the service port

### Sample file

```properties
api.local_ip_interface=wlp9s0
api.local_port=3000
```

## Using

Now, in your *app level* `root/app/build.gradle` call the method `getApiUrl()`.
Yes you can call methods inside the `build.gradle` files, so do it.

It will be like this
```gradle
buildConfigField "String", "API_URL", "\"${getApiUrl()}\""
```

### Sample
Here is a sample of how the `build.gradle` will end up.

```gradle
buildscript {
    repositories {
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.github.brunodles:IpGetter:-SNAPSHOT'
    }
}
apply plugin: 'com.github.brunodles.IpGetter'
android {
    ...
    defaultConfig {
        ...
        buildConfigField "String", "API_URL", '"http://localhost"'
    }
    buildTypes {
        ...
        release {
            ...
            buildConfigField "String", "API_URL", '"http://my-real-api.com"'
        }
        debug {
            ...
            buildConfigField "String", "API_URL", "\"${getApiUrl()}\""
        }
    }
}
```

# Contributing

Issues are welcome, create one and we will discourse about it.
If you saw any error, please reports, it will be a great help.

# Licence
You can use any code you found here, some of then I found on the internet too.

I'm using the MIT Licence, take a look on [Licence](LICENCE.md).

If you're using this plugin, please give me some credits too.

# Sources
This one have many sources, I don't remember where I found it at first, it was on 2014.
Now we have some articles about it, so I'll link some of then.
* [Bartinger](http://bartinger.at/inject-dynamic-host-ip-address-with-gradle/)
* [jeremie-martinez](http://jeremie-martinez.com/2015/05/05/inject-host-gradle/)

## Gradle
* [Writing Custom Plugins](https://docs.gradle.org/current/userguide/custom_plugins.html)
* [Custom Methods in build.gradle](http://stackoverflow.com/a/38032000/1622925)

## JitPack
* [JitPack](https://jitpack.io/)
* [Guide for Gradle Projects](https://jitpack.io/docs/BUILDING/#gradle-projects)
