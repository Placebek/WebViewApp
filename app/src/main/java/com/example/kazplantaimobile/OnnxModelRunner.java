package com.example.kazplantaimobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import ai.onnxruntime.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;

public class OnnxModelRunner {

    private static final String TAG = "OnnxModelRunner";
    private OrtEnvironment env;
    private OrtSession session;
    private Context context;

    public OnnxModelRunner(Context context) {
        this.context = context;
        try {
            // Initialize the ONNX Runtime environment
            env = OrtEnvironment.getEnvironment();
            Log.d(TAG, "ONNX Runtime environment initialized.");

            // Copy the ONNX model from assets to internal storage
            String modelPath = copyAssetToInternalStorage(context, "best_model.onnx");
            Log.d(TAG, "Model path: " + modelPath);

            session = env.createSession(modelPath);
            Log.d(TAG, "ONNX model session created.");
        } catch (OrtException | IOException e) {
            Log.e(TAG, "Error initializing ONNX model: " + e.getMessage());
            session = null;
        }
    }

    private String copyAssetToInternalStorage(Context context, String assetFileName) throws IOException {
        InputStream inputStream = context.getAssets().open(assetFileName);
        File outFile = new File(context.getFilesDir(), assetFileName);
        FileOutputStream outputStream = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        inputStream.close();
        outputStream.close();
        return outFile.getAbsolutePath();
    }

    public String runInference(Bitmap bitmap) {
        if (session == null) {
            Log.e(TAG, "Session is null");
            return null;
        }

        try {
            OnnxTensor inputTensor = bitmapToTensor(bitmap, env);
            OrtSession.Result result = session.run(Collections.singletonMap(session.getInputNames().iterator().next(), inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();
            int maxIdx = 0;
            for (int i = 1; i < output[0].length; i++) {
                if (output[0][i] > output[0][maxIdx]) maxIdx = i;
            }

            inputTensor.close();
            result.close();

            return "Class " + maxIdx; // Или имя класса, если у тебя есть список
        } catch (Exception e) {
            Log.e(TAG, "Inference error: " + e.getMessage());
            return "Error";
        }
    }

    private OnnxTensor bitmapToTensor(Bitmap bitmap, OrtEnvironment env) throws OrtException {
        int width = 224;
        int height = 224;
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, width, height, true);

        float[] inputTensor = new float[3 * height * width];

        // PyTorch mean and std
        float[] mean = {0.485f, 0.456f, 0.406f};
        float[] std = {0.229f, 0.224f, 0.225f};

        int[] intValues = new int[height * width];
        scaled.getPixels(intValues, 0, width, 0, 0, width, height);

        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];

            float r = ((val >> 16) & 0xFF) / 255.0f;
            float g = ((val >> 8) & 0xFF) / 255.0f;
            float b = (val & 0xFF) / 255.0f;

            inputTensor[i] = (r - mean[0]) / std[0]; // Red channel
            inputTensor[i + height * width] = (g - mean[1]) / std[1]; // Green channel
            inputTensor[i + 2 * height * width] = (b - mean[2]) / std[2]; // Blue channel
        }

        long[] shape = new long[]{1, 3, height, width};
        return OnnxTensor.createTensor(env, FloatBuffer.wrap(inputTensor), shape);
    }

    // Preprocess image: resize, normalize, and convert to float array
    private float[] preprocessImage(Bitmap bitmap) {
        // Resize the image to 224x224
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        // Convert the image to a float array (normalized)
        float[] imageData = new float[224 * 224 * 3];
        int index = 0;
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                // Convert to RGB values (normalized)
                imageData[index++] = (float) ((pixel >> 16) & 0xFF) / 255.0f; // Red
                imageData[index++] = (float) ((pixel >> 8) & 0xFF) / 255.0f;  // Green
                imageData[index++] = (float) (pixel & 0xFF) / 255.0f;         // Blue
            }
        }
        return imageData;
    }

    // Find the index of the maximum value in the array
    private int argMax(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void close() {
        try {
            if (session != null) {
                session.close();
            }
            if (env != null) {
                env.close();
            }
        } catch (OrtException e) {
            Log.e(TAG, "Error closing ONNX model: " + e.getMessage());
        }
    }
}
