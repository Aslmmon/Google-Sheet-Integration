package com.upwork.googlesheetreader.ui.postData

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.upwork.googlesheetreader.ui.ViewModel
import com.upwork.googlesheetreader.ui.ViewModel.HomeUiState


@Composable
fun PostDataScreen(modifier: Modifier, viewModel: ViewModel = ViewModel()) {

    val homeUiState by viewModel.homeUiState.collectAsState()
    var isSubmitClicked by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val playerData by remember { mutableStateOf(PlayerData()) }

    LaunchedEffect(isSubmitClicked) {
        if (playerData.age.isEmpty() ||
            playerData.firstName.isEmpty() ||
            playerData.secondName.isEmpty() ||
            playerData.position.isEmpty()
        ) {
            isSubmitClicked=false
        }else {
            if (isSubmitClicked) {
                viewModel.postDataToSpreadSheet(playerData)
                isSubmitClicked = false
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SimpleOutlinedTextFieldSample(modifier, "First Name") { firstName ->
            playerData.firstName = firstName
        }
        SimpleOutlinedTextFieldSample(modifier, "Second Name") { secondName ->
            playerData.secondName = secondName

        }
        SimpleOutlinedTextFieldSample(modifier, "Age") { age ->
            playerData.age = age

        }
        SimpleOutlinedTextFieldSample(modifier, "Position") { pos ->
            playerData.position = pos
        }
        when (homeUiState) {
            is HomeUiState.SuccessSubmitPost -> {
                Text(text = "Data Submittied Succesffuly")
            }

            is HomeUiState.Loading -> {
                CircularProgressIndicator()
            }

            is HomeUiState.Error -> {
                val error = (homeUiState as HomeUiState.Error).message
                Text(text = error)

            }

            else -> {}
        }


        Button(modifier = modifier
            .fillMaxWidth()
            .height(45.dp),
            shape = RoundedCornerShape(8.dp), onClick = {
                isSubmitClicked = true
            }) {
            Text(text = "submit data")
        }

    }
}


@Composable
fun SimpleOutlinedTextFieldSample(
    modifier: Modifier,
    label: String,
    textWritten: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        isError = isError,
        onValueChange = {
            text = it
            textWritten.invoke(text)
            isError = text.isEmpty()

        },
        label = { Text(label) }
    )
}

data class PlayerData(
    var firstName: String = "",
    var secondName: String = "",
    var age: String = "",
    var position: String = ""
)