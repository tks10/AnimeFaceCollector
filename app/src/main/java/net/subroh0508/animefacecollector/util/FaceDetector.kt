package net.subroh0508.animefacecollector.util

import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.*
import org.opencv.objdetect.CascadeClassifier
import java.io.File


class FaceDetector(cascadeFile: File?){
    val CASCADE_FILE = "lbpcascade_animeface.xml"
    val detector by lazy { CascadeClassifier(cascadeFile?.absolutePath) }
    val MIN_SIZE = Size(125.0, 125.0)

    init {
        if (!OpenCVLoader.initDebug()){
            Log.e("OpenCV", "Couldn't open OpenCV.")
        }
    }

    fun detect(image: Bitmap): Bitmap?{
        var mat = Mat()
        var originalMat = Mat()

        Utils.bitmapToMat(image, originalMat)
        Utils.bitmapToMat(image, mat)
        cvtColor(mat, mat, COLOR_BGR2GRAY)
        equalizeHist(mat, mat)

        var faces = MatOfRect()
        detector.detectMultiScale(mat, faces, 1.1, 5, 0,
                                  MIN_SIZE, Size(2048.0, 2048.0))

        for (face in faces.toArray()){
            Imgproc.rectangle(originalMat, face.tl(), face.br(), Scalar(0.0, 0.0, 255.0), 10)
        }

        var bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.RGB_565)
        Utils.matToBitmap(originalMat, bitmap)
        return bitmap
    }
}
