//package com.leishmaniapp.presentation.views
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.width
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Edit
//import androidx.compose.material3.Button
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.leishmaniapp.R
//import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
//import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
//
//data class elementCoordinates(
//    val id: Int,
//    val x: Int,
//    val y: Int,
//    var isPressed: Boolean
//)
//
//
//@Composable
//fun DiagnosticAndAnalysisC02_2 (){
//    var showImageAndButton by remember { mutableStateOf(true) }
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
//
//    )
//
//    LeishmaniappScaffold{
//        Column(modifier = Modifier.fillMaxSize()) {
//            imageResultView(showImageAndButton) {
//                showImageAndButton = it
//            }
//            nameSpetialist()
//            Spacer(modifier = Modifier.height(55.dp))
//            if (showImageAndButton) {
//                editOption()
//                Spacer(modifier = Modifier.height(16.dp))
//                Box(modifier = Modifier
//                    .width(318.dp)
//                    .height(300.dp)
//                    .align(Alignment.CenterHorizontally)
//                ){
//                    imageTaken(painter = painterResource(id = R.drawable.image_example))
//                    for (value in coordElements) {
//                        elementLocatedButton(
//                            xCoordinate = value.x.dp,
//                            yCoordinate = value.y.dp,
//                            isButtonPressed = isButtonPressed,
//                            onClick = {/* Acción al hacer clic */}
//                        )
//                    }
//
//                }
//                Spacer(modifier = Modifier.height(75.dp))
//                buttonNext()
//            }
//        }
//    }
//}
//
//
//@Composable
//fun elementLocatedButton(
//    xCoordinate: Dp,
//    yCoordinate: Dp,
//    isButtonPressed: Boolean,
//    onClick: () -> Unit
//){
//    val iconColor = if (isButtonPressed) {
//        MaterialTheme.colorScheme.error // Cambia el color cuando el botón está presionado
//    } else {
//        MaterialTheme.colorScheme.onPrimary // Color predeterminado
//    }
//
//    IconButton(
//        onClick = {
//            onClick()
//        },
//        modifier = Modifier
//            .offset(x = xCoordinate, y = yCoordinate)
//    ) {
//        Icon(
//            imageVector = Icons.Filled.Close,
//            contentDescription = "marcación de elemento encontrado",
//            tint = iconColor // Usa el color calculado
//        )
//    }
//}
//
//
//@Preview
//@Composable
//fun DiagnosticAndAnalysisPreview2 (){
//    LeishmaniappTheme {
//        DiagnosticAndAnalysisC02_2()
//    }
//}