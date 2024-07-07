package com.ngablak.hijaiyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme

class AboutAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HijaiyahTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AboutAppScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun AboutAppScreen(modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.groupphoto),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp)
            )
            TextBox()
            Image(
                painter = painterResource(id = R.drawable.backbtn),
                contentDescription = "Back Button",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 16.dp)
                    .clickable(onClick = onBackClick)
            )
        }
    }
}

@Composable
fun TextBox() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
            .drawBehind {
                drawRect(color = Color(0xFF242D33))
            }
            .padding(16.dp)
    ) {
        Text(
            text = "Aplikasi Hijaiyah adalah aplikasi yang digunakan untuk membantu pembelajaran anak dalam memahami huruf-huruf hija'iyah. Aplikasi ini dikembangkan oleh tim KKN Desa Ngablak yang beranggotakan:\n" +
                    "1. Gagah Pusoko Adilaga - FSM\n" +
                    "2. Hoga Cavan Afrinata - FT\n" +
                    "3. Ranisa Meifrita Damaranti - FISIP\n" +
                    "4. Risa Nurhaliza - FISIP\n" +
                    "5. Rahmandika Bagas Danendra - FH\n" +
                    "6. Talitha Sabiyamarla Tabina - FIB\n" +
                    "7. Venny Handayani M. - FPSI\n" +
                    "8. Elysa Agustina Hardiyanti - FISIP",
            color = Color.White,
            style = TextStyle.Default,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    HijaiyahTheme {
        AboutAppScreen(onBackClick = {})
    }
}
