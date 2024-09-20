package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.language_translation;

import static dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.Constant.TAG;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.BoxWithLabel;
import dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.helpers.ImageHelperActivity;

public class TextRecognitionFromBitmapAcitivity extends ImageHelperActivity {

    private TextRecognizer DefaultRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// When using Latin script library
        DefaultRecognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

    }

    protected void runOperationOnRecognisedText(String recognisedText) {

    }

    @Override
    protected void runClassification(Bitmap bitmap) {
        super.runClassification(bitmap);
        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        InputImage inputImage = InputImage.fromBitmap(outputBitmap, 0);

        if (DefaultRecognizer != null) {
            DefaultRecognizer.process(inputImage)
                    .addOnSuccessListener(visionText -> {
                        // Task completed successfully
                        runOnUiThread(() -> getMtV().setText(String.format("Text:\n%s", visionText.getText())));

                        runOperationOnRecognisedText(visionText.getText());
                    })
                    .addOnFailureListener(
                            e -> Log.e(TAG, "onFailure: ", e));

//        DrawRectangleAroundTextArea(result.getResult(), outputBitmap);

        }
    }

    private void DrawRectangleAroundTextArea(Text result, Bitmap outputBitmap) {
        List<BoxWithLabel> boxesList = new ArrayList<>();
        for (Text.TextBlock block : result.getTextBlocks()) {
            BoxWithLabel boxWithLabel = new BoxWithLabel(
                    block.getBoundingBox(),
                    block.getText() + " "
            );
            boxesList.add(boxWithLabel);
        }
        drawDetectionResult(boxesList, outputBitmap);
        getMtV().setText(MessageFormat.format("{0} has been detected. ", boxesList.size()));

//       String resultText = result.getText();
//        for (Text.TextBlock block : result.getTextBlocks()) {
//            String blockText = block.getText();
//            Point[] blockCornerPoints = block.getCornerPoints();
//            Rect blockFrame = block.getBoundingBox();
//            for (Text.Line line : block.getLines()) {
//                String lineText = line.getText();
//                Point[] lineCornerPoints = line.getCornerPoints();
//                Rect lineFrame = line.getBoundingBox();
//                for (Text.Element element : line.getElements()) {
//                    String elementText = element.getText();
//                    Point[] elementCornerPoints = element.getCornerPoints();
//                    Rect elementFrame = element.getBoundingBox();
//                    for (Text.Symbol symbol : element.getSymbols()) {
//                        String symbolText = symbol.getText();
//                        Point[] symbolCornerPoints = symbol.getCornerPoints();
//                        Rect symbolFrame = symbol.getBoundingBox();
//                    }
//                }
//            }
//        }
    }
}
