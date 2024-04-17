package com.upwork.googlesheetreader.ui.postData.components

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.upwork.googlesheetreader.network.model.spreadsheet.Sheet


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(
    modifier: Modifier,
    sheetList: List<Sheet>,
    selectedTextFunc: (String) -> Unit
) {
    val context = LocalContext.current
//    val coffeeDrinks = arrayOf("Americano", "Cappuccino", "Espresso", "Latte", "Mocha")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(sheetList.get(0).properties?.title ?: "") }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {
                    selectedTextFunc.invoke(selectedText)

                },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                sheetList.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.properties?.title ?: "") },
                        onClick = {
                            selectedText = item.properties?.title ?: ""
                            selectedTextFunc.invoke(selectedText)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}