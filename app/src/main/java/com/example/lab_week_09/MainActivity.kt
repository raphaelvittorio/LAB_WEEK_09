package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.squareup.moshi.Moshi // Import Moshi
import com.squareup.moshi.JsonClass // Import JsonClass
import com.squareup.moshi.Types // Import Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// Tambahkan anotasi untuk Moshi [cite: 750]
@JsonClass(generateAdapter = true)
data class Student(
    var name: String
)

// Inisialisasi Moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home { listDataJson ->
                // Perlu URL encoding untuk string JSON
                navController.navigate("resultContent/?listData=$listDataJson")
            }
        }
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            val listData = it.arguments?.getString("listData").orEmpty()
            ResultContent(listDataJson = listData) // Ganti nama parameter
        }
    }
}


@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    val inputField = remember { mutableStateOf(Student("")) }

    // Siapkan adapter Moshi
    val listType = Types.newParameterizedType(List::class.java, Student::class.java)
    val adapter = remember { moshi.adapter<List<Student>>(listType) }

    HomeContent(
        listData = listData,
        inputField = inputField.value,
        onInputValueChange = { newName ->
            inputField.value = inputField.value.copy(name = newName)
        },
        // Solusi Tugas 1: Cek isNotBlank() [cite: 739]
        onButtonClick = {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        // Solusi Tugas 2: Serialize ke JSON [cite: 750]
        navigateFromHomeToResult = {
            val json = adapter.toJson(listData.toList())
            navigateFromHomeToResult(json)
        }
    )
}

@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))
                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = onInputValueChange
                )

                Row {
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_click),
                        onClick = onButtonClick
                    )
                    PrimaryTextButton(
                        text = stringResource(id = R.string.button_navigate),
                        onClick = navigateFromHomeToResult
                    )
                }
            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// Solusi Tugas 2: Tampilkan list dengan LazyColumn [cite: 750]
@Composable
fun ResultContent(listDataJson: String) {
    // Siapkan adapter Moshi
    val listType = Types.newParameterizedType(List::class.java, Student::class.java)
    val adapter = remember { moshi.adapter<List<Student>>(listType) }

    // Parsing JSON
    val listData: List<Student> = try {
        adapter.fromJson(listDataJson) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    // Tampilkan di LazyColumn
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(listData) { student ->
            OnBackgroundItemText(text = student.name)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    LAB_WEEK_09Theme {
        val navController = rememberNavController()
        App(navController = navController)
    }
}