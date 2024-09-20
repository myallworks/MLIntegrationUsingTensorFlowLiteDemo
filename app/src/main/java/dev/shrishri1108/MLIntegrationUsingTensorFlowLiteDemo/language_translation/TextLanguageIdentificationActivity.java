package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.language_translation;

import static dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.Constant.TAG;

import android.os.Bundle;
import android.util.Log;

import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

import java.text.MessageFormat;

public class TextLanguageIdentificationActivity extends TextRecognitionFromBitmapAcitivity {

    private LanguageIdentifier languageIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageIdentifier =
                LanguageIdentification.getClient();

    }

    protected void runOperationAfterTextLanguageIdentification(String recognisedText, String languageCode) {

    }

    @Override
    protected void runOperationOnRecognisedText(String recognisedText) {
        super.runOperationOnRecognisedText(recognisedText);

        languageIdentifier.identifyLanguage(String.valueOf(recognisedText))
                .addOnSuccessListener(
                        languageCode -> {
                            assert languageCode != null;
                            if (languageCode.equals("und")) {
                                Log.i(TAG, "Can't identify language.");
                            } else {
                                Log.i(TAG, "Language: " + languageCode);
                                getMtV().setText(MessageFormat.format("{0}\n Text Language: {1}", getMtV().getText(), languageCode));

                                runOperationAfterTextLanguageIdentification(recognisedText, languageCode);
                            }
                        })
                .addOnFailureListener(
                        e -> {
                            Log.e(TAG, "onFailure: ", e);
                        });
    }
}
