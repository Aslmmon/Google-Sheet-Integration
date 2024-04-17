package com.upwork.googlesheetreader.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
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
import com.upwork.googlesheetreader.ui.ViewModel.*


@Composable
fun SpreadSheetList(
    modifier: Modifier,
    viewModel: ViewModel,
    onNavigate: () -> Unit,
    navigateToPostScreen: () -> Unit
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getSpreadsheet()

    }
    when (homeUiState) {
        is HomeUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = modifier.size(50.dp)
                )
            }
        }

        is HomeUiState.Success -> {
            val response = (homeUiState as HomeUiState.Success).data
            Scaffold(
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        viewModel.setSheetList(response)
                        navigateToPostScreen.invoke()
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {

                    LazyColumn(
                        modifier = modifier.padding(vertical = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(response) { item ->
                            Text(
                                modifier = modifier
                                    .padding(vertical = 10.dp, horizontal = 5.dp)
                                    .clickable {
                                        viewModel.setData(item.properties?.title ?: "")
                                        onNavigate.invoke()
                                    },
                                text = item.properties?.title ?: ""
                            )
                            Divider(Modifier.height(1.dp))
                        }
                    }
                }
            }

        }

        else -> {

        }
    }


}