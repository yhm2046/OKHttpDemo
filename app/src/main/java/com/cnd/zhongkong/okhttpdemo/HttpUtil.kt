package com.cnd.zhongkong.okhttpdemo

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * 网络操作公共类
 */
object HttpUtil {
    private val TAG="HttpUtil_wp"
    private val PORT=8000
//    OKhttp请求方法，比普通http请求方法简单
    fun sendOkHttpRequest(address: String,callback:okhttp3.Callback){
        val client=OkHttpClient()
        val request=Request.Builder().url(address).build()
        client.newCall(request).equals(callback)    //内部已经开好子线程，在子线程中执行HTTP请求，结果返回callback
    }
//    普通http请求方法
    fun sendHttpRequst(address:String, listener:HttpCallbackListener){
        thread {
            var connection:HttpURLConnection?=null
            try {
                val response=StringBuilder()
                val url= URL(address)
                connection=url.openConnection() as HttpURLConnection
                connection.connectTimeout= PORT
                connection.readTimeout= PORT
                val input=connection.inputStream
                val readr=BufferedReader(InputStreamReader(input))
                readr.use {
                    readr.forEachLine { response.append(it) }
                }
                listener.onFinish(response.toString())  //子线程无return，服务器响应数据传入onFinish
            }catch (e:Exception){
                e.printStackTrace()
                listener.onError(e) //出现错误
                Log.i(TAG,"HttpUtil err:${e.toString()}")
            }finally {
                connection?.disconnect()
            }
        }
    }
}