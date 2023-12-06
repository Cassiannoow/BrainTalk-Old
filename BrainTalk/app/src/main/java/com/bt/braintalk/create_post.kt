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
import org.json.JSONObject
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

                // Faça algo com a string Base64 (por exemplo, envie para o servidor)
                base64String?.let { base64 ->
                    // Realize a lógica de envio do post e do arquivo aqui
                    // ...
                    val timeMap = getCurrentTime()
                    val longValue: Long = timeMap["seconds"]!! * 1000 + timeMap["nanoseconds"]!! / 1000000

                    var post = Post(
                        id = UUID.randomUUID().toString(),
                        username = this.username,
                        content = content,
                        dataPost = longValue,
                        file = "data:image/jpeg;base64,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",
                        contenttype = "any"
                    )
                    val queue = Volley.newRequestQueue(this)
                    val url = "http://192.168.58.27:3000/post"
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
}