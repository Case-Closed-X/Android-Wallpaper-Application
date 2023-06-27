package com.x.wallpaper

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat

object Utils {
    fun setTextViewStyles(textView: TextView) {//渐变文字
        val mLinearGradient = LinearGradient(
            0f,
            0f,
            textView.paint.textSize * textView.text.length,
            0f,
            Color.parseColor("#FF5722"),
            Color.parseColor("#FF6200EE"),
            Shader.TileMode.CLAMP
        )
        //LinearGradient mLinearGradient = new LinearGradient(0,0,textView.getPaint().getTextSize() * textView.getText().length(),0,new int[]{R.color.purple_500,R.color.purple_200,R.color.orange},null, Shader.TileMode.CLAMP);
        textView.paint.shader = mLinearGradient
        textView.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun setStatusBarLight(decorView: View, light: Boolean) {//状态栏颜色
        val controller = decorView.windowInsetsController
        if (light) {
            controller?.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)//灰色状态栏
        } else {
            controller?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)//白色状态栏
        }
        //controller?.hide(WindowInsets.Type.navigationBars())//android 12不明原因导致导航栏透明变为半透明，届时可试一下这个好不好使
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.animateScale(scale: Float = 1.025f, duration: Long = 150) {//有阴影的点击动画效果
        this.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    this.animate().scaleX(scale).scaleY(scale).translationZ(4f).setDuration(duration).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    this.animate().scaleX(1.0f).scaleY(1.0f).translationZ(1.0f).setDuration(duration).start()
                    //v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();


                    val scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1f)
                    val scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1f)
                    val translationZ = ObjectAnimator.ofFloat(v,"translationZ",1f)

                    val animSet = AnimatorSet()
                    animSet.play(scaleX).with(scaleY)
                    animSet.duration = 150
                    animSet.start()


                }
            }
            // 点击事件处理，交给View自身
            this.onTouchEvent(event)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.animateScaleSize(scale: Float = 0.9f, duration: Long = 150) {//缩放点击动画效果
        this.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(scale).scaleY(scale).setDuration(duration).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(duration).start()
//                    this.animate().rotationY(0f).setDuration(duration).start()
                }
                /*MotionEvent.ACTION_MOVE ->{
                    this.animate().rotationY(-3f).setDuration(duration).start()
                }*/
            }
            //点击事件处理，交给View自身，才能实现按钮background的变化
            this.onTouchEvent(event)
            //true//如果直接返回true的话，按钮background无变化，按钮水波纹特效也会消失
        }
    }

    /*fun initSystemBars() {
        var controller = window.decorView.windowInsetsController
        // 设置状态栏反色
        controller?.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
        // 取消状态栏反色
        controller?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)
        // 设置导航栏反色
        controller?.setSystemBarsAppearance(APPEARANCE_LIGHT_NAVIGATION_BARS, APPEARANCE_LIGHT_NAVIGATION_BARS)
        // 取消导航栏反色
        controller?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_NAVIGATION_BARS)
        // 同时设置状态栏和导航栏反色
        controller?.setSystemBarsAppearance((APPEARANCE_LIGHT_STATUS_BARS or APPEARANCE_LIGHT_NAVIGATION_BARS),
            (APPEARANCE_LIGHT_STATUS_BARS or APPEARANCE_LIGHT_NAVIGATION_BARS))
        // 同时取消状态栏和导航栏反色
        controller?.setSystemBarsAppearance(0, (APPEARANCE_LIGHT_STATUS_BARS or APPEARANCE_LIGHT_NAVIGATION_BARS))
        // 隐藏状态栏
        controller?.hide(WindowInsets.Type.statusBars())
        // 显示状态栏
        controller?.show(WindowInsets.Type.statusBars())
        // 隐藏导航栏
        controller?.hide(WindowInsets.Type.navigationBars())
        // 显示导航栏
        controller?.show(WindowInsets.Type.navigationBars())
        // 同时隐藏状态栏和导航栏
        controller?.hide(WindowInsets.Type.systemBars())
        // 同时隐藏状态栏和导航栏
        controller?.show(WindowInsets.Type.systemBars())
    }*/
}