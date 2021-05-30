package com.salihselimsekerci.yemeksepetikotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.salihselimsekerci.yemeksepetikotlin.R
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val productName:String = intent.getStringExtra(resources.getString(R.string.product)).toString()
        val image:String = intent.getStringExtra(resources.getString(R.string.image)).toString()
        val detail:String = intent.getStringExtra(resources.getString(R.string.detail)).toString()
        val price_s:String = intent.getStringExtra("price").toString()
        var price = price_s.toDouble()
        var pcs:Int = 1;

        Glide.with(applicationContext).load(image).into(iv_productDetail)
        tv_productTitle.text = productName
        tv_productDetail.text = detail
        tv_pcs.text = "${pcs} ${resources.getString(R.string.pcs)}"
        tv_price.text = "${pcs * price} TL"
        tv_pricePcs.text = "${pcs * price} ${resources.getString(R.string.tl)}"

        iv_add.setOnClickListener(View.OnClickListener { v ->
            pcs ++
            tv_pcs.text = "${pcs} ${resources.getString(R.string.pcs)}"
            tv_price.text = "${pcs * price} ${resources.getString(R.string.tl)}"
        })
        iv_remove.setOnClickListener(View.OnClickListener { v ->
            if (pcs > 1){
                pcs --
                tv_pcs.text = "${pcs} ${resources.getString(R.string.pcs)}"
                tv_price.text = "${pcs * price} ${resources.getString(R.string.tl)}"
            }
        })
    }
}