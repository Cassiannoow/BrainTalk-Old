package com.bt.braintalk

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import Models.Post
import android.graphics.BitmapFactory
import android.widget.ImageView
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.Calendar
import java.util.TimeZone
import java.util.UUID

class create_post : AppCompatActivity() {
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var username: String

    private var selectedFileUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // O callback é chamado quando o usuário seleciona um arquivo
        uri?.let {
            // Salve a Uri do arquivo selecionado
            selectedFileUri = it

            exibirPreviewImagem(it)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        username = intent.getStringExtra("username").toString()
        editTextContent = findViewById(R.id.editTextContent)
        val buttonSelectFile = findViewById<Button>(R.id.buttonSelectFile)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSelectFile.setOnClickListener {
            // Inicie a atividade de seleção de arquivo
            getContent.launch("*/*")
        }



        buttonSubmit.setOnClickListener {
            // Obtenha os valores dos campos
            val content = editTextContent.text.toString()

            // Realize a lógica de envio do post aqui
            // ...

            // Verifique se há um arquivo selecionado
            selectedFileUri?.let { uri ->
                // Converte o arquivo para Base64
                val base64String = convertFileToBase64(this, uri)

                adicionarImagens("data:image/png;base64,$base64String", findViewById(R.id.imageView))

                // Faça algo com a string Base64 (por exemplo, envie para o servidor)
                base64String?.let { base64 ->

                    // Realize a lógica de envio do post e do arquivo aqui
                    // ...
                    val timeMap = getCurrentTime()
                    val longValue: Long = timeMap["seconds"]!! * 1000 + timeMap["nanoseconds"]!! / 1000000
                    showToast(base64String)

                    var post = Post(
                        id = UUID.randomUUID().toString(),
                        username = this.username,
                        content = content,
                        dataPost = longValue,
                        file = "data:image/png;base64," + base64String,
                        contenttype = "png"
                    )
                    val queue = Volley.newRequestQueue(this)
                    val url = "http://192.168.0.14:3000/post"
                    val userJson = JSONObject()
                    userJson.put("id", post.id)
                    userJson.put("username", post.username)
                    userJson.put("content", post.content)
                    userJson.put("dataPost", post.dataPost)
                    userJson.put("file", post.file)
                    userJson.put("contenttype", post.file)

                    val jsonObjectRequest = JsonObjectRequest(
                        Request.Method.POST, url, userJson,
                        { _ ->

                            finish()
                        },
                        { error ->
                            showToast("Registration failed: ${error.message}")
                        }
                    )
                    queue.add(jsonObjectRequest)
                    // Limpe os campos após enviar o post (opcional)
                    editTextContent.text.clear()
                    selectedFileUri = null
                }
            }
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    } //optimize


    fun getCurrentTime(): Map<String, Long> {
        val currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val seconds = currentTime.timeInMillis / 1000
        val nanoseconds = (currentTime.timeInMillis % 1000) * 1000000

        return mapOf(
            "seconds" to seconds,
            "nanoseconds" to nanoseconds
        )
    }
    fun convertFileToBase64(context: Context, fileUri: Uri): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
            inputStream?.let {
                val bytes = readBytes(inputStream)
                return Base64.encodeToString(bytes, Base64.DEFAULT)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // Função para ler bytes de um InputStream
    @Throws(IOException::class)
    fun readBytes(inputStream: InputStream): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        var len: Int
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }

        return byteBuffer.toByteArray()
    }

    fun adicionarImagens(s: String, imageView: ImageView){
        val imageData = s.split(",")[1]
        val decodedBytes = Base64.decode(imageData, Base64.DEFAULT)
        val decodedBitmap = BitmapFactory.decodeStream(ByteArrayInputStream(decodedBytes))
        imageView.setImageBitmap(decodedBitmap)
    }
    private fun exibirPreviewImagem(uri: Uri) {
        // Converte o arquivo para Base64
        val base64String = convertFileToBase64(this, uri)

        // Verifica se a conversão foi bem-sucedida
        base64String?.let { base64 ->
            // Exibe a prévia da imagem na ImageView
            adicionarImagens("data:image/png;base64,$base64", findViewById(R.id.imageView))
        }
    }
}