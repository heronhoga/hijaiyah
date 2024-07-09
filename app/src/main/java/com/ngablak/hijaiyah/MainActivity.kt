package com.ngablak.hijaiyah

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HijaiyahTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navController)
                        }
                        composable("main") {
                            MainScreen(
                                navigateToNewActivity = { navigateToNewActivity() },
                                navigateToAboutAppActivity = { navigateToAboutAppActivity() }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun navigateToNewActivity() {
        val intent = Intent(this, ContentActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAboutAppActivity() {
        val intent = Intent(this, AboutAppActivity::class.java)
        startActivity(intent)
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        CenteredLogo(
            modifier = Modifier
                .align(Alignment.Center)
                .size(300.dp)
        )
    }

    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

@Composable
fun MainScreen(navigateToNewActivity: () -> Unit, navigateToAboutAppActivity: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
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
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(300.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(10.dp))
            Image(
                painter = painterResource(id = R.drawable.playbutton),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = navigateToNewActivity
                    ),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, end = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Image(
                painter = painterResource(id = R.drawable.qbox),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = navigateToAboutAppActivity
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun CenteredLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HijaiyahTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            CenteredLogo(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(350.dp)
            )
        }
    }
}
