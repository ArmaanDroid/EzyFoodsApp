package sanguinebits.com.ezyfoods.changePassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BaseActivity;
import base.BaseView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sanguinebits.com.ezyfoods.R;
import utils.Validations;

public class ChangePasswordActivity extends BaseActivity implements BaseView, ChangePwdView {

    @BindView(R.id.editTextCurrentPassword)
    EditText editTextCurrentPassword;

    @BindView(R.id.textViewEmail)
    TextView textViewLabel;

    @BindView(R.id.editTextNewPassword)
    EditText editTextNewPassword;

    @BindView(R.id.editTextConfirmNewPassword)
    EditText editTextConfirmNewPassword;

    @BindView(R.id.btnChangePassword)
    Button buttonChangePassword;
    private ChangePwdPresenter presenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ChangePasswordActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);
        presenter = new ChangePwdPresenter(this, this);
    }

    @OnClick(R.id.imageView6)
    void back() {
        onBackPressed();
    }

    @OnClick(R.id.btnChangePassword)
    void changeUserPassword() {
        if (Validations.isFieldEmpty(editTextCurrentPassword.getText().toString().trim())) {
            showToast(getString(R.string.current_password_cannot_empty));
        } else if (Validations.isFieldEmpty(editTextNewPassword.getText().toString().trim())) {
            showToast(getString(R.string.new_password_cannt_empty));
        } else if (Validations.passwordDoesNotMatch(editTextNewPassword.getText().toString().trim(), editTextConfirmNewPassword.getText().toString())) {
            showToast(getString(R.string.password_does_not_match));
        } else if (editTextNewPassword.getText().toString().trim().contentEquals(editTextCurrentPassword.getText().toString().trim())) {
            showToast(getString(R.string.password_new_password_same));
        } else {
            WebRequestBody detailToServer = new WebRequestBody();
            detailToServer.setRequestName(RequestEndPoints.change_password);
            detailToServer.setPassword(editTextCurrentPassword.getText().toString());
            detailToServer.setNewPassword(editTextNewPassword.getText().toString());
            presenter.changePassword(detailToServer);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
