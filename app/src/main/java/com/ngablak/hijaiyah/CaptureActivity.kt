package com.ngablak.hijaiyah

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView
import com.google.common.util.concurrent.ListenableFuture
import com.ngablak.hijaiyah.ml.ModelHijaiyah
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CaptureActivity : ComponentActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var tfliteModel: ModelHijaiyah
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private lateinit var cameraExecutor: ExecutorService
    private var toast: Toast? = null

    private val numberToLabel = mapOf(
        0 to "alif",
        1 to "ra",
        2 to "zai",
        3 to "sin",
        4 to "syin",
        5 to "shad",
        6 to "dhad",
        7 to "tha",
        8 to "dha",
        9 to "ain",
        10 to "ghain",
        11 to "ba",
        12 to "fa",
        13 to "qaf",
        14 to "kaf",
        15 to "lam",
        16 to "mim",
        17 to "nun",
        18 to "hha",
        19 to "waw",
        20 to "ya",
        21 to "ta",
        22 to "tsa",
        23 to "jim",
        24 to "ha",
        25 to "kho",
        26 to "dal",
        27 to "dzal"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfliteModel = ModelHijaiyah.newInstance(this)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {
            HijaiyahTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraPreview()
                }
            }
        }
    }

    @Composable
    fun CameraPreview() {
        val context = LocalContext.current
        var label by remember { mutableStateOf("") }
        var previewView by remember { mutableStateOf(androidx.camera.view.PreviewView(context)) }

        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider, cameraSelector, previewView) { bitmap ->
                label = processFrame(bitmap)
            }
        }, ContextCompat.getMainExecutor(context))

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
            Text(text = "Detected Number: $label", modifier = Modifier.align(Alignment.TopCenter).padding(16.dp))
            Image(
                painter = painterResource(id = R.drawable.guide_image),  // Replace with your guide image resource ID
                contentDescription = "Guide Image",
                colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.White),
                modifier = Modifier.size(200.dp).align(Alignment.Center)  // Adjust the size and alignment as needed
            )
            Button(onClick = { switchCamera(previewView) }, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                Text(text = "Switch Camera")
            }
        }
    }

    private fun processFrame(bitmap: Bitmap): String {
        val croppedBitmap = cropCenterSquare(bitmap)
        val tbuffer = loadAndPreprocessImage(croppedBitmap)
        val byteBuffer = tbuffer.buffer

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 1), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = tfliteModel.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val maxIndex = getMaxIndex(outputFeature0.floatArray)
        val confidence = outputFeature0.floatArray[maxIndex] * 100

        Log.d("Max Value", "$maxIndex")
        val predictedLabel = numberToLabel[maxIndex] ?: "Unknown"

        Log.d("Accuracy", "Label: $predictedLabel, Confidence: $confidence")

        if (confidence >= 99f) {
            showToast(maxIndex.toString())
            return maxIndex.toString()
        }

        return ""
    }

    private fun showToast(message: String) {
        runOnUiThread {
            toast?.cancel()
            toast = Toast.makeText(this@CaptureActivity, message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    private fun cropCenterSquare(bitmap: Bitmap): Bitmap {
        val dimension = minOf(bitmap.width, bitmap.height)
        val x = (bitmap.width - dimension) / 2
        val y = (bitmap.height - dimension) / 2
        return Bitmap.createBitmap(bitmap, x, y, dimension, dimension)
    }

    private fun loadAndPreprocessImage(image: Bitmap): TensorBuffer {
        val resizedImage = Bitmap.createScaledBitmap(image, 128, 128, true)
        val byteBuffer = ByteBuffer.allocateDirect(128 * 128 * 1 * 4)  // Single channel grayscale image
        byteBuffer.order(ByteOrder.nativeOrder())

        for (y in 0 until 128) {
            for (x in 0 until 128) {
                val pixel = resizedImage.getPixel(x, y)
                val gray = (Color.red(pixel) * 0.299 + Color.green(pixel) * 0.587 + Color.blue(pixel) * 0.114).toFloat()
                val normalizedGray = gray / 255.0f
                byteBuffer.putFloat(normalizedGray)
            }
        }

        val tensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 128, 128, 1), DataType.FLOAT32)
        tensorBuffer.loadBuffer(byteBuffer)

        return tensorBuffer
    }

    private fun getMaxIndex(arr: FloatArray): Int {
        var index = 0
        var max = arr[0]

        for (i in arr.indices) {
            if (arr[i] > max) {
                max = arr[i]
                index = i
            }
        }
        return index
    }

    @OptIn(ExperimentalGetImage::class)
    private fun bindPreview(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        previewView: androidx.camera.view.PreviewView,
        onImageCaptured: (Bitmap) -> Unit
    ) {
        try {
            // Unbind all use cases before rebinding
            cameraProvider.unbindAll()

            // Set up the preview use case
            val preview = Preview.Builder()
                .setTargetResolution(Size(1280, 720))  // Ensure target resolution matches the expected preview resolution
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            // Set up the image analysis use case
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))  // Match the expected input size
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                val image = imageProxy.image
                if (image != null) {
                    val bitmap = imageProxy.toBitmap()
                    onImageCaptured(bitmap)
                    imageProxy.close()
                }
            }

            // Bind the use cases to the camera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        } catch (exc: Exception) {
            Log.e("CaptureActivity", "Use case binding failed", exc)
        }
    }

    private fun switchCamera(previewView: androidx.camera.view.PreviewView) {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera(previewView)
    }

    private fun startCamera(previewView: androidx.camera.view.PreviewView) {
        closeCamera()
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider, cameraSelector, previewView) { bitmap ->
                processFrame(bitmap)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun closeCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        toast?.cancel()
    }
}
