//package com.leishmaniapp.presentation.ui.views.menu
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.leishmaniapp.R
//import com.leishmaniapp.presentation.ui.layout.LeishmaniappScaffold
//import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
//
///**
// * @view F01
// */
//@Composable
//fun DatabaseScreen(onExit: () -> Unit, onImport: () -> Unit, onExport: () -> Unit) {
//    LeishmaniappScaffold(
//        title = stringResource(id = R.string.database),
//        backButtonAction = onExit,
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .padding(bottom = 0.dp, top = 20.dp)
//        ) {
//            Text(
//                text = stringResource(id = R.string.export_import),
//                style = MaterialTheme.typography.headlineMedium
//            )
//
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(32.dp)
//                    .weight(1f),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                Button(onClick = onExport) {
//                    Text(
//                        text = String.format(
//                            "%s %s",
//                            stringResource(id = R.string.action_export),
//                            stringResource(id = R.string.database)
//                        )
//                    )
//                }
//                Button(onClick = onImport) {
//                    Text(
//                        text = String.format(
//                            "%s %s",
//                            stringResource(id = R.string.action_import),
//                            stringResource(id = R.string.database)
//                        )
//                    )
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .padding(36.dp)
//                    .weight(1f)
//            ) {
//                Text(text = stringResource(id = R.string.export_import_screen_alert))
//            }
//        }
//    }
//}
//
//@Composable
//@Preview
//fun DatabaseScreenPreview() {
//    LeishmaniappTheme {
//        DatabaseScreen(onExit = {}, onImport = {}, onExport = {})
//    }
//}