package com.cnd.zhongkong.okhttpdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cnd.zhongkong.okhttpdemo.databinding.ActivityHttpURLConnectionBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder

/**
 * Retrofix 网络库用法
 * https://github.com/square/retrofit
 */
class RetrofitActivity : AppCompatActivity() {
    private val strURL2="http://192.168.1.187/"
    private val strURL="http://www.google.com"
    private val TAG="OKHttpActivity_tag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val blinding=ActivityHttpURLConnectionBinding.inflate(layoutInflater)
        setContentView(blinding.root)
        blinding.btnShow.setOnClickListener {
            Log.i(TAG,"click..")
            Log.i(TAG,"strURL2----->$strURL2")
            val retrofit= Retrofit.Builder().baseUrl(strURL2).addConverterFactory(
                GsonConverterFactory.create()).build()
            val appService=retrofit.create(AppService::class.java)
            appService.getAppData().enqueue(object :Callback<List<MyApp>>{
                override fun onResponse(call: Call<List<MyApp>>, response: Response<List<MyApp>>) {
                    val list=response.body()
                    if (list!=null){
                        for(app in list){
                            Log.i(TAG,"id--------->${app.id}")
                            Log.i(TAG,"name--------->${app.name}")
                            Log.i(TAG,"version--------->${app.version}")
                        }
                    }
                }

                override fun onFailure(call: Call<List<MyApp>>, t: Throwable) {
                    t.printStackTrace()
                    Log.i(TAG,"onFailure--------->${t.toString()}")
//                     onFailure--------->com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 3 column 53 path $[3]
                }
            })
        }
    }
}