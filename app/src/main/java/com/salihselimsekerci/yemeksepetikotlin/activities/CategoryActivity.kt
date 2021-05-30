/*
 * Firebase den çektiğimiz kategorileri Recylerview üzerinde gösteriyoruz. CategorySearch içerisine girdiğimiz her bir kelimeye
 * göre kategorinin ismi baz alınarak listeleme işlemini gerçekleştiriyoruz. Geri Tuşuna basıldığında ise bizi bir dialog ekranı
 * karşılıyor Çıkış yapmak isteyip istemediğimizi soruyor. Hayır dersek dialog kapanıyor Evet dersek uygulama kapanıyor.
 */
package com.salihselimsekerci.yemeksepetikotlin.activities

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salihselimsekerci.yemeksepetikotlin.adapters.CategoryAdapter
import com.salihselimsekerci.yemeksepetikotlin.classes.Categories
import com.salihselimsekerci.yemeksepetikotlin.classes.Utils
import com.salihselimsekerci.yemeksepetikotlin.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : AppCompatActivity() {
    // Firebasedatabase, DatabaseReference sınıflarından birer nesne üretmek için değişken tanımlamalarını yapıyoruz.
    lateinit var database : FirebaseDatabase
    lateinit var ref : DatabaseReference
    // Categories sınfından bir Arraylist oluşturduk Database den çektiğimiz verileri bu Arraylist e ekleyerek Adapterımızın constructer
    // ına göndereceğiz
    lateinit var categoryList:ArrayList<Categories>
    // Progressdialog kullanmak için Utils sınıfımızdam utils nesnemizi oluşturduk.
    lateinit var utils:Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
// init ayarları
        init()
    }
// Geri tuşuna basıldığında yapılması gerekenleri handle etmek için onBackPressed fonk override ettik.
    override fun onBackPressed() {
// AletDialog oluşturma ve ayarları
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.builderExit))
        builder.setMessage(resources.getString(R.string.builderAskExit))
        builder.setCancelable(false)
    // Evet tıklanırsa
        builder.setPositiveButton(resources.getString(R.string.YES)){ dialog: DialogInterface?, which: Int ->
            // çıkış yap
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
// hayır tıklanırsa
        builder.setNegativeButton(resources.getString(R.string.NO)){ dialog: DialogInterface?, which: Int ->
            // dialog nesnesi null deilse NullPointerException hatası almamak için
            if (dialog != null) {
                // dialogu kapat
                dialog.dismiss()
            }
        }
// dialog u göster
        builder.show()
    }

    private fun rv_settings(categories:ArrayList<Categories>){
        // gridLayoutManager ı initialize et
        GridLayoutManager(
            this, // context
            2, // span count
            RecyclerView.VERTICAL, // orientation olarak vertical
            false // reverse layout
        ).apply {
            // layout manager ı recyler view için özelleştirdik.
            rv_category.layoutManager = this
        }
// category_search içine girilen text e göre filtreleme işlemlerini yapıp adapter a gönderiyoruz.
        category_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                (rv_category.adapter as CategoryAdapter).filter.filter(text)
                return false
            }

        })
/* rv_category kompanentimizin adapter nesnesine kendi oluşturduğumuz CategoryAdapter sınıfımızı ve database den çektiğimiz kategori
 verileri ile içini doldurduğumuz categories listemizi gönderiyoruz.*/
        rv_category.adapter = CategoryAdapter(applicationContext,categories)
    }
// Firebasedatabase den kategori bilgilerini çekiyoruz
    private fun getCategoryInfos(){
    // progress dialog u ayarla ve başlat
        val progressDialog = ProgressDialog(this@CategoryActivity)
        utils.setProgressDialog(progressDialog,resources.getString(R.string.progressDialogCategoryTitle),resources.getString(R.string.progressDialogCategoryMessage))
    // database referance ımıza göre verileri listener ile çek.
        ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                progressDialog.dismiss()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                // Verileri DataSnopshot sınfından snapshot nesnesi olarak al aldığımız nesnenin içi dolu ise veri geldi ise
                if (snapshot.exists()){
                    // bu snapshot nesnemizin her bir alt üyesini tek tek dolaş
                    for (p in snapshot.children){
                        // her bir değeri Categories sınıfımızdaki değişkenlere göre yani bu formatta al ve category nesnesine at
                        val category = p.getValue(Categories::class.java)
                        // category nesnemiz null değil ise
                        if (category != null) {
                            // categoryList e category nesnesini ekle
                            categoryList.add(category)
                            rv_category.adapter = CategoryAdapter(applicationContext,categoryList)
                        }
                    }
                }
                // tüm veri çekme işlemleri tamamlandığında progressDialog u kapat
                progressDialog.dismiss()
            }

        })
    }
    // init ayarları
    private fun init(){
        utils = Utils() // uitls initialize
        database = FirebaseDatabase.getInstance() // database initialize
        ref = database.getReference(resources.getString(R.string.refCategories)) // referance initialize
        categoryList = ArrayList() // arraylist initialize

        getCategoryInfos()

        rv_settings(categoryList)
    }
}