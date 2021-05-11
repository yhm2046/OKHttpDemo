package com.cnd.zhongkong.okhttpdemo

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.lang.StringBuilder


class MyHandler: DefaultHandler() {
    private val TAG="MyHandler_wp"
    private var nodeName=""
    private lateinit  var id:StringBuilder
    private lateinit  var name:StringBuilder
    private lateinit  var version:StringBuilder
    override fun startDocument() {
//        开始解析,初始化StringBuilder对象
        id= StringBuilder()
        name=StringBuilder()
        version=StringBuilder()
    }

    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        //解析当前节点
        nodeName=localName
//        记录当前节点
        Log.i(TAG,"uri:$uri")
        Log.i(TAG,"localName:$localName")
        Log.i(TAG,"qName:$qName")
        Log.i(TAG,"attributes:$attributes")
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
//        获取节点内容
        when(nodeName){
            "id"->id.append(ch,start,length)
            "name"->name.append(ch,start,length)
            "version"->version.append(ch,start,length)
        }
    }

    override fun endElement(uri: String, localName: String, qName: String) {
//        完成解析节点
        if ("app"==localName){
            Log.i(TAG,"id----------->${id.toString().trim()}")
            Log.i(TAG,"name----------->${name.toString().trim()}")
            Log.i(TAG,"version----------->${version.toString().trim()}")
//            清空StringBuilder
            id.setLength(0)
            name.setLength(0)
            version.setLength(0)
        }
    }

    override fun endDocument() {
//        完成整个解析
    }
}