package com.upwork.googlesheetreader.ui.details

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.lightspark.composeqr.QrCodeView
import com.upwork.googlesheetreader.ui.ViewModel
import com.upwork.googlesheetreader.ui.ViewModel.HomeUiState
import com.upwork.googlesheetreader.ui.postData.PlayerData

@Composable
fun SpreadSheetDetails(modifier: Modifier, navigateBack: () -> Unit, viewModel: ViewModel) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val qrCodeText = remember { mutableStateOf("") }
    val playerData by remember {
        mutableStateOf(
            PlayerData()
        )
    }

    LaunchedEffect(Unit) {
        viewModel.getSpreadsheetDetails(viewModel.data.value)
    }


    BackHandler {
        // your action
        navigateBack.invoke()

    }


    when (homeUiState) {
        is HomeUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = modifier.size(50.dp),
                    strokeWidth = 10.dp,
                    strokeCap = StrokeCap.Square,
                    color = Color.Gray
                )
            }
        }

        is HomeUiState.Details -> {
            val response = (homeUiState as HomeUiState.Details).data
            if (response.isEmpty()) {
                Text(text = "empty Data")
            } else

                LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(response) { item ->

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .clickable {
                                    with(playerData) {
                                        try {
                                            firstName = item[0]
                                            secondName = item[1]
                                            age = item[2]
                                            position = item[3]
                                            other = item[4]
                                        } catch (e: Exception) {
                                            Log.e("error", e.message.toString())
                                        }

                                    }
                                    openAlertDialog.value = true
                                }
                                .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = modifier
                                    .padding(vertical = 10.dp, horizontal = 5.dp),
                                text = item[0],
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                            QrCodeView(
                                data = item[0],
                                modifier = Modifier
                                    .size(50.dp)
                            )
                        }
                        Divider(Modifier.height(1.dp))
                    }
                }
        }

        else -> {}
    }

    when (openAlertDialog.value) {
        true -> MinimalDialog(onDismissRequest = {
            openAlertDialog.value = false
        }, playerData)

        false -> {}
    }

}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit, playerData: PlayerData) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                QrCodeView(
                    data = playerData.firstName,
                    modifier = Modifier.size(200.dp)
                )
                Text(text = playerData.firstName + " " + playerData.secondName, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                if (playerData.age.isNotEmpty()) Text(text = "Age : " + playerData.age,fontWeight = FontWeight.Bold)
                if (playerData.position.isNotEmpty()) Text(text = "Position : " + playerData.position,fontWeight = FontWeight.Bold)
                if (playerData.other.isNotEmpty()) Text(text = "other : " + playerData.other,fontWeight = FontWeight.Bold)

            }


        }
    }
}