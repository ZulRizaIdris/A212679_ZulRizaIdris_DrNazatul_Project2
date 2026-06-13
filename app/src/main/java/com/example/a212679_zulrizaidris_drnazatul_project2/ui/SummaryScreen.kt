package com.example.a212679_zulrizaidris_drnazatul_project2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryScreen(
    applicationUiState: ApplicationUiState,
    onCancelButtonClicked: () -> Unit,
    onSubmitButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Application Summary",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Section 1: Job Title
            Text("JOB TITLE", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(applicationUiState.selectedJobTitle, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp)

            // Section 2: Applicant Info
            Text("APPLICANT INFO", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${applicationUiState.applicantName}\n${applicationUiState.applicantEmail}\n${applicationUiState.applicantPhone}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp)

            // Section 3: Interview Date
            Text("INTERVIEW DATE", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(applicationUiState.interviewDate, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp)
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(modifier = Modifier.fillMaxWidth(), onClick = onSubmitButtonClicked) {
                Text("Submit Application")
            }
            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = onCancelButtonClicked) {
                Text("Cancel")
            }
        }
    }
}