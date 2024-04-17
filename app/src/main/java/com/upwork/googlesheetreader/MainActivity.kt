package com.upwork.googlesheetreader

import android.os.Bundle
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
import com.upwork.googlesheetreader.ui.postData.PostDataScreen
import com.upwork.googlesheetreader.ui.details.SpreadSheetDetails
import com.upwork.googlesheetreader.ui.home.SpreadSheetList
import com.upwork.googlesheetreader.ui.ViewModel
import com.upwork.googlesheetreader.ui.splash.SplashScreen
import com.upwork.googlesheetreader.ui.theme.GoogleSheetReaderTheme

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
                    NavHost(navController, startDestination = "splash") {
                        composable("spreadsheet") {
                            SpreadSheetList(
                                modifier = Modifier,
                                onNavigate = {
                                    navController.navigate("spreadsheetDetails")
                                }, navigateToPostScreen = {
                                    navController.navigate("postScreen")
                                }, viewModel = viewModel
                            )

                        }
                        composable("spreadsheetDetails") {
                            SpreadSheetDetails(modifier = Modifier, navigateBack = {
                                navController.navigateUp()
                            }, viewModel = viewModel)
                        }
                        composable("postScreen") {
                            PostDataScreen(modifier = Modifier)
                        }

                        composable("splash") {
                            SplashScreen(modifier = Modifier, navigateToHome = {
                                navController.navigate("spreadsheet") {
                                    navController.popBackStack()
                                }
                            })
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