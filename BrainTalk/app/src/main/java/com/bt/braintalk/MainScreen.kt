package com.bt.braintalk

import PostAdapter
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import models.Post
import models.User
import java.io.ByteArrayInputStream

class MainScreen : AppCompatActivity() {
    private lateinit var user: User;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        user = intent.getSerializableExtra("user") as User

        val imageViewPhoto = findViewById<ImageView>(R.id.imgUser)
        adicionarImagens(user.photo, imageViewPhoto)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPosts)
        val postAdapter = PostAdapter(listOf()) // Inicialize com uma lista vazia por enquanto

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        // Chame a função para obter os posts da API
        getPostsFromApi(postAdapter)

    }

    fun adicionarImagens(s: String, imageView: ImageView){
        val imageData = s.split(",")[1]
        val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
        imageView.setImageBitmap(decodedBitmap)
    }

    fun getPostsFromApi(postAdapter: PostAdapter) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.14:3000/posts" // Substitua pela URL correta da sua API

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val posts = mutableListOf<Post>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
                    val id = jsonObject.getString("id")
                    val username = jsonObject.getString("username")
                    val content = jsonObject.getString("content")
                    val dataPostObject = jsonObject.getJSONObject("dataPost")
                    val seconds = dataPostObject.getLong("seconds")
                    val nanoseconds = dataPostObject.getLong("nanoseconds")
                    val dataPost = seconds * 1000 + nanoseconds / 1000000 // Convertendo para milissegundos

                    // Agora você pode usar o valor de dataPost (que é um Long) conforme necessário

                    val file = jsonObject.getString("file")
                    val contenttype = jsonObject.getString("contenttype")
                    // Outros campos da postagem

                    val post = Post(id,username, content, dataPost, file, contenttype)
                    posts.add(post)


                }
                Log.d("Lista Posts", posts.toString())
                postAdapter.updatePosts(posts) // Atualiza o adaptador com os novos posts
            },
            { error ->
                // Tratar erros na solicitação
            }
        )

        // Adicione a solicitação à fila de solicitações Volley (substitua mRequestQueue pelo seu RequestQueue existente)
        queue.add(request)
    }

    fun perfilPage(view: View){
        val intent = Intent(this, PerfilActivity::class.java)

        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.14:3000/users/" + user.username

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val correctPassword = response.optString("password")
                if (user.password.toString() == correctPassword) {
                    val user = User(
                        name = response.optString("name"),
                        email = response.optString("email"),
                        password = response.optString("password"),
                        photo = response.optString("photo"),
                        biograpy = response.optString("biograpy"),
                        username = response.optString("username"),
                        bannerPhoto = response.optString("bannerPhoto")
                    )
                    intent.putExtra("user", user)
                    startActivity(intent)
                } else {
                    showToast("Wrong password")
                }
            },
            { error ->
                showToast("Invalid username $error")
            }
        )
        queue.add(jsonObjectRequest)
    }
    private fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }

    fun darLike(view: View) {
        val imgLike: ImageView = findViewById(R.id.imageFile2)
        val drawable: Drawable? = imgLike.drawable

        if (drawable != null) {
            val currentImageResource = when (drawable) {
                is BitmapDrawable -> {
                    val bitmap: Bitmap = drawable.bitmap
                    val heartBlackBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.heart_black)
                    if (bitmap.sameAs(heartBlackBitmap)) {
                        R.drawable.heart_black
                    } else {
                        R.drawable.heart_red
                    }
                }
                else -> {
                    R.drawable.heart_black
                }
            }

            // Alternar entre heart_black e heart_red
            val newImageResource = if (currentImageResource == R.drawable.heart_black) {
                R.drawable.heart_red
            } else {
                R.drawable.heart_black
            }

            // Obter o nome do recurso correspondente ao ID
            val resourceName = resources.getResourceName(newImageResource)

            // Definir o novo recurso da imagem
            imgLike.setImageResource(newImageResource)
            Log.d("darLike", "Imagem alterada para $resourceName")
        } else {
            Log.e("darLike", "Drawable é nulo")
        }
    }
}