package com.cnd.zhongkong.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityMainBinding

/**
 * 网络应用demo
 */

class MainActivity : AppCompatActivity() {
    private val strURL="https://www.google.com/ncr"
    private val TAG="MainActivity_tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.wbShow.settings.javaScriptEnabled=true
        binding.wbShow.webViewClient= WebViewClient()
        binding.wbShow.loadUrl(strURL)
    }
}