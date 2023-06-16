package com.magma.virtuhire.ui.screen.introduction

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Checkbox
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.magma.virtuhire.data.remote.request.IntroductionRequest
import com.magma.virtuhire.data.remote.response.CityResponse
import com.magma.virtuhire.data.remote.response.DegreeResponse
import com.magma.virtuhire.data.remote.response.InstitutionResponse
import com.magma.virtuhire.data.remote.response.JobCategoryResponse
import com.magma.virtuhire.data.remote.response.JobType
import com.magma.virtuhire.data.remote.response.MajorResponse
import com.magma.virtuhire.data.remote.response.SkillResponse
import com.magma.virtuhire.ui.components.common.ButtonSize
import com.magma.virtuhire.ui.components.common.ButtonVariant
import com.magma.virtuhire.ui.components.common.DatePicker
import com.magma.virtuhire.ui.components.common.ErrorDialog
import com.magma.virtuhire.ui.components.common.InformationDialog
import com.magma.virtuhire.ui.components.common.InputField
import com.magma.virtuhire.ui.components.common.LargeDropdownMenu
import com.magma.virtuhire.ui.components.common.LargeDropdownMenuForMultiple
import com.magma.virtuhire.ui.components.common.LoadingDialog
import com.magma.virtuhire.ui.components.common.PrimaryButton
import com.magma.virtuhire.ui.theme.Primary100
import com.magma.virtuhire.ui.theme.Primary200
import com.magma.virtuhire.ui.theme.Primary50
import com.magma.virtuhire.ui.theme.Primary700
import com.magma.virtuhire.ui.theme.PrimaryGray100
import com.magma.virtuhire.ui.theme.PrimaryGray1200
import com.magma.virtuhire.ui.theme.PrimaryGray200
import com.magma.virtuhire.ui.theme.PrimaryGray300
import com.magma.virtuhire.ui.theme.VirtuHireTheme
import com.magma.virtuhire.utils.getDisplayText
import com.magma.virtuhire.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TempItem(val id: Int, val name: String)

