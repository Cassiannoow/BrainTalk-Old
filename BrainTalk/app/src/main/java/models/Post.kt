package models

import java.io.File

data class Post(
    val id: String,
    val username: String,
    val content: String,
    val dataPost: Long,
    val file: String,
    val contenttype: String
)
