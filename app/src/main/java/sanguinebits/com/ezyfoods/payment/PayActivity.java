package sanguinebits.com.ezyfoods.payment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import apis.RequestEndPoints;
import apis.WebRequestBody;
import base.BaseActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.ApiResponse;
import sanguinebits.com.ezyfoods.R;
import sanguinebits.com.ezyfoods.home.HomeActivity;
import utils.AppConst;

public class PayActivity extends BaseActivity implements PaymentView {
    public static final String CART_DETAILS = AppConst.PACKAGE + "cartDetails";
    public static final String CART_PRICE = AppConst.PACKAGE + "cartPrice";
    public static final String CART_ITEM_IDS = AppConst.PACKAGE + "cartItems";
    public static final String CART_COUNT_ARRAY = AppConst.PACKAGE + "itemsCount";
   private CardInputWidget mCardInputWidget;
    private TextView textViewMessage;

    private float price;
    private String[] itemIds;
    private String[] countArry;
    private PaymentPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        ButterKnife.bind(this);
        presenter=new PaymentPresenter(this,this);
        mCardInputWidget = findViewById(R.id.card_input_widget);
        textViewMessage = findViewById(R.id.textViewMessage);
        Intent intent = getIntent();
        if (intent != null) {
            price = intent.getFloatExtra(CART_PRICE, 0);
            itemIds = intent.getStringArrayExtra(CART_ITEM_IDS);
            countArry = intent.getStringArrayExtra(CART_COUNT_ARRAY);
        }

        textViewMessage.setText(TextUtils.concat("Enter your card details to make payment of $", String.valueOf(price)));
    }

    @OnClick(R.id.buttonPurchase)
    public void makePayment(View view) {
        Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            showToast("Invalid Card Data");
            return;
        }
        showProgress("Please wait...");
        Stripe stripe = new Stripe(this, "pk_test_RvJ5M4AjcJvhIEbp0IySMOhN");
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                        Log.d("token", "onSuccess: " + token);
                        hidePorgress();
                        presenter.buyDishes(token.getId(),countArry,itemIds);
                    }

                    public void onError(Exception error) {
                        hidePorgress();
                        // Show localized error message
                        Toast.makeText(PayActivity.this,
                                error.getLocalizedMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }



    public void back(View view) {
        onBackPressed();
    }
}
