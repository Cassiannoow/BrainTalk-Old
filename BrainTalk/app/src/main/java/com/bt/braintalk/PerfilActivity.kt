package com.bt.braintalk

import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import models.User
import android.util.Base64
import android.view.View
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
    private lateinit var TextPost: TextView;
    private lateinit var TextMaterial: TextView;
    private lateinit var TextForun: TextView;
    private lateinit var TextLiked: TextView;
    private lateinit var viewPost: View;
    private lateinit var viewMaterial: View;
    private lateinit var viewForun: View;
    private lateinit var viewLiked: View;
    private lateinit var user: User;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        user = intent.getSerializableExtra("user") as User

        val imageViewPhoto = findViewById<ImageView>(R.id.imgUser)
        val imageViewBanner = findViewById<ImageView>(R.id.imgBanner)
        adicionarImagens(user.photo, imageViewPhoto)
        adicionarImagens(user.bannerPhoto, imageViewBanner)
        atualizarDados()


        TextName = findViewById<EditText>(R.id.txtName)
        TextUsername = findViewById<EditText>(R.id.txtUsername)
        TextBiografia = findViewById<EditText>(R.id.txtBiograpy)
        TextFollower = findViewById<EditText>(R.id.txtFollower)
        TextFollowing = findViewById<EditText>(R.id.txtFollowing)
        TextPost = findViewById<EditText>(R.id.txtPosts)
        TextMaterial = findViewById<EditText>(R.id.txtMaterials)
        TextForun = findViewById<EditText>(R.id.txtForuns)
        TextLiked = findViewById<EditText>(R.id.txtLikeds)
        viewPost = findViewById<View>(R.id.bordaPost)
        viewMaterial = findViewById<View>(R.id.bordaMaterial)
        viewForun = findViewById<View>(R.id.bordaForun)
        viewLiked = findViewById<View>(R.id.bordaLiked)
        TextName.text = user.name
        TextUsername.text = "@" + user.username
        TextBiografia.text = user.biograpy
    }

    fun adicionarImagens(s: String, imageView: ImageView){
        val imageData = s.split(",")[1]
        val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
        imageView.setImageBitmap(decodedBitmap)
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

                    TextFollower.text = "$followers Seguidores"
                    TextFollowing.text = "$following Seguindo"

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

    fun disableAllBorder(){
        viewPost.visibility = View.INVISIBLE
        viewMaterial.visibility = View.INVISIBLE
        viewForun.visibility = View.INVISIBLE
        viewLiked.visibility = View.INVISIBLE
        TextPost.setTextColor(Color.parseColor("#002C4D"))
        TextMaterial.setTextColor(Color.parseColor("#002C4D"))
        TextForun.setTextColor(Color.parseColor("#002C4D"))
        TextLiked.setTextColor(Color.parseColor("#002C4D"))
    }

    fun alterarColorTextPost(view: View){
        disableAllBorder()
        viewPost.visibility = View.VISIBLE
        TextPost.setTextColor(Color.parseColor("#000000"))
    }

    fun alterarColorTextMaterial(view: View){
        disableAllBorder()
        viewMaterial.visibility = View.VISIBLE
        TextMaterial.setTextColor(Color.parseColor("#000000"))
    }
    fun alterarColorTextForun(view: View){
        disableAllBorder()
        viewForun.visibility = View.VISIBLE
        TextForun.setTextColor(Color.parseColor("#000000"))
    }
    fun alterarColorTextLikeds(view: View){
        disableAllBorder()
        viewLiked.visibility = View.VISIBLE
        TextLiked.setTextColor(Color.parseColor("#000000"))
    }

}