package id.itborneo.pdf_reader.VigerCustom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import com.necistudio.vigerpdf.utils.PhotoViewAttacher
import id.itborneo.pdf_reader.R
import java.util.*


class VigerAdapterV2Custom(private val context: Context, private val itemList: ArrayList<ByteArray>) :
    PagerAdapter() {

    private val mLayoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var pageBitmap: Bitmap? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int = itemList.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.pdf_item_custom, container, false)

        // convert byteArray
        val bytes = itemList[position]
        pageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        val imageView = itemView.findViewById<View>(R.id.imgData) as ImageView
        imageView.setImageBitmap(pageBitmap)

        //Set page number and colors if isPageNumberEnabled = true
        if (isPageNumberEnabled) {
            val pageNumber = itemView.findViewById<View>(R.id.pageNumber) as TextView
            pageNumber.visibility = View.VISIBLE
            pageNumber.text = (position + 1).toString() + "/" + itemList.size.toString()
            if (pageNumberColor != 0) pageNumber.setTextColor(
                ContextCompat.getColor(
                    context, pageNumberColor
                )
            )
            if (pageNumberBgColor != 0) {
                val bg_drawable = pageNumber.background.current as GradientDrawable
                bg_drawable.setColor(
                    ContextCompat.getColor(
                        context,
                        pageNumberBgColor
                    )
                )
            }
        }
        container.addView(itemView)
        PhotoViewAttacher(imageView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout?)
    }

    companion object {
        //Variables used to set page number and colors
        private var isPageNumberEnabled = true
        private var pageNumberColor = 0
        private var pageNumberBgColor = 0

        fun setPageNumberEnabled(enabled: Boolean) {
            isPageNumberEnabled = enabled
        }

        fun setPageNumberColors(pageNumberColor: Int) {
            this.pageNumberColor = pageNumberColor
        }

        fun setPageNumberBgColor(pageNumberBgColor: Int) {
            this.pageNumberBgColor = pageNumberBgColor
        }
    }

}