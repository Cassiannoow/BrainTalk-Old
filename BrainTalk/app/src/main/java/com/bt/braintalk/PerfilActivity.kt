package com.bt.braintalk

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import models.User
import android.util.Base64
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import java.io.ByteArrayInputStream


class PerfilActivity : AppCompatActivity() {
    private lateinit var TextName: TextView;
    private lateinit var TextUsername: TextView;
    private lateinit var TextBiografia: TextView;
    private lateinit var TextFollower: TextView;
    private lateinit var TextFollowing: TextView;
    private lateinit var user: User;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        user = intent.getSerializableExtra("user") as User
        atualizarDados()
        val imageView = findViewById<ImageView>(R.id.imgUser)
        val imageData = user.photo.split(",")[1]
        val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
        imageView.setImageBitmap(decodedBitmap)

        TextName = findViewById<EditText>(R.id.txtName)
        TextUsername = findViewById<EditText>(R.id.txtUsername)
        TextBiografia = findViewById<EditText>(R.id.txtBiograpy)
        TextFollower = findViewById<EditText>(R.id.txtFollower)
        TextFollowing = findViewById<EditText>(R.id.txtFollowing)
        TextName.text = user.name
        TextUsername.text = "@" + user.username
        TextBiografia.text = user.biograpy
    }
    private fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    } //optimize
    fun atualizarDados(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.17:3000/friends"
        var followers = 0
        var following = 0

        val jsonArrayRequest = JsonArrayRequest(
            url,
            { response ->
                try {
                    // A resposta é uma matriz JSON (JSONArray)
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val recipientFriendID = jsonObject.getString("recipientFriendID")
                        val senderFriendID = jsonObject.getString("senderFriendID")
                        val status = jsonObject.getString("status")

                        // Faça algo com os dados aqui, por exemplo, adicione-os a uma lista
                        if(recipientFriendID == user.username)
                        {
                            if(status == "accepted"){followers++}
                        }

                        if(senderFriendID == user.username)
                        {
                            if(status == "accepted"){following++}
                        }
                    }

                    TextFollower.text = followers.toString() + " Seguidores"
                    TextFollowing.text = following.toString() + " Seguindo"

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Trate erros de requisição aqui
            }
        )

// Adicione a solicitação à fila de solicitações Volley (substitua mRequestQueue pelo seu RequestQueue existente)
        queue.add(jsonArrayRequest)
    }

}