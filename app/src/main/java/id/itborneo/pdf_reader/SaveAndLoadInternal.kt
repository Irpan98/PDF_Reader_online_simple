package id.itborneo.pdf_reader

import android.content.Context
import java.io.File
import java.io.FileInputStream

class SaveAndLoadInternal {


    private fun storeFileInInternalStorage(
        context: Context,
        selectedFile: File,
        internalStorageFileName: String
    ) {
        val inputStream = FileInputStream(selectedFile) // 1
        val outputStream =
            context.openFileOutput(internalStorageFileName, Context.MODE_PRIVATE)  // 2
        val buffer = ByteArray(1024)
        inputStream.use {  // 3
            while (true) {
                val byeCount = it.read(buffer)  // 4
                if (byeCount < 0) break
                outputStream.write(buffer, 0, byeCount)  // 5
            }
            outputStream.close()  // 6
        }
    }
}