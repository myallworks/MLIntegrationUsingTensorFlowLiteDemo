package dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.language_translation;

import static dev.shrishri1108.MLIntegrationUsingTensorFlowLiteDemo.Constant.TAG;

import android.util.Log;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextTranslationAcitivity extends TextLanguageIdentificationActivity {

    private TranslatorOptions.Builder options;

    @Override
    protected void onStart() {
        super.onStart();

        // Create an Text-HINDI translator:
        options = new TranslatorOptions.Builder().setTargetLanguage(TranslateLanguage.HINDI);
    }

    @Override
    protected void runOperationAfterTextLanguageIdentification(String recognisedText, String languageCode) {
        super.runOperationAfterTextLanguageIdentification(recognisedText, languageCode);

        TranslatorOptions TranslatorOptionsBuilds = options.setSourceLanguage(languageCode).build();
        final Translator textHINDITranslator = Translation.getClient(TranslatorOptionsBuilds);
        getLifecycle().addObserver(textHINDITranslator);
        ExecutorService poolExecutor = Executors.newSingleThreadExecutor();

        poolExecutor.submit(() -> downloadTheTranslator(textHINDITranslator, recognisedText));
    }

    private void downloadTheTranslator(Translator textHINDITranslator, String recognisedText) {
        DownloadConditions conditions = new DownloadConditions.Builder().requireWifi().build();
        textHINDITranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(o -> {
                            /*
                             Model downloaded successfully. Okay to start translating.
                             (Set a flag, unhide the translation UI, etc.)
                            */

            startTranslation(textHINDITranslator, recognisedText);
        }).addOnFailureListener(e -> {
            // Model couldn’t be downloaded or other internal error.
            Log.e(TAG, "onFailure: ", e);
        });
    }

    private void startTranslation(Translator textHINDITranslator, String recognisedText) {
        textHINDITranslator.translate(recognisedText).addOnSuccessListener(o -> runOnUiThread(() -> getMtV().setText(MessageFormat.format("{0}\n\n{1}", getMtV().getText(), o + ".")))).addOnFailureListener(e -> Log.e(TAG, "onFailure: ", e));
    }

}
