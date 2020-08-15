@file:Suppress("DEPRECATION")

package com.khan.imageurltobitmapwithasynctask

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private var imageBitmap: Bitmap? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialization()

        setImageUrl()

        getBitmapFromImageUrl()
    }

    private fun setImageUrl() {
        imageUrl = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg"
    }

    private fun getBitmapFromImageUrl() {
        GetBitmapFromUrl(object : GetBitmapFromUrl.AsyncResponse {
            override fun processFinish(bitmap: Bitmap?) {
//                you can use this bitmap from asyncTask
                imageBitmap = bitmap
            }
        }, imageView).execute(imageUrl)
    }

    private fun initialization() {
        imageView = findViewById(R.id.imageViewId)
    }

    @Suppress("DEPRECATION")
    class GetBitmapFromUrl(delegate: AsyncResponse?, private var imageView: ImageView) :
            AsyncTask<String?, Void?, Bitmap?>() {
        //        interface for get the converted bitmap
        interface AsyncResponse {
            fun processFinish(bitmap: Bitmap?)
        }

        var delegate: AsyncResponse? = null

        init {
            this.delegate = delegate
        }

        override fun doInBackground(vararg url: String?): Bitmap? {
            val imageUrl = url[0]
            var bitmap: Bitmap? = null
            try {
                val inputStream = URL(imageUrl).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)

            } catch (e: MalformedURLException) {
                e.printStackTrace()

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmap
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            super.onPostExecute(bitmap)
            delegate!!.processFinish(bitmap)
            imageView.setImageBitmap(bitmap)
        }
    }
}