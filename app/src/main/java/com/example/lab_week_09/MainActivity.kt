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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// Definisikan Data Class [cite: 239-242]
data class Student(
    var name: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home()
                }
            }
        }
    }
}

@Composable
fun Home() {
    // State untuk daftar siswa [cite: 248, 255, 260]
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"), // [cite: 262]
            Student("Tina"), // [cite: 263]
            Student("Tono")  // [cite: 264]
        )
    }

    // State untuk input field [cite: 265, 267]
    val inputField = remember { mutableStateOf(Student("")) }

    // Memanggil HomeContent (Stateless) [cite: 268]
    HomeContent(
        listData = listData, // [cite: 274]
        inputField = inputField.value, // [cite: 275]
        onInputValueChange = { newName -> // [cite: 276]
            inputField.value = inputField.value.copy(name = newName)
        },
        onButtonClick = { // [cite: 278]
            // Modul ini memiliki bug, seharusnya pengecekan ada di sini
            if (inputField.value.name.isNotBlank()) { //
                listData.add(inputField.value) // Tambahkan ke list
                inputField.value = Student("") // Reset input field [cite: 281]
            }
        }
    )
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>, // [cite: 295]
    inputField: Student, // [cite: 296]
    onInputValueChange: (String) -> Unit, // [cite: 297]
    onButtonClick: () -> Unit // [cite: 298]
) {
    LazyColumn { // [cite: 299]
        item { // [cite: 300]
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally // [cite: 312, 316]
            ) {
                Text(text = stringResource(id = R.string.enter_item)) // [cite: 317]
                TextField( // [cite: 321]
                    value = inputField.name, // [cite: 323]
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text // [cite: 327]
                    ),
                    onValueChange = onInputValueChange // [cite: 338, 344]
                )
                Button(onClick = onButtonClick) { // [cite: 348, 353]
                    Text(text = stringResource(id = R.string.button_click)) // [cite: 355]
                }
            }
        }

        // Tampilkan daftar [cite: 362]
        items(listData) { item -> // [cite: 366]
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item.name) // [cite: 371]
            }
        }
    }
}

// Pratinjau tidak lagi berfungsi dengan state, tapi bisa dibuat
@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    LAB_WEEK_09Theme {
        Home()
    }
}