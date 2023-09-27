package com.bt.braintalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_main)
    }

    fun registerPage(view: View) {
        val intent = Intent(this, ResgisterActivity::class.java)
        startActivity(intent)
    }

    fun perfilPage(view: View){
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
    }
}