package sanguinebits.com.ezyfoods.forgotPwd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sanguinebits.com.ezyfoods.R;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPwdView {

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgotPasswordActivity.class);
        context.startActivity(starter);
    }
    @BindView(R.id.editText)
    EditText editTextEmail;
    private ForgetPwdPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        presenter=new ForgetPwdPresenter(this,this);
    }

    @OnClick(R.id.imageView6)
    void back(){
        onBackPressed();
    }

    @OnClick(R.id.submit)
    void sumbit(){
        presenter.makeForgetPwdRequest(editTextEmail.getText().toString().trim());
    }
}
