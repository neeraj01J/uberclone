package com.example.uber

import androidx.lifecycle.ViewModel
import com.example.uber.model.LocationDetails
import com.example.uber.model.RideType
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _pickupLocation = MutableStateFlow<LocationDetails?>(null)
    val pickupLocation: StateFlow<LocationDetails?> = _pickupLocation.asStateFlow()

    private val _destination = MutableStateFlow<LocationDetails?>(null)
    val destination: StateFlow<LocationDetails?> = _destination.asStateFlow()

    private val _selectedRideType = MutableStateFlow<RideType?>(null)
    val selectedRideType: StateFlow<RideType?> = _selectedRideType.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(email: String, pass: String) {
        // Simple dummy login
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            _isLoggedIn.value = true
        }
    }

    fun register(email: String, pass: String) {
        // Simple dummy register
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            _isLoggedIn.value = true
        }
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun setPickupLocation(location: LocationDetails) {
        _pickupLocation.value = location
    }

    fun setDestination(location: LocationDetails) {
        _destination.value = location
    }

    fun selectRideType(rideType: RideType) {
        _selectedRideType.value = rideType
    }

    val rideTypes = listOf(
        RideType("1", "Uber Go", 250.50, android.R.drawable.ic_menu_directions, "Affordable, everyday rides", 4),
        RideType("2", "Premier", 350.20, android.R.drawable.ic_menu_directions, "Comfortable sedans, top-rated drivers", 4),
        RideType("3", "Uber XL", 550.00, android.R.drawable.ic_menu_directions, "Extra space for groups up to 6", 6),
        RideType("4", "Uber Auto", 120.00, android.R.drawable.ic_menu_directions, "Doorstep pickup, no haggling", 3)
    )
}
