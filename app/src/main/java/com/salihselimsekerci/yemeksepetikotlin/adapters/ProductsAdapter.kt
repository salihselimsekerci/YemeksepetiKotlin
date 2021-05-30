/*
 * Firebase den çekilen ürün verilerini recylerview üzerinde recylerview için özel olarak oluşturduğumuz layout ile gösteriyoruz.
 */
package com.salihselimsekerci.yemeksepetikotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salihselimsekerci.yemeksepetikotlin.activities.ProductDetailActivity
import com.salihselimsekerci.yemeksepetikotlin.classes.Products
import com.salihselimsekerci.yemeksepetikotlin.R

class ProductsAdapter(private val mContext: Context,private val productsList:List<Products>)
    :RecyclerView.Adapter<ProductsAdapter.MyViewHolder>(){

    inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        var tv_product:TextView
        var iv_product:ImageView
        var cv_category:CardView

        init {
            tv_product = view.findViewById(R.id.tv_product)
            iv_product = view.findViewById(R.id.iv_product)
            cv_category = view.findViewById(R.id.cv_category)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_products_list,parent,false))
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_product.text = productsList[position].name;
        Glide.with(mContext).load(productsList[position].image).into(holder.iv_product)
        holder.cv_category.setOnClickListener(View.OnClickListener { v ->
            val intent = Intent(mContext.applicationContext, ProductDetailActivity::class.java)
            intent.putExtra(mContext.resources.getString(R.string.product),holder.tv_product.text.toString())
            intent.putExtra(mContext.resources.getString(R.string.detail),productsList[position].detail.toString())
            intent.putExtra(mContext.resources.getString(R.string.image),productsList[position].image.toString())
            intent.putExtra(mContext.resources.getString(R.string.price),productsList[position].price.toString())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            mContext.startActivity(intent)
        })
    }
}