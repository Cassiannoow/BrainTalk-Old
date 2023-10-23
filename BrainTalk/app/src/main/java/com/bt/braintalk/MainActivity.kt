package com.bt.braintalk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class MainActivity : AppCompatActivity() {
    private lateinit var txtTeste: TextView;
    private lateinit var TextEmail: EditText;
    private lateinit var TextSenha: EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide()
        setContentView(R.layout.activity_main)
        TextEmail = findViewById<EditText>(R.id.txtUsername)
        TextSenha = findViewById<EditText>(R.id.txtPassword)
        txtTeste = findViewById<EditText>(R.id.txtEsqueceuSenha)
    }

    interface AuthService {
        @POST("Home/login") // Substitua "login" pela rota de login da sua API
        fun login(@Body user: User): Call<User> // AuthResponse representa a resposta da autenticação
    }

    fun registerPage(view: View) {

    }

    fun perfilPage(view: View){
        val intent = Intent(this, PerfilActivity::class.java)
        val user = User(
            userID = 0,
            nome = "none",
            email = TextEmail.text.toString(),
            senha = TextSenha.text.toString(),
            fotoPerfil = ""
        )
        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:5030/api/") // Substitua pela sua URL base
            .addConverterFactory(GsonConverterFactory.create()) // ConverterFactory para trabalhar com JSON
            .build()

        val authService = retrofit.create(AuthService::class.java)
        val call = authService.login(user)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        intent.putExtra( "nomeDoUsuario", authResponse.nome)
                        println(authResponse)
                    }
                    startActivity(intent)
                    txtTeste.text = authResponse.toString()
                } else {
                    txtTeste.text = "erro de lgin"
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                txtTeste.text = t.message.toString()
            }
        })
    }
}