package com.bt.braintalk

import android.view.View

interface OnPostItemClickListener {
    fun onPostItemClick(postId: String, v: View)
    fun onLikeButtonClick(postId: String, v: View)
}