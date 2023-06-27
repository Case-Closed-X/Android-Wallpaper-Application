package com.x.wallpaper

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_LOCK
import android.app.WallpaperManager.FLAG_SYSTEM
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.viewpager2.widget.ViewPager2
import com.x.wallpaper.databinding.ActivityViewPagerBinding
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.animation.addListener
import com.x.wallpaper.Utils.animateScale
import com.x.wallpaper.Utils.animateScaleSize


class ViewPagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewPagerBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.setWallpaper.animateScale()

        val adapter = ImageAdapter(RecyclerViewActivity.imageList,false)
        binding.viewPager.adapter = adapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
       /* var win = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            win.setBackgroundBlurRadius(60)
        }*/
        /*binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Toast.makeText(this@ViewPagerActivity, "page selected $position", Toast.LENGTH_SHORT).show()
                Log.d("currentItem", binding.viewPager.currentItem.toString())
            }
        })*/

        binding.viewPager.setPageTransformer(DepthPageTransformer())//ZoomOutPageTransformer()

        val extraData = intent.getIntExtra("position",0)
        binding.viewPager.currentItem = extraData

        /*binding.setWallpaper.setOnClickListener {
            *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {//android 12模糊效果，看看以后能不能搞个渐变模糊的动画效果
                binding.viewPager.setRenderEffect(RenderEffect.createBlurEffect(20F, 20F, Shader.TileMode.CLAMP))
            }*//*
            showSetDialog()//对话框

        }*/
        binding.setWallpaper.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(1.1f).scaleY(1.1f).translationZ(4.0f).setDuration(150).start()
                }
                MotionEvent.ACTION_UP -> {
                    val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f)
                    val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f)
                    val translationZ = ObjectAnimator.ofFloat(v,"translationZ",1.0f)

                    val animator = AnimatorSet()
                    animator.apply {
                        play(scaleX).with(scaleY).with(translationZ)
                        duration = 150
                        addListener(onEnd= {
                            showSetDialog()
                        })
                        AccelerateDecelerateInterpolator()//插值器
                        start()
                    }
                    /*val animSet = AnimatorSet()
                    animSet.play(scaleX).with(scaleY).with(translationZ)
                    animSet.duration = 150
                    animSet.start()

                    animSet.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            showSetDialog()
                        }
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    })*/
                }
                /*MotionEvent.ACTION_CANCEL -> {//ACTION_CANCEL无效
                    v.animate().scaleX(1.0f).scaleY(1.0f).translationZ(1.0f).setDuration(150).start()
                }*/
            }
            this.onTouchEvent(event)
        }
    }


    private fun showSetDialog() { //弹出对话框
        //加载布局并初始化组件
        val dialogView: View = LayoutInflater.from(this).inflate(R.layout.dialog_set, null)
        //val dialogOK = dialogView.findViewById<Button>(R.id.dialog_buttonOK)
        //val dialogCancel = dialogView.findViewById<Button>(R.id.dialog_buttonCancel)
        var lockSelected = true
        var mainSelected = true
        val imageLock = dialogView.findViewById<ImageFilterView>(R.id.dialog_imageFilterViewLock2)
        val imageMain = dialogView.findViewById<ImageFilterView>(R.id.dialog_imageFilterViewMain2)
        val dialogSet = dialogView.findViewById<Button>(R.id.dialog_buttonSet)
        dialogSet.animateScaleSize()
        imageLock.animateScaleSize()
        imageMain.animateScaleSize()
        imageLock.setOnClickListener {
            if (!lockSelected) imageLock.setBackgroundResource(R.color.white_80)
            else imageLock.setBackgroundResource(R.color.white_50)
            lockSelected = !lockSelected
            when {
                lockSelected and mainSelected -> dialogSet.text = getString(R.string.dialog_setAll)
                mainSelected -> dialogSet.text = getString(R.string.dialog_setMain)
                lockSelected -> dialogSet.text = getString(R.string.dialog_setLock)
                else -> dialogSet.text = getString(R.string.dialog_setNothing)
            }
        }
        imageMain.setOnClickListener {
            if (!mainSelected) imageMain.setBackgroundResource(R.color.white_80)
            else imageMain.setBackgroundResource(R.color.white_50)
            mainSelected = !mainSelected
            when {
                lockSelected and mainSelected -> dialogSet.text = getString(R.string.dialog_setAll)
                mainSelected -> dialogSet.text = getString(R.string.dialog_setMain)
                lockSelected -> dialogSet.text = getString(R.string.dialog_setLock)
                else -> dialogSet.text = getString(R.string.dialog_setNothing)
            }
        }

        val layoutDialog = AlertDialog.Builder(this, R.style.TransparentDialog)
        layoutDialog.setView(dialogView)
        val dialog = layoutDialog.create()

        //dialog.setCancelable(false)//Back键关闭对话框不可用，只能通过点击按钮返回
        dialogSet.setOnClickListener { v: View? ->
            val wallpaperManager = WallpaperManager.getInstance(this)
            val id = RecyclerViewActivity.imageList[binding.viewPager.currentItem].imageId
            //wallpaperManager.clear()
            val result = when {
                lockSelected and mainSelected -> wallpaperManager.setResource(//一定要先判断是否同时选中
                    id,
                    FLAG_LOCK or FLAG_SYSTEM
                )
                mainSelected -> wallpaperManager.setResource(id, FLAG_SYSTEM)
                lockSelected -> wallpaperManager.setResource(id, FLAG_LOCK)
                else -> 0
            }
            if (result != 0) {
                Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT).show()
                val home = Intent(Intent.ACTION_MAIN)
                home.addCategory(Intent.CATEGORY_HOME)
                //home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(home)
                //finish()
            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                binding.viewPager.setRenderEffect(null)//android 12取消模糊效果
            }*/
            dialog.dismiss()

        }
        //dialogCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }
}

private const val MIN_SCALE_Zoom = 0.85f
private const val MIN_ALPHA = 0.5f

//ViewPager2动画1
class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE_Zoom, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE_Zoom) / (1 - MIN_SCALE_Zoom)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}

private const val MIN_SCALE_Depth = 0.75f

//ViewPager2动画2
@RequiresApi(21)
class DepthPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 0 -> { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    alpha = 1f
                    translationX = 0f
                    translationZ = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // (0,1]
                    // Fade the page out.
                    alpha = 1 - position

                    // Counteract the default slide transition
                    translationX = pageWidth * -position
                    // Move it behind the left page
                    translationZ = -1f

                    // Scale the page down (between MIN_SCALE and 1)
                    val scaleFactor = (MIN_SCALE_Depth + (1 - MIN_SCALE_Depth) * (1 - Math.abs(position)))
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}