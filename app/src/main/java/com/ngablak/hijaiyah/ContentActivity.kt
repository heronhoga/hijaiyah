package com.ngablak.hijaiyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
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
            Column {
                Banner(imageRes = R.drawable.banner_hijaiyah)
                HijaiyahContent(
                    items = listOf(
                        "أ" to "Alif", "ب" to "Ba", "ت" to "Ta", "ث" to "Tha", "ج" to "Jim", "ح" to "Ha", "خ" to "Kha", "د" to "Dal", "ذ" to "Dhal",
                        "ر" to "Ra", "ز" to "Zay", "س" to "Sin", "ش" to "Shin", "ص" to "Sad", "ض" to "Dad", "ط" to "Ta", "ظ" to "Zha", "ع" to "Ain",
                        "غ" to "Ghain", "ف" to "Fa", "ق" to "Qaf", "ك" to "Kaf", "ل" to "Lam", "م" to "Mim", "ن" to "Nun", "ه" to "Ha", "و" to "Waw", "ي" to "Ya", "ء" to "Hamzah"
                    )
                )
            }
        }
        1 -> {
            Column {
                Banner(imageRes = R.drawable.banner_asma)
                AsmaulHusnaContent(
                    items = listOf(
                        "Ar-Rahman", "Ar-Rahim", "Al-Malik", "Al-Quddus", "As-Salam", "Al-Mu'min", "Al-Muhaimin", "Al-Aziiz",
                        "Al-Jabbar", "Al-Mutakabbir", "Al-Khaliq", "Al-Baari'", " Al-Mushawwir", "Al-Ghaffaar", "Al-Qahhaar", " Al-Wahhaab",
                        "Ar-Razzaq", "Al-Fattaah", "Al-Aliim", "Al-Qaabidh", "Al-Baasith", "Al-Khaafidh", "Ar-Raafi'", "Al-Mu'iz", "Al-Mudzil",
                        "Al-Samii'", "Al-Bashiir", "Al-Hakam", "Al-Adl", "Al-Lathiif", "Al-Khabiir", "Al-Haliim", "Al-Azhiim", "Al-Ghafuur",
                        "As-Syakuur", "Al-Ali", "Al-Kabiir", "Al-Hafizh", "Al-Muqiit", "Al-Hasiib", "Al-Jaliil", "Al-Kariim", "Ar-Raqiib", "Al-Mujiib",
                        "Al-Waasi'", "Al-Hakiim", "Al-Waduud", "Al-Majiid", "Al-Baa'its", "As-Syahiid", "Al-Haqq", "Al-Wakiil", "Al-Qawiyyu", "Al-Matiin",
                        "Al-Waliyy", "Al-Hamiid", "Al-Mushii", "Al-Mubdi'", "Al-Mu'iid", "Al-Muhyii", "Al-Mumiitu", "Al-Hayyu", "Al-Qayyuum", "Al-Waajid",
                        "Al-Maajid", "Al-Wahiid", "Al-Ahad", "As-Shamad", "Al-Qaadir", "Al-Muqtadir", "Al-Muqaddim", "Al-Mu'akkhir", "Al-Awwal", "Al-Aakhir",
                        "Az-Zhaahir", "Al-Baathin", "Al-Waali", "Al-Muta'aalii", "Al-Barri", "At-Tawwaab", "Al-Muntaqim", "Al-Afuww", "Ar-Ra'uuf", "Malikum Mulk",
                        "Dzul Jalaali Wal Ikraam", "Al-Muqsith", "Al-Jamii'", "Al-Ghaniyy", "Al-Mughnii", "Al-Maani", "Ad-Dhaar", "An-Nafii'", "An-Nuur", "Al-Haadii",
                        "Al-Baadii", "Al-Baaqii", "Al-Waarits", "Ar-Rasyiid", "As-Shabuur"
                    )
                )
            }
        }
        2 -> {
            Column {
                Banner(imageRes = R.drawable.banner_doa)
                DoaHarianContent(
                    items = listOf("Doa 1", "Doa 2", "Doa 3", "Doa 4", "Doa 5", "Doa 6")
                )
            }
        }
    }
}

@Composable
fun HijaiyahContent(items: List<Pair<String, String>>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(5.dp, Color.White))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF617683),
                        Color(0xFF4F5C63)
                    )
                )
            )
            .padding(20.dp)
    ) {
        HijaiyahScrollableContent(items = items)
    }
}

@Composable
fun HijaiyahScrollableContent(items: List<Pair<String, String>>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.chunked(3)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(item.first, style = TextStyle(fontSize = 28.sp, color = Color.Black))
                        }
                        Text(item.second, style = TextStyle(fontSize = 16.sp, color = Color.White))
                    }
                }
            }
        }
    }
}

@Composable
fun AsmaulHusnaContent(items: List<String>) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.contains(searchQuery, ignoreCase = true) }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChanged = { searchQuery = it }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(BorderStroke(5.dp, Color.White))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF617683),
                            Color(0xFF4F5C63)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            AsmaulHusnaScrollableContent(items = filteredItems)
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChanged: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { Text("Search...") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(2.dp, Color.White))
    )
}

@Composable
fun AsmaulHusnaScrollableContent(items: List<String>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { item ->
                    BubbleText(item)
                }
            }
        }
    }
}

@Composable
fun BubbleText(text: String) {
    Box(
        modifier = Modifier
            .size(160.dp, 60.dp)
            .background(Color.White)
            .border(BorderStroke(2.dp, Color.Black))
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        AutoSizeText(text)
    }
}

@Composable
fun AutoSizeText(text: String, maxFontSize: Int = 20) {
    var textSize by remember { mutableStateOf(maxFontSize.sp) }
    var readyToDraw by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (readyToDraw) {
            Text(
                text = text,
                style = TextStyle(fontSize = textSize, color = Color.Black),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            Text(
                text = text,
                style = TextStyle(fontSize = textSize, color = Color.Black),
                textAlign = TextAlign.Center,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.didOverflowWidth || textLayoutResult.didOverflowHeight) {
                        textSize = textSize * 0.9f
                    } else {
                        readyToDraw = true
                    }
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun DoaHarianContent(items: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(BorderStroke(5.dp, Color.White))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF617683),
                        Color(0xFF4F5C63)
                    )
                )
            )
            .padding(20.dp)
    ) {
        DefaultScrollableContent(items = items)
    }
}

@Composable
fun DefaultScrollableContent(items: List<String>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.chunked(3)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { item ->
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(item, style = TextStyle(fontSize = 32.sp))
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)),
        containerColor = Color(0xFF617683)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
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
