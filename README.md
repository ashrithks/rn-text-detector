# rn-text-detector

## Getting started

`$ npm install rn-text-detector --save` or `yarn add rn-text-detector`

### Manual installation

#### iOS


##### Using Pods (Recommended)
1. In `<your_project>/ios` create Podfile file `pod init`
2. Add following in `ios/Podfile` 
```ruby
    pod 'yoga', :path => '../node_modules/react-native/ReactCommon/yoga'
    pod 'React', :path => '../node_modules/react-native'
    pod 'RNTextDetector', path: '../node_modules/react-native-text-detector/ios'
```
3. Run following from project's root directory
```bash
    pod update && pod install
```
4. Use `<your_project>.xcworkspace` to run your app

##### Direct Linking
1.  In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2.  Go to `node_modules` ➜ `rn-text-detector` and add `RNTextDetector.xcodeproj`
3.  In XCode, in the project navigator, select your project. Add `libRNTextDetector.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4.  Run your project (`Cmd+R`)<

#### Android

1.  Open up `android/app/src/main/java/[...]/MainApplication.java`

- Add `import com.fetchsky.RNTextDetector.RNTextDetectorPackage;` to the imports at the top of the file
- Add `new RNTextDetectorPackage()` to the list returned by the `getPackages()` method

2.  Append the following lines to `android/settings.gradle`:
    ```
    include ':rn-text-detector'
    project(':rn-text-detector').projectDir = new File(rootProject.projectDir, 	'../node_modules/rn-text-detector/android')
    ```
3.  Insert the following lines inside the dependencies block in `android/app/build.gradle`:

    ```
    ...
    dependencies {

        implementation 'rn-text-detector'
    }
#### MLKIT

Follow MLkit documentation,
https://developers.google.com/ml-kit/vision/text-recognition

## Usage

```javascript
/**
 *
 * This Example uses react-native-camera for getting image
 *
 */

import RNTextDetector from "rn-text-detector";

export class TextDetectionComponent extends PureComponent {
  ...

  detectText = async () => {
    try {
      const options = {
        quality: 0.8,
        base64: true,
        skipProcessing: true,
      };
      const { uri } = await this.camera.takePictureAsync(options);
      const visionResp = await RNTextDetector.detectFromUri(uri);
      console.log('visionResp', visionResp);
    } catch (e) {
      console.warn(e);
    }
  };

  ...
}
```
