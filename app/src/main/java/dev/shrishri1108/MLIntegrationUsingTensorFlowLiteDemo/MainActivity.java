package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.databinding.ActivityMainBinding;
import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.language_translation.TextTranslationAcitivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.imgClassificationLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, ImageClassificationActivity.class);
            startActivity(intents);
        });

        binding.imgPicturesLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, FlowerIdentificationActivity.class);
            startActivity(intents);
        });

        binding.imgFaceDetectionLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, FaceDetectionActivity.class);
            startActivity(intents);
        });

        binding.AudioClassificationLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, AudioClassificationActivity.class);
            startActivity(intents);
        });

        binding.TextTranslationLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, TextTranslationAcitivity.class);
            startActivity(intents);
        });

        binding.imgOBjDLays.setOnClickListener(viws -> {
            Intent intents = new Intent(MainActivity.this, ObjectDetectionActivity.class);
            startActivity(intents);
        });
    }


}