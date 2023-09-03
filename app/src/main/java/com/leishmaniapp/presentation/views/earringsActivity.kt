package com.leishmaniapp.presentation.views


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme
import com.leishmaniapp.presentation.ui.theme.Purple41
import com.leishmaniapp.presentation.ui.theme.md_theme_light_loading


@Composable
fun earringsActivity(){
    LeishmaniappScaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            //leishmaniapNameBar()
            Spacer(modifier = Modifier.height(53.dp))
            textTypeUser()
            Spacer(modifier = Modifier.height(9.dp))
            nameSpecialist(name = "Roberto Correa")
            Spacer(modifier = Modifier.height(25.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(445.dp)
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                earringsTable()
                element("1003452123 08/08/2023 9:00", true)
                element("1003452123 08/08/2023 9:00", false)
            }
            sincronice()
        }
    }
}

@Composable
fun textTypeUser(){
    Text(
        text = "Especialista",
        style = TextStyle(
            fontSize = 15.sp,
            //fontFamily = FontFamily(Font(R.font.inter)),
            fontWeight = FontWeight(500),
            color = Color(0xFF000000),
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .width(147.dp)
            .height(18.dp)
    )
}

@Composable
fun nameSpecialist(name : String){
    Text(
        text = name,
        style = TextStyle(
            fontSize = 15.sp,
            //fontFamily = FontFamily(Font(R.font.inter)),
            fontWeight = FontWeight(400),
            color = Color(0xFF000000),
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .width(171.dp)
            .height(20.dp)
    )
}

@Composable
fun element(id : String/*, date : Date*/, state : Boolean){
    Column {
        Row(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp)

        ) {
            Text(
                text = id,
            )
            Spacer(modifier = Modifier.width(50.dp))
            if(state)
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            else
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = MaterialTheme.colorScheme.error)

        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun earringsTable(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                color = Purple41,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Pendientes",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun sincronice(){
    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        IconButton(
            onClick = { /* doSomething() */ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.Refresh , contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Text(
            text = stringResource(id = R.string.label_synchronize),
            style = TextStyle(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .width(171.dp)
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview
@Composable
fun earringsActivityPreview(){
    LeishmaniappTheme {
        earringsActivity()
    }
}