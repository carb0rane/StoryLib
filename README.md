<h1 align="center">StoryLib</h1></br>
<p align="center">An Android Library for Seamless Story Implementations 💕 </p>

## Why StoryLib
<p align="center">
🎨 StoryLib library provides a seamless and customizable solution for integrating story-like features into your Android applications. Inspired by popular social media platforms such as WhatsApp and Instagram, this library allows developers to effortlessly incorporate engaging, swipeable stories with support for rich customization. Easily tailor the appearance, including colors, sizes, and other visual aspects, to align with the unique style of your app. Enhance user engagement by leveraging the intuitive and interactive storytelling experience offered by StoryLib.
</p>
<br>

<p align="center">
<img src="https://lookimg.com/images/2024/02/23/QmsStv.jpeg" width="31%"/>
<img src="https://lookimg.com/images/2024/02/23/Qm1x1U.jpeg" width="31%"/>
</p>



## Including in your project 


### Gradle 

Add the dependency below to your module's `build.gradle` file:

```gradle
dependencies {
    implementation "com.carb0rane:storylib:$version"
}
```
Get the latest version from the release/package section 



## Table of Contents
#### [1. StoryLib](https://github.com/carb0rane/storylib#usage)
- [StoryLib in layout](https://github.com/carb0rane/storylib#StoryLib-in-xml-layout)
- [Attributes](https://github.com/carb0rane/storylib#attributes)
- [Methods](https://github.com/skydoves/carb0rane/storylib#storybadgemanager-methods)

## Usage
Add following XML namespace inside your XML layout file.

```gradle
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### StoryLib in XML layout

You can simply use `StoryBadgeManager` by defining it on your XML files. This `StoryBadgeManager` will be initialized with the default values.

```gradle
<com.carb0rane.storylib.StoryBadgeManager
        android:id="@+id/storylib"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
```

### Attributes

You can customize the arc color and size or various options using the below attributes:

```gradle
app:storyUnVisitedColor="@color/black" // sets color for the arc represnting unvisited stories.
app:storyVisitedColor="#009988" // sets color for the arc represnting visited stories.
app:iRadius="80" // sets the size of badge.
app:bWidth="3" // sets the width of the arc.
app:barStoryPlaybackDuration="5000" // duration of slide in ms.
app:barOwnerTextColor="#efefef" // sets the color of the name of the story uploader.
app:barDisplayTimeTextColor="#efefef" // sets the color of the time of the story uploaded.
```

### Usages
`StoryBadgeManager` uses these functions.

```kotlin
storylib.setStoryOwner("Phoenix") // sets the name of the uploader of the story 

storylib.setRadius(2.5f) // the radius of the circle for badge (adjusts the gap in arc and image)

storylib.setStoriesList("https://url/image") // takes variable args as urls and uses them for the story
```



### StoryBadgeManager Methods
Methods | Return | Description
--- | --- | ---
setStoryOwner(name:String) | void | Sets the name of the uploader of the story
setRadius(radius:Float) | void | Changes the radius of the circle for badge (adjusts the gap in arc and image).
setStoriesList(vararg url: String) | void |  Takes variable args as urls and uses them for the story


## Find this library useful? :heart:
Support it by joining [stargazers](https://github.com/carb0rane/storylib/stargazers) for this repository. :star: <br>
And __[follow](https://github.com/carb0rane)__ me for my next creations! 🤩

# License
```xml
Copyright © 2024 carb0rane 

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
https://opensource.org/license/mit
```
