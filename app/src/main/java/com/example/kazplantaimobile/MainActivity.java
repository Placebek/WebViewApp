package com.example.kazplantaimobile;

import android.annotation.SuppressLint;
import com.google.gson.Gson;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//public class MainActivity extends AppCompatActivity {
//
//    private WebView webView;
//
//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.CAMERA,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//    private final static int FILECHOOSER_RESULTCODE = 1;
//
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permissionCamera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
//        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED && permissionRead != PackageManager.PERMISSION_GRANTED && permissionCamera != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }
//
//    @SuppressLint("SetJavaScriptEnabled")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
////        }
////        webView = findViewById(R.id.webview);
////
////        // Чтобы открывать страницы внутри приложения, а не в браузере
////        webView.setWebViewClient(new WebViewClient());
////
////        WebSettings webSettings = webView.getSettings();
////        webSettings.setJavaScriptEnabled(true); // если нужно
////        webSettings.setMediaPlaybackRequiresUserGesture(false);
////        webView.getSettings().setAllowFileAccess(true);
////        webView.getSettings().setAllowContentAccess(true);
////        webView.setWebViewClient(new WebViewClient() {
////            @SuppressLint("WebViewClientOnReceivedSslError")
////            @Override
////            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
////                handler.proceed(); // ⚠ Принять все сертификаты (НЕ делай это в продакшене!)
////            }
////        });
////        webView.setWebChromeClient(new WebChromeClient() {
////            // Обработка запроса к камере/файлам
////            @Override
////            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
////                // Ваша реализация вызова камеры или выбора файлов
////                return true;
////            }
////        });
//        setContentView(R.layout.activity_main);
////        verifyStoragePermissions(this);
//
//        webView = (WebView) findViewById(R.id.webview);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setAllowFileAccessFromFileURLs(true);
//        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
//
//        webView.setWebViewClient(new WebViewClient());
//        webView.setWebChromeClient(new WebChromeClient(){
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                String TAG = "";
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        request.grant(request.getResources());
//                    }
//                });
//            }
//            @Override
//            public void onPermissionRequestCanceled(PermissionRequest request) {
//                String TAG = "";
//                Log.d(TAG, "onPermissionRequestCanceled");
//            }
//
//            @Override
//            public void onRequestFocus(WebView view) {
//                super.onRequestFocus(view);
//            }
//        });
//
////        webView.setWebViewClient(new SSLTolerentWebViewClient());
//
//        webView.getSettings().setDomStorageEnabled(true);
////        webView.loadUrl("http://192.168.1.62:3000");
//        webView.loadUrl("https://192.168.253.118:3000"); // замените на нужный URL
//        webView.addJavascriptInterface(new com.example.kazplantaimobile.myJavascriptInterface(this), "Android");
////        webView.addJavascriptInterface(new Object());
////        {
////            public void performClick()
////            {
////                Toast.makeText(getApplicationContext(), "CLICKED EEEEEE", Toast.LENGTH_LONG).show();
////
////                // Deal with a click on the OK button
////            }
////        }, "ok");
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Камера разрешена", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Камера запрещена", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Обработка кнопки "назад"
//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//
//}
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private OnnxModelRunner modelRunner;
    private OnnxSegmentationModelRunner segmentationModelRunner;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final static int FILECHOOSER_RESULTCODE = 1;

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int permissionRead = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED && permissionRead != PackageManager.PERMISSION_GRANTED && permissionCamera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                String TAG = "";
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        request.grant(request.getResources());
                    }
                });
            }
            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                String TAG = "";
                Log.d(TAG, "onPermissionRequestCanceled");
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
            }
        });

        webView.setWebViewClient(new com.example.volunteertest.SSLTolerentWebViewClient());

        webView.getSettings().setDomStorageEnabled(true);
