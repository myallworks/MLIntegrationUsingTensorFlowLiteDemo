package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;

import java.util.ArrayList;
import java.util.List;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.BoxWithLabel;
import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.ImageHelperActivity;

public class ObjectDetectionActivity extends ImageHelperActivity {

    private ObjectDetector objectDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ObjectDetectorOptions options = new ObjectDetectorOptions.Builder()
                    .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                    .enableMultipleObjects()
                    .enableClassification()
                    .build();

            objectDetector = ObjectDetection.getClient(options);
        } catch (Exception exs) {
            Log.e(Constant.TAG, "onCreate: ", exs);
        }
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        objectDetector.process(inputImage).addOnSuccessListener(detectedObjects -> {
            if (!detectedObjects.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                List<BoxWithLabel> boxesList = new ArrayList<>();
                for (DetectedObject object : detectedObjects) {
                    if (!object.getLabels().isEmpty()) {
                        builder.append(object.getLabels().get(0).getText())
                                .append(" : ")
                                .append(object.getLabels().get(0).getConfidence())
                                .append("\n");
                        boxesList.add(new BoxWithLabel(object.getBoundingBox(), object.getLabels().get(0).getText()));
                        Log.d(Constant.TAG, "onSuccess: Object Detecteds :  " + builder);
                    } else {
                        builder.append("Unknown")
                                .append("\n");
                    }
                }
                drawDetectionResult(boxesList, bitmap);
                getMtV().setText(builder.toString());
            } else {
                getMtV().setText(getString(R.string.could_not_classify));
            }
        });
    }
}