package com.ngablak.hijaiyah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
                        AsmaulHusnaItem("Ar-Rahman", "الرَّحْمَنُ", "Yang Maha Pengasih"),
                        AsmaulHusnaItem("Ar-Rahim", "الرَّحِيمُ", "Yang Maha Penyayang"),
                        AsmaulHusnaItem("Al-Malik", "الْمَلِكُ", "Yang Maha Merajai"),
                        AsmaulHusnaItem("Al-Quddus", "الْقُدُّوسُ", "Yang Maha Suci"),
                        AsmaulHusnaItem("As-Salam", "السَّلاَمُ", "Yang Maha Memberi Kesejahteraan"),
                        AsmaulHusnaItem("Al-Mu'min", "الْمُؤْمِنُ", "Yang Maha Memberi Keamanan"),
                        AsmaulHusnaItem("Al-Muhaimin", "الْمُهَيْمِنُ", "Yang Maha Pemelihara"),
                        AsmaulHusnaItem("Al-Aziz", "الْعَزِيزُ", "Yang Maha Perkasa"),
                        AsmaulHusnaItem("Al-Jabbar", "الْجَبَّارُ", "Yang Memiliki Mutlak Kegagahan"),
                        AsmaulHusnaItem("Al-Mutakabbir", "الْمُتَكَبِّرُ", "Yang Maha Megah, Yang Memiliki Kebesaran"),
                        AsmaulHusnaItem("Al-Khaliq", "الْخَالِقُ", "Yang Maha Pencipta"),
                        AsmaulHusnaItem("Al-Bari'", "الْبَارِئُ", "Yang Maha Melepaskan"),
                        AsmaulHusnaItem("Al-Musawwir", "الْمُصَوِّرُ", "Yang Maha Membentuk Rupa"),
                        AsmaulHusnaItem("Al-Ghaffar", "الْغَفَّارُ", "Yang Maha Pengampun"),
                        AsmaulHusnaItem("Al-Qahhar", "الْقَهَّارُ", "Yang Maha Menundukkan"),
                        AsmaulHusnaItem("Al-Wahhab", "الْوَهَّابُ", "Yang Maha Pemberi Karunia"),
                        AsmaulHusnaItem("Ar-Razzaq", "الرَّزَّاقُ", "Yang Maha Pemberi Rezeki"),
                        AsmaulHusnaItem("Al-Fattah", "الْفَتَّاحُ", "Yang Maha Pembuka Rahmat"),
                        AsmaulHusnaItem("Al-Alim", "اَلْعَلِيْمُ", "Yang Maha Mengetahui"),
                        AsmaulHusnaItem("Al-Qabidh", "الْقَابِضُ", "Yang Maha Menyempitkan"),
                        AsmaulHusnaItem("Al-Basit", "الْبَاسِطُ", "Yang Maha Melapangkan"),
                        AsmaulHusnaItem("Al-Khafidh", "الْخَافِضُ", "Yang Maha Merendahkan"),
                        AsmaulHusnaItem("Ar-Rafi'", "الرَّافِعُ", "Yang Maha Meninggikan"),
                        AsmaulHusnaItem("Al-Mu'izz", "المعز", "Yang Maha Memuliakan"),
                        AsmaulHusnaItem("Al-Mudhill", "المذل", "Yang Maha Menghinakan"),
                        AsmaulHusnaItem("As-Sami'", "السَّمِيعُ", "Yang Maha Mendengar"),
                        AsmaulHusnaItem("Al-Basir", "الْبَصِيرُ", "Yang Maha Melihat"),
                        AsmaulHusnaItem("Al-Hakam", "الْحَكَمُ", "Yang Maha Menetapkan"),
                        AsmaulHusnaItem("Al-Adl", "الْعَدْلُ", "Yang Maha Adil"),
                        AsmaulHusnaItem("Al-Latif", "اللَّطِيفُ", "Yang Maha Lembut"),
                        AsmaulHusnaItem("Al-Khabir", "الْخَبِيرُ", "Yang Maha Mengenal"),
                        AsmaulHusnaItem("Al-Halim", "الْحَلِيمُ", "Yang Maha Penyantun"),
                        AsmaulHusnaItem("Al-Azim", "الْعَظِيمُ", "Yang Maha Agung"),
                        AsmaulHusnaItem("Al-Ghafur", "الْغَفُورُ", "Yang Maha Pengampun"),
                        AsmaulHusnaItem("Asy-Syakur", "الشَّكُورُ", "Yang Maha Pembalas Budi"),
                        AsmaulHusnaItem("Al-Aliyy", "الْعَلِيُّ", "Yang Maha Tinggi"),
                        AsmaulHusnaItem("Al-Kabir", "الْكَبِيرُ", "Yang Maha Besar"),
                        AsmaulHusnaItem("Al-Hafiz", "الْحَفِيظُ", "Yang Maha Memelihara"),
                        AsmaulHusnaItem("Al-Muqit", "المقيت", "Yang Maha Pemberi Kekuatan"),
                        AsmaulHusnaItem("Al-Hasib", "الْحسِيبُ", "Yang Maha Membuat Perhitungan"),
                        AsmaulHusnaItem("Al-Jalil", "الْجَلِيلُ", "Yang Maha Luhur"),
                        AsmaulHusnaItem("Al-Karim", "الْكَرِيمُ", "Yang Maha Mulia"),
                        AsmaulHusnaItem("Ar-Raqib", "الرَّقِيبُ", "Yang Maha Mengawasi"),
                        AsmaulHusnaItem("Al-Mujib", "الْمُجِيبُ", "Yang Maha Mengabulkan"),
                        AsmaulHusnaItem("Al-Wasi'", "الْوَاسِعُ", "Yang Maha Luas"),
                        AsmaulHusnaItem("Al-Hakim", "الْحَكِيمُ", "Yang Maha Bijaksana"),
                        AsmaulHusnaItem("Al-Wadud", "الْوَدُودُ", "Yang Maha Mengasihi"),
                        AsmaulHusnaItem("Al-Majid", "الْمَجِيدُ", "Yang Maha Mulia"),
                        AsmaulHusnaItem("Al-Ba'ith", "الْبَاعِثُ", "Yang Maha Membangkitkan"),
                        AsmaulHusnaItem("Asy-Syahid", "الشَّهِيدُ", "Yang Maha Menyaksikan"),
                        AsmaulHusnaItem("Al-Haqq", "الْحَقُ", "Yang Maha Benar"),
                        AsmaulHusnaItem("Al-Wakil", "الْوَكِيلُ", "Yang Maha Memelihara"),
                        AsmaulHusnaItem("Al-Qawiyy", "الْقَوِيُ", "Yang Maha Kuat"),
                        AsmaulHusnaItem("Al-Matin", "الْمَتِينُ", "Yang Maha Kokoh"),
                        AsmaulHusnaItem("Al-Waliyy", "الْوَلِيُّ", "Yang Maha Melindungi"),
                        AsmaulHusnaItem("Al-Hamid", "الْحَمِيدُ", "Yang Maha Terpuji"),
                        AsmaulHusnaItem("Al-Muhsi", "الْمُحْصِي", "Yang Maha Mengalkulasi"),
                        AsmaulHusnaItem("Al-Mubdi'", "الْمُبْدِئُ", "Yang Maha Memulai"),
                        AsmaulHusnaItem("Al-Mu'id", "المعيد", "Yang Maha Mengembalikan Kehidupan"),
                        AsmaulHusnaItem("Al-Muhyi", "المحيى", "Yang Maha Menghidupkan"),
                        AsmaulHusnaItem("Al-Mumit", "المميت", "Yang Maha Mematikan"),
                        AsmaulHusnaItem("Al-Hayy", "الْحَيُّ", "Yang Maha Hidup"),
                        AsmaulHusnaItem("Al-Qayyum", "الْقَيُّومُ", "Yang Maha Mandiri"),
                        AsmaulHusnaItem("Al-Wajid", "الْوَاجِدُ", "Yang Maha Menemukan"),
                        AsmaulHusnaItem("Al-Majid", "الْمَاجِدُ", "Yang Maha Mulia"),
                        AsmaulHusnaItem("Al-Wahid", "الْواحِدُ", "Yang Maha Tunggal"),
                        AsmaulHusnaItem("Al-Ahad", "اَلاَحَدُ", "Yang Maha Esa"),
                        AsmaulHusnaItem("As-Samad", "الصَّمَدُ", "Yang Maha Dibutuhkan, Tempat Meminta"),
                        AsmaulHusnaItem("Al-Qadir", "الْقَادِرُ", "Yang Maha Menentukan, Maha Menyeimbangkan"),
                        AsmaulHusnaItem("Al-Muqtadir", "المقتدر", "Yang Maha Berkuasa"),
                        AsmaulHusnaItem("Al-Muqaddim", "المقدم", "Yang Maha Mendahulukan"),
                        AsmaulHusnaItem("Al-Mu'akhkhir", "المؤخر", "Yang Maha Mengakhirkan"),
                        AsmaulHusnaItem("Al-Awwal", "الأوّل", "Yang Maha Awal"),
                        AsmaulHusnaItem("Al-Akhir", "الآخِرُ", "Yang Maha Akhir"),
                        AsmaulHusnaItem("Az-Zahir", "الظَّاهِرُ", "Yang Maha Nyata"),
                        AsmaulHusnaItem("Al-Batin", "الْبَاطِنُ", "Yang Maha Ghaib"),
                        AsmaulHusnaItem("Al-Wali", "الْوَالِي", "Yang Maha Memerintah"),
                        AsmaulHusnaItem("Al-Muta'ali", "المتعالي", "Yang Maha Tinggi"),
                        AsmaulHusnaItem("Al-Barr", "البر", "Yang Maha Penderma"),
                        AsmaulHusnaItem("At-Tawwab", "التواب", "Yang Maha Penerima Tobat"),
                        AsmaulHusnaItem("Al-Muntaqim", "المنتقم", "Yang Maha Pemberi Balasan"),
                        AsmaulHusnaItem("Al-Afuww", "العفو", "Yang Maha Pemaaf"),
                        AsmaulHusnaItem("Ar-Ra'uf", "الرؤوف", "Yang Maha Pengasuh"),
                        AsmaulHusnaItem("Malikul-Mulk", "مالك الملك", "Yang Maha Penguasa Kerajaan"),
                        AsmaulHusnaItem("Dzul-Jalali Wal-Ikram", "ذوالجلال wal ikram", "Yang Maha Pemilik Kebesaran dan Kemuliaan"),
                        AsmaulHusnaItem("Al-Muqsit", "المقسط", "Yang Maha Pemberi Keadilan"),
                        AsmaulHusnaItem("Al-Jami'", "الجامع", "Yang Maha Mengumpulkan"),
                        AsmaulHusnaItem("Al-Ghaniyy", "الغنى", "Yang Maha Kaya"),
                        AsmaulHusnaItem("Al-Mughni", "المغنى", "Yang Maha Pemberi Kekayaan"),
                        AsmaulHusnaItem("Al-Mani'", "المانع", "Yang Maha Mencegah"),
                        AsmaulHusnaItem("Ad-Darr", "الضار", "Yang Maha Penimpa Kemudharatan"),
                        AsmaulHusnaItem("An-Nafi'", "النافع", "Yang Maha Memberi Manfaat"),
                        AsmaulHusnaItem("An-Nur", "النور", "Yang Maha Bercahaya"),
                        AsmaulHusnaItem("Al-Hadi", "الْهَادِي", "Yang Maha Pemberi Petunjuk"),
                        AsmaulHusnaItem("Al-Badi'", "الْبَدِيعُ", "Yang Maha Pencipta Yang Tiada Bandingannya"),
                        AsmaulHusnaItem("Al-Baqi", "الباقى", "Yang Maha Kekal"),
                        AsmaulHusnaItem("Al-Warith", "الوارث", "Yang Maha Pewaris"),
                        AsmaulHusnaItem("Ar-Rashid", "الرشيد", "Yang Maha Pandai"),
                        AsmaulHusnaItem("As-Sabur", "الصبور", "Yang Maha Sabar")
                    )
                )
            }
        }
        2 -> {
            Column {
                Banner(imageRes = R.drawable.banner_doa)
                DoaHarianContent(
                    items = listOf("Doa Makan", "Doa Naik Kendaraan", "Doa Sebelum Tidur", "Doa Bangun Tidur", "Doa Masuk Kamar Mandi", "Doa Keluar Kamar Mandi")
                )
            }
        }
    }
}

