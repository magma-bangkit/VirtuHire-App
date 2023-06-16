package com.magma.virtuhire.ui.screen.introduction

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magma.virtuhire.data.remote.request.IntroductionRequest
import com.magma.virtuhire.data.remote.response.*
import com.magma.virtuhire.domain.repository.IntroductionRepository
import com.magma.virtuhire.ui.screen.introduction.IntroductionDataState
import com.magma.virtuhire.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val introductionRepository: IntroductionRepository
) : ViewModel() {

    private var _institutionState =
        mutableStateOf(IntroductionDataState<List<InstitutionResponse>>())
    val institutionState: State<IntroductionDataState<List<InstitutionResponse>>> =
        _institutionState

    private var _majorState = mutableStateOf(IntroductionDataState<List<MajorResponse>>())
    val majorState: State<IntroductionDataState<List<MajorResponse>>> = _majorState

    private var _degreeState = mutableStateOf(IntroductionDataState<List<DegreeResponse>>())
    val degreeState: State<IntroductionDataState<List<DegreeResponse>>> = _degreeState

    private var _jobCategoryState =
        mutableStateOf(IntroductionDataState<List<JobCategoryResponse>>())
    val jobCategoryState: State<IntroductionDataState<List<JobCategoryResponse>>> =
        _jobCategoryState

    private var _skillState = mutableStateOf(IntroductionDataState<List<SkillResponse>>())
    val skillState: State<IntroductionDataState<List<SkillResponse>>> = _skillState

    private var _cityState = mutableStateOf(IntroductionDataState<List<CityResponse>>())
    val cityState: State<IntroductionDataState<List<CityResponse>>> = _cityState

    private var _createProfileState = mutableStateOf(IntroductionDataState<Unit>())
    val createProfileState: State<IntroductionDataState<Unit>> = _createProfileState

    private var _resumeResponse = mutableStateOf(IntroductionDataState<ResumeResponse>())
    val resumeResponse: State<IntroductionDataState<ResumeResponse>> = _resumeResponse

    fun parseResume(fileUri: Uri) {
        viewModelScope.launch {
            val response = introductionRepository.parseResume(fileUri).collect { result ->
                _resumeResponse.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }

        }
    }

    fun getAllInstitutions(query: String, limit: Int) {
        viewModelScope.launch {
            introductionRepository.getAllInstitutions(query, limit).collect { result ->
                _institutionState.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun getAllMajors(query: String, limit: Int) {
        viewModelScope.launch {
            introductionRepository.getAllMajors(query, limit).collect { result ->
                _majorState.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun getAllDegrees(query: String, limit: Int) {
        viewModelScope.launch {
            introductionRepository.getAllDegrees(query, limit).collect { result ->
                _degreeState.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun getAllJobCategories(query: String, limit: Int) {
        viewModelScope.launch {
            introductionRepository.getAllJobCategories(query, limit).collect { result ->
                _jobCategoryState.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun getAllSkills(query: String, limit: Int) {
        viewModelScope.launch {
            introductionRepository.getAllSkills(query, limit).collect { result ->
                _skillState.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null,
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun getAllCities(query: String, limit: Int) {
        viewModelScope.launch {
            introductionRepository.getAllCities(query, limit).collect { result ->
                _cityState.value = when (result) {
                    is Resource.Success -> IntroductionDataState(
                        data = result.data,
                        isLoading = false,
                        errorMessage = null
                    )

                    is Resource.Error -> IntroductionDataState(
                        data = null,
                        isLoading = false,
                        errorMessage = result.message
                    )

                    is Resource.Loading -> IntroductionDataState(
                        data = null,
                        isLoading = true,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun createUserProfile(introductionRequest: IntroductionRequest) {
        viewModelScope.launch {
            introductionRepository.createUserProfile(introductionRequest)
                .collect { result ->
                    _createProfileState.value = when (result) {
                        is Resource.Success -> IntroductionDataState(
                            data = result.data,
                            isLoading = false,
                            isSuccess = true
                        )

                        is Resource.Error -> IntroductionDataState(
                            data = null,
                            isLoading = false,
                            errorMessage = result.message
                        )

                        is Resource.Loading -> IntroductionDataState(
                            data = null,
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                }
        }
    }

}
