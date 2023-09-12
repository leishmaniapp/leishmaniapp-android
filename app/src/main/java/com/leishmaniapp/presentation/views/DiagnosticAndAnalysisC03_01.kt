//package com.leishmaniapp.presentation.views
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.BadgedBox
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.leishmaniapp.R
//import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
//import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
//
//
//data class AcceptRejectBar(
//    val title: String,
//    val selectedIcon: ImageVector,
//)
//
//@Composable
//fun DiagnosticAndAnalysisC03_01 (){
//    var showImageAndButton by remember { mutableStateOf(true) }
//    var showUncheckButton by remember { mutableStateOf(false) }
//    var isButtonPressed by remember { mutableStateOf(false) }
//
//
//    val coordElements = listOf(
//        elementCoordinates(
//            id = 1,
//            x = 50,
//            y = 110,
//            isPressed = false
//        ),
//        elementCoordinates(
//            id = 2,
//            x = 70,
//            y = 30,
//            isPressed = false
//        ),
//        elementCoordinates(
//            id = 3,
//            x = 4,
//            y = 230,
//            isPressed = false
//        ),
//        )
//
//    LeishmaniappScaffold{
//        Column(modifier = Modifier.fillMaxSize()) {
//            imageResultView(showImageAndButton) {
//                showImageAndButton = it
//            }
//            nameSpetialist()
//            Spacer(modifier = Modifier.height(55.dp))
//            if (showImageAndButton) {
//                titleEdit()
//                Spacer(modifier = Modifier.height(44.dp))
//                Box(modifier = Modifier
//                    .width(318.dp)
//                    .height(300.dp)
//                    .align(Alignment.CenterHorizontally)
//                ){
//                    imageTaken(painter = painterResource(id = R.drawable.image_example))
//                    coordElements.forEach { element ->
//                        elementLocatedButton(
//                            xCoordinate = element.x.dp,
//                            yCoordinate = element.y.dp,
//                            isButtonPressed = element.isPressed,
//                            onClick = {
//                                // Cambiar el estado del botón cuando se presione
//                                element.isPressed = !element.isPressed
//                                showUncheckButton = true
//                            }
//                        )
//                    }
//
//                }
//                Spacer(modifier = Modifier.height(75.dp))
//                if(showUncheckButton)
//                    uncheckElement()
//            }
//            acceptReject()
//        }
//    }
//}
//
//@Composable
//fun titleEdit(){
//    Text(
//        text = "Editar diagnóstico",
//        style = TextStyle(
//            fontSize = 20.sp,
//            fontWeight = FontWeight(500),
//            color = MaterialTheme.colorScheme.primary,
//            textAlign = TextAlign.Center,
//        ),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(24.dp)
//    )
//}
//
//@Composable
//fun uncheckElement(){
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = "¿Desea desmarcar el elemento?",
//            style = TextStyle(
//                fontSize = 15.sp,
//                fontWeight = FontWeight(350),
//                textAlign = TextAlign.Center,
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(24.dp)
//        )
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            TextButton(
//                onClick = {
//                }
//            ) {
//                Text(text = "Si")
//            }
//            TextButton(
//                onClick = { /*TODO*/ }
//            ) {
//                Text(text = "No")
//            }
//        }
//    }
//}
//
//
//@Composable
//fun acceptReject(){
//    var selectedItem by remember { mutableIntStateOf(0) }
//    val items = listOf(
//        AcceptRejectBar(
//            title = "Descartar",
//            selectedIcon = Icons.Filled.Close,
//        ),
//        AcceptRejectBar(
//            title = "Confirmar",
//            selectedIcon = Icons.Filled.Check,
//        )
//    )
//
//    Scaffold(
//        bottomBar = {
//            NavigationBar {
//                items.forEachIndexed { index, item ->
//                    NavigationBarItem(
//                        selected = selectedItem == index,
//                        onClick =
//                        {
//                            selectedItem = index
//                        },
//                        label = {
//                            Text(text = item.title)
//                        },
//                        icon = {
//                            BadgedBox(
//                                badge = {
//
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = item.selectedIcon,
//                                    contentDescription = item.title
//                                )
//                            }
//                        }
//                    )
//                }
//            }
//        }
//    ) {
//    }
//}
//
//@Preview
//@Composable
//fun DiagnosticAndAnalysisC03_01Preview (){
//    LeishmaniappTheme {
//        DiagnosticAndAnalysisC03_01()
//    }
//}