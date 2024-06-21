//package com.leishmaniapp.presentation.ui.composables
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.ListItem
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.leishmaniapp.entities.disease.Disease
//import com.leishmaniapp.entities.disease.MockDotsDisease
//import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
//
//@Composable
//fun DiseaseItemList(
//    modifier: Modifier = Modifier, disease: Disease, onClick: () -> Unit
//) {
//    Column(modifier = modifier.clickable { onClick.invoke() }) {
//        ListItem(headlineContent = {
//            Text(
//                text = disease.displayName,
//                modifier = Modifier.padding(16.dp)
//            )
//        }, leadingContent = {
//            Image(
//                painter = disease.painterResource,
//                contentDescription = null,
//                modifier = modifier
//                    .size(65.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.FillBounds
//            )
//        }, trailingContent = {
//            Icon(
//                Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                contentDescription = null,
//            )
//        })
//
//        HorizontalDivider(
//            color = MaterialTheme.colorScheme.surfaceVariant
//        )
//    }
//}
//
//@Composable
//@Preview(showBackground = true)
//fun DiseaseItemListPreview() {
//    LeishmaniappTheme {
//        DiseaseItemList(disease = MockDotsDisease) {}
//    }
//}