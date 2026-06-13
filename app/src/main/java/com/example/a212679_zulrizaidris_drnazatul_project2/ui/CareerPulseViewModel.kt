package com.example.a212679_zulrizaidris_drnazatul_project2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a212679_zulrizaidris_drnazatul_project2.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ApplicationUiState(
    val selectedJobTitle: String = "",
    val selectedCompany: String = "",
    val applicantName: String = "",
    val applicantEmail: String = "",
    val applicantPhone: String = "",
    val interviewDate: String = ""
)

data class Education(
    val title: String,
    val institution: String,
    val expectedFinish: String
)

class CareerPulseViewModel(
    private val repository: ApplicationRepository,
    private val jobApiRepository: JobApiRepository,        // ✅ NEW
    private val firestoreRepository: FirestoreRepository   // ✅ NEW
) : ViewModel() {

    // ── Existing application form state ──────────────────────────────────
    private val _uiState = MutableStateFlow(ApplicationUiState())
    val uiState: StateFlow<ApplicationUiState> = _uiState.asStateFlow()

    val appliedJobs: StateFlow<List<JobEntity>> =
        repository.getAllApplicationsStream()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    private val _personalSummary = MutableStateFlow(
        "IT student passionate about software development and AI. Experienced in mobile applications."
    )
    val personalSummary: StateFlow<String> = _personalSummary.asStateFlow()

    private val _educationList = MutableStateFlow(listOf(
        Education("Bachelor of Information Technology", "Universiti Kebangsaan Malaysia", "Expected Mar 2028")
    ))
    val educationList: StateFlow<List<Education>> = _educationList.asStateFlow()

    private val _skillsList = MutableStateFlow(DataSource.skills)
    val skillsList: StateFlow<List<String>> = _skillsList.asStateFlow()

    // ── ✅ NEW: Web API (Retrofit) state ──────────────────────────────────
    private val _apiJobs = MutableStateFlow<List<RemoteJob>>(emptyList())
    val apiJobs: StateFlow<List<RemoteJob>> = _apiJobs.asStateFlow()

    private val _isLoadingJobs = MutableStateFlow(false)
    val isLoadingJobs: StateFlow<Boolean> = _isLoadingJobs.asStateFlow()

    private val _apiError = MutableStateFlow<String?>(null)
    val apiError: StateFlow<String?> = _apiError.asStateFlow()

    // ✅ ADD this new state to track if user has searched:
    private val _hasSearched = MutableStateFlow(false)
    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()

    // ✅ UPDATE fetchJobsFromApi to set the flag:
    fun fetchJobsFromApi(query: String = "") {
        viewModelScope.launch {
            _hasSearched.value = true   // only show results after user searches
            _isLoadingJobs.value = true
            _apiError.value = null
            try {
                val jobs = jobApiRepository.searchJobs(query)
                _apiJobs.value = jobs
            } catch (e: Exception) {
                _apiError.value = "Failed to load jobs: ${e.message}"
            } finally {
                _isLoadingJobs.value = false
            }
        }
    }

    // ✅ ADD this new function to set an API job directly (bypasses DataSource lookup):
    fun setApiJob(jobTitle: String, companyName: String) {
        _uiState.update {
            it.copy(
                selectedJobTitle = jobTitle,
                selectedCompany = companyName
            )
        }
    }

    // ── ✅ NEW: Firebase Firestore state ──────────────────────────────────
    val communityPosts: StateFlow<List<CommunityPost>> =
        firestoreRepository.getPostsStream()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), emptyList())

    fun addCommunityPost(authorName: String, jobTitle: String, company: String, tip: String) {
        viewModelScope.launch {
            firestoreRepository.addPost(
                CommunityPost(
                    authorName = authorName,
                    jobTitle = jobTitle,
                    companyName = company,
                    tipContent = tip
                )
            )
        }
    }

    // ── ✅ NEW: GPS location state ────────────────────────────────────────
    private val _currentLocation = MutableStateFlow<String?>(null)
    val currentLocation: StateFlow<String?> = _currentLocation.asStateFlow()

    fun updateLocation(lat: Double, lng: Double) {
        _currentLocation.value = "Lat: %.4f, Lng: %.4f".format(lat, lng)
        // Optionally fetch API jobs filtered by location
        fetchJobsFromApi("Malaysia")
    }

    // ── Existing form functions ───────────────────────────────────────────
    fun setJob(jobTitle: String) {
        val company = DataSource.jobOpenings.find { it.title == jobTitle }?.company ?: ""
        _uiState.update { it.copy(selectedJobTitle = jobTitle, selectedCompany = company) }
    }

    fun setApplicantDetails(name: String, email: String, phone: String) {
        _uiState.update { it.copy(applicantName = name, applicantEmail = email, applicantPhone = phone) }
    }

    fun setDate(date: String) {
        _uiState.update { it.copy(interviewDate = date) }
    }

    fun resetOrder() { _uiState.value = ApplicationUiState() }

    fun updatePersonalSummary(newSummary: String) { _personalSummary.value = newSummary }

    fun addEducation(title: String, institution: String, expectedFinish: String) {
        _educationList.update { it + Education(title, institution, expectedFinish) }
    }

    fun addSkill(newSkill: String) {
        _skillsList.update { it + newSkill }
    }

    fun submitApplication() {
        val s = _uiState.value
        if (s.selectedJobTitle.isNotEmpty()) {
            viewModelScope.launch {
                repository.insertApplication(
                    JobEntity(
                        jobTitle = s.selectedJobTitle,
                        applicantName = s.applicantName,
                        interviewDate = s.interviewDate,
                        company = s.selectedCompany,
                        status = "Applied - Pending Review"
                    )
                )
            }
        }
    }
}