//        webView.loadUrl("http://192.168.1.62:3000");
//        webView.loadUrl("https://kazplant-ai-admin-app.vercel.app/");
        webView.loadUrl("https://kazplant-vue.vercel.app/");
        webView.addJavascriptInterface(new myJavascriptInterface(this), "Android");
        class WebAppInterface {
            Context mContext;

            WebAppInterface(Context c) {
                mContext = c;
            }

            @JavascriptInterface
            public void recognizeDiseaseClick(String base64Image) {
                try {
                    modelRunner = new OnnxModelRunner(mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                byte[] decodedBytes = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT);
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                Activity activity = (Activity) mContext;

                activity.runOnUiThread(() -> {
                    String prediction = modelRunner.runInference(imageBitmap);

                    String js = "window.handlePredictionResult('" + prediction + "');";
                    WebView webView = activity.findViewById(R.id.webview);
                    webView.evaluateJavascript(js, null);
                });
            }

            @JavascriptInterface
            public void segmentDiseaseClick(String base64Image) {
                byte[] decodedBytes = Base64.decode(base64Image.split(",")[1], Base64.DEFAULT);
                Bitmap inputBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                ((Activity) mContext).runOnUiThread(() -> {
                    try {
                        // 1. Запуск модели
                        OnnxSegmentationModelRunner segmentationModelRunner = new OnnxSegmentationModelRunner(mContext);
                        Bitmap segmented = segmentationModelRunner.runInference(inputBitmap);

                        // 2. Преобразование результата в Base64
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        segmented.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        String base64Segmented = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                        // 3. Вызов JS-функции в WebView
                        String jsCode = "window.displaySegmentedImage('data:image/png;base64," + base64Segmented + "');";
                        webView.evaluateJavascript(jsCode, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mContext, "Ошибка сегментации" + e, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        webView.addJavascriptInterface(new WebAppInterface(this), "ok");
    }

    public String createAndSaveFileFromBase64Url(String name, String url, File directory) {
        File path = directory;
        String filename = name + "." + "jpeg";
        File file = new File(path, filename);
        try {
            if(!path.exists()) {
                path.mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }

            String base64EncodedString = url.substring(url.indexOf(",") + 1);
            byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
            OutputStream os = new FileOutputStream(file);
            os.write(decodedBytes);
            os.close();
            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
            String mimetype = url.substring(url.indexOf(":") + 1, url.indexOf("/"));
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), (mimetype + "/*"));
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            int notificationId = 85851;
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
            Toast.makeText(getApplicationContext(), "Ошибка! Проверьте разрешение устройства на использование внутреннего хранилища" + e, Toast.LENGTH_LONG).show();
        }

        return file.toString();
    }

    static Camera mcamera = null;

    @Override
    protected void onPause(){
        String TAG = "";
        // TODO Auto-generated method stub
        if (mcamera != null) {
            mcamera.stopPreview();
            mcamera.release();
            mcamera = null;
            Log.d(TAG, "releaseCamera -- done");
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        String TAG = "";
        if (mcamera != null) {
            Camera.open();
            Log.d(TAG, "openCamera -- done");
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String TAG = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
            Log.d(TAG, "has camera permission: " + hasCameraPermission);
            int hasRecordPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            Log.d(TAG, "has record permission: " + hasRecordPermission);
            int writeExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            Log.d(TAG, "write External Storage: " + writeExternalStorage);
            int readExternalStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            Log.d(TAG, "read External Storage: " + readExternalStorage);
            int hasAudioPermission = checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            Log.d(TAG, "has audio permission: " + hasAudioPermission);
            List<String> permissions = new ArrayList<>();
            if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (hasRecordPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            }
            if (readExternalStorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }
    }

    public class myJavascriptInterface {
        Context mContext;

        myJavascriptInterface(Context c) {
            mContext = c;
        }

        final int SELECT_PHOTO = 1;

        @JavascriptInterface
        public String choosePhoto(String imageNames) throws FileNotFoundException {
            Log.d("test", "*******************************AZAZAZAZAZAZAZA:      fdfdsfs" + imageNames);

            List<Image> entireImages = new ArrayList<Image>();
            try {
                // jsonString is a string variable that holds the JSON
                JSONArray itemArray = new JSONArray(imageNames);
                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject data = itemArray.getJSONObject(i);

                    String imageId = data.getString("quest");
//                    Toast.makeText(getApplicationContext(), "ID КАРТИНКИ:  " + imageId, Toast.LENGTH_LONG).show();

                    JSONArray images = data.getJSONArray("savedImages");

                    // закодированные в base64 картинки храним тут
                    ArrayList<String> encodedImages = new ArrayList<String>();
                    for (int j = 0; j < images.length(); j++) {
                        String imageName = images.getString(j);
//                        Toast.makeText(getApplicationContext(), "КАЖДАЯ КАРТИНКА:  J=" + j + " IMAGENAME= " + imageName, Toast.LENGTH_LONG).show();
                        File folder = new File(Environment.getExternalStorageDirectory() +
                                File.separator + "Volunteering");

                        FileInputStream ff = new FileInputStream(folder.getPath() + "/" + imageName);
                        Bitmap bitmap = BitmapFactory.decodeStream(ff);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();

                        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        encodedImages.add(encoded);
                    }
                    Log.e("encodeddd images ", "azaza   " + encodedImages.size());

                    entireImages.add(new Image(imageId, encodedImages) {
                        @Override
                        public int getFormat() {
                            return 0;
                        }

                        @Override
                        public int getWidth() {
                            return 0;
                        }

                        @Override
                        public int getHeight() {
                            return 0;
                        }

                        @Override
                        public long getTimestamp() {
                            return 0;
                        }

                        @Override
                        public android.media.Image.Plane[] getPlanes() {
                            return new android.media.Image.Plane[0];
                        }

                        @Override
                        public void close() {

                        }
                    });
                    Log.e("json", i + " = " + entireImages);
                }
                for (Image img : entireImages){
                    JSONObject myJsonObject = new JSONObject();

                    myJsonObject.put("imageId", img.imageId);
                    myJsonObject.put("images", img.images);
                }

                JSONArray jsonArray = new JSONArray();
                String jj = new Gson().toJson(entireImages);
                return jj;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "ERROR";
            }
        }

        @JavascriptInterface
        public String saveImages(String[] images, String[] imageNames) throws FileNotFoundException {
            Log.d("test", "*******************************EEEEEEEEEEEEEEE:      fdfdsfs" + images);

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "Volunteering");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Log.d("test", "*******************************DADADADADADADAADA:      " + folder.getPath());
            for(int i = 0; i < imageNames.length; i++)
            {
                if (images[i].startsWith("data:")) {  //when url is base64 encoded data
                    createAndSaveFileFromBase64Url(imageNames[i], images[i], folder);
                }
            }

            return "successfully";
        }

        private void saveTextAsFile(String filename, String content) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
