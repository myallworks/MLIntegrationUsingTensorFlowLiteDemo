package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.ImageHelperActivity;

public class ImageClassificationActivity extends ImageHelperActivity {

    private ImageLabeler imageLabeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder()
                    .setConfidenceThreshold(0.7f)
                    .build()
            );
        } catch (Exception exe) {
            Log.e(TAG, "onCreate: ", exe);
        }
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
            if (!imageLabels.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (ImageLabel labels : imageLabels)
                    builder.append(labels.getText())
                            .append(" : ")
                            .append(labels.getConfidence())
                            .append("\n");
                getMtV().setText(builder.toString());
            } else {
                getMtV().setText(getString(R.string.could_not_classify));
            }
        });
    }
}