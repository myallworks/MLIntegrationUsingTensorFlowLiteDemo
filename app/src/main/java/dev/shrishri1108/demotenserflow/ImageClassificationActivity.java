package dev.shrishri1108.demotenserflow;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.util.List;

import dev.shrishri1108.demotenserflow.helpers.ImageHelperActivity;

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
            exe.printStackTrace();
        }
    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(@NonNull List<ImageLabel> imageLabels) {
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
            }
        });
    }
}