package com.example.kazplantaimobile;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Collections;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

public class OnnxSegmentationModelRunner {
    private OrtEnvironment env;
    private OrtSession session;
    private static final int IMG_SIZE = 512;
    private Context context;

    public OnnxSegmentationModelRunner(Context context) throws Exception {
        env = OrtEnvironment.getEnvironment();
        InputStream modelStream = context.getAssets().open("deeplabv3_resnet50.onnx");
        byte[] modelBytes = new byte[modelStream.available()];
        modelStream.read(modelBytes);
        session = env.createSession(modelBytes, new OrtSession.SessionOptions());
        this.context = context;
    }

    public Bitmap runInference(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, IMG_SIZE, IMG_SIZE, true);
        float[] inputTensor = preprocess(resized);
        OnnxTensor input = null;
        try {
            input = OnnxTensor.createTensor(env, FloatBuffer.wrap(inputTensor), new long[]{1, 3, IMG_SIZE, IMG_SIZE});
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
        OrtSession.Result result = null;
        try {
            result = session.run(Collections.singletonMap("input", input));
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }

        float[][][] output = new float[0][][]; // Adjust based on model
        try {
            Toast.makeText(this.context, "SSSSSSSSSSSSSSSSSS111 ", Toast.LENGTH_LONG);
            float[][][][] outputRaw = (float[][][][]) result.get(0).getValue();
            output = outputRaw[0]; // убираем первую размерность (batch=1)
            Toast.makeText(this.context, "SSSSSSSSSSSSSSSSSS " + output.toString(), Toast.LENGTH_LONG);
        } catch (OrtException e) {
            throw new RuntimeException(e);
        }
        Bitmap segmented = postprocess(output);

        return overlay(bitmap, segmented);
    }

    private float[] preprocess(Bitmap bitmap) {
        float[] input = new float[3 * IMG_SIZE * IMG_SIZE];
        int[] pixels = new int[IMG_SIZE * IMG_SIZE];
        bitmap.getPixels(pixels, 0, IMG_SIZE, 0, 0, IMG_SIZE, IMG_SIZE);
        for (int i = 0; i < pixels.length; i++) {
            int c = pixels[i];
            input[i] = ((c >> 16) & 0xFF) / 255.0f;               // R
            input[i + IMG_SIZE * IMG_SIZE] = ((c >> 8) & 0xFF) / 255.0f;  // G
            input[i + 2 * IMG_SIZE * IMG_SIZE] = (c & 0xFF) / 255.0f;     // B
        }
        return input;
    }

    private Bitmap postprocess(float[][][] output) {
        Bitmap mask = Bitmap.createBitmap(IMG_SIZE, IMG_SIZE, Bitmap.Config.ARGB_8888);
        int numClasses = output.length;

        for (int y = 0; y < IMG_SIZE; y++) {
            for (int x = 0; x < IMG_SIZE; x++) {
                float[] pixelClasses = new float[numClasses];
                for (int c = 0; c < numClasses; c++) {
                    pixelClasses[c] = output[c][y][x];
                }
                int cls = argmax(pixelClasses);
                int color = cls == 0 ? Color.TRANSPARENT : Color.argb(128, 255, 0, 0); // example
                mask.setPixel(x, y, color);
            }
        }
        return mask;
    }

//    private Bitmap overlay(Bitmap original, Bitmap mask) {
//        int width = Math.min(original.getWidth(), mask.getWidth());
//        int height = Math.min(original.getHeight(), mask.getHeight());
//        Bitmap result = original.copy(Bitmap.Config.ARGB_8888, true);
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int maskPixel = mask.getPixel(x, y);
//                if ((maskPixel >> 24) != 0x00) {
//                    result.setPixel(x, y, blend(result.getPixel(x, y), maskPixel));
//                }
//            }
//        }
//        return mask;
//    }

//    private Bitmap overlay(Bitmap original, Bitmap mask) {
//        int width = Math.min(original.getWidth(), mask.getWidth());
//        int height = Math.min(original.getHeight(), mask.getHeight());
//        Bitmap result = original.copy(Bitmap.Config.ARGB_8888, true);
//
//        int highlightColor = Color.argb(128, 255, 0, 0);
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                int maskPixel = mask.getPixel(x, y);
//                if ((maskPixel >> 24) != 0x00) {
//                    result.setPixel(x, y, blend(result.getPixel(x, y), highlightColor));
//                }
//            }
//        }
//
//        return result;
//    }
private Bitmap overlay(Bitmap original, Bitmap mask) {
    int w = original.getWidth();
    int h = original.getHeight();
    // Растянем маску на размер оригинала
    Bitmap scaledMask = Bitmap.createScaledBitmap(mask, w, h, true);
    Bitmap result = original.copy(Bitmap.Config.ARGB_8888, true);

    for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
            int m = scaledMask.getPixel(x, y);
            int alpha = Color.alpha(m);
            if (alpha > 0) {
                int base = result.getPixel(x, y);
                // композитинг по альфе маски
                float a = alpha / 255f;
                int r = (int) (Color.red(base)   * (1 - a) + Color.red(m)   * a);
                int g = (int) (Color.green(base) * (1 - a) + Color.green(m) * a);
                int b = (int) (Color.blue(base)  * (1 - a) + Color.blue(m)  * a);
                result.setPixel(x, y, Color.argb(255, r, g, b));
            }
        }
    }
    return result;
}


    private int blend(int base, int overlay) {
        int r = (Color.red(base) + Color.red(overlay)) / 2;
        int g = (Color.green(base) + Color.green(overlay)) / 2;
        int b = (Color.blue(base) + Color.blue(overlay)) / 2;
        return Color.rgb(r, g, b);
    }

    private int argmax(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) maxIndex = i;
        }
        return maxIndex;
    }
}
