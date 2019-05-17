package com.example.chen.simpleparkingapp.controller.login;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chen.simpleparkingapp.R;
import com.example.chen.taco.mvvm.BaseActivity;


public class UserNeedKonwActivity extends BaseActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_need_konw);

        initView();
    }

    private void initView() {
        initTitle();
        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){

        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl("https://www.baidu.com/");
    }

    private void initTitle() {
        View titleView = findViewById(R.id.titleUserNeed);
        titleView.findViewById(R.id.ll_title_left).setOnClickListener(this);
        TextView tvTitle = titleView.findViewById(R.id.tv_title_middle);
        tvTitle.setText("用户须知");
    }

    @Override
    protected void onDestroy() {
        if (webView!=null) {
            webView.clearCache(true);
            webView.clearHistory();
            webView.loadUrl("about:blank");
            webView.removeAllViews();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
}
