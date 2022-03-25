package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var currentImageUrl: String? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMeme()

        binding.shareButton.setOnClickListener {
            shareUrl(currentImageUrl.toString())
        }
        binding.nextButton.setOnClickListener {
            loadMeme()
        }
    }

    private fun shareUrl(url: String) {
        val chooser = Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, "Hey, Checkout this cool Meme I got from Reddit $url")
                type = "text/plain"
            },
            "Share this Meme Using..."
        )
        startActivity(chooser)
    }

    private var imgUrl = ""

    private fun loadMeme() {
        val pBar = binding.progressBar
        pBar.visibility = View.VISIBLE
        val apiUrl = "https://meme-api.herokuapp.com/gimme"


        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, apiUrl, null,
            { response ->
                currentImageUrl = response.getString("url")
                val img = binding.memeImageView
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pBar.visibility = View.GONE
                        return false
                    }
                })
                    .into(img)
            }, {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_LONG).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}