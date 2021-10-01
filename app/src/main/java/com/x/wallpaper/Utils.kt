package com.x.wallpaper

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.TextView
import androidx.annotation.RequiresApi

object Utils {
    fun setTextViewStyles(textView: TextView) {
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
    fun setStatusBarLight(decorView: View, light: Boolean) {
        val controller = decorView.windowInsetsController
        if (light) {
            controller?.setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)//灰色状态栏
        } else {
            controller?.setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)//白色状态栏
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.animateScale(scale: Float = 1.025f, duration: Long = 150) {
        this.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    this.animate().scaleX(scale).scaleY(scale).translationZ(4f).setDuration(duration).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    this.animate().scaleX(1.0f).scaleY(1.0f).translationZ(1.0f).setDuration(duration).start()
                }
            }
            // 点击事件处理，交给View自身
            this.onTouchEvent(event)
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