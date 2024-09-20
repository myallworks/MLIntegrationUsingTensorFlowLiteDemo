package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.databinding.ActivityAudioHelperBinding;

public class AudioHelperActivity extends AppCompatActivity {

    private ActivityAudioHelperBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAudioHelperBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    // Permission is granted, you can now use the camera
                    // Permission denied
                });

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    protected void startRec() {
        binding.btnStartRec.setEnabled(false);

        binding.btnStopRec.setEnabled(true);
    }

    protected void stopRec() {
        binding.btnStartRec.setEnabled(true);

        binding.btnStopRec.setEnabled(false);
    }

    protected MaterialTextView getMtvOpAudio() {
        return binding.mtvOpAudio;
    }

    protected MaterialTextView getMtvSpec() {
        return binding.mtvSpecAudio;
    }
}