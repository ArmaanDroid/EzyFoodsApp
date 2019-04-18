package sanguinebits.com.ezyfoods.home.fragments.loginSignup;

import base.BaseView;

public interface LoginSignupView extends BaseView {
    void openStripeActivity(String stripeUrl);

    void stipeAccountCreated();

    void onLoginSuccses();
}
