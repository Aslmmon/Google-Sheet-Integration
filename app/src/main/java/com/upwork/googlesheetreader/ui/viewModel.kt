package com.upwork.googlesheetreader.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idmt.simplevoice.ui.network.RetrofitMoviesNetworkApi
import com.upwork.googlesheetreader.network.model.spreadsheet.Sheet
import com.upwork.googlesheetreader.ui.postData.PlayerData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {
    private var retrofitMoviesNetworkApi = RetrofitMoviesNetworkApi

    private var _homeUiState: MutableStateFlow<HomeUiState?> =
        MutableStateFlow(null)
    val homeUiState: MutableStateFlow<HomeUiState?> get() = _homeUiState

    private val _initApiCalled = mutableStateOf(false)
    val initApiCalled: State<Boolean> = _initApiCalled

    private val _data = mutableStateOf<String>("")
    val data: State<String> = _data

    private val _sheetList = mutableStateOf(emptyList<Sheet>())
    val sheetList: State<List<Sheet>> = _sheetList

    fun setData(newData: String) {
        _data.value = newData
    }

    fun setSheetList(newData: List<Sheet>) {
        _sheetList.value = newData
    }

    fun setInitApiCalled(isInit: Boolean) {
        _initApiCalled.value = isInit
    }

    init {
        //  getSpreadsheet()
    }

    fun getSpreadsheet() {
        viewModelScope.launch {
            _homeUiState.update {
                HomeUiState.Loading()
            }
            try {
                val data = retrofitMoviesNetworkApi.getSpreadSheetData()
                _homeUiState.update {
                    HomeUiState.Success(data.sheets)

                }
            } catch (e: Exception) {
                _homeUiState.update {
                    HomeUiState.Error(e.message.toString())
                }
            }
        }


    }

    fun getSpreadsheetDetails(sheetName: String) {


        viewModelScope.launch {
            _homeUiState.update {
                HomeUiState.Loading()
            }
            try {
                val data = retrofitMoviesNetworkApi.getSpreadSheetDataDetails(sheetName)
                _homeUiState.update {
                    HomeUiState.Details(data.values)

                }
            } catch (e: Exception) {
                _homeUiState.update {
                    HomeUiState.Error(e.message.toString())
                }
            }
        }


    }


    suspend fun postDataToSpreadSheet(playerData: PlayerData) {

        _homeUiState.update {
            HomeUiState.Loading()
        }
        viewModelScope.launch {

            try {
                retrofitMoviesNetworkApi.postDataToSpreadSheet(
                    playerData.spreadSheetName,
                    playerData.firstName,
                    playerData.secondName,
                    playerData.age,
                    playerData.position
                )
                _homeUiState.update {
                    HomeUiState.SuccessSubmitPost()
                }
            } catch (e: Exception) {
                _homeUiState.update {
                    HomeUiState.Error(e.message.toString())
                }
            }
        }


    }


    sealed class HomeUiState {
        data class Success(val data: List<Sheet>) : HomeUiState()
        data class Details(val data: List<List<String>>) : HomeUiState()
        class SuccessSubmitPost : HomeUiState()
        data class Error(val message: String) : HomeUiState()
        class Loading : HomeUiState()


    }

}