package com.example.a212679_zulrizaidris_drnazatul_project2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.Group

import com.example.a212679_zulrizaidris_drnazatul_project2.ui.ApplicationFormScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.CareerPulseViewModel
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.DateSelectionScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.JobSelectionScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.SummaryScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.MyActivityScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.ProfileScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.data.DataSource
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.AppViewModelProvider
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.CommunityBoardScreen
import com.example.a212679_zulrizaidris_drnazatul_project2.ui.NearbyJobsScreen

// 1. Enum containing all screens + Start
enum class CareerPulseScreen {
    Start, Form, Date, Summary, Activity, Profile, CommunityBoard, NearbyJobs
}

// 2. Class to handle the Bottom Navigation tabs
sealed class BottomNavItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : BottomNavItem(CareerPulseScreen.Start.name, "Home", Icons.Default.Home)
    object Activity : BottomNavItem(CareerPulseScreen.Activity.name, "My Activity", Icons.Default.List)
    object Profile : BottomNavItem(CareerPulseScreen.Profile.name, "Profile", Icons.Default.Person)
    object Community : BottomNavItem(CareerPulseScreen.CommunityBoard.name, "Community", Icons.Default.Group)
    object Nearby : BottomNavItem(CareerPulseScreen.NearbyJobs.name, "Nearby", Icons.Default.LocationOn)
}

// 3. The Bottom Navigation UI Component
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(BottomNavItem.Home, BottomNavItem.Activity, BottomNavItem.Community, BottomNavItem.Nearby, BottomNavItem.Profile)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

// 4. Main App Composable
@Composable
fun CareerPulseApp(
    viewModel: CareerPulseViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CareerPulseScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Screen 1: Job List (Home Tab)
            composable(route = CareerPulseScreen.Start.name) {
                JobSelectionScreen(
                    onJobSelected = { title ->
                        viewModel.setJob(title)
                        // This goes straight to the form when "Apply Now" is clicked on the expanded card
                        navController.navigate(CareerPulseScreen.Form.name)
                    },
                    onNavigateToForm = {
                        navController.navigate(route = CareerPulseScreen.Form.name)
                    },
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(route = CareerPulseScreen.Form.name) {
                ApplicationFormScreen(
                    selectedJob = uiState.selectedJobTitle,
                    onCancelButtonClicked = { cancelAndNavigateToStart(viewModel, navController) },
                    onSubmitButtonClicked = { name, email, phone ->
                        viewModel.setApplicantDetails(name, email, phone)
                        navController.navigate(CareerPulseScreen.Date.name)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            composable(route = CareerPulseScreen.Date.name) {
                DateSelectionScreen(
                    onNextButtonClicked = {
                        navController.navigate(CareerPulseScreen.Summary.name)
                    },
                    onCancelButtonClicked = {
                        navController.navigate(CareerPulseScreen.Start.name)
                    },
                    // ✅ This saves the picked date into the ViewModel
                    onDateSelected = { date ->
                        viewModel.setDate(date)
                    }
                )
            }

            composable(route = CareerPulseScreen.Summary.name) {
                SummaryScreen(
                    applicationUiState = uiState,
                    onCancelButtonClicked = { cancelAndNavigateToStart(viewModel, navController) },
                    onSubmitButtonClicked = {
                        // 1. Save the data to the ViewModel's list
                        viewModel.submitApplication()
                        // 2. Clear the form and go back to home
                        cancelAndNavigateToStart(viewModel, navController)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // New Activity Screen
            composable(route = CareerPulseScreen.Activity.name) {
                MyActivityScreen(viewModel = viewModel)
            }

            // New Profile Screen
            composable(route = CareerPulseScreen.Profile.name) {
                ProfileScreen(viewModel = viewModel)
            }

            composable(route = CareerPulseScreen.CommunityBoard.name) {
                CommunityBoardScreen(viewModel = viewModel)
            }
            composable(route = CareerPulseScreen.NearbyJobs.name) {
                NearbyJobsScreen(viewModel = viewModel)
            }
        }
    }
}

private fun cancelAndNavigateToStart(
    viewModel: CareerPulseViewModel,
    navController: NavHostController
) {
    viewModel.resetOrder()
    navController.popBackStack(CareerPulseScreen.Start.name, inclusive = false)
}