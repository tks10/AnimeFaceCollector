package net.subroh0508.animefacecollector.util

import android.util.Log
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR
import org.opencv.imgcodecs.Imgcodecs.imread
import org.opencv.imgproc.Imgproc.*
import org.opencv.objdetect.CascadeClassifier

class FaceDetector(){
    val CASCADE_FILE = "./lbpcascade_animeface.xml"
    val detector by lazy { CascadeClassifier(CASCADE_FILE) }

    init {
        if (!OpenCVLoader.initDebug()){
            Log.e("OpenCV", "Couldn't open OpenCV.")
        }
    }

    fun detect(fileName: String): IntArray{
        val image = imread(fileName, IMREAD_COLOR)
        var gray: Mat? = null
        cvtColor(image, gray, COLOR_BGR2GRAY)
        equalizeHist(gray, gray)

        var face = MatOfRect()
        detector.detectMultiScale(gray, face, 1.1, 5, 0,
                                  Size(75.0, 75.0), Size(2048.0, 2048.0))

        return face.toArray() as IntArray
    }
}
