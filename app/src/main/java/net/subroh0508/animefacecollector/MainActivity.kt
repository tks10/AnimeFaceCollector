package net.subroh0508.animefacecollector

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.subroh0508.animefacecollector.util.FaceDetector
import java.io.*

class MainActivity : AppCompatActivity() {
    private val detector by lazy { FaceDetector(setupCascadeFile())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        textView.text = stringFromJNI()

        detect.setOnClickListener{ _ ->
            val detectedBitmap = BitmapFactory.decodeResource(resources, R.drawable.face_detect_test).let {
                detector.detect(it)
            } ?: return@setOnClickListener

            imageView.setImageBitmap(detectedBitmap)
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

    private fun setupCascadeFile(xmlFileName: String = "lbpcascade_frontalface.xml"): File? {
        val cascadeFile = File(
                getDir("cascade", Context.MODE_PRIVATE),
                xmlFileName
        ).takeIf(File::exists) ?: throw FileNotFoundException("File: $xmlFileName not found")

        val inputStream: InputStream = resources.openRawResource(R.raw.lbpcascade_animeface)
        val outputStream = FileOutputStream(cascadeFile)

        return try {
            val buffer = ByteArray(4096)
            do {
                val readLen = inputStream.read(buffer)
                if (readLen == -1){
                    break
                }
                outputStream.write(buffer, 0, readLen)
            } while(true)

            inputStream.close()
            outputStream.close()

            cascadeFile
        } catch (e: IOException) {
            null
        }
    }
}
