package com.x.wallpaper

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.x.wallpaper.Utils.animateScale
import com.x.wallpaper.databinding.ActivityAboutBinding

@RequiresApi(Build.VERSION_CODES.R)
class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private val aboutList = ArrayList<AboutItem>()
    private val firstList = ArrayList<AboutItem>()

    init {
        initData()
    }

    private fun initData() {
        aboutList.add(AboutItem(R.drawable.ico_github,"github","访问我的github项目页"))
        aboutList.add(AboutItem(R.drawable.ico_outlook,"outlook mail","给我的outlook邮箱发送邮件"))
        aboutList.add(AboutItem(R.drawable.ico_qq,"qq mail","联系我的qq邮箱"))

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
        val urlQQ = "mailto:CaseClosedX@qq.com"

        val view = LayoutInflater.from(parent.context).inflate(R.layout.about_item,parent,false)

        val viewHolder = ViewHolder(view)

            viewHolder.itemView.setOnTouchListener { v, event ->
                val position = viewHolder.adapterPosition
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(when(position){
                    0-> urlGithub
                    1-> urlOutlook
                    2-> urlQQ
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
                        else if (position == 0){//后续会优化
                            Toast.makeText(parent.context,"已经是最新版本", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            }

        return viewHolder
    }

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

class AboutItem (val imageId : Int, val title : String, val content : String){

}