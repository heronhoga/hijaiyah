package com.ngablak.hijaiyah

import android.content.Context
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme
import android.media.MediaPlayer
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.runtime.Composable


class ContentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HijaiyahTheme {
                val navController = rememberNavController()
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
                        NavHost(navController = navController, startDestination = "main") {
                            composable("main") {
                                MainScreen(navController, selectedItem.value, ::navigateToCaptureActivity)
                            }
                            composable("doaDetail/{doaTitle}") { backStackEntry ->
                                val doaTitle = backStackEntry.arguments?.getString("doaTitle")
                                val doaItem = doaList.find { it.title == doaTitle }
                                doaItem?.let { DoaDetailScreen(navController, it) }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun navigateToCaptureActivity() {
        val intent = Intent(this, CaptureActivity::class.java)
        startActivity(intent)
    }
}



@Composable
fun MainScreen(navController: NavHostController, selectedItem: Int, onCameraButtonClick: () -> Unit) {
    Column {
        Content(navController = navController, selectedItem = selectedItem, onCameraButtonClick)
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
fun CameraButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.camera_icon),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clickable(
                    onClick = onClick
                ),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun Content(navController: NavHostController, selectedItem: Int, onCameraButtonClick: () -> Unit) {
    when (selectedItem) {
        0 -> {
            Column {
                Banner(imageRes = R.drawable.banner_hijaiyah)
                CameraButton(onClick = onCameraButtonClick)
                HijaiyahContent(
                    items = listOf(
                        "أ" to "Alif", "ب" to "Ba", "ت" to "Ta", "ث" to "Tsa", "ج" to "Jim", "ح" to "Ha", "خ" to "Kha", "د" to "Dal", "ذ" to "Dhal",
                        "ر" to "Ra", "ز" to "Zay", "س" to "Sin", "ش" to "Shin", "ص" to "Sad", "ض" to "Dad", "ط" to "To", "ظ" to "Zha", "ع" to "Ain",
                        "غ" to "Ghain", "ف" to "Fa", "ق" to "Qaf", "ك" to "Kaf", "ل" to "Lam", "م" to "Mim", "ن" to "Nun", "ه" to "Ha", "و" to "Waw", "ي" to "Ya"
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
                DoaHarianContent(navController = navController)
            }
        }
    }
}

data class AsmaulHusnaItem(val latin: String, val arabic: String, val meaning: String)


@Composable
fun HijaiyahContent(items: List<Pair<String, String>>) {
    val context = LocalContext.current
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
        HijaiyahScrollableContent(context, items)
    }
}

@Composable
fun HijaiyahScrollableContent(context: Context, items: List<Pair<String, String>>) {
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
                                .padding(8.dp)
                                .clickable {
                                    val mediaPlayer = MediaPlayer.create(context, getResId(context, item.second.lowercase()))
                                    mediaPlayer?.start()
                                },
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

fun getResId(context: Context, name: String): Int {
    return context.resources.getIdentifier(name, "raw", context.packageName)
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

data class DoaItem(val title: String, val arabic: String, val latin: String, val meaning: String)

val doaList = listOf(
    DoaItem("Doa Sebelum Tidur", "بِاسْمِكَا للَّهُمَ أَحْيَا وَ ِاسْمِكَا أَمُوتُ", "Bismika allahumma ahya wa bismika amut", "Dengan nama-Mu Ya Allah aku hidup dan dengan nama-Mu aku mati"),
    DoaItem("Doa Bangun Tidur", "الْحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ", "Alhamdulillahi al-ladhi ahyana ba'da ma amatana wa ilaihi-nnushur", "Segala puji bagi Allah yang telah mengembalikan kehidupan, mematikan kami, dan hanya kepada-Nya kami kembali"),
    DoaItem("Doa sebelum makan", "اَللّٰهُمَّ بَارِكْ لَنَا فِيْمَا رَزَقْتَنَا وَقِنَا عَذَابَ النَّارِ", "Allahumma baarik lanaa fiimaa rozaqtanaa wa qinaa 'adzaa bannaar", "Ya Allah, berkahilah kami dalam rezeki yang telah Engkau berikan kepada kami dan peliharalah kami dari siksa api neraka"),
    DoaItem("Doa sesudah makan", "اَلْحَمْدُ ِللهِ الَّذِىْ اَطْعَمَنَا وَسَقَانَا وَجَعَلَنَا مُسْلِمِيْنَ", "Alhamdulillahilladzi ath-amanaa wa saqoonaa wa ja'alanaa minal muslimiin", "Segala puji bagi Allah yang telah memberi kami makan dan minum serta menjadikan kami termasuk dari kaum Muslimin"),
    DoaItem("Doa keluar rumah", "بِسْمِ اللهِ تَوَكَّلْتُ عَلَى اللهِ، لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللهِ", "Bismillahhi tawakkaltu 'alallah, laa haula wa laa quwwata illa billaah", "Dengan menyebut nama Allah, aku bertawakal kepada Allah. Tiada daya dan kekuatan kecuali dengan Allah"),
    DoaItem("Doa masuk rumah", "بِسْمِ اللهِ وَلَجْنَا وَبِسْمِ اللهِ خَرَجْنَا ، وَعَلَى اللهِ رَبَّنَا تَوَكَّلْنَا", "Bismillahi walajnaa wa bismillahi kharajnaa wa-alallaahi rabbina tawak-kalnaa", "Dengan nama Allah, kami masuk (ke rumah), dengan nama Allah, kami keluar (darinya) dan kepada Allah, kami berserah diri"),
    DoaItem("Doa sebelum belajar", "رَضِتُ بِااللهِ رَبَا وَبِالْاِسْلاَمِ دِيْنَا وَبِمُحَمَّدٍ نَبِيَا وَرَسُوْلاَ رَبِّ زِدْ نِيْ عِلْمًـاوَرْزُقْنِـيْ فَهْمًـا", "Rodlittu billahiroba, Wabil islaamidiinaa, Wabimuhammadin nabiyyaa warasuula, Robbi zidnii ilmaan warzuqnii fahmaan", "Kami ridho Allah Swt sebagai Tuhanku, Islam sebagai agamaku, dan Nabi Muhammad sebagai Nabi dan Rasul, Ya Allah, tambahkanlah kepadaku ilmu dan berikanlah aku pengertian yang baik"),
    DoaItem("Doa setelah selesai belajar", "اَللّٰهُمَّ اِنِّى اِسْتَوْدِعُكَ مَاعَلَّمْتَنِيْهِ فَارْدُدْهُ اِلَىَّ عِنْدَ حَاجَتِىْ وَلاَ تَنْسَنِيْهِ يَارَبَّ الْعَالَمِيْنَ", "Allaahumma innii astaudi'uka maa 'allamtaniihi fardud-hu ilayya 'inda haajatii wa laa tansaniihi yaa robbal 'alamiin", "Ya Allah, sesungguhnya aku menitipkan kepada Engkau ilmu-ilmu yang telah Engkau ajarkan kepadaku, dan kembalikanlah kepadaku sewaktu aku butuh kembali dan janganlah Engkau lupakan aku kepada ilmu itu wahai Tuhan seru sekalian alam"),
    DoaItem("Doa sebelum mandi", "اَللّٰهُمَّ اغْفِرْلِى ذَنْبِى وَوَسِّعْ لِى فِىْ دَارِىْ وَبَارِكْ لِىْ فِىْ رِزْقِىْ", "Allahummaghfirlii dzambii wa wassi'lii fii daarii wa baarik lii fii rizqii", "Ya Allah ampunilah dosa kesalahanku dan berilah keluasaan di rumahku serta berkahilah pada rezekiku"),
    DoaItem("Doa memakai pakaian", "بِسْمِ اللهِ اَللّٰهُمَّ اِنِّى اَسْأَلُكَ مِنْ خَيْرِهِ وَخَيْرِ مَاهُوَ لَهُ وَاَعُوْذُبِكَ مِنْ شَرِّهِ وَشَرِّمَا هُوَلَهُ", "Bismillaahi, Alloohumma innii as-aluka min khoirihi wa khoiri maa huwa lahuu wa'a'uu dzubika min syarrihi wa syarri maa huwa lahuu", "Dengan nama-Mu yaa Allah akku minta kepada Engkau kebaikan pakaian ini dan kebaikan apa yang ada padanya, dan aku berlindung kepada Engkau dari kejahatan pakaian ini dan kejahatan yang ada padanya"),
    DoaItem("Doa memakai pakaian baru", "اَلْحَمْدُ لِلّٰهِ الَّذِىْ كَسَانِىْ هَذَا وَرَزَقَنِيْهِ مِنْ غَيْرِ حَوْلٍ مِنِّىْ وَلاَقُوَّةٍ", "Alhamdu lillaahil ladzii kasaanii haadzaa wa rozaqoniihi min ghoiri hawlim minni wa laa quwwatin", "Segala puji bagi Allah yang memberi aku pakaian ini dan memberi rizeki dengan tiada upaya dan kekuatan dariku"),
    DoaItem("Doa menyambut pagi hari", "اَللّٰهُمَّ بِكَ اَصْبَحْنَا وَبِكَ اَمْسَيْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوْتُ وَاِلَيْكَ النُّشُوْرُ", "Alloohumma bika ashbahnaa wa bika amsainaa wa bika nahyaa wa bika namuutu wa ilaikan nusyuuru", "Ya Allah, karena Engkau kami mengalami waktu pagi dan waktu petang, dan karena Engkau kami hidup dan mati dan kepada-Mu juga kami akan kembali"),
    DoaItem("Doa menyambut siang hari", "اَللّٰهُمَّ بِكَ اَمْسَيْنَا وَبِكَ اَصْبَحْنَا وَبِكَ نَحْيَا وَبِكَ نَمُوْتُ وَاِلَيْكَ الْمَصِيْر", "Alloohumma bika amsainaa wa bika ashbahnaa wa bika nahyaa wa bika namuutu wa ilaikal mashiir", "Ya Allah, karena Engkau kami mengalami waktu petang dan waktu pagi, karena Engkau kami hidup dan mati dan kepada-Mu juga kami akan kembali"),
    DoaItem("Doa masuk WC dan toilet", " اَللّٰهُمَّ اِنّىْ اَعُوْذُبِكَ مِنَ الْخُبُثِ وَالْخَبَآئِثِ", "Allahumma Innii a'uudzubika minal khubutsi wal khoaaitsi", "Ya Allah, aku berlindung pada-Mu dari godaan setan laki-laki dan setan perempuan"),
    DoaItem("Doa keluar WC dan toilet", "غُفْرَانَكَ الْحَمْدُ ِللهِ الَّذِىْ اَذْهَبَ عَنّى اْلاَذَى وَعَافَانِى", "Ghufraanakal hamdu lillaahil ladzii adzhaba 'annil adzaa wa 'aafaanii", "Dengan mengharap ampunanMu, segala puji milik Allah yang telah menghilangkan kotoran dari badanku dan yang telah menyejahterakan"),
    DoaItem("Doa sebelum wudhu", "نَوَيْتُ الْوُضُوْءَ لِرَفْعِ الْحَدَثِ اْلاَصْغَرِ فَرْضًا ِللهِ تَعَالَى", "Nawaitul wudhuu-a liraf'll hadatsil ashghari fardhal lilaahi ta'aalaa", "Saya niat berwudhu untuk menghilangkan hadast kecil fardu karena Allah"),
    DoaItem("Doa setelah wudhu", "أَشْهَدُ أَنْ لآّاِلَهَ إِلاَّاللهُ وَحْدَهُ لاَشَرِيْكَ لَهُ وَأَشْهَدُ أَنَّ مُحَمَّدًاعَبْدُهُ وَرَسُوْلُهُ. اللّهُمَّ اجْعَلْنِىْ مِنَ التَّوَّابِيْنَ وَاجْعَلْنِىْ مِنَ الْمُتَطَهِّرِيْنَ", "Asyhadu allâ ilâha illallâhu wahdahû lâ syarîka lahu wa asyhadu anna muhammadan 'abduhû wa rasûluhû, allâhummaj'alnî minat tawwâbîna waj'alnii minal mutathahhirîna", "Aku bersaksi bahwa tidak ada Tuhan selain Allah Yang Maha Esa, tidak ada sekutu bagi-Nya, dan aku bersaksi bahwa Nabi Muhammad adalah hamba dan utusan Allah. Ya Allah, jadikanlah aku termasuk dalam golongan orang-orang yang bertobat dan jadikanlah aku termasuk dalam golongan orang-orang yang bersuci (shalih)"),
    DoaItem("Doa setelah mendengarkan adzan", "اللَّهُمَّ رَبَّ هَذِهِ الدَّعْوَةِ التَّامَّةِ وَالصَّلَاةِ الْقَائِمَةِ آتِ مُحَمَّدًا الْوَسِيلَةَ وَالْفَضِيلَةَ وَابْعَثْهُ مَقَامًا مَحْمُودًا الَّذِي وَعَدْتَهُ اِنَكَ لاَ تُخْلِفُ اْلمِيْعَاد", "Allahumma rabba haadzihid da'watit taammah. Wash shalaatil qaa-imah. Aati muhammadal wasiilata wal fadhiilah, wab'atshu maqoomam mahmuudal ladzii wa'adtahu innaka la tukhliful mi'ad", "Ya Allah, Tuhan yang memiliki panggilan ini, yang sempurna dan memiliki salat yang didirikan. Berilah Nabi Muhammad wasilah dan keutamaan, serta kemuliaan dan derajat yang tinggi, dan angkatlah ia ke tempat yang terpuji sebagaimana yang Engkau telah janjikan"),
    DoaItem("Doa ketika bercermin", "اَلْحَمْدُ ِللهِ كَمَا حَسَّنْتَ خَلْقِىْ فَحَسِّـنْ خُلُقِىْ", "Alhamdulillaahi kamaa hassanta kholqii fahassin khuluqii", "Segala puji bagi Allah, baguskanlah budi pekertiku sebagaimana Engkau telah membaguskan rupa wajahku"),
    DoaItem("Doa untuk kedua orang tua", "رَّبِّ اغْفِرْلِي وَلِوَالِدَيَّ وَارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيراً", "Rabbighfirrlii waliwaalidayya warhamhumaa kamaa rabbayaanii shaghiira", "Ya Tuhanku, ampunilah dosaku dan dosa ayah serta ibuku, dan kasihilah mereka sebagaimana kasih mereka kepadaku sewaktu aku masih kecil"),
    DoaItem("Doa selamat dunia akhirat", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ", "Rabbanaa aatinaa fiddunnyaa hasanah, wa fil aakhirati hasanah, waqinaa 'adzaa ban naar", "Ya Tuhan kami, berilah kami kebaikan hidup di dunia dan kebaikan hidup di akhirat, dan jagalah kami dari siksa api neraka"),
    DoaItem("Doa naik kendaraan ", "سُبْحَانَ الَّذِىْ سَخَّرَلَنَا هَذَا وَمَاكُنَّالَهُ مُقْرِنِيْنَ وَاِنَّآ اِلَى رَبِّنَا لَمُنْقَلِبُوْنَ", "Subhaanalladzii sakkhara lanaa hadza wama kunna lahu muqriniin wa-inna ilaa rabbina lamunqalibuun", "Maha suci Allah yang telah menundukkan untuk kami (kendaraan) ini. padahal sebelumnya kami tidak mampu untuk menguasainya, dan hanya kepada-Mu lah kami akan kembali"),
    DoaItem("Doa saat turun hujan", "اللَّهُمَّصَيِّباًنَافِعاً", "Allahumma shoyyiban nafi'an", "Ya Allah, turunkan lah pada kami hujan yang bermanfaat"),
    DoaItem("Doa saat sakit", "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَأْسَ اشْفِ أَنْتَ الشَّافِي لَا شَافِيَ إلَّا أَنْتَ شِفَاءً لَا يُغَادِرُ سَقْمًا", "Allahumma rabban nasi, adzhibil ba'sa. Isyi. Antas syafi. La syafiya illa anta syifa'an la yudhadiru saqaman", "Tuhanku, Tuhan manusia, hilangkanlah penyakit. Berikanlah kesembuhan karena Kau adalah penyembuh. Tiada yang dapat menyembuhkan penyakit kecuali Kau dengan kesembuhan yang tidak menyisakan rasa nyeri")
)

@Composable
fun DoaHarianContent(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = doaList.filter { it.title.contains(searchQuery, ignoreCase = true) }

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
            DoaHarianScrollableContent(navController = navController, items = filteredItems)
        }
    }
}

@Composable
fun DoaHarianScrollableContent(navController: NavHostController, items: List<DoaItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            DoaHarianItem(navController = navController, item = item)
        }
    }
}

@Composable
fun DoaHarianItem(navController: NavHostController, item: DoaItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .clickable { navController.navigate("doaDetail/${item.title}") },
        contentAlignment = Alignment.Center
    ) {
        Text(item.title, style = TextStyle(fontSize = 20.sp, color = Color.Black, fontStyle = FontStyle.Italic))
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
        val navController = rememberNavController()
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
                    Content(navController = navController, selectedItem = selectedItem.value, onCameraButtonClick = {})
                }
            }
        }
    }
}


@Composable
fun DoaDetailScreen(navController: NavHostController, doaItem: DoaItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp, 60.dp)
            .background(color = Color(0xFF617683))
            .border(8.dp, Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.backbtn),
            contentDescription = "Back",
            modifier = Modifier
                .size(150.dp)
                .clickable { navController.popBackStack() }
        )
        HorizontalDivider(color = Color.White,
            thickness = 5.dp)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    doaItem.title,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    doaItem.arabic,
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = Color.White,
                        textAlign = TextAlign.Right
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    doaItem.latin,
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = Color.White,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    doaItem.meaning,
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}



