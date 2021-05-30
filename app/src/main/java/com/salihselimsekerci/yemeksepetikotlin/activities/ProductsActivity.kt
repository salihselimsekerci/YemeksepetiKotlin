package com.salihselimsekerci.yemeksepetikotlin.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.salihselimsekerci.yemeksepetikotlin.adapters.ProductsAdapter
import com.salihselimsekerci.yemeksepetikotlin.classes.Products
import com.salihselimsekerci.yemeksepetikotlin.classes.Utils
import com.salihselimsekerci.yemeksepetikotlin.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_products.*
import java.util.*
import kotlin.collections.ArrayList

class ProductsActivity : AppCompatActivity() {

    private lateinit var utils:Utils
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var mProductsList: ArrayList<Products>
    private lateinit var mAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        init()

        ib_1.setOnClickListener(View.OnClickListener { v ->
            // layoutmanager ı dolayısıyla Recylerview ile gelen verilerin sıralanış şekli değiştirilir.
            rv_products.layoutManager = LinearLayoutManager(this)
        })

        ib_2.setOnClickListener(View.OnClickListener { v ->
            // layoutmanager ı dolayısıyla Recylerview ile gelen verilerin sıralanış şekli değiştirilir.
            rv_products.layoutManager = GridLayoutManager(this,2)
        })

        // Database den çekilen kategorilerin sıralanış şekli dialogtan seçilen sıralama şekline göre değiştirilir.
        btn_sortCategories.setOnClickListener(View.OnClickListener { v ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.builderSetTitle))
            builder.setMessage(resources.getString(R.string.builderSetMessage))
            builder.setCancelable(false)
            builder.setPositiveButton(resources.getString(R.string.builderSetPositiveButton)) { dialog: DialogInterface?, which: Int ->
                btn_sortCategories.setText(resources.getString(R.string.builderAscendingByName))
                sortListAtoZ()
                mAdapter = ProductsAdapter(applicationContext,mProductsList)
                rv_products.adapter = mAdapter
            }
            builder.setNegativeButton(resources.getString(R.string.builderSetNegativeButton)){dialog, which ->
                btn_sortCategories.setText(resources.getString(R.string.builderDescendingByName))
                sortListZtoA()
                mAdapter = ProductsAdapter(applicationContext,mProductsList)
                rv_products.adapter = mAdapter
            }

            builder.show()
        })

    }

    // Collections.sort ile gelen dataların name parametrelerine göre kıyaslanarak sıralama işlemi yapılır. datalar kendinden
    // sonra gelen data ile karşılaştırılır böylece A da Z ye isme göre artan bir liste oluşturulmuş olur
    private fun sortListAtoZ() {
        Collections.sort(mProductsList, kotlin.Comparator { o1, o2 ->
            return@Comparator o2.name?.let { o1.name?.compareTo(it) }!!
        })
    }
    // Collections.sort ile gelen dataların name parametrelerine göre kıyaslanarak sıralama işlemi yapılır. datalar kendinden
    // önce gelen data ile karşılaştırılır böylece Z den A ya isme göre azalan bir liste oluşturulmuş olur
    private fun sortListZtoA() {
        Collections.sort(mProductsList, kotlin.Comparator { o1, o2 ->
            return@Comparator o1.name?.let { o2.name?.compareTo(it) }!!
        })
    }
// recylerview ayarları burada yapılıyor
    private fun recyclerViewSettings(){
    // adapter a gönderilecek arraylist initialize ediliyor
        mProductsList = ArrayList<Products>()
    // recylerview ın tam oturması için ekrana setHasFixedSize true gönderilir
        rv_products.setHasFixedSize(true)
    // recylerview ın default açılış halini linearlayoutmanager olarak belirledik.
        rv_products.layoutManager = LinearLayoutManager(this)
    // ProductsAdapter sınıfımızdan mAdapter isminde bir nesne oluşturup bu nesneyi recylerview ımızın adapterı olarak set ettik
        mAdapter = ProductsAdapter(applicationContext,mProductsList)
        rv_products.adapter = mAdapter
    }
// firebase ayarları
    private fun firebaseInit(categoryName:String?=""){
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("${resources.getString(R.string.products)}/$categoryName")
    }
// Firebase den data çekilme işlemleri
    private fun getDatas(){
        val progressDialog = ProgressDialog(this@ProductsActivity)
    // progressDialog ayarları
        utils.setProgressDialog(progressDialog,resources.getString(R.string.productsProgressDialogTitle),resources.getString(R.string.productsProgressDialogMessage))
    // referance üzerinden addValueEventListener methodu ile verileri alıyoruz. 2 adet override methodumuz var onCancelled ve onDataChange
        reference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
// Datasnapshot sınıfında bir snapshot döndürüyor.
            override fun onDataChange(snapshot: DataSnapshot) {
    // eğer snapshot içi dolu ise
                if (snapshot.exists()){
                    // snapshot içindeki verileri teker teker alıyoruz
                    for (p in snapshot.children){
                        // verileri alıp product değişkenine atıyoruz
                        val product = p.getValue(Products::class.java)
                        // product değişkeni null değil ise NullPointerException hatası almamak için
                        if (product != null) {
                            // arraylist imize product nesnemizi ekliyoruz
                            mProductsList.add(product)
                        }
                    }
                }
//tüm veri çekme işlemleri tamamlandığında progrssDialog kapatılır
                progressDialog.dismiss()
            }

        });
    }

    // init işlemleri
    private fun init(){
        // bir önceki aktiviteden category ismi alınır.
        val categoryName:String = intent.getStringExtra("category").toString()
        utils = Utils()
        firebaseInit(categoryName)
        getDatas()
        recyclerViewSettings()

    }
}