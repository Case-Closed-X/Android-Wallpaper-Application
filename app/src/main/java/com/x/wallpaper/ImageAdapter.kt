package com.x.wallpaper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.*
import android.view.RoundedCorner.POSITION_TOP_RIGHT
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.view.ViewCompat.getRootWindowInsets
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val imageList : List<ImageItem>,private val isRecyclerAdapter : Boolean)
    : RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val image = if (isRecyclerAdapter){
            view.findViewById<ImageView>(R.id.imageViewRecycler)
        } else{
            view.findViewById<ImageFilterView>(R.id.imageFilterView)
        }
        val card = view.findViewById<CardView>(R.id.cardRecycler)//不要val card:CardView，会crash
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
        if (isRecyclerAdapter){//是主界面的recycler item的话
            /*viewHolder.itemView.scaleX = 0f
            viewHolder.itemView.scaleY = 0f
            viewHolder.itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
*/
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
                        parent.context.startActivity(intent)//传送选中的item数据
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
        if (holder.image is ImageFilterView) {//可以动态设置设置壁纸界面显示的圆角
            //holder.image.round = 100F
        }
        else{
            holder.card.animate().scaleX(1.0f).scaleY(1.0f).setDuration(400).start()
        }

    }

    override fun onViewRecycled(holder: ViewHolder) {
        //super.onViewRecycled(holder)
        /*if (holder.image is ImageView){
            holder.card.scaleX = 0f
            holder.card.scaleY = 0f
        }*/
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}

class ImageItem (val imageId : Int)