package com.ngablak.hijaiyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme

class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HijaiyahTheme {
                val selectedItem = remember { mutableStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            selectedItem = selectedItem.value,
                            onItemSelected = { selectedItem.value = it }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        Image(
                            painter = painterResource(id = R.drawable.bg),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Column {
                            Content(selectedItem = selectedItem.value)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Banner(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .height(100.dp)
    )
}

@Composable
fun Content(selectedItem: Int) {
    when (selectedItem) {
        0 -> {
            Banner(imageRes = R.drawable.banner_hijaiyah)
        }
        1 -> {
            Banner(imageRes = R.drawable.banner_asma)
        }
        2 -> {
            Banner(imageRes = R.drawable.banner_doa)
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)),
        containerColor = Color(0xFF242D33)
    ) {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.botnav_hija),
                    contentDescription = null,
                    modifier = Modifier.size(65.dp)
                )
            },
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.botnav_asma),
                    contentDescription = null,
                    modifier = Modifier.size(65.dp)
                )
            },
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) }
        )
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.botnav_doa),
                    contentDescription = null,
                    modifier = Modifier.size(65.dp)
                )
            },
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewActivityPreview() {
    HijaiyahTheme {
        val selectedItem = remember { mutableStateOf(0) }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    selectedItem = selectedItem.value,
                    onItemSelected = { selectedItem.value = it }
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)) {
                Image(
                    painter = painterResource(id = R.drawable.bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column {
                    Content(selectedItem = selectedItem.value)
                }
            }
        }
    }
}
