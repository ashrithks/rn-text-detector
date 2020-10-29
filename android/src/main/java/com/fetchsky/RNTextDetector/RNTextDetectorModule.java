
package com.fetchsky.RNTextDetector;

import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.IOException;
import java.net.URL;

import androidx.annotation.NonNull;

public class RNTextDetectorModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  private TextRecognizer detector;
  private InputImage image;

  public RNTextDetectorModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    try {
        detector = TextRecognition.getClient();
    }
    catch (IllegalStateException e) {
        e.printStackTrace();
    }
  }

  @ReactMethod
    public void detectFromFile(String uri, final Promise promise) {
        try {
            image = InputImage.fromFilePath(this.reactContext, android.net.Uri.parse(uri));
            Task<Text> result =
                    detector.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    promise.resolve(getDataAsArray(visionText));
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                            promise.reject(e);
                                        }
                                    });;
        } catch (IOException e) {
            promise.reject(e);
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void detectFromUri(String uri, final Promise promise) {
        try {
            URL url = new URL(uri);
            int rotationDegree = 0;
            image = InputImage.fromBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()),rotationDegree);
            Task<Text> result =
                    detector.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text visionText) {
                                    promise.resolve(getDataAsArray(visionText));
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                            promise.reject(e);
                                        }
                                    });;
        } catch (IOException e) {
            promise.reject(e);
            e.printStackTrace();
        }
    }

    /**
     * Converts visionText into a map
     *
     * @param visionText
     * @return
     */
    private WritableArray getDataAsArray(Text visionText) {
        WritableArray data = Arguments.createArray();

        for (Text.TextBlock block: visionText.getTextBlocks()) {
            WritableArray blockElements = Arguments.createArray();

            for (Text.Line line: block.getLines()) {
                WritableArray lineElements = Arguments.createArray();
                for (Text.Element element: line.getElements()) {
                    WritableMap e = Arguments.createMap();

                    WritableMap eCoordinates = Arguments.createMap();
                    eCoordinates.putInt("top", element.getBoundingBox().top);
                    eCoordinates.putInt("left", element.getBoundingBox().left);
                    eCoordinates.putInt("width", element.getBoundingBox().width());
                    eCoordinates.putInt("height", element.getBoundingBox().height());

                    e.putString("text", element.getText());
//                    e.putDouble("confidence", element.getConfidence());
                    e.putMap("bounding", eCoordinates);
                    lineElements.pushMap(e);
                }

                WritableMap l = Arguments.createMap();

                WritableMap lCoordinates = Arguments.createMap();
                lCoordinates.putInt("top", line.getBoundingBox().top);
                lCoordinates.putInt("left", line.getBoundingBox().left);
                lCoordinates.putInt("width", line.getBoundingBox().width());
                lCoordinates.putInt("height", line.getBoundingBox().height());

                l.putString("text", line.getText());
//                l.putDouble("confidence", line.getConfidence());
                l.putMap("bounding", lCoordinates);
                l.putArray("elements", lineElements);

                blockElements.pushMap(l);
            }
            WritableMap info = Arguments.createMap();
            WritableMap coordinates = Arguments.createMap();

            coordinates.putInt("top", block.getBoundingBox().top);
            coordinates.putInt("left", block.getBoundingBox().left);
            coordinates.putInt("width", block.getBoundingBox().width());
            coordinates.putInt("height", block.getBoundingBox().height());

            info.putMap("bounding", coordinates);
            info.putString("text", block.getText());
            info.putArray("lines", blockElements);
//            info.putDouble("confidence", block.getConfidence());
            data.pushMap(info);
        }

        return data;
    }


  @Override
  public String getName() {
    return "RNTextDetector";
  }
}