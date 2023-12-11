package com.bt.braintalk

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import Models.User
import org.json.JSONObject

class ResgisterActivity : AppCompatActivity() {
    private lateinit var TextName: EditText;
    private lateinit var TextUsername: EditText;
    private lateinit var TextEmail: EditText;
    private lateinit var TextPassword: EditText;
    private lateinit var TextErrors: TextView;
    private lateinit var user: User;
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resgister)
        supportActionBar?.hide()
        TextName = findViewById<EditText>(R.id.txtNomeR)
        TextUsername = findViewById<EditText>(R.id.txtUsernameR)
        TextEmail = findViewById<EditText>(R.id.txtEmailR)
        TextPassword = findViewById<EditText>(R.id.txtPasswordR)
        TextErrors = findViewById<TextView>(R.id.txtErros)
    }

    private fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    } //optimize
    fun cadastrar(view: View){

        if(TextEmail.text.toString() == "cassiannoow@gmail.com"|| TextEmail.text.toString() == "pedro@email.com")
        {
            showToast("Email ja cadastrado")
        }
        else
        {
            if(TextUsername.text.toString() == "jotta.cassiano"|| TextUsername.text.toString() == "bey0ndzin")
            {
                showToast("Username ja cadastrado")
            }
            else{
                user = User(
                    name = TextName.text.toString(),
                    email = TextEmail.text.toString(),
                    password = TextPassword.text.toString(),
                    photo = "data:image/jpeg;base64,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",
                    biograpy = "nothing here!",
                    username = TextUsername.text.toString(),
                    bannerPhoto = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAA1BMVEUvvtUYlJlqAAAASElEQVR4nO3BgQAAAADDoPlTX+AIVQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADwDcaiAAFXD1ujAAAAAElFTkSuQmCC"
                )
                val queue = Volley.newRequestQueue(this)
                val url = "http://192.168.0.14:3000/user"
                val userJson = JSONObject()
                userJson.put("username", user.username)
                userJson.put("name", user.name)
                userJson.put("email", user.email)
                userJson.put("password", user.password)
                userJson.put("photo", user.photo)
                userJson.put("bannerPhoto", user.bannerPhoto)
                userJson.put("biograpy", user.biograpy)

                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST, url, userJson,
                    { _ ->
                        val intent = Intent(this, PerfilActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    },
                    { error ->
                        showToast("Registration failed: ${error.message}")
                        TextErrors.text = "Registration failed: ${error.message}"
                    }
                )
                queue.add(jsonObjectRequest)
            }
        }

    }

    fun loginPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }
}