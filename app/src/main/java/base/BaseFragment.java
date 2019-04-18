package base;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.fragments.home.HomeFragment;
import utils.AppConst;
import utils.GeneralFunctions;
import utils.GetSampledImage;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by ABC on 12/15/2017.
 */

public class BaseFragment extends Fragment implements BaseView, View.OnClickListener {
    private static final String KEY_PICKED_IMAGE_PATH = AppConst.PACKAGE + "imageUri";
    private Toast mToast;
    private AlertDialog mDialog;
    private int permissionNeeded;
    protected DateFormat dateFormatApi = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
    protected DateFormat dateFormatApp = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
    private android.app.AlertDialog imagePickerDialog;
    protected String picturePath;
    protected Uri pickupedImagePath;

    protected static final int REQUEST_CODE_GALLERY = 2012;
    protected static final int REQUEST_CODE_CAMERA = 2013;
    protected OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            pickupedImagePath=savedInstanceState.getParcelable(KEY_PICKED_IMAGE_PATH);
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void cancelToast() {
        mToast.cancel();
    }

    @Override
    public void showProgress(String message) {
        if(mDialog==null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setCancelable(false);
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.view_progress_bar, null);
            TextView mTextViewLoadingMessage = view.findViewById(R.id.textViewLoadingMessage);
            mTextViewLoadingMessage.setText(message);
            alertDialogBuilder.setView(view);
            mDialog = alertDialogBuilder.create();
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mDialog.show();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = mDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }else if(!mDialog.isShowing()){
            mDialog.show();
        }
    }

    @Override
    public void hidePorgress() {
        if(mDialog!=null)
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
    }

    @Override
    public void showToast(String message) {
        if (mToast != null) {
            cancelToast();
        }
        try {
           /* View view = LayoutInflater.from(activity).inflate(R.layout.view_custom_toast, null);
            TextView mTextView = view.findViewById(R.id.textViewMessage);
            mTextView.setText(message);
            mToast = new Toast(activity);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            mToast.setView(view);*/
            mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>
    public int checkPermission(String[] permission) {
        permissionNeeded = 0;
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < permission.length; i++) {
                int result = ContextCompat.checkSelfPermission(getContext(), permission[i]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    permissionNeeded++;
                }
            }
        }
        return permissionNeeded;
    }

    //<editor-fold desc="date picker dialog">
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        calendar.getTime();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void dialogForCameraGallery() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.popup_camera_gallery, null);
        View mTextViewGallery = view.findViewById(R.id.textViewGallery);
        View mTextViewTakePic = view.findViewById(R.id.textViewTakePic);
        View mTextViewCancel = view.findViewById(R.id.textViewCancel);
        mTextViewGallery.setOnClickListener(this);
        mTextViewTakePic.setOnClickListener(this);
        mTextViewCancel.setOnClickListener(this);
        dialogBuilder.setView(view);
        dialogBuilder.setCancelable(false);
        imagePickerDialog = dialogBuilder.show();
        imagePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void alertDialogWithOKButton(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.permission_denied);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, AppConst.REQUEST_PERMISSION_SETTING);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewGallery:

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, AppConst.GALLERY_INTENT);
                imagePickerDialog.hide();
                break;
            case R.id.textViewTakePic:

                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                File f = null;
                try {
                    f = GeneralFunctions.setUpImageFile(AppConst
                            .LOCAL_STORAGE_BASE_PATH_FOR_USER_PHOTOS);
                    picturePath = f.getAbsolutePath();
                    Uri uri = FileProvider.getUriForFile(getContext()
                            , getActivity().getPackageName() + ".provider"
                            , f);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    picturePath = null;
                }
                startActivityForResult(takePictureIntent,
                        REQUEST_CODE_CAMERA);
                imagePickerDialog.hide();
                break;
            case R.id.textViewCancel:
                imagePickerDialog.hide();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(pickupedImagePath!=null)
        outState.putParcelable(KEY_PICKED_IMAGE_PATH,pickupedImagePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            boolean isGalleryImage = false;
            if (requestCode == AppConst.GALLERY_INTENT) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                isGalleryImage = true;
            }
            showProgress("Processing...");
            new GetSampledImage(this).execute(picturePath,
                    AppConst.LOCAL_STORAGE_BASE_PATH_FOR_USER_PHOTOS,
                    String.valueOf(isGalleryImage),
                    String.valueOf((int) getResources().getDimension(R.dimen.h_400)));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                        dialogForCameraGallery();
                    } else {
                        if (Build.VERSION.SDK_INT >= 23) {
                            boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            boolean showRationale2 = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                            boolean showRationale3 = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                            if (!showRationale) {
                                alertDialogWithOKButton(getString(R.string.storagePermission));
                            } else if (!showRationale2) {
                                alertDialogWithOKButton(getString(R.string.storagePermission));
                            } else if (!showRationale3) {
                                alertDialogWithOKButton(getString(R.string.cameraPermission));
                            } else {
                                ActivityCompat.requestPermissions((Activity) getActivity(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                        111);
                            }
                        }
                    }
                }
            }
        }
    }

    public interface OnFragmentInteractionListener {

       void openFoodScreen();
    }
}
