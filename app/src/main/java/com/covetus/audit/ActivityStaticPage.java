package com.covetus.audit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import ABS_CUSTOM_VIEW.TextViewSemiBold;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static ABS_HELPER.CommonUtils.hidePDialog;
import static ABS_HELPER.CommonUtils.showPDialog;

/**
 * Created by admin18 on 19/12/18.
 */

public class ActivityStaticPage extends Activity {
    @BindView(R.id.mDocWebview)
    WebView mDocWebview;
    @BindView(R.id.mTextTitle)
    TextViewSemiBold mTextTitle;
    @BindView(R.id.mImgBack)
    ImageView mImgBack;
    String mPage, mType;

    @OnClick(R.id.mImgBack)
    void mImgBack() {
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_pages);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPage = bundle.getString("mPage");
            mType = bundle.getString("mType");
            System.out.println("<><><mPage" + mPage);
        }
        mTextTitle.setText(mType);
        showPDialog(ActivityStaticPage.this);
        setmDocWebview(mPage);

    }

    public void setmDocWebview(String mDoc) {
        mDocWebview.getSettings().setJavaScriptEnabled(true);
        mDocWebview.getSettings().setSupportZoom(true);
        mDocWebview.loadUrl(mDoc);
        mDocWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                System.out.println("<><><>filurl" + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hidePDialog();
            }
        });


    }
}
