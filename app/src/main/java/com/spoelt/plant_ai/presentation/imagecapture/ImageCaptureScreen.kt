package com.spoelt.plant_ai.presentation.imagecapture

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.spoelt.plant_ai.presentation.imagecapture.components.CameraPreviewScreen
import com.spoelt.plant_ai.presentation.imagecapture.components.LoadingScreen
import com.spoelt.plant_ai.util.getString

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImageCaptureScreen(
    viewModel: ImageCaptureViewModel = hiltViewModel(),
    onPermissionDenied: () -> Unit,
    onIdentificationSuccess: () -> Unit
) {
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val hasPermission = remember(cameraPermissionState.status) {
        cameraPermissionState.status.isGranted
    }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) onPermissionDenied()
    }
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.snackbarText.collect { text ->
            snackbarHostState.showSnackbar(message = text.getString(context))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.identificationSuccess.collect { success ->
            if (success) onIdentificationSuccess()
        }
    }

    LifecycleResumeEffect(true) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }

        onPauseOrDispose {
            // do nothing
        }
    }

    if (hasPermission) {
        if (isLoading) {
            LoadingScreen()
        } else {
            CameraPreviewScreen(
                snackbarHostState = snackbarHostState,
                onImageCaptureSuccess = viewModel::identifyPlant,
                onImageCaptureFailure = viewModel::handleImageCaptureFailure
            )
        }
    } else {
        NoPermissionScreen()
    }
}

@Composable
fun NoPermissionScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
    )
}