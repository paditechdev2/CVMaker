package com.paditech.cvmarker.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.paditech.cvmarker.R;
import com.paditech.cvmarker.base.BaseActivity;
import com.paditech.cvmarker.utils.Constant;
import com.paditech.cvmarker.utils.LocaleUtil;
import com.paditech.cvmarker.utils.NetworkUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by USER on 17/6/2016.
 */
public class PrintActivity extends BaseActivity {

    private static final String PRINT_DIALOG_URL = "https://www.google.com/cloudprint/dialog.html";
    private static final String JS_INTERFACE = "AndroidPrintDialog";
    private static final String CONTENT_TRANSFER_ENCODING = "base64";

    private static final String ZXING_URL = "http://zxing.appspot.com";
    private static final int ZXING_SCAN_REQUEST = 65743;

    private static final String CLOSE_POST_MESSAGE_NAME = "cp-dialog-on-close";

    private Intent cloudPrintIntent;

    @InjectView(R.id.webview)
    WebView dialogWebView;


    @Override
    protected void initView() {
        if(NetworkUtils.isNetworkAvailable(this)) {
            init();
        }
    }

    private void init() {
        cloudPrintIntent = getIntent();
        WebSettings settings = dialogWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        dialogWebView.setWebViewClient(new PrintDialogWebClient());
        dialogWebView.addJavascriptInterface(new PrintDialogJavaScriptInterface(), JS_INTERFACE);
        dialogWebView.loadUrl(PRINT_DIALOG_URL);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_print;
    }

    @Override
    protected String getHeaderTitle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) return bundle.getString(Constant.RESUME_NAME);
        return "";
    }

    @Override
    protected Drawable getLeftDrawable() {
        return getResources().getDrawable(R.mipmap.ic_back);
    }

    @Override
    protected Drawable getRightDrawable() {
        return getResources().getDrawable(R.mipmap.ic_refresh);
    }

    @OnClick(R.id.btn_left)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.btn_right)
    public void onRefresh() {
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ZXING_SCAN_REQUEST && resultCode == RESULT_OK) {
            dialogWebView.loadUrl(data.getStringExtra("SCAN_RESULT"));
        }
    }

    @SuppressLint("JavascriptInterface")
    public final class PrintDialogJavaScriptInterface {
        public String getType() {
            return cloudPrintIntent.getType();
        }

        public String getTitle() {
            return cloudPrintIntent.getExtras().getString("title");
        }

        public String getContent() {
            try {
                ContentResolver contentResolver = getContentResolver();
                InputStream is = contentResolver.openInputStream(cloudPrintIntent.getData());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                byte[] buffer = new byte[4096];
                int n = is.read(buffer);
                while (n >= 0) {
                    baos.write(buffer, 0, n);
                    n = is.read(buffer);
                }
                is.close();
                baos.flush();

                return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        public String getEncoding() {
            return CONTENT_TRANSFER_ENCODING;
        }

        public void onPostMessage(String message) {
            if (message.startsWith(CLOSE_POST_MESSAGE_NAME)) {
                finish();
            }
        }
    }

    private class PrintDialogWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith(ZXING_URL)) {
                Intent intentScan = new Intent("com.google.zxing.client.android.SCAN");
                intentScan.putExtra("SCAN_MODE", "QR_CODE_MODE");
                try {
                    startActivityForResult(intentScan, ZXING_SCAN_REQUEST);
                } catch (ActivityNotFoundException error) {
                    view.loadUrl(url);
                }
            } else {

                view.loadUrl(url);
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (PRINT_DIALOG_URL.equals(url)) {
                // Submit print document.
                view.loadUrl("javascript:printDialog.setPrintDocument(printDialog.createPrintDocument("
                        + "window." + JS_INTERFACE + ".getType(),window." + JS_INTERFACE + ".getTitle(),"
                        + "window." + JS_INTERFACE + ".getContent(),window." + JS_INTERFACE + ".getEncoding()))");

                // Add post messages listener.
                view.loadUrl("javascript:window.addEventListener('message',"
                        + "function(evt){window." + JS_INTERFACE + ".onPostMessage(evt.data)}, false)");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocaleUtil.updateDefaultLanguage(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);

        LinearLayout mRootAds=(LinearLayout)findViewById(R.id.root_ads);
        adView.setAdUnitId(getResources().getString(R.string.banner_home_footer));
        mRootAds.addView(adView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AdRequest ar = new AdRequest.Builder().build();
        adView.loadAd(ar);
    }
}
