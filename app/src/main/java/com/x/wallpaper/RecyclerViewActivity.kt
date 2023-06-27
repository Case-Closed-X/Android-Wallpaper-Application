package com.x.wallpaper

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.Window
import android.view.WindowInsets
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.x.wallpaper.databinding.ActivityRecyclerViewBinding
import java.io.InputStream
import kotlin.math.roundToInt

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerViewBinding

    companion object{
        val imageList = ArrayList<ImageItem>()
    }

    init {
        initImages()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.setStatusBarLight(window.decorView, true)
        Utils.setTextViewStyles(binding.textView)

        val adapter = ImageAdapter(imageList,true)
        binding.recyclerView.adapter = adapter

        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        //recyclerView's BUG:在页面尚未加载完成时，滑动屏幕闪退

        binding.recyclerView.translationY = 600f
        binding.recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
            //binding.recyclerView.animate().alpha(0f).alpha(1f).setDuration(1500).start()
            val alphaAnim = ObjectAnimator.ofFloat(binding.recyclerView, "alpha", 1f)
            val translationY = ObjectAnimator.ofFloat(binding.recyclerView, "translationY", 0f)
            AnimatorSet().apply {
                play(alphaAnim).with(translationY)
                duration = 400
                interpolator = DecelerateInterpolator()
                start()
            }
        }

        binding.textView.setOnClickListener {
            val intent = Intent(this,AboutActivity::class.java)
            //startActivity(intent)
            //开启过渡动画
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    override fun onDestroy() {//解决返回键后再次初始化，造成imageList呈指数增长的bug
        imageList.clear()
        super.onDestroy()
    }

    private fun initImages() {//后续将改为从网络获取
        imageList.add(ImageItem(R.drawable.space))
        imageList.add(ImageItem(R.drawable.mountain))
        imageList.add(ImageItem(R.drawable.aperture))
        imageList.add(ImageItem(R.drawable.ship))
        imageList.add(ImageItem(R.drawable.technology))
        imageList.add(ImageItem(R.drawable.sky))
        imageList.add(ImageItem(R.drawable.tree))
        imageList.add(ImageItem(R.drawable.road))
    }

    /*fun readBitmapFromResource(
        resources: Resources,
        resourcesId: Int,
        width: Int,
        height: Int
    ): Bitmap? {
        val ins: InputStream = resources.openRawResource(resourcesId)
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(ins, null, options)
        val srcWidth: Int = options.outWidth
        val srcHeight: Int = options.outHeight
        var inSampleSize = 1
        if (srcHeight > height || srcWidth > width) {
            inSampleSize = if (srcWidth > srcHeight) ({
                (srcHeight / height).toDouble().roundToInt()
            }).toInt() else ({
                (srcWidth / width).toDouble().roundToInt()
            }).toInt()
        }
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        return BitmapFactory.decodeStream(ins, null, options)
    }*/
}