package com.bt.braintalk

import PostAdapter
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
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
        val url = "http://192.168.56.1:3000/posts" // Substitua pela URL correta da sua API

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val posts = mutableListOf<Post>()

                for (i in 0 until response.length()) {
                    val jsonObject = response.getJSONObject(i)
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

                    val post = Post(username, content, dataPost, file, contenttype)
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

}