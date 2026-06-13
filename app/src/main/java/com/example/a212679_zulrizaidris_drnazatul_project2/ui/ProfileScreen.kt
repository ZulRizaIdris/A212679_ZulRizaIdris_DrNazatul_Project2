package com.example.a212679_zulrizaidris_drnazatul_project2.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(viewModel: CareerPulseViewModel) { // Added ViewModel parameter
    val scrollState = rememberScrollState()

    // Collect states from ViewModel
    val personalSummary by viewModel.personalSummary.collectAsState()
    val educationList by viewModel.educationList.collectAsState()
    val skillsList by viewModel.skillsList.collectAsState()

    // Dialog states
    var showEditSummaryDialog by remember { mutableStateOf(false) }
    var showAddEducationDialog by remember { mutableStateOf(false) }
    var showAddSkillDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // --- Profile Header ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Zul Riza Idris", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Shah Alam, Selangor", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Personal Summary Section ---
        SectionHeader(title = "Personal Summary", actionText = "Edit", onActionClick = { showEditSummaryDialog = true })
        Text(
            text = personalSummary, // Read from ViewModel
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Education Section ---
        SectionHeader(title = "Education", actionText = "Add", onActionClick = { showAddEducationDialog = true })
        Spacer(modifier = Modifier.height(8.dp))
        educationList.forEach { edu ->
            DetailCard(title = edu.title, subtitle = edu.institution, caption = edu.expectedFinish)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Skills Section ---
        SectionHeader(title = "Skills", actionText = "Add", onActionClick = { showAddSkillDialog = true })
        Spacer(modifier = Modifier.height(8.dp))

        val chunkedSkills = skillsList.chunked(2)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            chunkedSkills.forEach { rowSkills ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowSkills.forEach { skill ->
                        SuggestionChip(onClick = { }, label = { Text(skill) }, shape = RoundedCornerShape(16.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }

    // --- Dialogs ---
    if (showEditSummaryDialog) {
        var tempSummary by remember { mutableStateOf(personalSummary) }
        AlertDialog(
            onDismissRequest = { showEditSummaryDialog = false },
            title = { Text("Edit Summary") },
            text = { OutlinedTextField(value = tempSummary, onValueChange = { tempSummary = it }, modifier = Modifier.fillMaxWidth()) },
            confirmButton = {
                TextButton(onClick = { viewModel.updatePersonalSummary(tempSummary); showEditSummaryDialog = false }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showEditSummaryDialog = false }) { Text("Cancel") } }
        )
    }

    if (showAddEducationDialog) {
        var title by remember { mutableStateOf("") }
        var institution by remember { mutableStateOf("") }
        var finish by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddEducationDialog = false },
            title = { Text("Add Education") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Degree/Certificate") }, singleLine = true)
                    OutlinedTextField(value = institution, onValueChange = { institution = it }, label = { Text("Institution") }, singleLine = true)
                    OutlinedTextField(value = finish, onValueChange = { finish = it }, label = { Text("Expected Finish (e.g. Mar 2028)") }, singleLine = true)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank()) viewModel.addEducation(title, institution, finish)
                    showAddEducationDialog = false
                }) { Text("Add") }
            },
            dismissButton = { TextButton(onClick = { showAddEducationDialog = false }) { Text("Cancel") } }
        )
    }

    if (showAddSkillDialog) {
        var newSkill by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddSkillDialog = false },
            title = { Text("Add Skill") },
            text = { OutlinedTextField(value = newSkill, onValueChange = { newSkill = it }, label = { Text("Skill Name") }, singleLine = true) },
            confirmButton = {
                TextButton(onClick = {
                    if (newSkill.isNotBlank()) viewModel.addSkill(newSkill)
                    showAddSkillDialog = false
                }) { Text("Add") }
            },
            dismissButton = { TextButton(onClick = { showAddSkillDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
fun SectionHeader(title: String, actionText: String, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        TextButton(onClick = onActionClick) { Text(text = actionText, fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun DetailCard(title: String, subtitle: String, caption: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            if (caption.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = caption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}