@Composable
fun IntroductionScreen(
    isFromLogin: Boolean,
    navigateToHome: () -> Unit,
    introductionViewModel: IntroductionViewModel = hiltViewModel()
) {
    val institutionState by introductionViewModel.institutionState
    val degreeState by introductionViewModel.degreeState
    val majorState by introductionViewModel.majorState
    val cityState by introductionViewModel.cityState
    val skillState by introductionViewModel.skillState
    val jobCategoryState by introductionViewModel.jobCategoryState
    val createProfileState by introductionViewModel.createProfileState
    val resumeState by introductionViewModel.resumeResponse

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val defaultStartDate = LocalDateTime.now().format(formatter)

    val introductionSteps = listOf(
        IntroductionStep.Resume,
        IntroductionStep.Birthday,
        IntroductionStep.Education,
        IntroductionStep.Skills,
        IntroductionStep.PreferredJobCategories,
        IntroductionStep.PreferredJobType,
        IntroductionStep.Salary,
        IntroductionStep.City,
        IntroductionStep.PreferredCities,
    )
    var currentStep by remember { mutableStateOf(0) }
    var birthdayState by remember { mutableStateOf("") }
    var degreeIdState by remember { mutableStateOf(0) }
    var institutionIdState by remember { mutableStateOf(institutionState.data?.get(0)?.id ?: 0) }
    var majorIdState by remember { mutableStateOf(majorState.data?.get(0)?.id ?: 0) }
    var educationStartDateState by remember { mutableStateOf(defaultStartDate ?: "") }
    var educationEndDateState by remember { mutableStateOf(defaultStartDate ?: "") }
    var skillsState by remember { mutableStateOf(emptyList<TempItem>()) }
    var cityIdState by remember { mutableStateOf(cityState.data?.get(0)?.id ?: 0) }
    var preferredJobTypesState by remember { mutableStateOf(emptyList<JobType>()) }
    var expectedSalaryState by remember { mutableStateOf(0) }
    var preferredCitiesState by remember { mutableStateOf(emptyList<TempItem>()) }
    var preferredJobCategoriesState by remember { mutableStateOf(emptyList<TempItem>()) }

    var isLoadingDialogExpanded by remember { mutableStateOf(false) }
    var isErrorDialogExpanded by remember { mutableStateOf(false) }
    var isWelcomeDialogExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        introductionViewModel.getAllInstitutions("", 10)
        introductionViewModel.getAllDegrees("", 10)
        introductionViewModel.getAllMajors("", 10)
        introductionViewModel.getAllCities("", 10)
        introductionViewModel.getAllSkills("", 10)
        introductionViewModel.getAllJobCategories("", 10)
    }

    LaunchedEffect(institutionState) {
        if (institutionState.data != null)
            institutionIdState = institutionState.data!![0].id
    }

    LaunchedEffect(majorState) {
        if (majorState.data != null)
            majorIdState = majorState.data!![0].id
    }

    LaunchedEffect(cityState) {
        if (cityState.data != null)
            cityIdState = cityState.data!![0].id
    }

    LaunchedEffect(degreeState) {
        if (degreeState.data != null)
            degreeIdState = degreeState.data!![0].id
    }

    LaunchedEffect(createProfileState) {
        if (createProfileState.errorMessage != null) {
            isErrorDialogExpanded = true
        }
        if (createProfileState.isSuccess) {
            navigateToHome()
        }
        isLoadingDialogExpanded = createProfileState.isLoading
    }

    LaunchedEffect(isFromLogin) {
        if (isFromLogin) {
            isWelcomeDialogExpanded = true
        }
    }

    val introductionRequest = createRequestPayload(
        birthdayState,
        degreeIdState,
        institutionIdState,
        majorIdState,
        educationStartDateState,
        educationEndDateState,
        skillsState,
        cityIdState,
        preferredJobTypesState,
        expectedSalaryState,
        preferredCitiesState,
        preferredJobCategoriesState
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGray100)
    ) {
        ErrorDialog(
            isDialogOpen = isErrorDialogExpanded,
            onDialogClose = { isErrorDialogExpanded = false },
            errorMessage = createProfileState.errorMessage ?: ""
        )
        LoadingDialog(isDialogOpen = isLoadingDialogExpanded) {
            isLoadingDialogExpanded = false
        }
        InformationDialog(
            isDialogOpen = isWelcomeDialogExpanded,
            onDialogClose = { isWelcomeDialogExpanded = false },
            title = "Welcome to Virtuhire",
            message = "Welcome, before you can start using Virtuhire, please complete this few question about yourself."
        )
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 80.dp
            )
        ) {
            val context = LocalContext.current
            when (introductionSteps[currentStep]) {
                is IntroductionStep.Resume -> ResumeStep(
                    onUploadFile = {
                        it.let { uri ->
                            scope.launch {
                                introductionViewModel.parseResume(uri)
                            }
                        }
                    }
                )

                is IntroductionStep.Birthday -> BirthdayStep(
                    birthday = birthdayState,
                    onBirthdayChange = { birthdayState = it }
                )

                is IntroductionStep.Education -> EducationStep(
                    selectedInstitutionId = institutionIdState,
                    onSelectedInstitutionIdChange = { institutionIdState = it },
                    selectedDegreeId = degreeIdState,
                    onSelectedDegreeIdChange = { degreeIdState = it },
                    selectedMajorId = majorIdState,
                    onSelectedMajorIdChange = { majorIdState = it },
                    educationStartDate = educationStartDateState,
                    educationEndDate = educationEndDateState,
                    onSearchInstitutionChange = {
                        introductionViewModel.getAllInstitutions(it, 10)
                    },
                    onSearchDegreeChange = {
                        introductionViewModel.getAllDegrees(it, 10)
                    },
                    onSearchMajorChange = {
                        introductionViewModel.getAllMajors(it, 10)
                    },
                    onEducationStartDateChange = { educationStartDateState = it },
                    onEducationEndDateChange = { educationEndDateState = it },
                    institutionState,
                    degreeState,
                    majorState,
                )

                is IntroductionStep.Skills -> SkillsStep(
                    selectedSkills = skillsState,
                    onSelectedSkillsChange = { id, name ->
                        skillsState = skillsState + TempItem(id, name)
                    },
                    onSelectedSkillsRemove = {
                        skillsState = skillsState.toMutableList().apply {
                            remove(it)
                        }
                    },
                    onSearchSkillChange = {
                        introductionViewModel.getAllSkills(it, 10)
                    },
                    skillState
                )

                is IntroductionStep.PreferredJobCategories -> PreferredJobCategoriesStep(
                    selectedCategories = preferredJobCategoriesState,
                    onSelectedCategoriesChange = { id, name ->
                        preferredJobCategoriesState =
                            preferredJobCategoriesState + TempItem(id, name)
                    },
                    onSelectedCategoriesRemove = {
                        preferredJobCategoriesState =
                            preferredJobCategoriesState.toMutableList().apply {
                                remove(it)
                            }
                    },
                    onSearchCategoryChange = {
                        introductionViewModel.getAllJobCategories(it, 10)
                    },
                    categoryState = jobCategoryState
                )

                is IntroductionStep.PreferredJobType -> JobTypeSelection(
                    selectedJobTypes = preferredJobTypesState,
                    onJobTypeSelected = { jobType ->
                        preferredJobTypesState = if (preferredJobTypesState.contains(jobType)) {
                            preferredJobTypesState - jobType
                        } else {
                            preferredJobTypesState + jobType
                        }
                    }
                )

                is IntroductionStep.Salary -> SalaryStep(
                    expectedSalary = expectedSalaryState,
                    onExpectedSalaryChange = { expectedSalaryState = it }
                )

                is IntroductionStep.City -> CityStep(
                    selectedCityId = cityIdState,
                    onSelectedCityIdChange = { cityIdState = it },
                    onSearchCityChange = {
                        introductionViewModel.getAllCities(it, 10)
                    },
                    cityState = cityState
                )

                is IntroductionStep.PreferredCities -> PreferredJobCitiesStep(
                    selectedCities = preferredCitiesState,
                    onSelectedCitiesChange = { id, name ->
                        preferredCitiesState = preferredCitiesState + TempItem(id, name)
                    },
                    onSelectedCitiesRemove = { city ->
                        preferredCitiesState = preferredCitiesState.toMutableList().apply {
                            remove(city)
                        }
                    },
                    onSearchCityChange = {
                        introductionViewModel.getAllCities(it, 10)
                    },
                    cityState = cityState,
                )
            }
        }
        Text(text = resumeState.toString())
        IntroductionScreenBottomSection(
            currentStep = currentStep,
            introductionSteps = introductionSteps,
            onConfirmClick = {
                scope.launch {
                    introductionViewModel.createUserProfile(introductionRequest = introductionRequest)
                }
            },
            onNextClicked = {
                currentStep++
            },
            onPreviousClicked = {
                if (currentStep > 0) {
                    currentStep--
                }
            }
        )
    }
}

