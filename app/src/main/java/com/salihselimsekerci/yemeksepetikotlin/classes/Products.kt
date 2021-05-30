/*
 * Products sınıfını firebase den gelen verileri kendi tipinde alıp recylerview içerinde göstermek amacıyla adapter a göndermek
 * için oluşturduk.
 */
package com.salihselimsekerci.yemeksepetikotlin.classes
// sınıfımız String türünde name,image,detail ve price parametreleri alıyor. Default olarak boş değer atadık.
data class Products(var name : String?="",var image : String?="",var detail:String?="",var price:String?="") {
}