package tk.hongbo.hbcar

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.things.contrib.driver.button.Button
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import java.io.IOException


private val TAG = MainActivity::class.java.simpleName
private val gpioButtonPinName = "GPIO6_IO14"

class MainActivity : Activity() {
    private lateinit var mButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        startFaceImage()
    }

    private fun startFaceImage() {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.15f)
                .setTrackingEnabled(true)
                .build()
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.t1)
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        val result = detector.detectInImage(image)
                .addOnSuccessListener {
                    doThings(it)
                }
                .addOnFailureListener(
                        object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                Log.d(TAG, "Error face in image")
                            }
                        })
    }

    private fun doThings(faces: List<FirebaseVisionFace>) {
        for (face in faces) {
            val bounds = face.getBoundingBox()
            val rotY = face.getHeadEulerAngleY()  // Head is rotated to the right rotY degrees
            val rotZ = face.getHeadEulerAngleZ()  // Head is tilted sideways rotZ degrees

            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
            // nose available):
            val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)
            if (leftEar != null) {
                val leftEarPos = leftEar!!.getPosition()
            }

            // If classification was enabled:
            if (face.getSmilingProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val smileProb = face.getSmilingProbability()
            }
            if (face.getRightEyeOpenProbability() != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                val rightEyeOpenProb = face.getRightEyeOpenProbability()
            }

            // If face tracking was enabled:
            if (face.getTrackingId() != FirebaseVisionFace.INVALID_ID) {
                val id = face.getTrackingId()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyButton()
    }

    private fun setupButton() {
        try {
            mButton = Button(gpioButtonPinName,
                    // high signal indicates the button is pressed
                    // use with a pull-down resistor
                    Button.LogicState.PRESSED_WHEN_HIGH
            )
            mButton.setOnButtonEventListener(object : Button.OnButtonEventListener {
                override fun onButtonEvent(button: Button, pressed: Boolean) {
                    // do something awesome
                }
            })
        } catch (e: IOException) {
            // couldn't configure the button...
        }

    }

    private fun destroyButton() {
        Log.i(TAG, "Closing button")
        try {
            mButton.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing button", e)
        }
    }

}
