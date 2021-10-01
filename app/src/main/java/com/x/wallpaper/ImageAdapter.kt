package com.x.wallpaper

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val imageList : List<ImageItem>,private val isRecyclerAdapter : Boolean)
    : RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val image : ImageView = if (isRecyclerAdapter){
            view.findViewById(R.id.imageViewRecycler)
        } else{
            view.findViewById(R.id.imageFilterView)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        val intent = Intent(parent.context,ViewPagerActivity::class.java)
        val view = if (isRecyclerAdapter){
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item,parent,false)
        } else{
            LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
        }

        val viewHolder = ViewHolder(view)
        if (isRecyclerAdapter){
            viewHolder.itemView.setOnTouchListener { v, event ->
                val position = viewHolder.adapterPosition
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(150).start()
                        v.animate().alpha(0.7f).setDuration(150).start()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                        v.animate().alpha(1.0f).setDuration(150).start()
                    }
                    MotionEvent.ACTION_UP -> {
                        v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
                        v.animate().alpha(1.0f).setDuration(150).start()
                        intent.putExtra("position",position)
                        parent.context.startActivity(intent)
                    }
                }
                true
            }
        }

        return viewHolder
        //return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        val imageItem = imageList[position]
        holder.image.setImageResource(imageItem.imageId)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}