package com.cnd.zhongkong.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityHttpURLConnectionBinding
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread

/**
 * OKhttp demo:發送http請求功能
 */
class OKHttpActivity : AppCompatActivity() {
    private val strURL="https://www.google.com/ncr"
    private val strURL2="http://bing.com"
    private val TAG="OKHttpActivity_tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHttpURLConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnShow.setOnClickListener {
            Log.i(TAG,"click btn============")
            val response=HttpUtil.sendOkHttpRequest(strURL2,object:Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.i(TAG,"11111111111111111111111")
                    e.printStackTrace()
                    Log.i(TAG,"sendOkHttpRequest Callback err:${e.toString()}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i(TAG,"22222222222222222222")
                    val responseData=response.body?.string()    //get value
                    showResponse(responseData)
                }
            })
//            sendRequestWithOKHttp() //单独的okhttp请求方法
        }
    }

    public fun showResponse(response:String?){  //异步消息处理
        runOnUiThread {
            val binding =ActivityHttpURLConnectionBinding.inflate(layoutInflater)   //必须要载入layoutInflater，不能全局，暂时没有其他解决办法
            setContentView(binding.root)
            binding.tvResponseText.text=response    //UI操作，显示结果
        }
    }

    private fun sendRequestWithOKHttp(){
        thread {    //子线程发出请求
            try {

                val client=OkHttpClient()
                val request=Request.Builder().url(strURL).build()   //http request
                val response=client.newCall(request).execute()  //send request and get response
                val responseData=response.body?.string()    //get value
                if (response!=null){showResponse(responseData)}
            }catch (e:Exception){
                e.printStackTrace()
                Log.i(TAG,"err:${e.toString()}")
            }}

    }
}