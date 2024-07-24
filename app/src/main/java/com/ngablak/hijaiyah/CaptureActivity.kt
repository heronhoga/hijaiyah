package com.ngablak.hijaiyah

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.compose.ui.window.Dialog
import com.google.common.util.concurrent.ListenableFuture
import com.ngablak.hijaiyah.ml.ModelHijaiyah
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
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
    private var imageCapture: ImageCapture? = null
    private var capturedBitmap: Bitmap? = null
    private var label by mutableStateOf("")
    private var showDialog by mutableStateOf(false)

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
                    CameraPreview(
                        onCapture = { bitmap ->
                            capturedBitmap = cropToGuideBox(bitmap)
                            label = processFrame(capturedBitmap!!)
                            showDialog = true
                        }
                    )
                    if (showDialog) {
                        PredictionDialog(
                            imageBitmap = capturedBitmap!!.asImageBitmap(),
                            label = label,
                            onDismiss = { showDialog = false }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CameraPreview(onCapture: (Bitmap) -> Unit) {
        val context = LocalContext.current
        val previewView = remember { androidx.camera.view.PreviewView(context) }
        val capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(Unit) {
            cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider, cameraSelector, previewView)
            }, ContextCompat.getMainExecutor(context))
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
            Text(text = "Detected: $label", modifier = Modifier.align(Alignment.TopCenter).padding(16.dp))
            capturedBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(200.dp)
                        .background(MaterialTheme.colorScheme.surface)
                )
                Text(text = label, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp))
            }
            Button(onClick = { takePhoto(onCapture) }, modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)) {
                Text(text = "Capture Photo")
            }
            DrawSquareOverlay()
        }
    }

    @Composable
    fun DrawSquareOverlay() {
        val color = MaterialTheme.colorScheme.primary

        Canvas(modifier = Modifier.fillMaxSize()) {
            val boxColor = androidx.compose.ui.graphics.Color(color.toArgb())
            val boxSize = 256.dp.toPx()
            val centerX = size.width / 2
            val centerY = size.height / 2

            drawRect(
                color = boxColor,
                topLeft = androidx.compose.ui.geometry.Offset(centerX - boxSize / 2, centerY - boxSize / 2),
                size = androidx.compose.ui.geometry.Size(boxSize, boxSize),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f)
            )
        }
    }

    @Composable
    fun PredictionDialog(imageBitmap: androidx.compose.ui.graphics.ImageBitmap, label: String, onDismiss: () -> Unit) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Predicted Image",
                        modifier = Modifier
                            .size(200.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = label, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onDismiss() }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }

    private fun processFrame(bitmap: Bitmap): String {
        val tbuffer = loadAndPreprocessImage(bitmap)
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

        showToast("$predictedLabel: $confidence%")
        return "$predictedLabel: $confidence%"
    }

    private fun showToast(message: String) {
        runOnUiThread {
            toast?.cancel()
            toast = Toast.makeText(this@CaptureActivity, message, Toast.LENGTH_SHORT)
            toast?.show()
        }
    }

    private fun cropToGuideBox(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val boxSize = (width * 700) / 1280
        val centerX = width / 2
        val centerY = height / 2
        val left = (centerX - boxSize / 2).toInt()
        val top = (centerY - boxSize / 2).toInt()

        val croppedBitmap = Bitmap.createBitmap(bitmap, left, top, boxSize, boxSize)

        val matrix = Matrix()
        matrix.postRotate(90f)
        val rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.width, croppedBitmap.height, matrix, true)

        return Bitmap.createScaledBitmap(rotatedBitmap, 128, 128, true)
    }

    private fun loadAndPreprocessImage(image: Bitmap): TensorBuffer {
        //Convert image to grayscale
        val grayImage = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayImage)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvas.drawBitmap(image, 0f, 0f, paint)

        //Resize image to 128x128
        val resizedImage = Bitmap.createScaledBitmap(grayImage, 128, 128, true)

        //Create ByteBuffer and set to LITTLE_ENDIAN
        val byteBuffer = ByteBuffer.allocateDirect(1 * 128 * 128 * 4)  // 1 batch, 128x128 image, 1 channel (grayscale), 4 bytes per float
        byteBuffer.order(ByteOrder.nativeOrder())  // Set byte order to LITTLE_ENDIAN

        //Normalize pixel values to [0, 1]
        for (y in 0 until 128) {
            for (x in 0 until 128) {
                val pixel = resizedImage.getPixel(x, y)
                val gray = Color.red(pixel).toFloat() / 255.0f
                byteBuffer.putFloat(gray)
            }
        }

        //Load ByteBuffer into TensorBuffer
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

    private fun bindPreview(
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        previewView: androidx.camera.view.PreviewView
    ) {
        try {
            cameraProvider.unbindAll()

            val preview = Preview.Builder()
                .setTargetResolution(Size(1280, 720))
                .build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            imageCapture = ImageCapture.Builder()
                .setTargetResolution(Size(1280, 720))
                .build()

            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (exc: Exception) {
            Log.e("CaptureActivity", "Use case binding failed", exc)
        }
    }

    private fun takePhoto(onCapture: (Bitmap) -> Unit) {
        val imageCapture = imageCapture ?: return

        val photoFile = File(externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    onCapture(bitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CaptureActivity", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        toast?.cancel()
    }
}
