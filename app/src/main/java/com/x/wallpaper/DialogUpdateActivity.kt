package com.x.wallpaper
/*

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import com.x.wallpaper.databinding.DialogUpdateBinding

import android.widget.TextView




class DialogUpdateActivity(context: Context, private var dialogName: String?) : Dialog(context) {
    private lateinit var binding:DialogUpdateBinding
    //var mContext: Context
    //constructor(context: Context) : super(context) { this.mContext = context}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)//去除标题

        binding = DialogUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewDialogUpdate.text = dialogName
        binding.buttonDialogUpdateCancel.setOnClickListener { dismiss() }
        binding.buttonDialogUpdateOK.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Case-Closed-X/Android-Wallpaper-Application/releases/"))
            MyApplication.context.startActivity(intent)
        }
    }

    */
/*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialogUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonDialogUpdateCancel.setOnClickListener {
            finish()
        }

        binding.buttonDialogUpdateOK.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Case-Closed-X/Android-Wallpaper-Application/releases/"))
            MyApplication.context.startActivity(intent)
        }
    }*//*

}*/
