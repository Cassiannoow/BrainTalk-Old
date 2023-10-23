package com.bt.braintalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class PerfilActivity : AppCompatActivity() {
    private lateinit var TextName: TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val imageView = findViewById<ImageView>(R.id.imgUser)

        Glide.with(this)
            .load(R.drawable.foto_perfil)
            .circleCrop() // Isso faz com que a imagem seja exibida em forma de círculo
            .into(imageView)

        TextName = findViewById<EditText>(R.id.txtName)
        val nomeDoUsuario = intent.getStringExtra("nomeDoUsuario")
        TextName.text = nomeDoUsuario.toString()
    }

}