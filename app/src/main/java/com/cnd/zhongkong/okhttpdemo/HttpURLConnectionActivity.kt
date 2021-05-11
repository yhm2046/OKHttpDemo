package com.cnd.zhongkong.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityHttpURLConnectionBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * HttpURLConnection demo
 */
class HttpURLConnectionActivity : AppCompatActivity() {

    private val strURL="https://www.google.com/ncr"
    private val TAG="HttpURLConnectionActivity_tay"
    private val PORT80=8000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =ActivityHttpURLConnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnShow.setOnClickListener {
//            sendRequestWithHttpURLConnection()    //单独一个http请求
//            使用通用方法发起http请求
            val response=HttpUtil.sendHttpRequst(strURL,object:HttpCallbackListener{
                override fun onFinish(responese: String) {
                    var connection:HttpURLConnection?=null
                    try {
                        val response=StringBuilder()
                        val url = URL(strURL)
                        connection=url.openConnection() as HttpURLConnection
                        connection.connectTimeout=PORT80
                        connection.readTimeout=PORT80
                        val input=connection.inputStream
                        val reader=BufferedReader(InputStreamReader(input)) //读取获取的输入流
                        reader.use {
                            reader.forEachLine { response.append(it) }
                        }
                        showResponse(response.toString())
                    }catch (e:Exception ){
                        e.printStackTrace()
                        Log.i(TAG,"HttpCallbackListener.onFinish:${e.toString()}")
                    }finally {
                        connection?.disconnect()
                    }//end try
                }

                override fun onError(e: java.lang.Exception) {
                    Log.i(TAG,"HttpCallbackListener.onError:${e.toString()}")
                }
            })
            Log.i(TAG,"click...")
        }
    }//end onCreate

    private fun sendRequestWithHttpURLConnection(){
        thread {    //开启子线程发送网络请求
            var connection:HttpURLConnection?=null
            try {
                val response=StringBuilder()
                val url = URL(strURL)
                connection=url.openConnection() as HttpURLConnection
                connection.connectTimeout=PORT80
                connection.readTimeout=PORT80
                val input=connection.inputStream
                val reader=BufferedReader(InputStreamReader(input)) //读取获取的输入流
                reader.use {
                    reader.forEachLine { response.append(it) }
                }
                showResponse(response.toString())
            }catch (e:Exception ){
                e.printStackTrace()
                Log.i(TAG,"sendRequestWithHttpURLConnection:${e.toString()}")
            }finally {
                connection?.disconnect()
            }//end try
        }// end thread
    }//end sendRequestWithHttpURLConnection
    public fun showResponse(response:String){  //异步消息处理
        runOnUiThread {
            val binding =ActivityHttpURLConnectionBinding.inflate(layoutInflater)   //必须要载入layoutInflater，不能全局，暂时没有其他解决办法
            setContentView(binding.root)
            binding.tvResponseText.text=response    //UI操作，显示结果
        }
    }
}