package com.leishmaniapp.presentation.views


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leishmaniapp.MainActivity
import com.leishmaniapp.R
import com.leishmaniapp.presentation.ui.LeishmaniappScaffold
import com.leishmaniapp.presentation.ui.theme.LeishmaniappTheme

@Composable
fun DiseasesMenuActivity(){
    LeishmaniappScaffold {
            val context = LocalContext.current

            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(52.dp))
                diseaseLabel()
                Spacer(modifier = Modifier.height(26.dp))
                searchDisease()
                Spacer(modifier = Modifier.height(43.dp))
                pacientList(
                    painter = painterResource(id = R.drawable.image_pantalla_inicial),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    stringResId = R.string.label_leishmaniasis,
                    /*MenuTypeDeseaseActivity::class.java*/
                )
                pacientList(
                    painter = painterResource(id = R.drawable.image_pantalla_inicial),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    stringResId = R.string.label_Malaria,
                    /*MenuTypeDeseaseActivity::class.java*/
                )
            }
    }
}

@Composable
fun leishmaniapNameBar(){
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Black)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.White,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFFFFFFFF),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .width(223.dp)
                    .height(39.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun diseaseLabel(){
    Text(
        text = stringResource(id = R.string.label_diseases),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight(700),
            //textAlign = TextAlign.Center
            color = Color.Black
        ),
        modifier = Modifier
            .width(280.dp)
            .height(39.dp)
            .padding(horizontal = 24.dp)
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun searchDisease(){
    var ctx = LocalContext.current
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(
        query = query,
        onQueryChange = { query = it},
        onSearch = {
            Toast.makeText(ctx, query, Toast.LENGTH_SHORT).show()
            active = false
        },
        active = active,
        onActiveChange = { active = it},
        placeholder = { Text(text = "Buscar enfermedad")},
        leadingIcon = {
            IconButton(onClick = { /*SE HACE LA FUNCION PARA HACER LA BUSQUEDA*/ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "icono siguiente"
                )
            }
        },
        modifier = Modifier
            .padding(horizontal = 26.dp)
    )
    {

    }
}

@Composable
fun pacientList(painter: Painter, modifier: Modifier, stringResId: Int /*menuTypeDeseaseActivity: Class<*>*/){
    val context = LocalContext.current

    Column(modifier = Modifier.clickable{
        /*val intent = Intent(context, menuTypeDeseaseActivity)
        context.startActivity(intent)*/
    }) {
        Row (
            modifier = Modifier.padding(29.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            diseaseImage(
                painter = painter,
                modifier = modifier
            )
            Spacer(modifier = Modifier.width(38.dp))
            diseaseName(stringResId = stringResId)
            Spacer(modifier = Modifier.width(78.dp))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
        }
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
fun diseaseImage(painter: Painter, modifier: Modifier){
    Image(
        painter = painter,
        contentDescription = "imagen de enfermedad",
        modifier = modifier
            .size(65.dp)
            .clip(CircleShape) // Aplica un recorte circular
    )
}

@Composable
fun diseaseName(stringResId: Int){
    Text(
        text = stringResource(id = stringResId),
        style = TextStyle(
            fontSize = 17.sp,
            //fontFamily = FontFamily(Font(R.font.inter)),
            fontWeight = FontWeight(400),
            color = Color(0xFF000000),
        ),
        modifier = Modifier
            .width(140.dp)
            .height(26.dp)
    )
}

@Preview
@Composable
fun menuDiseasesPreview(){
    LeishmaniappTheme {
        DiseasesMenuActivity()
    }
}