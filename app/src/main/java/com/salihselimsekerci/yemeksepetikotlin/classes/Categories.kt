/*
 * Categories sınıfını firebase den gelen verileri kendi tipinde alıp recylerview içerinde göstermek amacıyla adapter a göndermek
 * için oluşturduk.
 */

package com.salihselimsekerci.yemeksepetikotlin.classes
// sınıfımız String türünde name ve image parametreleri alıyor. Default olarak boş değer atadık.
data class Categories(var name : String? = "",var image : String? = "") {
}