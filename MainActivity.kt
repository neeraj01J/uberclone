package com.example.uber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uber.model.UberScreen
import com.example.uber.ui.auth.LoginScreen
import com.example.uber.ui.auth.RegisterScreen
import com.example.uber.ui.home.HomeScreen
import com.example.uber.ui.ride.BookingConfirmedScreen
import com.example.uber.ui.ride.PaymentScreen
import com.example.uber.ui.ride.RideSelectionScreen
import com.example.uber.ui.search.SearchScreen
import com.example.uber.ui.settings.SettingsScreen
import com.example.uber.ui.theme.UberTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UberTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UberApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun UberApp(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val pickupLocation by viewModel.pickupLocation.collectAsState()
    val destination by viewModel.destination.collectAsState()
    val selectedRideType by viewModel.selectedRideType.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) UberScreen.Home.route else UberScreen.Login.route
    ) {
        composable(UberScreen.Login.route) {
            LoginScreen(
                onLoginSuccess = { email, pass ->
                    viewModel.login(email, pass)
                    navController.navigate(UberScreen.Home.route) {
                        popUpTo(UberScreen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(UberScreen.Register.route)
                }
            )
        }
        composable(UberScreen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { email, pass ->
                    viewModel.register(email, pass)
                    navController.navigate(UberScreen.Home.route) {
                        popUpTo(UberScreen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(UberScreen.Home.route) {
            HomeScreen(
                onSearchClick = {
                    navController.navigate(UberScreen.Search.route)
                },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(UberScreen.Login.route) {
                        popUpTo(UberScreen.Home.route) { inclusive = true }
                    }
                },
                onSettingsClick = {
                    navController.navigate(UberScreen.Settings.route)
                }
            )
        }
        composable(UberScreen.Search.route) {
            SearchScreen(
                onLocationsConfirmed = { pickup, dest ->
                    viewModel.setPickupLocation(pickup)
                    viewModel.setDestination(dest)
                    navController.navigate(UberScreen.RideSelection.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(UberScreen.RideSelection.route) {
            RideSelectionScreen(
                pickup = pickupLocation,
                destination = destination,
                rideTypes = viewModel.rideTypes,
                selectedRide = selectedRideType,
                onRideSelected = { viewModel.selectRideType(it) },
                onBack = {
                    navController.popBackStack()
                },
                onConfirm = {
                    navController.navigate(UberScreen.Payment.route)
                }
            )
        }
        composable(UberScreen.Payment.route) {
            PaymentScreen(
                selectedRide = selectedRideType,
                onPaymentConfirmed = {
                    navController.navigate(UberScreen.BookingConfirmed.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(UberScreen.BookingConfirmed.route) {
            BookingConfirmedScreen(
                selectedRide = selectedRideType,
                onDone = {
                    navController.popBackStack(UberScreen.Home.route, false)
                }
            )
        }
        composable(UberScreen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogoutConfirmed = {
                    viewModel.logout()
                    navController.navigate(UberScreen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
