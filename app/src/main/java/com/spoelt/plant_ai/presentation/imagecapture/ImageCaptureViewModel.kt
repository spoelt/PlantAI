package com.spoelt.plant_ai.presentation.imagecapture

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoelt.plant_ai.R
import com.spoelt.plant_ai.data.repository.PlantAIException
import com.spoelt.plant_ai.domain.repository.PlantRepository
import com.spoelt.plant_ai.util.UIText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageCaptureViewModel @Inject constructor(
    private val repository: PlantRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _snackbarText = MutableSharedFlow<UIText>()
    val snackbarText = _snackbarText.asSharedFlow()

    private val _identificationSuccess = MutableSharedFlow<Boolean>()
    val identificationSuccess = _identificationSuccess.asSharedFlow()

    fun identifyPlant(bitmap: Bitmap) {
        _isLoading.value = true

        viewModelScope.launch {
            val result = repository.identifyPlant(bitmap)
            _isLoading.value = false

            if (result is PlantAIException) {
                // show error
            } else {
                _identificationSuccess.emit(true)
            }
        }
    }

    fun handleImageCaptureFailure(imageCaptureException: ImageCaptureException) {
        logImageCaptureError(imageCaptureException)
        triggerSnackbar()
    }

    private fun logImageCaptureError(imageCaptureException: ImageCaptureException) {
        Log.e(TAG, "Couldn't capture image: $imageCaptureException")
    }

    private fun triggerSnackbar() {
        viewModelScope.launch {
            _snackbarText.emit(UIText.ResourceText(R.string.general_error))
        }
    }

    companion object {
        private const val TAG = "ImageCaptureViewModel"
    }
}