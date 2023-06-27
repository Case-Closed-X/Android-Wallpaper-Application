package com.x.wallpaper

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.x.wallpaper.databinding.ActivityRecyclerViewMainBinding

class RecyclerViewActivityMain : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerViewMainBinding

    companion object{
        val mainList = ArrayList<RecyclerViewItem>()
        val imageList = ArrayList<ImageItem>()
    }

    init {
        initMain()
        initImages()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecyclerViewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.setStatusBarLight(window.decorView, true)
        Utils.setTextViewStyles(binding.textViewMain)

        val adapter = MainAdapter(mainList)
        binding.viewPagerMain.adapter = adapter
        binding.viewPagerMain.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.textViewMain.setOnClickListener {
            val intent = Intent(this,AboutActivity::class.java)
            //startActivity(intent)
            //开启过渡动画
            startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    override fun onDestroy() {//解决返回键后再次初始化，造成imageList呈指数增长的bug
        mainList.clear()
        super.onDestroy()
    }

    private fun initMain() {
        mainList.add(RecyclerViewItem(R.id.recyclerView))
        mainList.add(RecyclerViewItem(R.id.recyclerView))//改
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


class MainAdapter(private val mainList : ArrayList<RecyclerViewItem>)
    : RecyclerView.Adapter<MainAdapter.ViewHolder>(){

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val recyclerView: RecyclerView = view.findViewById(R.id.mainItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        if (position == 0){//非local
            RecyclerViewActivityMain.imageList.clear()
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.space))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.mountain))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.aperture))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.ship))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.technology))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.sky))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.tree))
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.road))
            val adapter = ImageAdapter(RecyclerViewActivityMain.imageList,true)
            holder.recyclerView.adapter = adapter

            val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            //val layoutManager = LinearLayoutManager(this)
            holder.recyclerView.layoutManager = layoutManager

            //recyclerView's BUG:在页面尚未加载完成时，滑动屏幕闪退

            holder.recyclerView.translationY = 600f
            holder.recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                //binding.recyclerView.animate().alpha(0f).alpha(1f).setDuration(1500).start()
                val alphaAnim = ObjectAnimator.ofFloat(holder.recyclerView, "alpha", 1f)
                val translationY = ObjectAnimator.ofFloat(holder.recyclerView, "translationY", 0f)
                AnimatorSet().apply {
                    play(alphaAnim).with(translationY)
                    duration = 400
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }
        else{
            RecyclerViewActivityMain.imageList.clear()
            RecyclerViewActivityMain.imageList.add(ImageItem(R.drawable.plus))

            val adapter = ImageAdapter(RecyclerViewActivityMain.imageList,true)
            holder.recyclerView.adapter = adapter

            val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            //val layoutManager = LinearLayoutManager(this)
            holder.recyclerView.layoutManager = layoutManager

            //recyclerView's BUG:在页面尚未加载完成时，滑动屏幕闪退

            holder.recyclerView.translationY = 600f
            holder.recyclerView.viewTreeObserver.addOnGlobalLayoutListener {
                //binding.recyclerView.animate().alpha(0f).alpha(1f).setDuration(1500).start()
                val alphaAnim = ObjectAnimator.ofFloat(holder.recyclerView, "alpha", 1f)
                val translationY = ObjectAnimator.ofFloat(holder.recyclerView, "translationY", 0f)
                AnimatorSet().apply {
                    play(alphaAnim).with(translationY)
                    duration = 400
                    interpolator = DecelerateInterpolator()
                    start()
                }
            }
        }
        //val mainItem = mainList[position]
        //holder.recyclerView.

    }

    override fun onViewRecycled(holder: ViewHolder) {
        //super.onViewRecycled(holder)

    }

    override fun getItemCount(): Int {
        return mainList.size
    }
}

class RecyclerViewItem (val id : Int)