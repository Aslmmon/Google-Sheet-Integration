package com.upwork.googlesheetreader.ui.postData

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.upwork.googlesheetreader.ui.ViewModel
import com.upwork.googlesheetreader.ui.ViewModel.HomeUiState
import com.upwork.googlesheetreader.ui.postData.components.ExposedDropdownMenuBox
import com.upwork.googlesheetreader.ui.postData.components.SimpleOutlinedTextFieldSample


@Composable
fun PostDataScreen(modifier: Modifier, viewModel: ViewModel) {
    val context = LocalContext.current

    val homeUiState by viewModel.homeUiState.collectAsState()
    var isSubmitClicked by remember { mutableStateOf(false) }
    val playerData by remember {
        mutableStateOf(
            PlayerData(
                spreadSheetName = viewModel.sheetList.value.get(
                    0
                ).properties?.title ?: ""
            )
        )
    }

    LaunchedEffect(isSubmitClicked) {
        if (playerData.age.isEmpty() ||
            playerData.firstName.isEmpty() ||
            playerData.secondName.isEmpty() ||
            playerData.position.isEmpty()
        ) {
            isSubmitClicked = false
        } else {
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


        ExposedDropdownMenuBox(modifier, viewModel.sheetList.value) { selectedText ->
            Toast.makeText(
                context,
                selectedText,
                Toast.LENGTH_SHORT
            ).show()
            playerData.spreadSheetName = selectedText

        }

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
                //    val error = (homeUiState as HomeUiState.Error).message
                // Text(text = error)

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


data class PlayerData(
    var spreadSheetName: String="",
    var firstName: String = "",
    var secondName: String = "",
    var age: String = "",
    var position: String = "",
    var other: String = ""

)