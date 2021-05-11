package com.cnd.zhongkong.okhttpdemo

import java.lang.Exception

/**
 * 回调接口
 */
interface HttpCallbackListener {
    fun onFinish(responese:String)  //响应请求的调用
    fun onError(e:Exception)    //网络出错的调用
}