@SuppressLint("Recycle")
@Composable
fun ResumeStep(onUploadFile: (Uri) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    // File selection result
    val fileUri = remember { mutableStateOf<Uri?>(null) }

    // Activity ResultLauncher for file selection
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fileUri.value = uri
    }


    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(text = fileContent.toString())
        Text(
            text = "Please upload your resume or CV", style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(onClick = { launcher.launch("application/pdf,image/*")}, text = "Upload Resume")
        Spacer(modifier = Modifier.height(9.dp))
        // Pass the selected fileUri to the ViewModel to initiate the upload
        PrimaryButton(onClick = {
            if (fileUri.value == null) {
                scope.launch {
                    showToast(context, "No File")
                }
            } else {
                onUploadFile(fileUri.value!!)
            }

        }, text = "Send Resume")
    }
    // Call the parseResume function when needed, e.g., on a button click
}

@Composable
fun ConfirmationDialog(modifier: Modifier = Modifier, onConfirmClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    PrimaryButton(text = "Submit", onClick = { expanded = true }, modifier = modifier)
    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryGray100)
                        .padding(20.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Confirmation",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Are you sure you want to save your profile?",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        PrimaryButton(
                            size = ButtonSize.Normal,
                            variant = ButtonVariant.Light,
                            onClick = { expanded = false },
                            modifier = Modifier.weight(1f),
                            text = "Cancel"
                        )
                        PrimaryButton(
                            size = ButtonSize.Normal,
                            onClick = {
                                expanded = false
                                onConfirmClick()
                            },
                            modifier = Modifier.weight(1f),
                            text = "Confirm"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BirthdayStep(birthday: String, onBirthdayChange: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = "When is your birthday?", style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        DatePicker(label = "Birthday", value = birthday, onValueChange = { onBirthdayChange(it) })
    }
}

@Composable
fun EducationStep(
    selectedInstitutionId: Int,
    onSelectedInstitutionIdChange: (Int) -> Unit,
    selectedDegreeId: Int,
    onSelectedDegreeIdChange: (Int) -> Unit,
    selectedMajorId: Int,
    onSelectedMajorIdChange: (Int) -> Unit,
    educationStartDate: String,
    educationEndDate: String,
    onSearchInstitutionChange: (String) -> Unit,
    onSearchDegreeChange: (String) -> Unit,
    onSearchMajorChange: (String) -> Unit,
    onEducationStartDateChange: (String) -> Unit,
    onEducationEndDateChange: (String) -> Unit,
    institutionState: IntroductionDataState<List<InstitutionResponse>>,
    degreeState: IntroductionDataState<List<DegreeResponse>>,
    majorState: IntroductionDataState<List<MajorResponse>>
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var institutionSearchQuery by remember { mutableStateOf("") }
    var degreeSearchQuery by remember { mutableStateOf("") }
    var majorSearchQuery by remember { mutableStateOf("") }
    var selectedInstitution by remember { mutableStateOf(institutionState.data?.find { it.id == selectedInstitutionId }) }
    var selectedDegree by remember { mutableStateOf(degreeState.data?.find { it.id == selectedDegreeId }) }
    var selectedMajor by remember { mutableStateOf(majorState.data?.find { it.id == selectedMajorId }) }

    // Perform search query debounce for institution
    LaunchedEffect(institutionSearchQuery) {
        delay(300)
        onSearchInstitutionChange(institutionSearchQuery)
    }

    // Perform search query debounce for degree
    LaunchedEffect(degreeSearchQuery) {
        delay(300)
        onSearchDegreeChange(degreeSearchQuery)
    }

    // Perform search query debounce for major
    LaunchedEffect(majorSearchQuery) {
        delay(300)
        onSearchMajorChange(majorSearchQuery)
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = "What is your highest level of education?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenu(
            label = "Institution",
            items = institutionState.data ?: emptyList(),
            selectedIndex = selectedIndex,
            selectedItem = selectedInstitution,
            onItemSelected = { index, item ->
                selectedIndex = index
                selectedInstitution = item
                onSelectedInstitutionIdChange(item.id)
            },
            searchValue = institutionSearchQuery,
            onSearchValueChange = { institutionSearchQuery = it },
            selectedItemToString = { it.name },
            checkIsItemSelected = { item, selectedItem -> item.id == selectedItem.id }
        )
        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenu(
            label = "Degree",
            items = degreeState.data ?: emptyList(),
            selectedIndex = selectedIndex,
            selectedItem = selectedDegree,
            onItemSelected = { index, item ->
                selectedIndex = index
                selectedDegree = item
                onSelectedDegreeIdChange(item.id)
            },
            searchValue = degreeSearchQuery,
            onSearchValueChange = { degreeSearchQuery = it },
            selectedItemToString = { it.name },
            checkIsItemSelected = { item, selectedItem -> item.id == selectedItem.id }
        )
        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenu(
            label = "Major",
            items = majorState.data ?: emptyList(),
            selectedIndex = selectedIndex,
            selectedItem = selectedMajor,
            onItemSelected = { index, item ->
                selectedIndex = index
                selectedMajor = item
                onSelectedMajorIdChange(item.id)
            },
            searchValue = majorSearchQuery,
            onSearchValueChange = { majorSearchQuery = it },
            selectedItemToString = { it.name },
            checkIsItemSelected = { item, selectedItem -> item.id == selectedItem.id }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DatePicker(
                label = "Start Date",
                value = educationStartDate,
                onValueChange = { onEducationStartDateChange(it) },
                modifier = Modifier.weight(1f)
            )
            DatePicker(
                label = "End Date",
                value = educationEndDate,
                onValueChange = { onEducationEndDateChange(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Composable
fun SkillsStep(
    selectedSkills: List<TempItem>,
    onSelectedSkillsChange: (id: Int, name: String) -> Unit,
    onSelectedSkillsRemove: (TempItem) -> Unit,
    onSearchSkillChange: (String) -> Unit,
    skillState: IntroductionDataState<List<SkillResponse>>
) {
    var skillSearchQuery by remember { mutableStateOf("") }
    // var selectedSkillsIdState by remember { mutableStateOf(emptyList<Int>()) }

    LaunchedEffect(skillSearchQuery) {
        delay(300)
        onSearchSkillChange(skillSearchQuery)
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = "What skills do you possess?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        val scrollState = rememberScrollState()
        var previousScrollPosition by remember { mutableStateOf(0) }
        var previousSelectedSkills by remember { mutableStateOf(emptyList<TempItem>()) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
        ) {
            selectedSkills.forEachIndexed { index, skill ->
                SelectedBadge(
                    item = skill,
                    onClearClick = {
                        previousScrollPosition = scrollState.value
                        onSelectedSkillsRemove(it)
                    },
                    selectedItemToString = { it.name }
                )
            }

            LaunchedEffect(selectedSkills) {
                if (selectedSkills.size < previousSelectedSkills.size) {
                    scrollState.scrollTo(previousScrollPosition)
                } else {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
                previousSelectedSkills = selectedSkills
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenuForMultiple(
            label = "Skills",
            items = skillState.data ?: emptyList(),
            onItemSelected = { _, item ->
                onSelectedSkillsChange(item.id, item.name)
            },
            searchValue = skillSearchQuery,
            onSearchValueChange = { skillSearchQuery = it },
            selectedItemToString = { it.name }
        )
    }
}

@Composable
fun <T> SelectedBadge(
    item: T,
    onClearClick: (T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(5.dp)
            .clip(
                RoundedCornerShape(10.dp)
            )
            .background(Primary50)
            .padding(horizontal = 4.dp, vertical = 1.dp)

    ) {
        Text(
            modifier = Modifier.padding(horizontal = 7.dp),
            text = selectedItemToString(item),
            style = MaterialTheme.typography.bodyMedium,
            color = Primary700
        )
        IconButton(onClick = { onClearClick(item) }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "", tint = Primary700)
        }
    }
}

@Composable
fun CityStep(
    selectedCityId: Int,
    onSelectedCityIdChange: (Int) -> Unit,
    onSearchCityChange: (String) -> Unit,
    cityState: IntroductionDataState<List<CityResponse>>
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var selectedCity by remember { mutableStateOf(cityState.data?.find { it.id == selectedCityId }) }
    var citySearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(citySearchQuery) {
        delay(300)
        onSearchCityChange(citySearchQuery)
    }

    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = "Where is your current place of residence?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenu(
            label = "City",
            items = cityState.data ?: emptyList(),
            selectedIndex = selectedIndex,
            selectedItem = selectedCity,
            onItemSelected = { index, item ->
                selectedIndex = index
                selectedCity = item
                onSelectedCityIdChange(item.id)
            },
            searchValue = citySearchQuery,
            onSearchValueChange = { citySearchQuery = it },
            selectedItemToString = { it.name },
            checkIsItemSelected = { item, selectedItem -> item.id == selectedItem.id }
        )
    }
}

@Composable
fun JobTypeSelection(
    selectedJobTypes: List<JobType>,
    onJobTypeSelected: (JobType) -> Unit
) {
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = "What types of jobs are you interested in?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            JobType.values().forEach { jobType ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selectedJobTypes.contains(jobType)) Primary50 else PrimaryGray300)
                        .clickable { onJobTypeSelected(jobType) }
                        .padding(vertical = 10.dp)
                ) {
                    Checkbox(
                        checked = selectedJobTypes.contains(jobType),
                        onCheckedChange = { checked ->
                            if (checked) {
                                onJobTypeSelected(jobType)
                            } else {
                                onJobTypeSelected(jobType)
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Primary700
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = getDisplayText(jobType),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedJobTypes.contains(jobType)) Primary700 else PrimaryGray1200
                    )
                }
            }
        }
    }

}


@Composable
fun SalaryStep(expectedSalary: Int, onExpectedSalaryChange: (Int) -> Unit) {
    fun isNumeric(value: String): Boolean {
        val regex =
            Regex("-?\\d+") // Regex to match an optional negative sign followed by one or more digits
        return regex.matches(value)
    }
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
        Text(
            text = "What is your expected salary range?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Salary (Rp.)", style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        InputField(
            value = expectedSalary.toString(),
            onValueChange = {
                if (isNumeric(it)) {
                    onExpectedSalaryChange(it.toInt())
                }
            },
            isNumeric = true
        )
    }
}

@Composable
fun PreferredJobCitiesStep(
    selectedCities: List<TempItem>,
    onSelectedCitiesChange: (id: Int, name: String) -> Unit,
    onSelectedCitiesRemove: (TempItem) -> Unit,
    onSearchCityChange: (String) -> Unit,
    cityState: IntroductionDataState<List<CityResponse>>
) {
    var citySearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(citySearchQuery) {
        delay(300)
        onSearchCityChange(citySearchQuery)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "What are your preferred job cities?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        val scrollState = rememberScrollState()
        var previousScrollPosition by remember { mutableStateOf(0) }
        var previousSelectedCities by remember { mutableStateOf(emptyList<TempItem>()) }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
        ) {
            selectedCities.forEachIndexed { index, city ->
                SelectedBadge(
                    item = city,
                    onClearClick = {
                        previousScrollPosition = scrollState.value
                        onSelectedCitiesRemove(it)
                    },
                    selectedItemToString = { it.name }
                )
            }

            LaunchedEffect(selectedCities) {
                if (selectedCities.size < previousSelectedCities.size) {
                    scrollState.scrollTo(previousScrollPosition)
                } else {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
                previousSelectedCities = selectedCities
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenuForMultiple(
            label = "Job Cities",
            items = cityState.data ?: emptyList(),
            onItemSelected = { _, item ->
                onSelectedCitiesChange(item.id, item.name)
            },
            searchValue = citySearchQuery,
            onSearchValueChange = { citySearchQuery = it },
            selectedItemToString = { it.name }
        )
    }
}


@Composable
fun PreferredJobCategoriesStep(
    selectedCategories: List<TempItem>,
    onSelectedCategoriesChange: (id: Int, name: String) -> Unit,
    onSelectedCategoriesRemove: (TempItem) -> Unit,
    onSearchCategoryChange: (String) -> Unit,
    categoryState: IntroductionDataState<List<JobCategoryResponse>>
) {
    var categorySearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(categorySearchQuery) {
        delay(300)
        onSearchCategoryChange(categorySearchQuery)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text(
            text = "What are your preferred job categories?",
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))

        val scrollState = rememberScrollState()
        var previousScrollPosition by remember { mutableStateOf(0) }
        var previousSelectedCategories by remember { mutableStateOf(emptyList<TempItem>()) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
        ) {
            selectedCategories.forEachIndexed { index, category ->
                SelectedBadge(
                    item = category,
                    onClearClick = {
                        previousScrollPosition = scrollState.value
                        onSelectedCategoriesRemove(it)
                    },
                    selectedItemToString = { it.name }
                )
            }
            LaunchedEffect(selectedCategories) {
                if (selectedCategories.size < previousSelectedCategories.size) {
                    scrollState.scrollTo(previousScrollPosition)
                } else {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
                previousSelectedCategories = selectedCategories
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LargeDropdownMenuForMultiple(
            label = "Job Categories",
            items = categoryState.data ?: emptyList(),
            onItemSelected = { _, item ->
                onSelectedCategoriesChange(item.id, item.name)
            },
            searchValue = categorySearchQuery,
            onSearchValueChange = { categorySearchQuery = it },
            selectedItemToString = { it.name }
        )
    }
}


@Composable
fun BoxScope.IntroductionScreenBottomSection(
    currentStep: Int,
    introductionSteps: List<IntroductionStep>,
    onConfirmClick: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent, PrimaryGray100
                    ),
                    startY = 0.0f,
                    endY = 75f
                )
            )
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp, top = 15.dp)
            .align(Alignment.BottomCenter)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            PrimaryButton(
                onClick = { onPreviousClicked() },
                text = "Previous",
                size = ButtonSize.Large,
                variant = ButtonVariant.Light,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            )
            if (currentStep < introductionSteps.size - 1) {
                PrimaryButton(
                    onClick = { onNextClicked() },
                    text = "Next",
                    size = ButtonSize.Large,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                )
            } else {
                ConfirmationDialog(
                    modifier = Modifier.weight(1f),
                    onConfirmClick = { onConfirmClick() })
            }
        }

    }
}


fun createRequestPayload(
    birthdayState: String,
    degreeIdState: Int,
    institutionIdState: Int,
    majorIdState: Int,
    educationStartDateState: String,
    educationEndDateState: String,
    skillsState: List<TempItem>,
    cityIdState: Int,
    preferredJobTypesState: List<JobType>,
    expectedSalaryState: Int,
    preferredCitiesState: List<TempItem>,
    preferredJobCategoriesState: List<TempItem>
): IntroductionRequest {
    val skills = skillsState.map { it.id }
    val preferredCities = preferredCitiesState.map { it.id }
    val preferredJobCategories = preferredJobCategoriesState.map { it.id }
    val preferredJobTypes = preferredJobTypesState.map { it.name }

    return IntroductionRequest(
        birthdayState,
        degreeIdState,
        institutionIdState,
        majorIdState,
        educationStartDateState,
        educationEndDateState,
        skills,
        cityIdState,
        preferredJobTypes,
        expectedSalaryState,
        preferredCities,
        preferredJobCategories
    )
}


//@Preview
@Composable
fun IntroductionScreenPreview() {
    VirtuHireTheme {
        IntroductionScreen(isFromLogin = false, navigateToHome = {})
    }
}

