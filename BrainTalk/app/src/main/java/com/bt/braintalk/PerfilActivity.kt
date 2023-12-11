package com.bt.braintalk

import Models.Like
import PostAdapter
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import Models.User
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import Models.Post
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.util.UUID


class PerfilActivity : AppCompatActivity(), OnPostItemClickListener {
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

        // Suponha que você tenha uma Activity ou Fragment onde está usando o RecyclerView e o PostAdapter
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewPosts)
        val postAdapter = PostAdapter(listOf(), user, this) // Inicialize com uma lista vazia por enquanto

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter

        // Chame a função para obter os posts da API
        getPostsFromApi(postAdapter)

    }

    lateinit var postId: String
    fun onPostItemClick(postId: String) {
        this.postId = postId
    }

    fun onLikeButtonClick(postId: String) {
        TODO("Not yet implemented")
    }

    fun adicionarImagens(s: String, imageView: ImageView){
        val imageData = s.split(",")[1]
        val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
        imageView.setImageBitmap(decodedBitmap)
    }
    fun atualizarDados(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.14:3000/friends"
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

    fun getPostsFromApi(postAdapter: PostAdapter) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.14:3000/posts" // Substitua pela URL correta da sua API

        val request = JsonArrayRequest(Request.Method.GET, url, null,
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

                    val post = Post(id, username, content, dataPost, file, contenttype)

                    if(post.username == user.username)
                    {
                        posts.add(post)
                    }
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

    // Outros métodos da classe


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

    override fun onLikeButtonClick(postId: String, view: View) {
        val queue2 = Volley.newRequestQueue(this)
        val url2 = "http://192.168.0.14:3000/likes/"

        val request = JsonArrayRequest(
            Request.Method.GET, url2, null,
            { response ->
                try {
                    // Crie uma cópia da lista de likes
                    val likesList = mutableListOf<Like>()
                    for (i in 0 until response.length()) {
                        val jsonObject = response.getJSONObject(i)
                        val id = jsonObject.getString("id")
                        val idPost = jsonObject.getString("postId")
                        val username = jsonObject.getString("username")

                        val like = Like(id, idPost, username)
                        likesList.add(like)
                    }

                    var hasUserLiked = false

                    // Itere sobre a cópia da lista
                    for (like in likesList) {
                        if (postId == like.postID) {
                            Log.d("Comparação de Strings", "user.username: ${user.username}, username: ${like.username}")
                            if (user.username == like.username) {
                                delete(view, like.id)
                                hasUserLiked = true
                                break
                            }
                        }
                    }

                    // Se o usuário não tiver um like para este postId, adicione um novo like
                    if (!hasUserLiked) {
                        post(view, postId)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Tratar erros na solicitação
            }
        )

        // Adicione a solicitação à fila de solicitações Volley
        queue2.add(request)
    }


    fun darLike(postId: String, view: View) {
        var imgLike: ImageView
        imgLike = view.findViewById<ImageView>(R.id.imageFile2)
        imgLike.setImageResource(R.drawable.heart_red)
    }


    fun delete(view: View, id: String) {
        val queue2 = Volley.newRequestQueue(this)
        val url2 = "http://192.168.0.14:3000/like/$id"

        val request = object : StringRequest(
            Method.DELETE, url2,
            { response ->
                runOnUiThread {
                    // Alterações na interface do usuário
                    var imgLike: ImageView = view.findViewById(R.id.imageFile2)
                    imgLike.setImageResource(R.drawable.heart_black)
                    var textLikes: TextView
                    textLikes = view.findViewById<TextView>(R.id.textLIkes)
                    var j = (textLikes.text.toString().toInt() - 1)
                    textLikes.text = j.toString()
                }
            },
            { error ->
                // Tratar erros na solicitação
            }
        ) {
            // Você pode adicionar cabeçalhos personalizados aqui, se necessário
        }

        // Adicione a solicitação à fila de solicitações Volley
        queue2.add(request)
    }




    fun post(view: View, postId: String){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.0.14:3000/like"
        val userJson = JSONObject()
        userJson.put("id", UUID.randomUUID().toString())
        userJson.put("postId", postId)
        userJson.put("username", user.username)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, userJson,
            { _ ->
                var imgLike: ImageView
                imgLike = view.findViewById<ImageView>(R.id.imageFile2)
                imgLike.setImageResource(R.drawable.heart_red)
                var textLikes: TextView
                textLikes = view.findViewById<TextView>(R.id.textLIkes)
                var j = (textLikes.text.toString().toInt() + 1)
                textLikes.text = j.toString()
            },
            { error ->
            }
        )
        queue.add(jsonObjectRequest)
    }


    override fun onPostItemClick(postId: String, v: View) {
        TODO("Not yet implemented")
    }
}