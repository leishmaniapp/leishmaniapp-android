package com.leishmaniapp.presentation.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

data class NavigationBar(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun DiagnosticAndAnalysis (){
    var showImageAndButton by remember { mutableStateOf(true) }

    LeishmaniappScaffold{
        Column(modifier = Modifier.fillMaxSize()) {
            imageResultView(showImageAndButton) {
                showImageAndButton = it
            }
            nameSpetialist()
            Spacer(modifier = Modifier.height(104.dp))
            if (showImageAndButton) {
                imageTaken(painter = painterResource(id = R.drawable.image_example))
                Spacer(modifier = Modifier.height(75.dp))
                buttonNext()
            }
            navBar()
        }
    }
}

@Composable
fun imageResultView(showImageAndButton: Boolean, onButtonClick: (Boolean) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .height(50.dp)
    ) {
        Column {
            TextButton(
                onClick = {
                    onButtonClick(true)
                }
            ) {
                Column {
                    Text(
                        text = "Imagen",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(400),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    if (showImageAndButton) {
                        Spacer(
                            modifier = Modifier
                                .height(2.dp)
                                .width(140.dp)
                                .background(Color.White)
                        )
                    }
                }
            }
        }

        Column {
            TextButton(
                onClick = {
                    onButtonClick(false)
                }
            ) {
                Column {
                    Text(
                        text = "Resultados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight(400),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    if (!showImageAndButton) {
                        Spacer(
                            modifier = Modifier
                                .height(2.dp)
                                .width(140.dp)
                                .background(Color.White)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun nameSpetialist() {
    Text(
        text = "Name",
        style = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFFFFFFFF),
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
    )
}

@Composable
fun imageTaken(painter: Painter){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // Opcional, para dar un fondo blanco si lo deseas
    ) {
        Image(
            painter = painter,
            contentDescription = "image taken",
            modifier = Modifier
                .width(318.dp)
                .height(300.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun buttonNext (){
    val context = LocalContext.current
    Button(
        onClick = {
            //val intent = Intent(context, DiseasesMenuActivity::class.java)
            //context.startActivity(intent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 86.dp)
    ){
        Text(
            text = "Ver resultados",
            style = TextStyle(
                fontSize = 20.sp,
                //fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun navBar(){
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavigationBar(
            title = "Repetir",
            selectedIcon = Icons.Filled.Refresh,
            unselectedIcon = Icons.Filled.Refresh,
        ),
        NavigationBar(
            title = "Analizar",
            selectedIcon = Icons.Filled.Send,
            unselectedIcon = Icons.Outlined.Send,
        ),
        NavigationBar(
            title = "Siguiente",
            selectedIcon = Icons.Filled.CameraAlt,
            unselectedIcon = Icons.Outlined.CameraAlt,
        ),
        NavigationBar(
            title = "Finalizar",
            selectedIcon = Icons.Filled.Check,
            unselectedIcon = Icons.Outlined.Check,
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick =
                        {
                            selectedItem = index
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            BadgedBox(
                                badge = {

                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItem)
                                    {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {
    }
}

@Preview
@Composable
fun DiagnosticAndAnalysisPreview (){
    LeishmaniappTheme {
        DiagnosticAndAnalysis()
    }
}