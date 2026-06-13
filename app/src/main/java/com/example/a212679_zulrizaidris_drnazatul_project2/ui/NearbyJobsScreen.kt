package com.example.a212679_zulrizaidris_drnazatul_project2.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyJobsScreen(viewModel: CareerPulseViewModel) {
    val context = LocalContext.current
    val apiJobs by viewModel.apiJobs.collectAsState()
    val isLoading by viewModel.isLoadingJobs.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()

    var permissionGranted by remember { mutableStateOf(false) }
    var locationMessage by remember { mutableStateOf("Tap the button below to find nearby jobs") }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted) {
            locationMessage = "Getting your location..."
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        locationMessage = "📍 Lat: %.4f, Lng: %.4f".format(location.latitude, location.longitude)
                        viewModel.updateLocation(location.latitude, location.longitude)
                    } else {
                        locationMessage = "Could not get location. Try again."
                    }
                }
                .addOnFailureListener {
                    locationMessage = "Location error: ${it.message}"
                }
        } else {
            locationMessage = "Location permission denied."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Jobs", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh location")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Location Card (GPS Sensor output)
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.width(8.dp))
                    Text(text = locationMessage, style = MaterialTheme.typography.bodyMedium)
                }
            }

            // Get Location Button (shown before permission is granted)
            if (!permissionGranted && currentLocation == null) {
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Find Jobs Near Me")
                }
            }

            // Jobs from API
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("Loading jobs...")
                    }
                }
            } else if (apiJobs.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No jobs found. Try refreshing.")
                }
            } else {
                Text(
                    text = "Jobs found: ${apiJobs.size}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(apiJobs) { job ->
                        ApiJobCard(
                            title = job.title,
                            company = job.companyName,
                            location = job.location.ifBlank { "Remote" },
                            jobType = job.jobType
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApiJobCard(title: String, company: String, location: String, jobType: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text(text = company, color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(onClick = {}, label = { Text(location) })
                if (jobType.isNotBlank()) {
                    SuggestionChip(onClick = {}, label = { Text(jobType) })
                }
            }
        }
    }
}