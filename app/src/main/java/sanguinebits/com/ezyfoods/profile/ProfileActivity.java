package sanguinebits.com.ezyfoods.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.user.User;
import model.user.UserRepository;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.changePassword.ChangePasswordActivity;
import sanguinebits.com.ezyfoods.home.HomeActivity;
import sanguinebits.com.ezyfoods.webViewAct.WebViewActivity;
import utils.AppConst;
import utils.AppUserType;
import utils.SharedPrefs;

public class ProfileActivity extends BaseActivity implements ProfileView {

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etZipCode)
    EditText etZipCode;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.becomeChef)
    Button btnBecomeChef;

    @BindView(R.id.imageViewLoginSignup)
    SimpleDraweeView simpleDraweeView;

    private static final int WEBACTIVITY_REQUEST_CODE = 2134;
    private ProfilePresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        presenter = new ProfilePresenter(this, this);
        setData(UserRepository.getUser(SharedPrefs.getInstance(this)));
    }

    private void setData(User user) {
        etName.setText(user.getName());
        etZipCode.setText(user.getZipcode());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());

        if (user.getProfilePic() != null && !user.getProfilePic().isEmpty()) {
            String uri = AppConst.USER_IMAGE_BASE_URL + user.getProfilePic();
            simpleDraweeView.setImageURI(uri);
        }

        if (SharedPrefs.getInstance(this).getString(UserRepository.KEY_USER_TYPE).equalsIgnoreCase(AppUserType.foodie))
            btnBecomeChef.setVisibility(View.VISIBLE);
        else
            btnBecomeChef.setVisibility(View.GONE);

    }

    @OnClick(R.id.imageView6)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.becomeChef)
    void becomeChef() {
        String stripeUrl = SharedPrefs.getInstance(this).getString(AppConst.KEY_STRIPE_URL);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.AUTH_LINK, stripeUrl);
        startActivityForResult(intent, WEBACTIVITY_REQUEST_CODE);
    }

    @OnClick(R.id.btnChangePassword)
    void openChangePwd() {
        ChangePasswordActivity.start(this);
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
    public void stipeAccountCreated() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
