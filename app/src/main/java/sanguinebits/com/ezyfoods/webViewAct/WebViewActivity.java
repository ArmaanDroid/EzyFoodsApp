package sanguinebits.com.ezyfoods.webViewAct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import sanguinebits.com.ezyfoods.R;
import utils.AppConst;

public class WebViewActivity extends AppCompatActivity {
    public static final String AUTH_LINK = AppConst.PACKAGE + ".link";
    public static final String IS_STANDARD = "is_standard_integration";
    public static final String STRIPE_ACCOUNT_RESULT =AppConst.PACKAGE +".stripe_accunt_id";
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        progressBar = findViewById(R.id.progress_circular);
        webView = findViewById(R.id.webView);
        Intent resultintent=new Intent();
        setResult(Activity.RESULT_CANCELED,resultintent);
        Intent intent = getIntent();

        if (intent != null) {
//            if (intent.getBooleanExtra(IS_STANDARD, false)) {
//                String link = "https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_ELvgD8LYJuAw50AtdbKIVQul03IcN64L&scope=read_write";
//                webView.loadUrl(link);
//
//            } else {
//                String link = "https://connect.stripe.com/express/oauth/authorize?redirect_uri=http://18.223.221.135/MidoFood/redirect.php&client_id=ca_ELvgD8LYJuAw50AtdbKIVQul03IcN64L&state={STATE_VALUE}";
//                webView.loadUrl(link);
//
//            }

            String link = intent.getStringExtra(AUTH_LINK);
            webView.loadUrl(link);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
//        webView.loadUrl(link);

        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
//
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (Uri.parse(url).getHost().equals("https://www.example.com")) {
//                // This is my website, so do not override; let my WebView load the page
//                return false;
//            }
//            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
//            return true;
            return false;
        }
    }

    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String error, String accountId) {
            if (error.equalsIgnoreCase("true")) {
                Toast.makeText(mContext, "Please try again", Toast.LENGTH_SHORT).show();

            } else {
                Intent intent=new Intent();
                intent.putExtra(WebViewActivity.STRIPE_ACCOUNT_RESULT,accountId);
                setResult(Activity.RESULT_OK,intent);
                WebViewActivity.this.finish();
            }
        }
    }

}
