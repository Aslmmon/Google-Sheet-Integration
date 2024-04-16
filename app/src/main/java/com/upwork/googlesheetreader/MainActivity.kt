package com.upwork.googlesheetreader

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.idmt.simplevoice.ui.network.RetrofitMoviesNetworkApi
import com.idmt.simplevoice.ui.network.RetrofitNetworkApi
import com.upwork.googlesheetreader.ui.PostDataScreen
import com.upwork.googlesheetreader.ui.SpreadSheetDetails
import com.upwork.googlesheetreader.ui.SpreadSheetList
import com.upwork.googlesheetreader.ui.ViewModel
import com.upwork.googlesheetreader.ui.theme.GoogleSheetReaderTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var viewModel = ViewModel()
        setContent {
            GoogleSheetReaderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "spreadsheet") {
                        composable("spreadsheet") {
                            SpreadSheetList(
                                modifier = Modifier,
                                onNavigate = {
                                    navController.navigate("spreadsheetDetails")
                                }, navigateToPostScreen = {
                                    navController.navigate("postScreen")
                                }, viewModel = viewModel)

                        }
                        composable("spreadsheetDetails") {
                            SpreadSheetDetails(modifier = Modifier, navigateBack = {
                                navController.navigateUp()
                            },viewModel=viewModel)
                        }
                        composable("postScreen") {
                            PostDataScreen(modifier = Modifier)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GoogleSheetReaderTheme {
        Greeting("Android")
    }
}