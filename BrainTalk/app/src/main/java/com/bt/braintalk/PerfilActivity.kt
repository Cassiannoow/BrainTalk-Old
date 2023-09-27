package com.bt.braintalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide


class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val imageView = findViewById<ImageView>(R.id.imgUser)

        Glide.with(this)
            .load(R.drawable.foto_perfil)
            .circleCrop() // Isso faz com que a imagem seja exibida em forma de círculo
            .into(imageView)
    }

}