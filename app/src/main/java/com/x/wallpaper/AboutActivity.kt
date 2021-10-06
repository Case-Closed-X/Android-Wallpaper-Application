package com.x.wallpaper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.x.wallpaper.databinding.ActivityAboutBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.concurrent.thread

@RequiresApi(Build.VERSION_CODES.R)
class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private val aboutList = ArrayList<AboutItem>()
    private val firstList = ArrayList<AboutItem>()

    init {
        initData()
    }

    private fun initData() {
        aboutList.add(AboutItem(R.drawable.ico_github,"开源项目","访问我的Github项目页"))
        aboutList.add(AboutItem(R.drawable.ico_outlook,"联系作者","给我的Outlook邮箱发送邮件"))
        aboutList.add(AboutItem(R.drawable.ico_qq,"个人主页","访问我的个人网站"))

        firstList.add(AboutItem(R.drawable.ico_version,"关于","APP Version 1.0"))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        // 开启Material动画
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val slide=Slide()
        slide.slideEdge=Gravity.BOTTOM
        //slide.duration=300

        window.apply {
            enterTransition = Slide()
            exitTransition = Slide()
            reenterTransition = Slide()
            returnTransition = Slide()
        }
        /*//设置进入的动画
        window.enterTransition = Slide() //Explode() Fade()
        //设置退出动画
        window.exitTransition = Slide()
        //重新进入界面的过渡动画
        window.reenterTransition = Slide()
        window.returnTransition = Slide()*/

        Utils.setStatusBarLight(window.decorView, true)

        val adapter = AboutAdapter(aboutList,true)
        binding.recyclerViewAbout.adapter = adapter

        val adapterFirst = AboutAdapter(firstList,false)
        binding.recyclerViewFirst.adapter = adapterFirst

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewAbout.layoutManager = layoutManager
        binding.recyclerViewFirst.layoutManager = LinearLayoutManager(this)

        binding.imageFilterViewSera.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    //binding.imageFilterViewSera.saturation = 1.3f
                    v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(150).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    //binding.imageFilterViewSera.saturation = 1f
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                }
            }
            true
        }
    }
}

class AboutAdapter(private val aboutList : List<AboutItem>,private val isAbout : Boolean)
    : RecyclerView.Adapter<AboutAdapter.ViewHolder>(){

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val image : ImageFilterView = view.findViewById(R.id.imageFilterViewAbout)
        val title : TextView = view.findViewById(R.id.textViewAboutTitle)
        val content : TextView = view.findViewById(R.id.textViewAboutContent)
        val cardView : CardView = view.findViewById(R.id.cardViewAbout)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutAdapter.ViewHolder {
        //待项目开源后，改变此处url为新项目的地址
        val urlGithub =
            "https://github.com/Case-Closed-X/Android-Wallpaper-Application"
        val urlOutlook = "mailto:CaseClosedX@outlook.com"
        val urlHomePage = "https://case-closed-x.github.io/"

        val view = LayoutInflater.from(parent.context).inflate(R.layout.about_item,parent,false)

        val viewHolder = ViewHolder(view)

            viewHolder.itemView.setOnTouchListener { v, event ->
                val position = viewHolder.adapterPosition
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(when(position){
                    0-> urlGithub
                    1-> urlOutlook
                    2-> urlHomePage
                    else -> null
                }))
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#1A000000"))
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#00000000"))
                    }
                    MotionEvent.ACTION_UP -> {
                        viewHolder.cardView.setCardBackgroundColor(Color.parseColor("#00000000"))
                        if (isAbout){
                            parent.context.startActivity(intent)
                        }
                        else if (position == 0){// 使用网络获取版本，可以设置flag获取时无效点击
                            // 获取成功打开github下载页面或者弹出对话框，直接创建下载任务
                            retrofit()
                        }
                    }
                }
                true
            }

        return viewHolder
    }

    private fun retrofit() {
        val versionService = ServiceCreator.create<VersionService>()
        versionService.getVersionData().enqueue(object : Callback<List<AppVersion>>{
            override fun onResponse(
                call: Call<List<AppVersion>>,
                response: retrofit2.Response<List<AppVersion>>
            ) {
                val list = response.body()
                if (list!=null){
                    for (version in list){
                        if (version.version == "1.0") {
                            Toast.makeText(MyApplication.context, "已经是最新版本", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else{
                            Toast.makeText(MyApplication.context, "有新版本更新", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Case-Closed-X/Android-Wallpaper-Application/releases/"))
                            MyApplication.context.startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<AppVersion>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(MyApplication.context, "获取更新超时，请检查网络连接", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

/*    private fun okHttp() {
        thread {
            try {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://case-closed-x.github.io/Json/wallpaper_version.json")
                    .build()
                val response = client.newCall(request).execute()
                val responseData = response.body?.string()
                if (responseData!=null){
                    parseJSONWithGSON(responseData)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }*/

/*    private fun parseJSONWithGSON(jsonData: String) {
        val gson = Gson()
        val typeOf = object : TypeToken<List<AppVersion>>() {}.type
        val versionList = gson.fromJson<List<AppVersion>>(jsonData,typeOf)
        for(version in versionList){
            if (version.version=="1.0")
            {

            }
        }
    }*/

/*    private fun httpURLConnection() {
        thread {
            var connection:HttpsURLConnection ?= null

            var response = StringBuilder()
            val url = URL("https://raw.githubusercontent.com/Case-Closed-X/Android-Wallpaper-Application/ecf047dc0e396ccd0f9c0e85b0f134e7e63df06a/app/src/main/res/raw/wallpaper_version.json")
            connection = url.openConnection() as HttpsURLConnection
            connection.connectTimeout = 8000
            connection.readTimeout = 8000
            val input = connection.inputStream
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    response.append(it)
                }
            }
        }
    }*/

    override fun onBindViewHolder(holder: AboutAdapter.ViewHolder, position: Int) {
        val aboutItem = aboutList[position]
        holder.image.setImageResource(aboutItem.imageId)
        holder.title.text = aboutItem.title
        holder.content.text = aboutItem.content
    }

    override fun getItemCount(): Int {
        return aboutList.size
    }
}

class AboutItem (val imageId : Int, val title : String, val content : String)

class AppVersion(val version: String)

interface VersionService{
    @GET("wallpaper_version.json")
    fun getVersionData():Call<List<AppVersion>>
}

object ServiceCreator{
    private const val BASE_URL = "https://case-closed-x.github.io/Json/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)
    inline fun <reified T> create() : T = create(T::class.java)
}