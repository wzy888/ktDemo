package com.zhouzi.retrofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonSyntaxException
import com.orhanobut.logger.Logger
import com.zhouzi.retrofit.http.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException

class MainActivity : AppCompatActivity() {

    val coroutineScope = CoroutineScope(Dispatchers.Main)
//    https://gank.io/api/v2/data/category/Girl/type/Girl/page/1/count/10

    val categroy: String = "Girl"
    val type: String = "Girl"
    val page: Int = 1
    val count: Int =10
    private lateinit var result: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//         launch(
//            block = {
//
//                    RetrofitFactory.getInstence().api().login(categroy, type, page, count)
//                        .result()!!
//
//            },
//
//
//            error = {
//                Logger.d(it)
//            }
//
//
//        )

        GlobalScope.launch(Dispatchers.Main) {
            try {
                var  result =     RetrofitFactory.getInstence().api().login(categroy, type, page, count)
                    .result()!!
//
                Logger.e("res 1===", "Dispatchers.Main isMainThread ${result.toString()}")//输出true
                tvBtn.text = result.toString()
            }catch (e:Exception){
                tvBtn.text = e.toString()
            }
        }


//        getData()


//        ————————————————
//        版权声明：本文为CSDN博主「夜枫狂」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//        原文链接：https://blog.csdn.net/weixin_44407870/article/details/86767075

    }

    fun getData() {
        //启动一个协程
        coroutineScope.launch {
            //因为还是在主线程,所以可以直接操作UI
//                progress_bar.visibility = View.VISIBLE
            /*因为Android规定网络请求必须在子线程,
            所以这里我们通过withContext获取请求结果,
            通过调度器Dispatcher切换到IO线程,
            这个操作会挂起当前协程,但是不会阻塞当前线程*/
          withContext(Dispatchers.IO) {
                /*这里已经是在子线程了,所以使用的是excute()而不是enqueue()
                execute()返回的是Response类型
                  withContext会把闭包最后一行代码的返回值返回出去
                  所以上面的result就是Response类型*/
//                    retrofit.getListRepos(editText.text.toString()).execute()
                val result =
                    RetrofitFactory.getInstence().api().login(categroy, type, page, count)
                        .result()

                Logger.d("result-->", result.toString())
                //上下文切换到主线程
                GlobalScope.launch(Dispatchers.Main){
                    Logger.i("res ===", "Dispatchers.Main isMainThread ${result.toString()}")//输出true
                }
            }
            //上面请求结束之后,又返回到了主线程
            //打一个log,用于一会儿测试看看activity关闭了,协程还会不会继续执行

//                progress_bar.visibility = View.GONE
//                if (result.isSuccessful) {
            //因为返回到了主线程,所以可以直接操作UI
//                    result.body()?.forEach {
//                        //为了简单起见,我们只打印请求结果的一个字段
//                        tv_content.append("${it.fullName}\n")
//                    }
//                }
        }

    }

    fun launch(
        start: (() -> Unit)? = null,
        block: suspend () -> Unit,
        error: ((HttpException) -> Unit)? = null,
        finally: (() -> Unit)? = null
    ) =
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    block()
                }



            } catch (e: Exception) {
                Logger.d(e)
                when (e) {
                    is CodeException -> {
                        error(e)
                    }
                    is ConnectException -> showToast("网络连接失败：请检查您的网络后重试\n详细信息：${e.message}")
                    is HttpException ->
                        when (e.code()) {
                            404 -> showToast("网络连接失败：您所请求的资源无法找到\n错误码：${e.code()}\n详细信息：${e.message()}")
                            500 -> showToast("网络连接失败：服务器异常，请稍后再试\n错误码：${e.code()}\n详细信息：${e.message()}")
                            else -> showToast("网络连接失败：网络异常，请稍后再试\n错误码：${e.code()}\n详细信息：${e.message()}")
                        }
                    is IOException -> {
                        e.printStackTrace()
                    }
                    is JSONException -> showToast("数据错误：服务器数据异常\n详细信息：${e.message}")
                    is JsonSyntaxException -> showToast("数据错误：服务器数据异常\n详细信息：${e.message}")
                    is NoSuchFileException -> showToast("文件不存在")
                    else -> {
                        showToast("未知错误：未知原因错误\n详细信息：${e.message}")
                        Logger.d(e.message)
                    }
                }
            } finally {
                finally?.invoke()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }


}