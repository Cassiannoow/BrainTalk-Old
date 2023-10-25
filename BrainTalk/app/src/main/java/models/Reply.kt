package models

data class Reply(
    val replyId: String,
    val postId: String,
    val userId: String,
    val content: String,
    val timestamp: Long
)
