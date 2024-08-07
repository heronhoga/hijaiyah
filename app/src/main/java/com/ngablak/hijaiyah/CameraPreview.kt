package com.ngablak.hijaiyah

import android.content.Context
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            setupCamera(ctx, lifecycleOwner, previewView)
            previewView
        },
        modifier = modifier
    )
}

private fun setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder()
            .setTargetResolution(android.util.Size(720, 1280))
            .build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraSelector, preview
            )
        } catch (exc: Exception) {
            // Handle exceptions
            exc.printStackTrace()
        }
    }, ContextCompat.getMainExecutor(context))
}
