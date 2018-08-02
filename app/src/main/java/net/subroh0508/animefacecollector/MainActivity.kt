package net.subroh0508.animefacecollector

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import net.subroh0508.animefacecollector.util.FaceDetector
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    val detector by lazy { FaceDetector(setupCascadeFile())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        sample_text.text = stringFromJNI()

        detect_button.setOnClickListener{ _ ->
            liner_layout.addView(ImageView(this).apply{
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.face_detect_test)
                val bitmapDetected = detector.detect(bitmap)
                if (bitmap != null) setImageBitmap(bitmapDetected)
            })
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    private fun setupCascadeFile(): File? {
        val cascadeDir = getDir("cascade", Context.MODE_PRIVATE)
        var cascadeFile: File? = null
        var `is`: InputStream? = null
        var os: FileOutputStream? = null
        try {
            cascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
            if (!cascadeFile!!.exists()) {
                `is` = getResources().openRawResource(R.raw.lbpcascade_animeface)
                os = FileOutputStream(cascadeFile)
                val buffer = ByteArray(4096)
                var readLen = 0
                do {
                    readLen = `is`!!.read(buffer)
                    if (readLen == -1){
                        break
                    }
                    os!!.write(buffer, 0, readLen)
                } while(true)
            }
        } catch (e: IOException) {
            return null
        } finally {
            if (`is` != null) {
                try {
                    `is`!!.close()
                } catch (e: Exception) {
                    // do nothing
                }

            }
            if (os != null) {
                try {
                    os!!.close()
                } catch (e: Exception) {
                    // do nothing
                }

            }
        }
        return cascadeFile
    }
}