data class AsmaulHusnaItem(val latin: String, val arabic: String, val meaning: String)

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
fun AsmaulHusnaContent(items: List<AsmaulHusnaItem>) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { it.latin.contains(searchQuery, ignoreCase = true) }

    var selectedAsmaulHusna by remember { mutableStateOf<AsmaulHusnaItem?>(null) }

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
            AsmaulHusnaScrollableContent(items = filteredItems, onItemClick = { item ->
                selectedAsmaulHusna = item
            })
        }

        selectedAsmaulHusna?.let { item ->
            ShowAsmaulHusnaDialog(item = item, onDismiss = { selectedAsmaulHusna = null })
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
fun AsmaulHusnaScrollableContent(items: List<AsmaulHusnaItem>, onItemClick: (AsmaulHusnaItem) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { item ->
                    BubbleText(item.latin, onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
fun BubbleText(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(160.dp, 60.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(Color.White)
            .padding(8.dp)

            .clickable { onClick() },
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
                .padding(20.dp)

        ) {
            DoaHarianScrollableContent(items = filteredItems)
        }
    }
}

@Composable
fun DoaHarianScrollableContent(items: List<String>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            DoaHarianItem(item = item)
        }
    }
}

@Composable
fun DoaHarianItem(item: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)) // Apply the clip modifier before the background
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(item, style = TextStyle(fontSize = 20.sp, color = Color.Black, fontStyle = FontStyle.Italic))
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

@Composable
fun ShowAsmaulHusnaDialog(item: AsmaulHusnaItem, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = item.latin, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) },
        text = {
            Column {
                Text(text = item.arabic, style = TextStyle(fontSize = 30.sp, color = Color.White))
                Spacer(modifier = Modifier.size(10.dp))
                Text(text = item.meaning, style = TextStyle(fontSize = 18.sp, color = Color.Gray))
            }

        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
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
