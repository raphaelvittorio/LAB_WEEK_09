package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Di sini, kita memanggil Home composable [cite: 213]
                    val list = listOf("Tanu", "Tina", "Tono") //
                    Home(items = list) // [cite: 213]
                }
            }
        }
    }
}

@Composable
fun Home(items: List<String>) { // [cite: 130]
    LazyColumn { // [cite: 135]
        // Item pertama: Input field dan Button [cite: 137]
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally // [cite: 147, 155]
            ) {
                Text(text = stringResource(id = R.string.enter_item)) // [cite: 156]
                TextField( // [cite: 159]
                    value = "", // [cite: 160]
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text // Modul salah, seharusnya Text [cite: 163]
                    ),
                    onValueChange = {} // [cite: 167]
                )
                Button(onClick = {}) { // [cite: 173]
                    Text(text = stringResource(id = R.string.button_click)) // [cite: 175]
                }
            }
        }
        // Item kedua: Daftar nama [cite: 184]
        items(items) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item) // [cite: 188]
            }
        }
    }
}

// Fungsi Pratinjau [cite: 195-202]
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    LAB_WEEK_09Theme {
        Home(listOf("Tanu", "Tina", "Tono"))
    }
}