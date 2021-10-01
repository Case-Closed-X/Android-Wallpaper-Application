package com.x.wallpaper

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_LOCK
import android.app.WallpaperManager.FLAG_SYSTEM
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.x.wallpaper.Utils.animateScale
import com.x.wallpaper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSetWallpaper.animateScale()

        val wallpaperManager = WallpaperManager.getInstance(this)

        binding.buttonSetWallpaper.setOnClickListener {
            val result = wallpaperManager.setResource(R.raw.labrador,FLAG_SYSTEM or FLAG_LOCK)
            if (result != 0) {
                Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.labrador)
    /*binding.buttonSetWallpaper.text = null
           binding.buttonSetWallpaper.isEnabled = false
           binding.progressBar.visibility = View.VISIBLE

           thread {
               val result = wallpaperManager.setBitmap(bitmap, null, true)
               runOnUiThread {
                   if (result != 0) {
                       Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT).show()
                       binding.buttonSetWallpaper.text = getString(R.string.setWallpaper)
                       binding.buttonSetWallpaper.isEnabled = true
                       binding.progressBar.visibility = View.GONE
                   }
               }
           }*/

    /*
        val wallpaper: String by lazy {
            thread {
                result = wallpaperManager.setBitmap(bitmap, null, true)
            }
        if (result==0) "设置中..." else "Done"
        }
        /*val job = Job()
        val scope = CoroutineScope(job)
        scope.launch {
            thread {
                val result = wallpaperManager.setBitmap(bitmap, null, true)

                Log.d("result", result.toString())
                if (result != 0) {
                    binding.buttonSetWallpaper.isEnabled = true
                    Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT).show()
                }
            }
        }*/
        //val result = wallpaperManager.setBitmap(BitmapFactory.decodeResource(resources,R.drawable.labrador),null, true)
        //job.cancel()
        //finish()
     */
}