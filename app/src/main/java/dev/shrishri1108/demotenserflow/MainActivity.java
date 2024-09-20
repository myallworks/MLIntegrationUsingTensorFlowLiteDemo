package dev.shrishri1108.demotenserflow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import dev.shrishri1108.demotenserflow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dev.shrishri1108.demotenserflow.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.imgClassificationLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, ImageClassificationActivity.class);
            startActivity(intents);
        });

        binding.imgPicturesLays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(MainActivity.this, FlowerIdentificationActivity.class);
                startActivity(intents);
            }
        });

        binding.imgFaceDetectionLays.setOnClickListener(v -> {
            Intent intents = new Intent(MainActivity.this, FaceDetectionActivity.class);
            startActivity(intents);
        });

        binding.imgOBjDLays.setOnClickListener(viws -> {
            Intent intents = new Intent(MainActivity.this, ObjectDetectionActivity.class);
            startActivity(intents);
        });
    }


}