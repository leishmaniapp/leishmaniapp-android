package com.leishmaniapp.presentation.views

import android.os.Bundle
import com.leishmaniapp.presentation.ui.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.theme.LeishmaniappTheme
import androidx.compose.material3.Text as Text
import com.leishmaniapp.presentation.ui.AppNameHeader


class PatientListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LeishmaniappTheme {
                previewPatientList()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PatientListScreen(/*listPatients: List<String>*/) {
        // val context = LocalContext.current
        val listPatients: List<String> = resources.getStringArray(R.array.patients).toList()
        var filteredPatients: List<String> = listPatients
        var query by remember {
            mutableStateOf("")
        }
        var onSearch: (String) -> Unit = {
            println("query: $query")
        }

        var active by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                AppNameHeader()
                Text(
                    text = stringResource(R.string.title_patients),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(20.dp, 20.dp, 0.dp, 5.dp)
                )
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        onSearch = onSearch,
                        active = active,
                        onActiveChange = { active = it },
                        modifier = Modifier
                            .padding(5.dp)
                            .height(70.dp)
                            .width(300.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_patients),
                                color = Color.Gray
                            )
                        },
                        leadingIcon = {
                            IconButton(
                                onClick = { onSearch(query) },
                                enabled = query.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            }
                        }
                    ) {
                        filteredPatients = if (query.isNotEmpty()) {
                            listPatients.filter { it.contains(query, true) }
                        } else {
                            listPatients
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    IconButton(
                        onClick = { onSearch(query) },
                        enabled = query.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                }
                LazyColumn() {
                    items(filteredPatients) {
                        Row(
                            modifier= Modifier.padding(20.dp, 0.dp, 0.dp, 0.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Text(
                                text = it,
                                fontWeight = FontWeight.Light,
                                modifier = Modifier.padding(15.dp)
                            )
                        }
                    }

                }
            }
        }
    }


    @Preview
    @Composable
    fun previewPatientList() {
        LeishmaniappTheme {
            PatientListScreen()
        }

    }
}

