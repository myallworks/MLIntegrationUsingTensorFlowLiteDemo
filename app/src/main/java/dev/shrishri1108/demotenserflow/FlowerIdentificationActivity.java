package dev.shrishri1108.demotenserflow;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;

import dev.shrishri1108.demotenserflow.helpers.ImageHelperActivity;

public class FlowerIdentificationActivity extends ImageHelperActivity {

    private ImageLabeler imageLabeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            LocalModel localModel = new LocalModel.Builder().setAssetFilePath("model_flowers.tflite").build();
            CustomImageLabelerOptions options = new CustomImageLabelerOptions.Builder(localModel)
                    .setConfidenceThreshold(0.7f)
                    .setMaxResultCount(10)
                    .build();
            imageLabeler = ImageLabeling.getClient(options);
        } catch (Exception ex) {
            Log.e(Constant.TAG, "onCreate: ", ex);
        }
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
            if (imageLabels.size() > 0) {
                StringBuilder builder = new StringBuilder();
                for (ImageLabel labels : imageLabels) {
                    builder.append(labels.getText())
                            .append(" : ")
                            .append(labels.getConfidence())
                            .append("\n");
                }
                getMtV().setText(builder.toString());
            } else {
                getMtV().setText("Could not Classify. ");
            }
        });
    }
}