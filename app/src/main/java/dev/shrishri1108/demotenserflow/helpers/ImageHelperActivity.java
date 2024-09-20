package dev.shrishri1108.demotenserflow.helpers;

import android.Manifest;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dev.shrishri1108.demotenserflow.Constant;
import dev.shrishri1108.demotenserflow.databinding.ActivityImageHelperBinding;

public class ImageHelperActivity extends AppCompatActivity {

    private static final int REQ_PICK_IMG = 109;
    private static final int REQ_TAKE_IMG = 1013;
    private static final int REQ_PERMISSION_CODE = 1011;
    private ActivityImageHelperBinding bindings;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindings = ActivityImageHelperBinding.inflate(LayoutInflater.from(this));
        setContentView(bindings.getRoot());


        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_PICK_IMG) {
                Uri ur = null;
                if (data != null) {
                    ur = data.getData();
                }
                Bitmap Bitmap = loadImgFromUri(ur);
                runClassification(Bitmap);
                bindings.ivImage.setImageBitmap(Bitmap);
            } else if (requestCode == REQ_TAKE_IMG) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                runClassification(bitmap);
                bindings.ivImage.setImageBitmap(bitmap);
            }
        }

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

        Uri fileUri = FileProvider.getUriForFile(this, "com.iago.fileprovider", photoFile);
        // create an Intent
        Intent intents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intents.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        //startActivityForResult( )
        startActivityForResult(intents, REQ_TAKE_IMG);
    }

    public void pickFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_PICK_IMG);
    }

    private File createPhotoFile() {
        File photoFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ML_IMAGE_HELPER");

        if (!photoFile.exists()) {
            photoFile.mkdirs();
        }

        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File files = new File(photoFile.getPath() + File.separator + name);
        return files;
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