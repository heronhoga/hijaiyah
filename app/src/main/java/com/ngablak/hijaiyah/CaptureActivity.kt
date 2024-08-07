package com.ngablak.hijaiyah

import android.Manifest
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.widget.Toast
import android.media.MediaPlayer
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.ui.window.Dialog
import com.google.common.util.concurrent.ListenableFuture
import com.ngablak.hijaiyah.ml.ModelHijaiyah
import com.ngablak.hijaiyah.ui.theme.HijaiyahTheme
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
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
    private var mediaPlayer: MediaPlayer? = null
    private var isHorizontalDefault = false

    private val numberToLabel = mapOf(
        0 to "alif",
        1 to "ra",
        2 to "zay",
        3 to "sin",
        4 to "shin",
        5 to "sad",
        6 to "dad",
        7 to "to",
        8 to "zha",
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
        25 to "kha",
        26 to "dal",
        27 to "dhal"
    )

    private val CAMERA_PERMISSION_REQUEST_CODE = 100

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
                    if (checkCameraPermission()) {
                        startCamera()
                    } else {
                        requestCameraPermission()
                    }
                }
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(this, "Camera permission is needed to use this app", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startCamera()
                } else {
                    Toast.makeText(this, "Permission denied to use camera", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
            }
        }
    }

    private fun startCamera() {
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
                isHorizontalDefault = checkIfCameraIsHorizontal(context)
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
        playSound(predictedLabel)
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

        val boxSize = (width * 400) / 1280
        val centerX = width / 2
        val centerY = height / 2
        val left = (centerX - boxSize / 2).toInt()
        val top = (centerY - boxSize / 2).toInt()

        val croppedBitmap = Bitmap.createBitmap(bitmap, left, top, boxSize, boxSize)

        return if (isHorizontalDefault) {
            rotateBitmap(croppedBitmap, 180f)
        } else {
            croppedBitmap
        }
    }

    private fun loadAndPreprocessImage(image: Bitmap): TensorBuffer {
        // Convert image to grayscale
        val grayImage = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayImage)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = filter
        canvas.drawBitmap(image, 0f, 0f, paint)

        // Resize image to 128x128
        val resizedImage = Bitmap.createScaledBitmap(grayImage, 128, 128, true)

        // Create ByteBuffer and set to LITTLE_ENDIAN
        val byteBuffer = ByteBuffer.allocateDirect(1 * 128 * 128 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        // Normalize pixel values to [0, 1]
        for (y in 0 until 128) {
            for (x in 0 until 128) {
                val pixel = resizedImage.getPixel(x, y)
                val gray = Color.red(pixel).toFloat() / 255.0f
                byteBuffer.putFloat(gray)
            }
        }

        // Load ByteBuffer into TensorBuffer
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

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val rotationDegrees = image.imageInfo.rotationDegrees
                    val bitmap = imageProxyToBitmap(image)
                    val adjustedBitmap = if (isHorizontalDefault) {
                        rotateBitmap(bitmap, -90f)
                    } else {
                        bitmap
                    }
                    onCapture(adjustedBitmap)
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CaptureActivity", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun checkIfCameraIsHorizontal(context: Context): Boolean {
        val cameraManager = context.getSystemService(CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics[CameraCharacteristics.LENS_FACING]
            facing != null && facing == CameraCharacteristics.LENS_FACING_BACK
        } ?: return false

        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val orientation = characteristics[CameraCharacteristics.SENSOR_ORIENTATION] ?: 0
        return orientation == 90 || orientation == 270
    }

    private fun playSound(label: String) {
        val soundResId = when (label.split(":")[0].trim()) {
            "alif" -> R.raw.alif
            "ra" -> R.raw.ra
            "zay" -> R.raw.zay
            "sin" -> R.raw.sin
            "shin" -> R.raw.shin
            "sad" -> R.raw.sad
            "dad" -> R.raw.dad
            "to" -> R.raw.to
            "zha" -> R.raw.zha
            "ain" -> R.raw.ain
            "ghain" -> R.raw.ghain
            "ba" -> R.raw.ba
            "fa" -> R.raw.fa
            "qaf" -> R.raw.qaf
            "kaf" -> R.raw.kaf
            "lam" -> R.raw.lam
            "mim" -> R.raw.mim
            "nun" -> R.raw.nun
            "hha" -> R.raw.hha
            "waw" -> R.raw.waw
            "ya" -> R.raw.ya
            "ta" -> R.raw.ta
            "tsa" -> R.raw.tsa
            "jim" -> R.raw.jim
            "ha" -> R.raw.ha
            "kha" -> R.raw.kha
            "dal" -> R.raw.dal
            "dhal" -> R.raw.dhal
            else -> return
        }
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        toast?.cancel()
        mediaPlayer?.release()
    }
}
