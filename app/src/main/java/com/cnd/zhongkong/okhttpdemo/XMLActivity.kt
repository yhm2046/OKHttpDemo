package com.cnd.zhongkong.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.xml.sax.InputSource
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.lang.Exception
import java.net.ContentHandler
import javax.xml.parsers.SAXParserFactory
import kotlin.concurrent.thread

/**
 * xml解析
 */
class XMLActivity : AppCompatActivity() {
    private val strUrl="http://127.0.0.1/get_data.xml"  //本机地址
    private val strUrl2="http://192.168.1.187/get_data.xml"
    private val strUrl3="http://192.168.1.187/get_data.json"
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
//            unSafeOkHttpClient
            val client=OkHttpClient()
//            val client=unSafeOkHttpClient()
            val request= Request.Builder().url(strUrl3).build() //使用127.0.0.1会报错：java.net.ConnectException: Failed to connect to /127.0.0.1:80
            /*
            * 使用https报错：Trust anchor for certification path not found
            * */
            val response=client.newCall(request).execute()
            val responseData=response.body?.string()    //服务器返回的数据
            if (responseData!=null){
                parseJSONWithGSON(responseData) //gson解析
//                parseJSONWithJSONObject(responseData) //json解析
//                parseXMLWithSAX(responseData)    //sax解析
//                parseXMLWithPull(responseData) //pull解析
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.i(TAG,"err-->${e.toString()}")
        } }
    }

//    gson解析方法
    private fun parseJSONWithGSON(jsonData: String){
        val gson= Gson()
        val typeOf=object: TypeToken<List<MyApp>>(){}.type
        val appList=gson.fromJson<List<MyApp>>(jsonData,typeOf)
        for (app in appList){
            Log.i(TAG,"id-->${app.id}")
            Log.i(TAG,"name-->${app.name}")
            Log.i(TAG,"version-->${app.version}")
        }
    }

    //pull解析方法
    private fun parseXMLWithPull(xmlData :String){
        try {
            val factory=XmlPullParserFactory.newInstance()
            val xmlPullParser=factory.newPullParser()
            xmlPullParser.setInput(StringReader(xmlData))
            var eventType=xmlPullParser.eventType   //获取解析事件
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

    //sax解析方法
    private fun parseXMLWithSAX(xmlData: String){
        try {
            val factory = SAXParserFactory.newInstance()
            val xmlReader = factory.newSAXParser().getXMLReader()
            val handler = MyHandler()
            // 将ContentHandler的实例设置到XMLReader中
            xmlReader.contentHandler = handler
            // 开始执行解析
            xmlReader.parse(InputSource(StringReader(xmlData)))
        }catch (e:Exception){
            e.printStackTrace()
            Log.i(TAG,"err:${e.toString()}")
        }
    }

    //json解析方法
    private fun parseJSONWithJSONObject(jsonData:String){
        try {
            val jsonArray=JSONArray(jsonData)
            for (i in 0 until jsonArray.length()){
                val jsonObject=jsonArray.getJSONObject(i)
                val id=jsonObject.getString("id")
                val name=jsonObject.getString("name")
                val version=jsonObject.getString("version")
                Log.i(TAG,"id------->$id")
                Log.i(TAG,"name------->$name")
                Log.i(TAG,"version------->$version")
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.i(TAG,"parseJSONWithJSONObject err-->${e.toString()}")
        }
    }
}