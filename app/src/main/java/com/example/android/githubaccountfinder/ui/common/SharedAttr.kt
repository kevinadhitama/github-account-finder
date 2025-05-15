package com.example.android.githubaccountfinder.ui.common

import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.ui.graphics.Color

object SharedAttr {

    @OptIn(ExperimentalMaterial3Api::class)
    fun getAppBarColors() = TopAppBarColors(
        containerColor = Color.White,
        titleContentColor = Color.Black,
        scrolledContainerColor = Color.Gray,
        navigationIconContentColor = Color.Black,
        actionIconContentColor = Color.Black
    )

    fun getCardColors() = CardColors(
        containerColor = Color.White,
        contentColor = Color.Black,
        disabledContainerColor = Color.LightGray,
        disabledContentColor = Color.Gray
    )
}