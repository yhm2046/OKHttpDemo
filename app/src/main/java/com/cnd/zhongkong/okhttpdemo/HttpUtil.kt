package com.cnd.zhongkong.okhttpdemo

import android.util.Log
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