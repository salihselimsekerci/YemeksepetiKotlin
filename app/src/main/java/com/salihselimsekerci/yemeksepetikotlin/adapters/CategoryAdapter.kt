package com.salihselimsekerci.yemeksepetikotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salihselimsekerci.yemeksepetikotlin.classes.Categories
import com.salihselimsekerci.yemeksepetikotlin.activities.ProductsActivity
import com.salihselimsekerci.yemeksepetikotlin.R
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.rv_category.view.*
import java.util.*
import kotlin.collections.ArrayList
// Adapter sınıfımız parametre olarak context ve liste almaktadır. RecylerView ve Filterable interface'ini implemente etmektedir.
class CategoryAdapter(private val mContext: Context,private val categories: List<Categories>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() , Filterable{
//Categories türünden bir arraylist tanımlıyoruz construkter ile ana aktiviteden gelen listeyi bu liste ile karşıalaycağız.
    var categoryFilterList = ArrayList<Categories>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // rv_category xml dosyasını inflate ediyoruz.
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_category,parent,false)

        // Categories sınıfından categoryFilterList tanımladık
        categoryFilterList = categories as ArrayList<Categories>
        // geriye viewholder döndürüyoruz.
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // burada recylerview layout dosyamızdaki kompanaentlere ulaşıp adapterımızdaki verileri yazıyoruz bunu holder nesnesi ile yapıyoruz
        holder.cv_category.setOnClickListener(View.OnClickListener { v: View? ->
            // bir sonraki aktiviteye veri göndermek ve o aktiviteye geçiş yapmak için intent kullandık
            val intent = Intent(mContext.applicationContext, ProductsActivity::class.java)
            intent.putExtra(mContext.resources.getString(R.string.category),holder.tv_categoryName.text.toString())
            // NEW_TASK flagını intente ekledik
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(intent)
        })
        // recylerview için özel hazırladığımız layout dosyamızdaki kompanente arraylistimizdeki verileri sırasıyla yazıyoruz
        holder.itemView.tv_categoryName.text = categoryFilterList[position].name.toString()
        // Burada Glide ile firebase den gelen image dosyalarını recylerview layout dosyamızdaki ImageView içerisine aktarıp kullanıcıya gösteriyoruz
        Glide.with(mContext).load(categoryFilterList[position].image.toString()).into(holder.itemView.iv_category)
    }


    override fun getItemCount(): Int {
        // adapter tarafınan ele alınan data setlerinin sayısı adedince dödürüyor
        return categoryFilterList.size
    }

//Recylerview layout dosyamızdaki komponentlere adapterımızdan ulaşıyoruz
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val cv_category: MaterialCardView = itemView.cv_category
        val tv_categoryName: TextView = itemView.tv_categoryName
    }

//Aşağıdaki iki fonksiyon item ların duplicate edilmesini engellemektedir.
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    // Filtreleme işlemlerini yapıldığı fonksiyonumuz
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                // searchbar a yazılan texti string formatında alıyor
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    categoryFilterList = categories as ArrayList<Categories>
                } else {
                    val resultList = ArrayList<Categories>()
                    for (row in categories) {
                        // filtreleme işlemleri
                        if (row.name?.toLowerCase(Locale.ROOT)?.contains(charSearch.toLowerCase(Locale.ROOT))!!) {
                            resultList.add(row)
                        }
                    }
                    categoryFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = categoryFilterList
                // filtreleme bitiminde çıkan sonucu döndür.
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                categoryFilterList = results?.values as ArrayList<Categories>
                notifyDataSetChanged()
            }
        }
    }
}