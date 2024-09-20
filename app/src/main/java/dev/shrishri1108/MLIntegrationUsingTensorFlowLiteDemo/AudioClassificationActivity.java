package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo;

import android.os.Bundle;
import android.util.Log;

import org.tensorflow.lite.support.audio.TensorAudio;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.audio.classifier.AudioClassifier;
import org.tensorflow.lite.task.audio.classifier.Classifications;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.AudioHelperActivity;

public class AudioClassificationActivity extends AudioHelperActivity {

    private AudioClassifier audioClassifier;
    private TensorAudio tensorAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String model = "yamnet_classification.tflite";
            audioClassifier = AudioClassifier.createFromFile(this, model);
        } catch (Exception ex) {
            Log.e(Constant.TAG, "onCreate: ", ex);
        }
        tensorAudio = audioClassifier.createInputTensorAudio();
    }

    @Override
    protected void startRec() {
        super.startRec();

// showing the audio recorder specification \
        TensorAudio.TensorAudioFormat format = audioClassifier.getRequiredTensorAudioFormat();
        String specs = "Number of channels: " + format.getChannels() + "\n";
        getMtvSpec().setText(specs);

        new TimerTask() {
            @Override
            public void run() {
                List<Classifications> outputLst = audioClassifier.classify(tensorAudio);

                // filtering out classifications wiht low probability
                AtomicReference<List<Category>> finalOutput = new AtomicReference<>(new ArrayList<>());
                for (Classifications classifications : outputLst) {
                    for (Category category : classifications.getCategories()) {
                        if (category.getScore() > 0.3f) {
                            finalOutput.get().add(category);
                        }
                    }
                }

            }
        };

    }
}