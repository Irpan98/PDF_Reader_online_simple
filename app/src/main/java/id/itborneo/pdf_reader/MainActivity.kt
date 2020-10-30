package id.itborneo.pdf_reader

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.necistudio.vigerpdf.manage.OnResultListenerV2
import com.necistudio.vigerpdf.utils.ViewPagerZoomHorizontal
import id.itborneo.pdf_reader.VigerCustom.VigerAdapterV2Custom
import id.itborneo.pdf_reader.VigerCustom.VigerPDFv2Custom
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private lateinit var itemDataV2: ArrayList<ByteArray>
    private lateinit var adapterV2: VigerAdapterV2Custom
    private lateinit var vigerPDFV2: VigerPDFv2Custom

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById<ViewPagerZoomHorizontal>(R.id.viewPager)
//        btnFromFile = findViewById<Button>(R.id.btnFile)
//        btnFromNetwork = findViewById(R.id.btnNetwork) as Button
//        btnCancle = findViewById(R.id.btnCancle) as Button
        setupV2()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun setupV2() {
        vigerPDFV2 = VigerPDFv2Custom(this)
//        btnCancle?.setOnClickListener { vigerPDFV2.cancel() }
//
//        btnNetwork.setOnClickListener {
//            itemDataV2.clear()
//            adapterV2.notifyDataSetChanged()
////            fromNetwork("http://www.pdf995.com/samples/pdf.pdf")
//
//
////            fromNetwork("https://sholva.untan.org/ppta/10%20Contoh%20Bab%201%20Skripsi%20BINUS.pdf")
//        }

        itemDataV2 = ArrayList()
        adapterV2 = VigerAdapterV2Custom(applicationContext, itemDataV2)
        viewPager?.adapter = adapterV2

        fromNetwork("http://192.168.43.46/project_baca_suara/m.pdf")

//        btnCancle.setOnClickListener {
//            nextPage()
//
//        }
    }

    private fun nextPage() {
        val position: Int = viewPager?.currentItem ?: 0
        viewPager?.currentItem = 1 + position

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun fromNetwork(endpoint: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        itemDataV2.clear()
        adapterV2.notifyDataSetChanged()
        vigerPDFV2.cancel()
        vigerPDFV2.initFromNetwork(endpoint, object : OnResultListenerV2 {
            override fun resultData(data: ByteArray) {
                Log.e("data", "run")
                itemDataV2.add(data)
            }

            override fun progressData(progress: Int) {
                Log.e("data", "" + progress)
            }

            override fun failed(t: Throwable) {
                Log.e("error", " : " + t.message)
                progressDialog.dismiss()
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {
                progressDialog.dismiss()
                adapterV2.notifyDataSetChanged()
            }
        })

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onDestroy() {
        super.onDestroy()
        vigerPDFV2.cancel()
    }
}
