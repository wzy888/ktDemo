package com.zhouzi.retrofit.http

import retrofit2.Call
import retrofit2.http.*

/**
 * Created by ZhouZi on 2020/5/27.
 * time:11:20
 * ----------Dragon be here!----------/
 * 　　　┏┓　　 ┏┓
 * 　　┏┛┻━━━┛┻┓━━━
 * 　　┃　　　　　 ┃
 * 　　┃　　　━　  ┃
 * 　　┃　┳┛　┗┳
 * 　　┃　　　　　 ┃
 * 　　┃　　　┻　  ┃
 * 　　┃　　　　   ┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛━━━━━
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━神兽出没━━━━━━━━━━━━━━
 */
interface ApiFunction {

//    https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10
//    https://gank.io/api/v2/data/category/<category>/type/<type>/page/<page>/count/<count>

    @GET("v2/data/category/{category}/type/{type}/page/{page}/count/{count}")
    suspend fun login(
        @Path("category") category: String, @Path("type") type: String
        , @Path("page") page: Int, @Path("count") count: Int
    ): BaseResponse<Any>

}