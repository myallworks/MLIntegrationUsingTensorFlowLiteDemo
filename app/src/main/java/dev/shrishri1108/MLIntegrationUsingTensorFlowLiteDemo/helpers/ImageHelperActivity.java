package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers;

import static java.text.DateFormat.getDateTimeInstance;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.Constant;
import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.databinding.ActivityImageHelperBinding;

public class ImageHelperActivity extends AppCompatActivity {

    private ActivityImageHelperBinding bindings;
    private File photoFile;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindings = ActivityImageHelperBinding.inflate(LayoutInflater.from(this));
        setContentView(bindings.getRoot());

        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    // Permission is granted, you can now use the camera
                    // Permission denied
                });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                Bitmap Bitmap = loadImgFromUri(result.getData().getData());
                runClassification(Bitmap);
                bindings.ivImage.setImageBitmap(Bitmap);
            }
        });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {

                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                runClassification(bitmap);
                bindings.ivImage.setImageBitmap(bitmap);
            }
        });
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    protected void runClassification(Bitmap bitmap) {

    }

    private Bitmap loadImgFromUri(Uri uris) {
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                ImageDecoder.Source sources = ImageDecoder.createSource(getContentResolver(), uris);
                bitmap = ImageDecoder.decodeBitmap(sources);
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uris);
            }
        } catch (IOException ex) {
            Log.e(Constant.TAG, "loadImgFromUri: ", ex);
        }

        return bitmap;
    }


    public void pickFromCamera(View view) {

        // Create a file to share with Camera
        photoFile = createPhotoFile();

        Uri fileUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
        // create an Intent
        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        cameraLauncher.launch(intents);
    }

    public void pickFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        galleryLauncher.launch(intent);
    }

    private File createPhotoFile() {
        File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ML_IMAGE_HELPER");

        if (!photoFile.exists()) photoFile.mkdirs();

        return new File(photoFile.getPath() + File.separator + getDateTimeInstance());
    }

    protected MaterialTextView getMtV() {
        return bindings.mtvOp;
    }

    protected ImageView getImageV() {
        return bindings.ivImage;
    }

    protected void drawDetectionResult(List<BoxWithLabel> boxesList, Bitmap bitmap) {
        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(outputBitmap);
        Paint penRect = new Paint();
        penRect.setColor(Color.RED);
        penRect.setStyle(Paint.Style.STROKE);
        penRect.setStrokeWidth(8f);

        Paint penLabel = new Paint();
        penLabel.setColor(Color.YELLOW);
        penLabel.setStyle(Paint.Style.FILL_AND_STROKE);
        penLabel.setStrokeWidth(2f);
        penLabel.setTextSize(96f);

        for (BoxWithLabel boxx : boxesList) {
            canvas.drawRect(boxx.rect, penRect);

            // Rect
            Rect labelSize = new Rect(0, 0, 0, 0);
            penLabel.getTextBounds(boxx.label, 0, boxx.label.length(), labelSize);

            float fontSize = penLabel.getTextSize() * boxx.rect.width() / labelSize.width();
            if (fontSize < penLabel.getTextSize()) {
                penLabel.setTextSize(fontSize);
            }

            canvas.drawText(boxx.label, boxx.rect.left, boxx.rect.top + labelSize.height() + 1, penLabel);
        }
        getImageV().setImageBitmap(outputBitmap);
    }
}