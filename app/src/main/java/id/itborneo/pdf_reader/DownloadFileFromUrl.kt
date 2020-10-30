@file:Suppress("DEPRECATION")

package id.itborneo.pdf_reader

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


internal class DownloadFileFromURL(private val context: Context) :
    AsyncTask<String?, String?, String>() {
    private lateinit var pd: ProgressDialog
    var pathFolder = ""
    var pathFile = ""
    override fun onPreExecute() {
        super.onPreExecute()
        pd = ProgressDialog(context)
        pd.setTitle("Processing...")
        pd.setMessage("Please wait.")
        pd.setMax(100)
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        pd.setCancelable(true)
        pd.show()
    }

    override fun doInBackground(vararg f_url: String?): String? {
        var count: Int
        try {
            pathFolder = Environment.getExternalStorageDirectory().toString() + "/YourAppDataFolder"
            pathFile = "$pathFolder/play.mp3"
            val futureStudioIconFile = File(pathFolder)
            if (!futureStudioIconFile.exists()) {
                futureStudioIconFile.mkdirs()
            }
            val url = URL(f_url[0])
            val connection: URLConnection = url.openConnection()
            connection.connect()

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            val lengthOfFile: Int = connection.contentLength

            // download the file
            val input: InputStream = BufferedInputStream(url.openStream())
            val output = FileOutputStream(pathFile)
            val data = ByteArray(1024) //anybody know what 1024 means ?
            var total: Long = 0
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (total * 100 / lengthOfFile).toInt())

                // writing data to file
                output.write(data, 0, count)
            }

            // flushing output
            output.flush()

            // closing streams
            output.close()
            input.close()
        } catch (e: Exception) {
            Log.e("Error: ", e.message.toString())
        }
        return pathFile
    }

    override fun onProgressUpdate(vararg values: String?) {
        // setting progress percentage
//        pd.progress = progress[0].toInt()
    }

    override fun onPostExecute(file_url: String) {
        pd.dismiss()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val i = Intent(Intent.ACTION_VIEW)
        i.setDataAndType(Uri.fromFile(File(file_url)), "application/vnd.android.package-archive")
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.applicationContext.startActivity(i)
    }
}