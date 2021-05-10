package com.cnd.zhongkong.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.lang.Exception
import kotlin.concurrent.thread

/**
 * xml解析
 */
class XMLActivity : AppCompatActivity() {
    private val strUrl="http://127.0.0.1/get_data.xml"  //本机地址
    private val strUrl2="http://192.168.1.187/get_data.xml"
    private val NULL=""
    private val TAG="XMLActivity_wp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sendRequestWithOkHttp()
    }

    //发送请求方法
    private fun sendRequestWithOkHttp(){
        thread { try {
            val client=OkHttpClient()
            val request= Request.Builder().url(strUrl2).build() //使用127.0.0.1会报错：java.net.ConnectException: Failed to connect to /127.0.0.1:80
            val response=client.newCall(request).execute()
            val responseData=response.body?.string()
            if (responseData!=null)parseXMLWithPull(responseData)
        }catch (e:Exception){
            e.printStackTrace()
            Log.i(TAG,"err-->${e.toString()}")
        } }
    }

    //xml解析方法
    private fun parseXMLWithPull(xmlData :String){
        try {
            val factory=XmlPullParserFactory.newInstance()
            val xmlPullParser=factory.newPullParser()
            xmlPullParser.setInput(StringReader(xmlData))
            var eventType=xmlPullParser.eventType
            var id=NULL
            var name=NULL
            var version=NULL
            while (eventType!=XmlPullParser.END_DOCUMENT){
                val nodeName=xmlPullParser.name
                when(eventType){
                    XmlPullParser.START_TAG -> {    //开始解析某个节点
                        when(nodeName){
                            "id"->id=xmlPullParser.nextText()
                            "name"->name=xmlPullParser.nextText()
                            "version"->version=xmlPullParser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG->{    //完成解析某个节点
                        if ("app"==nodeName){
                            Log.i(TAG,"id-->$id")
                            Log.i(TAG,"name-->$name")
                            Log.i(TAG,"version-->$version")
                        }
                    }
                }//end when
                eventType=xmlPullParser.next()
            }//end while
        }catch (e:Exception){
            e.printStackTrace()
            Log.i(TAG,"err-->${e.toString()}")
        }
    }
}