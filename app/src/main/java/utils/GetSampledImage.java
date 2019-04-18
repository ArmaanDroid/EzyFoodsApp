package utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class GetSampledImage extends AsyncTask<String, Void, File> {

    private Context mActivity;
    private SampledImageAsyncResp mSampledImageAsyncResp = null;

    public GetSampledImage(Fragment fragment) {
        mActivity = fragment.getActivity();
        mSampledImageAsyncResp = (SampledImageAsyncResp) fragment;
    }

    public GetSampledImage(Activity activity) {
        mActivity = activity;
        mSampledImageAsyncResp = (SampledImageAsyncResp) activity;
    }

    public GetSampledImage(Context con, SampledImageAsyncResp callback) {
        mActivity = con;
        mSampledImageAsyncResp = callback;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Override
    protected File doInBackground(String... params) {
        try {
            String picturePath = params[0];
            String imageDirectory = params[1];
            boolean isGalleryImage = Boolean.valueOf(params[2]);
            int reqImageWidth = Integer.parseInt(params[3]);
            if (picturePath != null) {
                ExifInterface exif = new ExifInterface(picturePath);
                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 1);
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);
                options.inSampleSize = calculateInSampleSize(options, reqImageWidth, reqImageWidth);
                options.inJustDecodeBounds = false;
                Bitmap imageBitmap = BitmapFactory.decodeFile(picturePath, options);
                if (orientation == 6) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                            imageBitmap.getWidth(), imageBitmap.getHeight(),
                            matrix, true);
                } else if (orientation == 8) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(270);
                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                            imageBitmap.getWidth(), imageBitmap.getHeight(),
                            matrix, true);
                } else if (orientation == 3) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(180);
                    imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0,
                            imageBitmap.getWidth(), imageBitmap.getHeight(),
                            matrix, true);
                }
                if (imageBitmap != null) {
                    return getImageFile(imageBitmap, picturePath, imageDirectory, isGalleryImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        try {
            if (mSampledImageAsyncResp != null)
                mSampledImageAsyncResp.onSampledImageAsyncPostExecute(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getImageFile(Bitmap bmp, String picturePath, String imageDirectory,
                              boolean isGalleryImage) {
        try {
            OutputStream fOut = null;
            File file;
            if (isGalleryImage)
                file = setUpImageFile(imageDirectory);
            else file = new File(picturePath);
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
            fOut.flush();
            fOut.close();
//            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),
//                    file.getAbsolutePath(), file.getName(),
//                    file.getName());
            return file;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File setUpImageFile(String directory) throws IOException {
        File imageFile = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File storageDir = new File(directory);
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
            imageFile = File.createTempFile(AppConst.JPEG_FILE_PREFIX
                            + System.currentTimeMillis() + "_",
                    AppConst.JPEG_FILE_SUFFIX, storageDir);
        }
        return imageFile;
    }

    public interface SampledImageAsyncResp {
        void onSampledImageAsyncPostExecute(File file);
    }


}