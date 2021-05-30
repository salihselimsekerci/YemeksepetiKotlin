/*
 * Progress Dialog gösterme işlemini birden çok aktivitede kullandığımız için Utils isminde bir sınıf oluşturduk.
 * Sürekli kod tekrarına düşmemek adına tek bir yerden ulaşmamıza yarıyor. Clan kod prensiplerine de uygundur.
 * Sınıfımız Utilinterface i implement ederek interface içerisinde tanımlanan methodları override etmektedir.
 */
package com.salihselimsekerci.yemeksepetikotlin.classes

import android.app.ProgressDialog
import com.salihselimsekerci.yemeksepetikotlin.interfaces.UtilInterface

class Utils : UtilInterface {
// 3 adet parametre almaktadır. ProgressDialog sınıfından bir progressDialog nesnesi, String türünde title ve mesaj.
    override fun setProgressDialog(progressDialog: ProgressDialog, title: String, message: String) {
    // progressDialog title'ı set edildi.
        progressDialog.setTitle(title)
    // progressDialog mesaj set edildi.
        progressDialog.setMessage(message)
    // progressDialog gösterildi.
        progressDialog.show()
    }
}