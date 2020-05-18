package com.recart
//import android.support.v7.app.AppCompatActivity

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.activity_webview.view.*


class webview : AppCompatActivity() {
     lateinit var progressBar:ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        web.webViewClient = MyWebViewClient(this)
        web.settings.displayZoomControls=true
        web.settings.setSupportZoom(true)
        web.settings.builtInZoomControls=true
        web.settings.supportZoom()
        web.settings.javaScriptEnabled=true
   //     web.settings.loadsImagesAutomatically=true
        web.invokeZoomPicker()
   //     web.getSettings().setUseWideViewPort(true)
        web.getSettings().setLoadWithOverviewMode(true)
         progressBar=findViewById<ProgressBar>(R.id.progress_bar)
        web.loadUrl(intent.getStringExtra("link"))
        if (web.canGoForward()) {
            forward.isVisible = true
            forward.setImageResource(R.drawable.ic_chevron_right_black_24dp)
            forward.setOnClickListener{
            web.goForward() } }
        else {
            forward.isVisible = true
            forward.setImageResource(R.drawable.ic_refresh_black_24dp)
         //   forward.isVisible = false
            forward.setOnClickListener{web.reload()}
        }
        if (web.canGoBack()) {
            backward.isVisible = true
            backward.setImageResource(R.drawable.ic_chevron_left_black_24dp)
            backward.setOnClickListener{
            web.goBack() } }
        else {            backward.isVisible = true
            backward.setImageResource(R.drawable.ic_close_black_24dp)
            backward.setOnClickListener{ onBackPressed()}
        }
    }



    override fun onBackPressed() {
        if (web.canGoBack())
            web.goBack()
        else
        super.onBackPressed()
    }
    class MyWebViewClient internal constructor(private val activity: Activity) : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            Toast.makeText(activity,"overridden by $url",Toast.LENGTH_SHORT).show()
            activity.progress_bar.visibility=View.VISIBLE
            webView.loadUrl(url)
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
         //   Toast.makeText(activity, "Got Error! $error"+ view?.originalUrl.toString(), Toast.LENGTH_SHORT).show()
            super.onReceivedError(view, request, error)
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
  //          activity?.progress_bar?.isVisible=false
            super.onPageCommitVisible(view, url)
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            activity.progress_bar.visibility=View.VISIBLE
            if (view?.canGoForward()!!) {
                activity.forward.isVisible = true
                activity.forward.setImageResource(R.drawable.ic_chevron_right_black_24dp)
                activity.forward.setOnClickListener{
                    view.goForward() } }
            else{
                activity.forward.isVisible = true
                activity.forward.setImageResource(R.drawable.ic_refresh_black_24dp)
                //   forward.isVisible = false
                activity.forward.setOnClickListener{activity.web.reload()}
            }
            if (view.canGoBack()) {
                activity.backward.isVisible = true
                activity.backward.setImageResource(R.drawable.ic_chevron_left_black_24dp)
                activity.backward.setOnClickListener{
                    view.goBack() } }
            else
            {            activity.backward.isVisible = true
                activity.backward.setImageResource(R.drawable.ic_close_black_24dp)
                activity.backward.setOnClickListener{ activity.onBackPressed()}
            }
            super.onPageStarted(view, url, favicon)
        }
        override fun onPageFinished(view: WebView?, url: String?) {
            activity?.progress_bar?.isVisible=false
       //     activity.swipeContainer.isRefreshing=false
            super.onPageFinished(view, url)
        }


        override fun onLoadResource(view: WebView?, url: String?) {
            if (url != null) {
                if(url.contains(".pdf",true)) {
                    if (!url.contains("https://docs.google.com/viewer?url=")){
                        var newurl="https://docs.google.com/viewer?url=" + url
                        view?.loadUrl(newurl)}
                }
        //            Toast.makeText(activity,"resource by $url",Toast.LENGTH_SHORT).show()
            }
            view?.progress_bar?.isVisible=false
            super.onLoadResource(view, url) }

    }
}
