package models

data class Post(
    val postId: String,
    val userId: String,
    val content: String,
    val timestamp: Long
)
