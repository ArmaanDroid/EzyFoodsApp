package sanguinebits.com.ezyfoods.home.fragments.loginSignup;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.Objects;

import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.forgotPwd.ForgotPasswordActivity;
import sanguinebits.com.ezyfoods.home.HomeViewModel;
import sanguinebits.com.ezyfoods.webViewAct.WebViewActivity;
import utils.AppConst;
import utils.AppUserType;
import utils.GetSampledImage;
import utils.PermissionHelper;


public class LoginSignupFragment extends BaseFragment implements LoginSignupView, GetSampledImage.SampledImageAsyncResp {
    private static final String ARG_PARAM1 = "param1";
    private static final int WEBACTIVITY_REQUEST_CODE = 2134;

    private Boolean isLogin;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LoginSignPresenter presenter;
    private HomeViewModel vmModel;
//    private FragmentLoginSignupBinding dataBinding;

    public LoginSignupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vmModel= ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(HomeViewModel.class);
        isLogin=vmModel.isLogin;
    }

    @BindView(R.id.layoutName)
    LinearLayout layoutName;

    @BindView(R.id.layoutZipCode)
    LinearLayout layoutZipCode;

    @BindView(R.id.layoutEmail)
    LinearLayout layoutEmail;

    @BindView(R.id.layoutPassword)
    LinearLayout layoutPassword;

    @BindView(R.id.layoutPhone)
    LinearLayout layoutPhone;

    @BindView(R.id.layoutAddress)
    LinearLayout layoutAddress;

    @BindView(R.id.layoutReferedBy)
    LinearLayout layoutReferedBy;

    @BindView(R.id.layoutSignupAs)
    LinearLayout layoutSignupAs;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnSignup)
    Button btnSignup;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etZipCode)
    EditText etZipCode;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.etReferedBy)
    EditText etReferedBy;

    @BindView(R.id.textViewUploadImage)
    TextView textViewUploadImage;

    @BindView(R.id.textViewForgotPassword)
    TextView textViewForgotPassword;

    @BindView(R.id.imageViewLoginSignup)
    SimpleDraweeView simpleDraweeView;

    @BindView(R.id.viewFlipperHome)
    ViewFlipper mViewFlipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        dataBinding=DataBindingUtil.inflate(inflater,R.layout.fragment_login_signup,container,false);
//        return getRoot();

        presenter = new LoginSignPresenter(getActivity(), this);
        Objects.requireNonNull(getActivity()).findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageView6).setVisibility(View.VISIBLE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imgSideMenu).setVisibility(View.GONE);
        Objects.requireNonNull(getActivity()).findViewById(R.id.imageViewCart).setVisibility(View.GONE);
        TextView textViewTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.textViewTitle);
        if (isLogin)
            textViewTitle.setText(R.string.login);
        else
            textViewTitle.setText(R.string.sign_up);

        View view = inflater.inflate(R.layout.fragment_login_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @BindView(R.id.radioGroupUserType)
    RadioGroup radioGroupUserType;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isLogin) {
            layoutEmail.setVisibility(View.VISIBLE);
            layoutPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            textViewForgotPassword.setVisibility(View.VISIBLE);

            layoutName.setVisibility(View.GONE);
            layoutAddress.setVisibility(View.GONE);
            layoutReferedBy.setVisibility(View.GONE);
            layoutPhone.setVisibility(View.GONE);
            layoutZipCode.setVisibility(View.GONE);
            layoutSignupAs.setVisibility(View.GONE);
            btnSignup.setVisibility(View.GONE);
            simpleDraweeView.setVisibility(View.GONE);
            textViewUploadImage.setVisibility(View.GONE);

            mViewFlipper.setVisibility(View.VISIBLE);
            mViewFlipper.setAutoStart(true);
            mViewFlipper.setFlipInterval(2000);
            mViewFlipper.startFlipping();
        } else {
            layoutEmail.setVisibility(View.VISIBLE);
            layoutPassword.setVisibility(View.VISIBLE);
            layoutName.setVisibility(View.VISIBLE);
            layoutAddress.setVisibility(View.VISIBLE);
            layoutReferedBy.setVisibility(View.VISIBLE);
            layoutPhone.setVisibility(View.VISIBLE);
            layoutZipCode.setVisibility(View.VISIBLE);
            layoutSignupAs.setVisibility(View.VISIBLE);
            simpleDraweeView.setVisibility(View.VISIBLE);
            textViewUploadImage.setVisibility(View.VISIBLE);

            btnLogin.setVisibility(View.GONE);
            btnSignup.setVisibility(View.VISIBLE);

            mViewFlipper.setVisibility(View.GONE);
            textViewForgotPassword.setVisibility(View.GONE);

        }
    }

    @OnClick(R.id.btnLogin)
    void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        presenter.login(email, password);
    }


    @OnClick({R.id.imageViewLoginSignup, R.id.textViewUploadImage})
    void selectImage() {
        String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        PermissionHelper.checkPermissions(getActivity(), PermissionHelper.permissionPicture, new PermissionHelper.PermissionCallbacks() {
            @Override
            public void allOk() {
                dialogForCameraGallery();
            }

            @Override
            public void permissionNeeded(String[] permissions) {
                alertDialogWithOKButton(getString(R.string.cameraPermission));
            }

            @Override
            public void permissionDenied() {
                boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                boolean showRationale2 = shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
                boolean showRationale3 = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);
                if (!showRationale) {
                    alertDialogWithOKButton(getString(R.string.storagePermission));
                } else if (!showRationale2) {
                    alertDialogWithOKButton(getString(R.string.storagePermission));
                } else if (!showRationale3) {
                    alertDialogWithOKButton(getString(R.string.cameraPermission));
                }
            }

            @Override
            public void error() {

            }
        });

    }

    @OnClick(R.id.btnSignup)
    void signup() {
        String name = etName.getText().toString().trim();
        String zipCode = etZipCode.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String referedBy = etReferedBy.getText().toString().trim();
        String userType="";
        if(radioGroupUserType.getCheckedRadioButtonId()==R.id.radioChef){
            userType= AppUserType.chef;
        }else if(radioGroupUserType.getCheckedRadioButtonId()==R.id.radioFoodie){
            userType= AppUserType.foodie;
        }

        presenter.signup(name, zipCode, email, password, phone, address, pickupedImagePath, referedBy,userType);

    }

    @OnClick(R.id.textViewForgotPassword)
    void frgtPwd() {
        ForgotPasswordActivity.start(getActivity());
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

    @Override
    public void openStripeActivity(String stripeUrl) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.AUTH_LINK, stripeUrl);
        startActivityForResult(intent, WEBACTIVITY_REQUEST_CODE);
    }

    @Override
    public void stipeAccountCreated() {
        mListener.onLoginSuccess();
        getFragmentManager().popBackStack();
    }

    @Override
    public void onLoginSuccses() {
        mListener.onLoginSuccess();
        getFragmentManager().popBackStack();
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

        void onLoginSuccess();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEBACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String stripeAccID = data.getStringExtra(WebViewActivity.STRIPE_ACCOUNT_RESULT);
                presenter.updateStripeID(stripeAccID);
            } else {
                showToast("Please complete the stripe connection process.");
            }
        }
    }


    @Override
    public void onSampledImageAsyncPostExecute(File file) {
        hidePorgress();
        pickupedImagePath = Uri.parse(AppConst.LOCAL_FILE_PREFIX + file);
        simpleDraweeView.setImageURI(pickupedImagePath);
    }

}
