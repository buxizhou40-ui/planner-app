package com.zhou.planner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.webkit.WebViewAssetLoader;
import androidx.webkit.WebViewClientCompat;

/**
 * 任务规划 App
 *
 * 用 WebViewAssetLoader 把 assets 里的网页挂到一个虚拟的 https 域名下
 * （https://appassets.androidplatform.net/assets/...），而不是用 file:// 加载。
 *
 * 这一步很关键：
 *   - file:// 的 origin 是 "null"，Android WebView 对它限制 localStorage，
 *     导致"设置存不住、刷新就丢"；
 *   - 虚拟 https 域名有正常的 origin，localStorage / 缓存 / 相对路径图片都正常。
 */
public class MainActivity extends Activity {

    private static final String START_URL =
            "https://appassets.androidplatform.net/assets/index.html";

    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 把 /assets/ 路径映射到 APK 内的 assets 目录
        final WebViewAssetLoader loader = new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        web = new WebView(this);
        WebSettings s = web.getSettings();

        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);     // localStorage —— 数据能不能存住就靠它
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        s.setAllowContentAccess(true);
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        // 拦截请求，交给 AssetLoader 处理（这样 https 域名才能映射到本地文件）
        web.setWebViewClient(new WebViewClientCompat() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view,
                                                              WebResourceRequest request) {
                return loader.shouldInterceptRequest(request.getUrl());
            }
        });

        web.setBackgroundColor(0xFFF4F5F7);
        web.loadUrl(START_URL);
        setContentView(web);
    }

    /** 返回键：优先在网页里后退，退无可退再退出 App */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web != null && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (web != null) {
            web.destroy();
            web = null;
        }
        super.onDestroy();
    }
}
