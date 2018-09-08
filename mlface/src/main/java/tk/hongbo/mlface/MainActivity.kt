package tk.hongbo.mlface

import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import kotlinx.android.synthetic.main.activity_main.*

private val TAG = MainActivity::class.java.simpleName
//private val pics = arrayOf(R.mipmap.t1, R.mipmap.t2, R.mipmap.t3, R.mipmap.t4)
private val pics = arrayOf(
        "http://img1.niutuku.com/hd/1308/44/44-niutuku.com-223472.jpg",
        "https://i7.wenshen520.com/c/42.jpg",
        "https://haxibiao.com/storage/images/600757.jpg",
        "http://t2.hddhhn.com/uploads/tu/201612/98/st94.png",
        "http://pic2.66wz.com/0/10/56/27/10562767_873941.jpg",
        "https://cdn.pixabay.com/photo/2018/07/31/11/14/lion-3574819__340.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTkS_mSub27dsyMOWTJcxLZUcliZQk8ZGsYy9lYZM_fvvm0OXDX",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfVr3nSnJdRMPpQF5XVlpuY9afMGXLfIJ9chwnYSLmxAwjUK3D",
        "http://imgcdn.toutiaoyule.com/20180105/20180105234001645775.jpg",
        "http://images.china.cn/attachement/jpg/site1004/20151202/001ec949fb1b17c8cc1631.jpg",
        "http://www.mamidy.com/zb_users/upload/2018/07/201807071530903734189726.jpg",
        "http://img.daizitouxiang.com/?tag=a&url=mmbizz-zqpicz-zcn/mmbiz_jpg/KIZeN0icHEDlHy8H7m8aYCRJ7JDrxSqq8jeaWria2koH4UMdg79aOibWiarm3RA9kd9MeCdAITlmNcbLkU0eP8e7JQ/640?wx_fmt=jpeg",
        "https://hexiup.2344.com/nqvaoZtoY6GlxJuvYKfWmZlkxaJhpc-bna-SmqKfYq/CXnrx6qqGegayUeX2Khs6biGmYnJ1vym6rnYGkd4SLg3eB2JfZsmx9a8uUipqn0YKvr7aYk5y0s36Pe4ZjiaNsnIOcebeynZdsuZR2iXq6mZlvs5ekjaVuq2Rjb6mwkp2jq6Ob0Z6b.jpg",
        "https://i7.wenshen520.com/s/47.jpg",
        "https://img.laonanren.com/Public/articleimage/20171013/thum_59e05bde1f3c3.jpg",
        "http://www.people.com.cn/mediafile/pic/20151026/65/4108959294922870317.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGBR3lItIt_ugdGdu8smYhX-AdR9fHHo1JEY0ZOXnNAz0R1Bb_",
        "http://img.ylq.com/2015/1224/20151224045459665.jpg",
        "http://www.nvshen.net/uploads/allimg/160105/36-160105125156.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSPL9Whh7v-1HakUWfDIDhU4EvVOg083ypngmytPwC3BA8QfqYJ",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy658btXCzOckXG3Un0zVKzhM8oYSj7vOo2dMHRPdq21GxIW5NCw",
        "http://www.09zz.cn/qingchunmeinv/20170821/rvkk0iybeha.jpg",
        "https://i2.hdslb.com/bfs/archive/17aacbd0f0a5a962be7cedd4d41115836cffe333.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9B30Hhi_Ies23wp0M-pV3R_hWvN8oVVdfTRBbg6crXLhBdYz7Yw",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbOSsEW-JZTlGgEYIR4retiVo2VkPPTxV4PP4Gqr77Nn2RJQJj",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzcxSogMOQZFUavmjNTDPhLwSDd9S6hvBEvdCQWZloBWPX3lBE",
        "https://uc.udn.com.tw/photo/2017/05/28/realtime/3569237.jpg",
        "http://img.piaoliang.com/uploads/allimg/180125/105-1p1251hs5f4.jpg",
        "https://pic.pimg.tw/typhoon1212/1515266479-398887472_n.jpg",
        "http://i2.sinaimg.cn/ent/v/h/2013-01-15/U5910P28T3D3836217F328DT20130115231135.JPG",
        "https://pic.pimg.tw/typhoon1212/1515266479-1195222219_n.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdBGClv0uqv0zQAfX2W85m-fg-V4VyHqiOFoXhV5nlSYdhmltS",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgOiYey8d_11q5EI7beehUfT9cpyVZHLYEar23YAvlH920t5N9",
        "http://www.quazero.com/uploads/allimg/140319/1-140319151223.jpg",
        "http://n2-q.mafengwo.net/s6/M00/FC/94/wKgB4lKVUPOATQEQAAbuJAOYrd489.jpeg?imageView2%2F2%2Fw%2F600%2Fq%2F90%7CimageMogr2%2Fstrip%2Fquality%2F90",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGYF3M0O8hwlXXxpANE6Wunsfo1s5y66koLKODNRM4NBAOUptKmQ",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBZvBn8b85iVxIrA5X0SGt5h75iDWsHJ_yuRLyEXnLcWqhJFQo",
        "https://pic.pimg.tw/aprilradio/1403023917-3126329216_n.jpg?v=1403023918",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ2dXdbo129adcME-jsiC_EHauaWggZIDytgvco_1UAHkv6Or-",
        "http://1.bp.blogspot.com/_0Ewvo6NzCQU/SvEd-nk31PI/AAAAAAAAAXc/-didP2Z5iSQ/s400/031109+101.JPG",
        "https://i2.hdslb.com/bfs/archive/dca0e29c0e3e51c26e7acc166e0c6eb5fe39ed1c.jpg",
        "https://i.ytimg.com/vi/3LSD1Y1w3tU/hqdefault.jpg")

class MainActivity : AppCompatActivity() {

    var sequence = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startFaceImage(v: View) {
        button.isEnabled = false
        if (sequence >= pics.size) {
            sequence = 0
        }
//        val bitmap = BitmapFactory.decodeResource(resources, pics.get(sequence))
        try {
            object : AsyncTask<Any, Any, Any>() {
                override fun doInBackground(vararg params: Any?): Any {
                    return getNetImag(pics.get(sequence))
                }

                override fun onPostExecute(result: Any?) {
                    imageView.setImageBitmap(result as Bitmap?)
                    runRask(result as Bitmap)
                }
            }.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun runRask(bitmap: Bitmap) {
        val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setMinFaceSize(0.15f)
                .setTrackingEnabled(true)
                .build()
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
        detector.detectInImage(image)
                .addOnSuccessListener {
                    doThings(it)
                    sequence++
                    button.isEnabled = true
                }
                .addOnFailureListener(
                        object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                val info = "Error face in image"
                                msg.setText(info)
                                Log.d(TAG, info)
                                button.isEnabled = true
                            }
                        })
    }

    private fun getNetImag(url: String): Bitmap {
        return Glide.with(this).asBitmap().load(url).submit().get()
    }

    private fun doThings(faces: List<FirebaseVisionFace>) {
        msg.setText("识别出人头个数${faces.size}")
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
}
