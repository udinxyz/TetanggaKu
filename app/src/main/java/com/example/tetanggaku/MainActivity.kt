package com.example.tetanggaku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tetanggaku.presentation.screens.AuthScreen
import com.example.tetanggaku.presentation.screens.CreateJobScreen
import com.example.tetanggaku.presentation.screens.HomeScreen
import com.example.tetanggaku.presentation.screens.JobDetailScreen
import com.example.tetanggaku.presentation.screens.ProfileScreen
import com.example.tetanggaku.presentation.screens.RegisterScreen
import com.example.tetanggaku.presentation.screens.SplashScreen
import com.example.tetanggaku.ui.theme.TetanggakuTheme

// -------------------------
// Route untuk setiap Screen
// -------------------------
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object JobDetail : Screen("job_detail")
    object CreateJob : Screen("create_job")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TetanggakuTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TetanggakuApp()
                }
            }
        }
    }
}

@Composable
fun TetanggakuApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // =======================
        // Splash
        // =======================
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // =======================
        // Login
        // =======================
        composable(route = Screen.Login.route) {
            AuthScreen(
                onLoggedIn = {
                    navController.navigate(Screen.Home.route) {
                        // hapus layar login dari back stack
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // =======================
        // Register
        // =======================
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    // setelah daftar sukses, balik ke login
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // =======================
        // Home (tab Beranda / Job Saya / Profil)
        // =======================
        composable(route = Screen.Home.route) {
            HomeScreen(
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onJobDetailClick = {
                    navController.navigate(Screen.JobDetail.route)
                },
                onCreateJobClick = {
                    navController.navigate(Screen.CreateJob.route)
                }
            )
        }

        // =======================
        // Buat Permintaan Job
        // =======================
        composable(route = Screen.CreateJob.route) {
            CreateJobScreen(
                onBack = { navController.popBackStack() },
                onJobCreated = {
                    // setelah sukses buat job, balik ke beranda
                    navController.popBackStack()
                }
            )
        }

        // =======================
        // Profile
        // =======================
        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onHomeClick = {
                    // balik ke Home (yang ada di bawahnya di back stack)
                    navController.popBackStack()
                },
                onJobClick = {
                    // sementara juga balik ke Home
                    navController.popBackStack()
                },
                onLogout = {
                    // logout → kembali ke login, hapus sampai Home
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // =======================
        // Detail Job
        // =======================
        composable(route = Screen.JobDetail.route) {
            JobDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onTakeJob = {
                    // TODO: update status job / XP
                    navController.popBackStack()
                }
            )
        }
    }
}
