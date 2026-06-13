package com.example.a212679_zulrizaidris_drnazatul_project2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.a212679_zulrizaidris_drnazatul_project2.data.DataSource
import com.example.a212679_zulrizaidris_drnazatul_project2.data.JobOpening
import com.example.a212679_zulrizaidris_drnazatul_project2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSelectionScreen(
    onJobSelected: (String) -> Unit,
    onNavigateToForm: () -> Unit,
    viewModel: CareerPulseViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val apiJobs by viewModel.apiJobs.collectAsState()
    val isLoading by viewModel.isLoadingJobs.collectAsState()
    val hasSearched by viewModel.hasSearched.collectAsState()

    // ✅ FIX 1: searchText is now a proper mutable state — typing will work
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "CareerPulse",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Good evening, Zul",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        //Search Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                // ✅ FIX 1: value now reads from searchText state
                value = searchText,
                // ✅ FIX 1: onValueChange now updates searchText so typing works
                onValueChange = { searchText = it },
                placeholder = { Text("Enter job preference...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Button(
                onClick = { viewModel.fetchJobsFromApi(searchText) },
                enabled = !isLoading
            ) {
                Text("Search")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ── API Results Section ──────────────────────────────────────────
        when {
            // Loading spinner — shown while API call is in progress
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Loading live jobs...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ✅ FIX 2: Only show this prompt BEFORE the user has searched
            !hasSearched -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "🔍", style = MaterialTheme.typography.titleLarge)
                        Column {
                            Text(
                                text = "Search for live job listings",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Try: \"developer\", \"designer\", \"engineer\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // No results found after a search
            apiJobs.isEmpty() -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "No live jobs found for \"$searchText\". Try another keyword.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // ✅ FIX 2 & 3: Show results ONLY after searching, with Apply button
            else -> {
                Text(
                    text = "Live Job Listings  •  ${apiJobs.size} found",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                // Each API job card now has an Apply button
                apiJobs.forEach { job ->
                    // ✅ FIX 3: Using ApiJobCardWithApply instead of ApiJobCard
                    ApiJobCardWithApply(
                        title = job.title,
                        company = job.companyName,
                        location = job.location.ifBlank { "Remote" },
                        jobType = job.jobType,
                        onApply = {
                            // Store the selected API job in ViewModel then go to Form
                            viewModel.setApiJob(job.title, job.companyName)
                            onNavigateToForm()
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── Recommended Section Header ────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Recommended",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "All",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                BadgedBox(
                    badge = {
                        Badge(containerColor = Color.Red) {
                            Text("15", color = Color.White)
                        }
                    }
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = "New to you",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Static Job Cards from DataSource ─────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            DataSource.jobOpenings.forEach { job ->
                JobCard(
                    job = job,
                    onApplyClicked = { onJobSelected(job.title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ✅ NEW: API job card WITH an Apply button
// Used for live Retrofit results — tapping Apply saves the job and goes to Form
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ApiJobCardWithApply(
    title: String,
    company: String,
    location: String,
    jobType: String,
    onApply: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Job title
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            // Company name
            Text(
                text = company,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            // Location + job type chips + Apply button all in one row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chips on the left
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SuggestionChip(
                        onClick = {},
                        label = {
                            Text(
                                text = location,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                    if (jobType.isNotBlank()) {
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = jobType,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }

                // ✅ Apply button on the right
                Button(
                    onClick = onApply,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Apply",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Existing static job card — unchanged from your original code
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun JobCard(
    job: JobOpening,
    onApplyClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Logo and Bookmark Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = job.logoRes),
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = "Save Job",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job Title & Company
            Text(
                text = job.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = job.company,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Job detail rows
            JobDetailRow(icon = Icons.Default.LocationOn, text = job.location, iconTint = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            JobDetailRow(icon = Icons.Default.DateRange, text = job.type, iconTint = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            JobDetailRow(icon = Icons.Default.Info, text = job.salary, iconTint = Color(0xFF4CAF50))

            Spacer(modifier = Modifier.height(16.dp))

            // Expandable description + Apply button
            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Text(
                    text = "Job Description:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                job.description.forEach { point ->
                    Row(
                        modifier = Modifier.padding(bottom = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "• ",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = point,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onApplyClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Apply Now", fontWeight = FontWeight.Bold)
                }

            } else {
                Text(
                    text = "Tap to view full details...",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Helper composable — unchanged
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun JobDetailRow(icon: ImageVector, text: String, iconTint: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}