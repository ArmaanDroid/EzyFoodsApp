package sanguinebits.com.ezyfoods.home.fragments.uploadDish;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import dialogs.DatePickerFragment;
import model.ApiResponse;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeActivity;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import utils.AppConst;
import utils.GeneralFunctions;
import utils.GetSampledImage;
import utils.LocationHelper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WhatYouCookFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WhatYouCookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhatYouCookFragment extends BaseFragment implements PostDishView, GetSampledImage.SampledImageAsyncResp {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    private PostDishPresenter presenter;

    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.editTextAvailableOn)
    TextView editTextAvailableOn;
    @BindView(R.id.editTextOrderBy)
    TextView editTextOrderBy;
    @BindView(R.id.editTextPickup)
    TextView editTextPickup;
    @BindView(R.id.editTextCuisineType)
    EditText editTextCuisineType;
    @BindView(R.id.textViewDelivery)
    TextView textViewDelivery;
    @BindView(R.id.editTextQuantity)
    EditText editTextQuantity;
    @BindView(R.id.editTextZipCode)
    EditText editTextZipCode;
    @BindView(R.id.editTextDescription)
    EditText editTextDescription;
    @BindView(R.id.editTextPrice)
    EditText editTextPrice;
    @BindView(R.id.editTextWeight2)
    EditText editTextWeight2;
    @BindView(R.id.editTextSpiceLevel)
    TextView editTextSpiceLevel;
    @BindView(R.id.textViewWeightUnit)
    TextView textViewWeightUnit;
    @BindView(R.id.imageView7)
    SimpleDraweeView imageView;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private HomeViewModel vmHome;
    private Date availableOnDate;
    private Date orderByDate;

    public WhatYouCookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhatYouCookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhatYouCookFragment newInstance(String param1, String param2) {
        WhatYouCookFragment fragment = new WhatYouCookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        vmHome=ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewCart).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageView6).setVisibility(View.GONE);
        TextView textViewTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.cook);
        return inflater.inflate(R.layout.fragment_what_you_cook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter = new PostDishPresenter(getActivity(), this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


//    @OnTouch(R.id.editTextDescription)
//    public boolean editTextAboutClick(View v, MotionEvent event) {
//        scrollView.requestDisallowInterceptTouchEvent(true);
//        return false;
//    }


    @OnClick(R.id.postDish)
    void addDish() {
        String title = editTextTitle.getText().toString().trim();
        String availableOn = editTextAvailableOn.getText().toString().trim();
        String orderBy = editTextOrderBy.getText().toString().trim();
        String spiceLevel = editTextSpiceLevel.getText().toString().trim();
        String cuisineType = editTextCuisineType.getText().toString().trim();
        String pickUp = editTextPickup.getText().toString().trim();
        String delivery = textViewDelivery.getText().toString().trim();
        String quantity = editTextQuantity.getText().toString().trim();
        String zipCode = editTextZipCode.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String weight = editTextWeight2.getText().toString().trim();

        if(LocationHelper.isLocationAvailable(getActivity())){
            presenter.postDish(title, availableOn, orderBy, spiceLevel, cuisineType, pickUp, delivery
                    , quantity, zipCode, description,price,weight,pickupedImagePath,availableOnDate,orderByDate);
        }else{
            showToast("Please try again.");
            LocationHelper.checkLocationPermission(getActivity(),(HomeActivity)getActivity());
        }
    }

    @OnClick({R.id.textView12,R.id.imageView7})
    public void circularImageViewClick() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (checkPermission(permission) > 0) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission[0], permission[1], permission[2]},
                    111);
        } else {
            dialogForCameraGallery();
        }
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @OnClick(R.id.editTextAvailableOn)
    void selectAvailableDate() {

        editTextAvailableOn.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editTextAvailableOn.setEnabled(true);
            }
        }, 1000);

        DatePickerFragment datePicker = new DatePickerFragment(Calendar.getInstance().getTime(), new DatePickerFragment.DateSelectionListner() {
            @Override
            public void onDateSelected(Date date) {
                editTextAvailableOn.setText(dateFormatApp.format(date));
                availableOnDate=date;
            }
        });
        datePicker.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.editTextOrderBy)
    void getOrderByDate() {
        editTextOrderBy.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editTextOrderBy.setEnabled(true);
            }
        }, 1000);
        DatePickerFragment datePicker = new DatePickerFragment(Calendar.getInstance().getTime(), new DatePickerFragment.DateSelectionListner() {
            @Override
            public void onDateSelected(Date date) {
                editTextOrderBy.setText(dateFormatApp.format(date));
                orderByDate=date;
            }
        });
        datePicker.show(getFragmentManager(), "datePicker2");
    }

    @OnClick(R.id.editTextSpiceLevel)
    void selectSpiceLevel() {
        showSpiceLevelPopUp(editTextSpiceLevel);
    }

    @OnClick(R.id.editTextPickup)
    void showOption() {
        showYesNoPopUp(editTextPickup);
    }

    @OnClick(R.id.textViewDelivery)
    void showOption2() {
        showDeliveryPopUp(textViewDelivery);
    }

    @OnClick(R.id.textViewWeightUnit)
    void showWeightUnit() {
        showWeightUnitPopUp(textViewWeightUnit);
    }

    private void showWeightUnitPopUp(final TextView textViewWeightUnit) {
        PopupMenu mMenu = new PopupMenu(getActivity(), textViewWeightUnit);
        mMenu.getMenuInflater().inflate(R.menu.popup_weight_units, mMenu.getMenu());

        mMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                textViewWeightUnit.setText(item.getTitle());
                return false;
            }
        });
        mMenu.show();
    }

    private void showDeliveryPopUp(final TextView textView) {
        PopupMenu mMenu = new PopupMenu(getActivity(), textView);
        mMenu.getMenuInflater().inflate(R.menu.popup_delivery_options, mMenu.getMenu());

        mMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                textView.setText(item.getTitle());
                return false;
            }
        });
        mMenu.show();
    }

    private void showYesNoPopUp(final TextView textView) {
        PopupMenu mMenu = new PopupMenu(getActivity(), textView);
        mMenu.getMenuInflater().inflate(R.menu.popup_yes_no, mMenu.getMenu());

        mMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = null;
                textView.setText(item.getTitle());

                return false;
            }
        });
        mMenu.show();
    }

    private void showSpiceLevelPopUp(final TextView textView) {
        PopupMenu mMenu = new PopupMenu(getActivity(), textView);

        mMenu.getMenuInflater().inflate(R.menu.popup_spice_level, mMenu.getMenu());

        mMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent i = null;
                textView.setText(item.getTitle());
                return false;
            }
        });
        mMenu.show();
    }

    @Override
    public void onDishPosted(ApiResponse commonPojo) {
        editTextTitle.setText("");
        editTextAvailableOn.setText("");
        editTextOrderBy.setText("");
        editTextSpiceLevel.setText("");
        editTextCuisineType.setText("");
        editTextPickup.setText("");
        textViewDelivery.setText("");
        editTextQuantity.setText("");
        editTextZipCode.setText("");
        editTextDescription.setText("");
        editTextPrice.setText("");
        editTextWeight2.setText("");

        pickupedImagePath=null;
        imageView.setImageResource(R.color.white);
        vmHome.isDishesChanged=true;

    }




    @Override
    public void onSampledImageAsyncPostExecute(File file) {
        hidePorgress();
        pickupedImagePath = Uri.parse(AppConst.LOCAL_FILE_PREFIX + file);
        imageView.setImageURI(pickupedImagePath);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
