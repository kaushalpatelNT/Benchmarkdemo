package com.nichetech.smartonsite.benchmark.Common;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.nichetech.smartonsite.benchmark.BuildConfig;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * CONECT_Working(com.collabera.conect.commons) <br />
 * Developed by <b><a href="http://RohitSuthar.com/">Suthar Rohit</a></b>  <br />
 * on 05-Dec-2016.
 *
 * @author Suthar Rohit
 */
public class FilesUtils {
    private static final String TAG = FilesUtils.class.getSimpleName();

    public static final String IMAGE_EXTENSION = ".png";
    public static final String PDF_EXTENSION = ".pdf";

    public static final String IMAGE_PICTURES_DIR = Environment.DIRECTORY_PICTURES + "/FamiJoy";
    public static final String CACHE_DIR = ".FamiJoy";
    public static final String PROFILE_IMAGE_CACHE_DIR = CACHE_DIR + "/.profile";
    public static final String DOCUMENT_CACHE_DIR = ".imgdoc";

    public static File getProfilePicFile(Context context, long imageId) {
        return new File(context.getFilesDir().getAbsolutePath(), "imagename" + imageId + ".png");
    }

    public static File saveBitmapImage(Bitmap mBitmap, String imageLocation, String imageName) {
        File imageFile = null;
        try {
            File directory = new File(imageLocation);
            if (!directory.exists())
                directory.mkdirs();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            //you can create a new file name "test.jpg" in sdcard folder.
            imageFile = new File(imageLocation + imageName + FilesUtils.IMAGE_EXTENSION);
            if (BuildConfig.DEBUG)
                Log.e("saveBitmapImage", "imageFile=>" + imageFile);

            imageFile.createNewFile();
            //write the bytes in file
            FileOutputStream fo = new FileOutputStream(imageFile);
            fo.write(bytes.toByteArray());
            // remember close de FileOutput
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public static boolean isDownloaded(String imageLocation, String imageName) {
        try {
            File directory = new File(imageLocation);
            if (!directory.exists())
                directory.mkdirs();

            File imageFile = new File(imageLocation + imageName + IMAGE_EXTENSION);

            return imageFile.exists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFilePath(Context context) {
        return Environment.getExternalStorageDirectory() + "/MatrubhartiAuthor";
        //return context.getFilesDir().toString();
    }

    public static void renameFile(String imageLocation, String oldFileName, String newFileName) {
        try {
            File directory = new File(imageLocation);
            if (!directory.exists())
                directory.mkdirs();

            File oldFile = new File(imageLocation + oldFileName + IMAGE_EXTENSION);
            File newFile = new File(imageLocation + newFileName + IMAGE_EXTENSION);
            if (oldFile.exists())
                oldFile.renameTo(newFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renameFileNew(String imageLocation, String oldFileName, String newFileName) {
        try {
            File directory = new File(imageLocation);
            if (!directory.exists())
                directory.mkdirs();

            File oldFile = new File(imageLocation + oldFileName);
            File newFile = new File(imageLocation + newFileName);
            if (oldFile.exists())
                oldFile.renameTo(newFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();

            for (String aChildren : children) {
                FilesUtils.copyDirectory(new File(sourceLocation, aChildren), new File(targetLocation, aChildren));
            }

        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from inStream to outStream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }

    public static void refreshGallery(Context context, File f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            //File f = new File("file://" + Environment.getExternalStoragePublicDirectory(FilesUtility.IMAGE_PICTURES_DIR));
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        } else {
            //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
            //        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(f)));
        }
    }

    /**
     * Copy a file from one location to another.
     *
     * @param sourceFile File to copy from.
     * @param destFile   File to copy to.
     * @return True if successful, false otherwise.
     * @throws IOException
     */
    public static boolean copyFiles(File sourceFile, File destFile) {

        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileChannel sourceChannel = null;
            FileChannel destChannel = null;

            try {
                sourceChannel = new FileInputStream(sourceFile).getChannel();
                destChannel = new FileOutputStream(destFile).getChannel();
                sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
            } finally {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (destChannel != null) {
                    destChannel.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // PARSE FILE PATH
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] project = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, project, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Read bytes from a File into a byte[].
     *
     * @param file The File to read.
     * @return A byte[] containing the contents of the File.
     * @throws IOException Thrown if the File is too long to read or couldn't be
     *                     read fully.
     */
    public static byte[] readBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    // convert from bitmap to byte array
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String fileToBase64(File file) {
        byte[] bytes = new byte[0];
        try {
            bytes = loadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    //---------- STORE FILE IN TEMPORARY FILE  ----------//
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static File createTemporaryFile(Context context, InputStream in, String fileName, String extension) throws IOException {
        String pathPrefix = Validate.isNotNull(fileName) ? removeExtension(fileName) : "createTemporaryFile";
        //File tempFile = File.createTempFile(pathPrefix, "." + extension);
        File tempFile = new File(context.getCacheDir(), pathPrefix + "." + extension);
        Log.e(TAG, "EXTENSION==>" + tempFile);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            FilesUtils.copyLarge(in, out);
        }
        return tempFile;
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri contentUri) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(contentUri);
            Drawable d = Drawable.createFromStream(inputStream, "imagename");
            bitmap = ((BitmapDrawable) d).getBitmap();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static File base64toFile(Context context, String b64, String fileName) {
        File f = null;
        try {
            Bitmap bitmap = FilesUtils.base64ToBitmap(b64);
            byte[] imageAsBytes = FilesUtils.getBytesFromBitmap(bitmap);

            f = new File(fileName);
            if (!f.exists())
                if (f.createNewFile()) Log.i("contentUri", "file created");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(imageAsBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static Bitmap scaleBitmap(Bitmap bitmapToScale) {

        //return bitmapToScale;

        int oldWidth = bitmapToScale.getWidth();
        int oldHeight = bitmapToScale.getHeight();

        float aspectRatio = oldWidth / (float) oldHeight;
        int newWidth = 500;
        int newHeight = Math.round(newWidth / aspectRatio);

        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        Matrix matrix = new Matrix();

        matrix.postScale(newWidth / width, newHeight / height);
        return Bitmap.createScaledBitmap(bitmapToScale, newWidth, newHeight, false);
    }

    private static final String DOCUMENT_URIS =
            "com.android.providers.media.documents " +
                    "com.android.externalstorage.documents " +
                    "com.android.providers.downloads.documents " +
                    "com.android.providers.media.documents";

    private static final String PATH_DOCUMENT = "document";

    public static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() < 2) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }

        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            throw new IllegalArgumentException("Not a document: " + documentUri);
        }
        return paths.get(1);
    }

    public static boolean isDocumentUri(Uri uri) {
        final List<String> paths = uri.getPathSegments();
        Log.v(TAG, "paths[" + paths + "]");
        if (paths.size() < 2) {
            return false;
        }
        if (!PATH_DOCUMENT.equals(paths.get(0))) {
            return false;
        }
        return DOCUMENT_URIS.contains(uri.getAuthority());
    }

    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String getContentResolverFileName(Context context, Uri uri) {

        Cursor cursor = null;
        final String column = OpenableColumns.DISPLAY_NAME;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists.
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Removes the extension from a filename.
     * <p>
     * This method returns the textual part of the filename before the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt    --> foo
     * a\b\c.jpg  --> a\b\c
     * a\b\c      --> a\b\c
     * a.b\c      --> a.b\c
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to query, null returns null
     * @return the filename minus the extension
     */
    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return filename;
        } else {
            return filename.substring(0, index);
        }
    }

    /**
     * The extension separator character.
     *
     * @since Commons IO 1.4
     */
    public static final char EXTENSION_SEPARATOR = '.';
    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * Returns the index of the last extension separator character, which is a dot.
     * <p>
     * This method also checks that there is no directory separator after the last dot.
     * To do this it uses {@link #indexOfLastSeparator(String)} which will
     * handle a file in either Unix or Windows format.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
        int lastSeparator = indexOfLastSeparator(filename);
        return (lastSeparator > extensionPos ? -1 : extensionPos);
    }

    /**
     * Returns the index of the last directory separator character.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The position of the last forward or backslash is returned.
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filename the filename to find the last path separator in, null returns -1
     * @return the index of the last separator character, or -1 if there
     * is no such character
     */
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }

}
