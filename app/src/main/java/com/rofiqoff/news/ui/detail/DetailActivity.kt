package com.rofiqoff.news.ui.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.rofiqoff.news.databinding.ActivityDetailBinding
import com.rofiqoff.news.ui.base.BaseActivity
import com.rofiqoff.news.utils.gone
import com.rofiqoff.news.utils.visible

class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = ActivityDetailBinding::inflate

    override fun initView() {
        val url = intent?.getStringExtra(PARAM_URL).orEmpty()
        setUpWebView(url)

        binding.ivBack.setOnClickListener { finish() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView(url: String) = with(binding) {
        wvContent.settings.apply {
            javaScriptEnabled = true
        }
        wvContent.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                pbLoading.visible()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pbLoading.gone()
            }
        }
        wvContent.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pbLoading.isVisible = newProgress != 100
            }
        }
        wvContent.loadUrl(url)
    }

    companion object {
        const val PARAM_URL = "param-url"
    }

}
