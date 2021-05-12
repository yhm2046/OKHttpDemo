package com.cnd.zhongkong.okhttpdemo

import retrofit2.Call
import retrofit2.http.GET

/**
 * retrofit需要的接口
 */
interface AppService {
    @GET("get_data.json")   //注解，调用getAppData()时候会发起get请求,注意格式不能有多余标点，否则报错：
    // com.google.gson.stream.MalformedJsonException: Use JsonReader.setLenient(true) to accept malformed JSON at line 3 column 53 path $[3]
    fun getAppData():Call<List<MyApp>> //泛型指定服务器响应数据应该转换成什么对象
}