package com.example.kazplantaimobile;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kazplantaimobile.MainActivity;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class myJavascriptInterface extends Context {
    Context mContext;

    myJavascriptInterface(MainActivity c) {
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
                    //add support for jpg and more.

                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                        Toast.makeText(getApplicationContext(), "COMPLETE CONVERTATION", Toast.LENGTH_LONG).show();
                    encodedImages.add(encoded);
//                        Toast.makeText(getApplicationContext(), "ENCODED IMAGES:   " + encodedImages, Toast.LENGTH_LONG).show();
                }
                Log.e("encodeddd images ", "azaza   " + encodedImages.size());

                boolean add = entireImages.add(new com.example.kazplantaimobile.Image(imageId, encodedImages) {
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
//                Toast.makeText(getApplicationContext(), "ENCODED IMAGES:   " + entireImages.size(), Toast.LENGTH_LONG).show();
//                Log.e("жайсон", " = " + entireImages.size() + " /// " + entireImages);
//                Log.e("жайсон2", " = " + entireImages.size() + " /// " + entireImages.get(0).imageId);
//                Log.e("жайсон3", " = " + entireImages.size() + " /// " + entireImages.get(1).imageId);
            for (Image img : entireImages){
//                    Toast.makeText(getApplicationContext(), img.imageId + " ПОЭЛЕМЕНТНО:    " + img.images, Toast.LENGTH_LONG).show();
                JSONObject myJsonObject = new JSONObject();
//                    Toast.makeText(getApplicationContext(), "ENCODED IMAGES:   " + img.imageId, Toast.LENGTH_LONG).show();

                myJsonObject.put("imageId", img.imageId);
                myJsonObject.put("images", img.images);
//                    jsonArray.put(myJsonObject);
            }

            JSONArray jsonArray = new JSONArray();
            String jj = new Gson().toJson(entireImages);
//                Toast.makeText(getApplicationContext(), "ДЛЯ АКЖОЛА:   " + jj, Toast.LENGTH_LONG).show();
//                saveTextAsFile("asd.txt", jj);
            return jj;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR";
        }
    }

    @JavascriptInterface
    public String saveImages(String[] images, String[] imageNames) throws FileNotFoundException {
//            Toast.makeText(getApplicationContext(), "МЫ УЖЕ ТУТ!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
        Log.d("test", "*******************************EEEEEEEEEEEEEEE:      fdfdsfs" + images);

        // создаем папку приложения
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Volunteering");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        Log.d("test", "*******************************DADADADADADADAADA:      " + folder.getPath());
//            Toast.makeText(getApplicationContext(), "У ЦИКЛА: " + folder.getPath(), Toast.LENGTH_LONG).show();
//            Toast.makeText(getApplicationContext(), "ФОТКИ:: " + imageNames, Toast.LENGTH_LONG).show();
//            try{
//                JSONObject images = new JSONObject(imageNames);
//                Toast.makeText(getApplicationContext(), "JSON---------------------   : " + images, Toast.LENGTH_LONG).show();
//
//                String username = images.getString("image");
//                String password = images.getString("description");
//
//                Log.i("TAG",username + " - " + password);
//
//            }catch (Exception ex){
//                Toast.makeText(getApplicationContext(), "ОШИБКА: " + ex, Toast.LENGTH_LONG).show();
//                Log.i("TAG","error : " + ex);
//            }

//            Toast.makeText(getApplicationContext(), "У ЦИКЛА: " + images, Toast.LENGTH_LONG).show();
        for(int i = 0; i < imageNames.length; i++)
        {
//                Toast.makeText(getApplicationContext(), "В ЦИКЛЕ: " + imageNames[i], Toast.LENGTH_LONG).show();
            if (images[i].startsWith("data:")) {  //when url is base64 encoded data
                createAndSaveFileFromBase64Url(imageNames[i], images[i], folder);
            }
        }

        return "successfully";
    }
    public String createAndSaveFileFromBase64Url(String name, String url, File directory) {
        File path = directory;
//        String filetype = url.substring(url.indexOf("/") + 1, url.indexOf(";"));
//        String filename = System.currentTimeMillis() + "." + filetype;
        String filename = name + "." + "jpeg";
        File file = new File(path, filename);
        try {
            if(!path.exists()) {
//                Toast.makeText(getApplicationContext(), "PATH EXISTS: ", Toast.LENGTH_LONG).show();
                path.mkdirs();
//                Toast.makeText(getApplicationContext(), "DIRS CREATED: ", Toast.LENGTH_LONG).show();
            }
            if(!file.exists()){
//                Toast.makeText(getApplicationContext(), "IN CREATE NEW FILE: ", Toast.LENGTH_LONG).show();
                file.createNewFile();
//                Toast.makeText(getApplicationContext(), "CREATED NEW FILE!!!!!: ", Toast.LENGTH_LONG).show();
            }

//            Toast.makeText(getApplicationContext(), "downloading1", Toast.LENGTH_LONG).show();
            String base64EncodedString = url.substring(url.indexOf(",") + 1);
            byte[] decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT);
            OutputStream os = new FileOutputStream(file);
            os.write(decodedBytes);
            os.close();
//            Toast.makeText(getApplicationContext(), "downloading2", Toast.LENGTH_LONG).show();
            //Tell the media scanner about the new file so that it is immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
//            Toast.makeText(getApplicationContext(), "downloading3", Toast.LENGTH_LONG).show();
            //Set notification after download complete and add "click to view" action to that
            String mimetype = url.substring(url.indexOf(":") + 1, url.indexOf("/"));
            Intent intent = new Intent();
            intent.setAction(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), (mimetype + "/*"));
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
//            Toast.makeText(getApplicationContext(), "downloading4", Toast.LENGTH_LONG).show();
//            Notification notification = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentText("R.string.msg_file_downloaded")
//                    .setContentTitle(filename)
//                    .setContentIntent(pIntent)
//                    .build();
//            Toast.makeText(getApplicationContext(), "downloading5", Toast.LENGTH_LONG).show();
//            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            int notificationId = 85851;
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationId, notification);
        } catch (IOException e) {
            Log.w("ExternalStorage", "Error writing " + file, e);
            Toast.makeText(getApplicationContext(), "Ошибка! Проверьте разрешение устройства на использование внутреннего хранилища" + e, Toast.LENGTH_LONG).show();
        }

        return file.toString();
    }

    private void saveTextAsFile(String filename, String content) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
