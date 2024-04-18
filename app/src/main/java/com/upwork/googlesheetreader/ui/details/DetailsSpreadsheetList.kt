package com.upwork.googlesheetreader.ui.details

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lightspark.composeqr.QrCodeView
import com.upwork.googlesheetreader.ui.ViewModel
import com.upwork.googlesheetreader.ui.ViewModel.HomeUiState
import com.upwork.googlesheetreader.ui.postData.PlayerData
import com.upwork.googlesheetreader.ui.postData.components.LoaderIndicator
import com.upwork.googlesheetreader.ui.postData.components.MinimalDialog

@Composable
fun SpreadSheetDetails(modifier: Modifier, navigateBack: () -> Unit, viewModel: ViewModel) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
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
            LoaderIndicator(modifier)
        }

        is HomeUiState.Details -> {
            val response = (homeUiState as HomeUiState.Details).data
            if (response.isEmpty()) {
                Text(text = "empty Data")
            } else

                Column(modifier = modifier.padding(vertical = 15.dp, horizontal = 5.dp)) {
                    Text(
                        text = viewModel.data.value,
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                    LazyColumn(
                        modifier = modifier,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
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
                                                isCaptured = item[4]
                                                other = item[5]
                                            } catch (e: Exception) {
                                                Log.e("error", e.message.toString())
                                            }

                                        }
                                        openAlertDialog.value = true
                                    }
                                    .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(modifier = modifier.weight(1f)) {
                                    Text(
                                        modifier = modifier
                                            .weight(0.5f)
                                            .padding(vertical = 10.dp, horizontal = 5.dp),
                                        text = item[0] + " " + item[1],
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif
                                    )
                                    if (item.size > 4) {
                                        if (item[4] == "FALSE") {
                                            Icon(
                                                Icons.Rounded.ErrorOutline,
                                                contentDescription = "",
                                                tint = Color.Red,
                                                modifier = modifier
                                                    .size(30.dp)
                                                    .weight(0.5f)
                                            )
                                        } else {
                                            Icon(
                                                Icons.Rounded.DoneOutline,
                                                contentDescription = "",
                                                tint = Color.Green,
                                                modifier = modifier
                                                    .size(30.dp)
                                                    .weight(0.5f)
                                            )
                                        }
                                    }
                                }
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

