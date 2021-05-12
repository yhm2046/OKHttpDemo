package com.cnd.zhongkong.okhttpdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityHttpURLConnectionBinding
import okio.IOException
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import kotlin.concurrent.thread

/**
 * HttpURLConnection demo
 */
class HttpURLConnectionActivity : AppCompatActivity() {
    private val urlAddressFront = "http://tcc.taobao.com"
    private val urlAddressMethod = "/cc/json/mobile_tel_segment.htm"
    private val urlString = "http://www.51work6.com/service/mynotes/WebService.php"
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
//            val response=HttpUtil.sendHttpRequst(strURL,object:HttpCallbackListener{
//                override fun onFinish(responese: String) {
//                    var connection:HttpURLConnection?=null
//                    try {
//                        val response=StringBuilder()
//                        val url = URL(strURL)
//                        connection=url.openConnection() as HttpURLConnection
//                        connection.connectTimeout=PORT80
//                        connection.readTimeout=PORT80
//                        val input=connection.inputStream
//                        val reader=BufferedReader(InputStreamReader(input)) //读取获取的输入流
//                        reader.use {
//                            reader.forEachLine { response.append(it) }
//                        }
//                        showResponse(response.toString())
//                    }catch (e:Exception ){
//                        e.printStackTrace()
//                        Log.i(TAG,"HttpCallbackListener.onFinish:${e.toString()}")
//                    }finally {
//                        connection?.disconnect()
//                    }//end try
//                }
//
//                override fun onError(e: java.lang.Exception) {
//                    Log.i(TAG,"HttpCallbackListener.onError:${e.toString()}")
//                }
//            })
            Log.i(TAG, "click...")
            request("000")  //需要获取的电话号码
        }
    }//end onCreate

//    HttpURLConnection求get
    private fun request(s: String){
    Log.d("TAGPOST", "aaaaaaaaaaaaaaaaaaaa")
        thread {
            Log.d("TAGPOST", "bbbbbbbbbbbbbbbbbbbbbbbb")
            try {
                Log.d("TAGPOST", "ccccccccccccccccccccccccccccc")
                val url = URL(urlAddressFront + urlAddressMethod) //这里是服务器地址，不是完整的url地址
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.doInput = true //打开httpURLConnection的输入流
                httpURLConnection.doOutput = true //打开httpURLConnection的输出流
                httpURLConnection.requestMethod = "POST" //设置请求方法
                httpURLConnection.useCaches = false //设置不使用缓存，POST请求使用缓存可能会出现一些异常
                httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8") //设置请求头
                httpURLConnection.setRequestProperty("Content-Type", "application/x-form-urlencoded") //设置请求头
                httpURLConnection.connect() //连接

                //把我们的请求参数通过输出流的方式写给服务器
                val outputStream = DataOutputStream(httpURLConnection.outputStream)
                val content = "?tel=$s" //要给服务器写的参数
                outputStream.writeBytes(content) //以字节的形式写出去
                outputStream.flush() //刷新
                outputStream.close() //关闭流
                Log.d("TAGPOST", "1111111111111111111111111111111111111111111111")
                //服务器响应，与doGet相同
                if (httpURLConnection.responseCode == 200) {        //服务器响应码
                    Log.d("TAGPOST", "222222222222222222222222222222222")
                    val inputStream = httpURLConnection.inputStream //输入流
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream)) //BufferedReader构造方法
                    val stringBuffer = StringBuffer() //用来存储读的结果
                    var readLine: String? = "" //存储每行的结果
                    Log.d("TAGPOST", "333333333333333333333333333333333333333")
                    while (bufferedReader.readLine().also { readLine = it } != null) {     //按行读取，每行读到的字符串放到readLine中
                        Log.d("TAGPOST", "44444444444444444444444444444444444444444")
                        stringBuffer.append(readLine)
                        inputStream.close()
                        bufferedReader.close()
                        httpURLConnection.disconnect()
                        Log.d("TAGPOST", "5555555555555555555555555555555")
                        Log.d("TAGPOST", stringBuffer.toString())
                        Log.d("TAGPOST", "66666666666666666666666666666")
                    }
                } else {
                    Log.d("TAGPOST", "failed")
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }//end request

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
                showResponse(response.toString())   //异步UI显示
            }catch (e: Exception){
                e.printStackTrace()
                Log.i(TAG, "sendRequestWithHttpURLConnection:${e.toString()}")
            }finally {
                connection?.disconnect()
            }//end try
        }// end thread
    }//end sendRequestWithHttpURLConnection
    public fun showResponse(response: String){  //异步消息处理
        runOnUiThread {
            val binding =ActivityHttpURLConnectionBinding.inflate(layoutInflater)   //必须要载入layoutInflater，不能全局，暂时没有其他解决办法
            setContentView(binding.root)
            binding.tvResponseText.text=response    //UI操作，显示结果
        }
    }
}