//                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public AssetManager getAssets() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public PackageManager getPackageManager() {
        return null;
    }

    @Override
    public ContentResolver getContentResolver() {
        return null;
    }

    @Override
    public Looper getMainLooper() {
        return null;
    }

    @Override
    public Context getApplicationContext() {
        return null;
    }

    @Override
    public void setTheme(int resid) {

    }

    @Override
    public Resources.Theme getTheme() {
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        return null;
    }

    @Override
    public String getPackageResourcePath() {
        return null;
    }

    @Override
    public String getPackageCodePath() {
        return null;
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return null;
    }

    @Override
    public boolean moveSharedPreferencesFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteSharedPreferences(String name) {
        return false;
    }

    @Override
    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean deleteFile(String name) {
        return false;
    }

    @Override
    public File getFileStreamPath(String name) {
        return null;
    }

    @Override
    public File getDataDir() {
        return null;
    }

    @Override
    public File getFilesDir() {
        return null;
    }

    @Override
    public File getNoBackupFilesDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalFilesDir(@Nullable String type) {
        return null;
    }

    @Override
    public File[] getExternalFilesDirs(String type) {
        return new File[0];
    }

    @Override
    public File getObbDir() {
        return null;
    }

    @Override
    public File[] getObbDirs() {
        return new File[0];
    }

    @Override
    public File getCacheDir() {
        return null;
    }

    @Override
    public File getCodeCacheDir() {
        return null;
    }

    @Nullable
    @Override
    public File getExternalCacheDir() {
        return null;
    }

    @Override
    public File[] getExternalCacheDirs() {
        return new File[0];
    }

    @Override
    public File[] getExternalMediaDirs() {
        return new File[0];
    }

    @Override
    public String[] fileList() {
        return new String[0];
    }

    @Override
    public File getDir(String name, int mode) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return null;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        return null;
    }

    @Override
    public boolean moveDatabaseFrom(Context sourceContext, String name) {
        return false;
    }

    @Override
    public boolean deleteDatabase(String name) {
        return false;
    }

    @Override
    public File getDatabasePath(String name) {
        return null;
    }

    @Override
    public String[] databaseList() {
        return new String[0];
    }

    @Override
    public Drawable getWallpaper() {
        return null;
    }

    @Override
    public Drawable peekWallpaper() {
        return null;
    }

    @Override
    public int getWallpaperDesiredMinimumWidth() {
        return 0;
    }

    @Override
    public int getWallpaperDesiredMinimumHeight() {
        return 0;
    }

    @Override
    public void setWallpaper(Bitmap bitmap) throws IOException {

    }

    @Override
    public void setWallpaper(InputStream data) throws IOException {

    }

    @Override
    public void clearWallpaper() throws IOException {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {

    }

    @Override
    public void startActivities(Intent[] intents) {

    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {

    }

    @Override
    public void startIntentSender(IntentSender intent, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

    }

    @Override
    public void startIntentSender(IntentSender intent, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable Bundle options) throws IntentSender.SendIntentException {

    }

    @Override
    public void sendBroadcast(Intent intent) {

    }

    @Override
    public void sendBroadcast(Intent intent, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(Intent intent, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcast(@NonNull Intent intent, @Nullable String receiverPermission, @Nullable BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendBroadcastAsUser(Intent intent, UserHandle user, @Nullable String receiverPermission) {

    }

    @Override
    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, @Nullable String receiverPermission, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void sendStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcast(Intent intent) {

    }

    @Override
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Override
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, @Nullable Handler scheduler, int initialCode, @Nullable String initialData, @Nullable Bundle initialExtras) {

    }

    @Override
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter, int flags) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler) {
        return null;
    }

    @Nullable
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, @Nullable String broadcastPermission, @Nullable Handler scheduler, int flags) {
        return null;
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {

    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        return null;
    }

    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return null;
    }

    @Override
    public boolean stopService(Intent service) {
        return false;
    }

    @Override
    public boolean bindService(@NonNull Intent service, @NonNull ServiceConnection conn, int flags) {
        return false;
    }

    @Override
    public void unbindService(@NonNull ServiceConnection conn) {

    }

    @Override
    public boolean startInstrumentation(@NonNull ComponentName className, @Nullable String profileFile, @Nullable Bundle arguments) {
        return false;
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        return null;
    }

    @Nullable
    @Override
    public String getSystemServiceName(@NonNull Class<?> serviceClass) {
        return null;
    }

    @Override
    public int checkPermission(@NonNull String permission, int pid, int uid) {
        return 0;
    }

    @Override
    public int checkCallingPermission(@NonNull String permission) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfPermission(@NonNull String permission) {
        return 0;
    }

    @Override
    public int checkSelfPermission(@NonNull String permission) {
        return 0;
    }

    @Override
    public void enforcePermission(@NonNull String permission, int pid, int uid, @Nullable String message) {

    }

    @Override
    public void enforceCallingPermission(@NonNull String permission, @Nullable String message) {

    }

    @Override
    public void enforceCallingOrSelfPermission(@NonNull String permission, @Nullable String message) {

    }

    @Override
    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(Uri uri, int modeFlags) {

    }

    @Override
    public void revokeUriPermission(String toPackage, Uri uri, int modeFlags) {

    }

    @Override
    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return 0;
    }

    @Override
    public int checkUriPermission(@Nullable Uri uri, @Nullable String readPermission, @Nullable String writePermission, int pid, int uid, int modeFlags) {
        return 0;
    }

    @Override
    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

    }

    @Override
    public void enforceUriPermission(@Nullable Uri uri, @Nullable String readPermission, @Nullable String writePermission, int pid, int uid, int modeFlags, @Nullable String message) {

    }

    @Override
    public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createContextForSplit(String splitName) throws PackageManager.NameNotFoundException {
        return null;
    }

    @Override
    public Context createConfigurationContext(@NonNull Configuration overrideConfiguration) {
        return null;
    }

    @Override
    public Context createDisplayContext(@NonNull Display display) {
        return null;
    }

    @Override
    public Context createDeviceProtectedStorageContext() {
        return null;
    }

    @Override
    public boolean isDeviceProtectedStorage() {
        return false;
    }
}