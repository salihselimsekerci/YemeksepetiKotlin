/*
 * Firebase e kayıt işlemlerinin yapıldığı aktivite.
 */
package com.salihselimsekerci.yemeksepetikotlin.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.salihselimsekerci.yemeksepetikotlin.classes.Durations
import com.salihselimsekerci.yemeksepetikotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
// kayıt işlemi için gerekli değişkenlerin tanımlandığı alan
    private lateinit var email: String
    private lateinit var psw1: String
    private lateinit var psw2: String
    private lateinit var nameSurname: String
    private lateinit var adress: String

// Firebase bağlantı ve kayıt işlemleri için değişkenlerin tanımlanması
    private lateinit var auth: FirebaseAuth
    private lateinit var mRef : DatabaseReference
    private lateinit var firebaseDatabase: FirebaseDatabase

    private var psw1Visible:Boolean = false
    private var psw2Visible:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
// firebase initialize
        firebaseInits()
// kayıt ol butonuna tıklandığında
        btn_register.setOnClickListener { v ->
            // kayıt işlemleri
            register()
        }
// hesabım var textview ına tıklandığında login Activity e git
        tv_login.setOnClickListener {v ->
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
// göz ikonuna tıklandığında girilen şifreyi görünür kıl
        iv_pswEye.setOnClickListener(View.OnClickListener { v ->
            if (psw1Visible){
                et_password1.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                psw1Visible = false
            }else {
                et_password1.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                psw1Visible = true
            }
        })

        iv_pswEye2.setOnClickListener(View.OnClickListener { v ->
            if (psw2Visible){
                et_password2.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                psw2Visible = false
            }else {
                et_password2.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                psw2Visible = true
            }
        })
    }
// firebase initialize ayarları
    private fun firebaseInits(){
        firebaseDatabase = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
    }
// kullanıcı kayıt işlemlerini yapılması email ve şifre geçiyoruz. Bize bir listener üzerinden task nesnesi döndürüyor
    private fun registerUser (email:String,password:String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
            // eğer kayıt işlemi başarılı ise
            if (task.isSuccessful) {
                // şu anki kullanıcıyı auth.currentUser ile al
                val user = auth.currentUser
                // bu kullanıcının uuid değerini al
                val uid = user!!.uid
                // Firebase Database de referanse olarak users içerisinde kişiye özgü uuid ile bir node oluştur
                mRef = firebaseDatabase.getReference("${resources.getString(R.string.users)}/${uid}")
                // bu node a aşağıdaki değerleri string olarak yaz
                mRef.child(resources.getString(R.string.uid)).setValue(uid)
                mRef.child(resources.getString(R.string.firebaseEmail)).setValue(et_mail.text.toString())
                mRef.child(resources.getString(R.string.firebasePassword)).setValue(et_password1.text.toString())
                mRef.child(resources.getString(R.string.firebaseNameSurname)).setValue(et_nameSurname.text.toString())
                mRef.child(resources.getString(R.string.firebaseAdress)).setValue(et_adress.text.toString())
                // yazma işlemi bittikten sonra yeni aktiviteye geçiş yap
                startActivity(Intent(this, LoginActivity::class.java))
                // kayıt işleminin başarılı olduğuna dair toast mesajını kullanıcıya göster
                toast(resources.getString(R.string.toastSuccesfullregister),Durations.short)
            }else {
                // kayıt işlemi başarısız ise kullanıcıyı toast mesajı ile bilgilendir.
                toast(resources.getString(R.string.toastFail),Durations.short)
            }
            })

    }
//register fonksiyonu ile girilen değerleri kontrol et eğer doğru değerler girilmiş ise registerUser() fonk çağırarak kayıt işlemini başlat
    private fun register() {
        email = et_mail.text.toString()
        psw1 = et_password1.text.toString()
        psw2 = et_password2.text.toString()
        nameSurname = et_nameSurname.text.toString()
        adress = et_adress.text.toString()
        if (!email.isEmpty() && !psw1.isEmpty() && !psw2.isEmpty() && !nameSurname.isEmpty() && !adress.isEmpty()) {
            if (psw1.equals(psw2)){
                registerUser(email,psw1)
            }else {
                toast(resources.getString(R.string.toastPasswordsnotmatch),Durations.short)
            }
        }else {
            toast(resources.getString(R.string.toastFillallfields),Durations.short)
        }
    }
// toast mesajı methodu
    private fun toast(message:String,duration:Int){
        Toast.makeText(applicationContext,message,duration).show()
    }

}