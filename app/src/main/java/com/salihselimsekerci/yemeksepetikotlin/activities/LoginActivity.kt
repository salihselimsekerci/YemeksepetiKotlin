/*
 * Firebase ayarları yapılır. Burada FirebaseAuth sınıfından yararlanarak email ve şifre ile Login işlemini gerçekleştiriyoruz.
 */
package com.salihselimsekerci.yemeksepetikotlin.activities
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.salihselimsekerci.yemeksepetikotlin.classes.Durations
import com.salihselimsekerci.yemeksepetikotlin.classes.Utils
import com.salihselimsekerci.yemeksepetikotlin.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    // Utils sınıfından bir utils değişkeni
    private lateinit var utils:Utils
    // FirebaseAuth sınıfından bir auth değişkeni
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // utils initialize
        utils = Utils()
        // firebase initialize
        firebaseInits()

        btn_login.setOnClickListener { v ->
            // Giriş butonuna tıklanırsa login() fonksiyonunu çağır. login() fonksiyonu olarak burada yazmamızın sebebi
            //onCreate içerisini temiz ve düzenli tutmanın CelanCode prensiplerinin bir parçası olması
            login()
        }

        btn_signup.setOnClickListener(View.OnClickListener { v ->
            // Kayıt ol butonuna tıklanırsa yeni aktiviteye git
            startActivity(Intent(applicationContext,RegisterActivity::class.java))
        })

        // Şifremi unuttum text'ine tıklanırsa
        tv_forgotPsw.setOnClickListener(View.OnClickListener { v ->
            // kullanıcı emailini edittext ten al email değişkenine ata
            var email:String = et_loginMail.text.toString()
            // mAuth initialize
            auth = FirebaseAuth.getInstance()
            //email girilmiş ise ve boş değilse
            if (!email.isEmpty()){
                // girilen email adresine şifre sıfırlama linkini gönder
                auth!!.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Başarılı ise başarılı olduğuna dair toast mesajını kullanıcıya göster
                                toast(resources.getString(R.string.toastSentResetPasswordLink),Durations.short)
                            } else {
                                // Başarılı değil ise başarılı olduğuna dair toast mesajını kullanıcıya göster
                                toast(resources.getString(R.string.toastFAILResetPAssword),Durations.short)
                            }
                        }
            }else {
                // email kısmı boş ise boş olduğuna dair toast mesajını kullanıcıya göster
                toast(resources.getString(R.string.emailNotEmpty),Durations.short)
            }
        })

        iv_eye.setOnClickListener(View.OnClickListener { v ->
            // Göz ikonuna tıklanırsa gizlenmiş olan şifre görünür hale gertirlir.
            et_loginPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        })

    }

    private fun login(){
        // Edittext lere girilmiş email ve password değerleri alınır.
        var email:String = et_loginMail.text.toString()
        var password:String = et_loginPassword.text.toString()
        // Kullanıcı tarafondan girilen email ve password alanları boş mu dolu mu kontrol edilir.
        if (!email.isEmpty() && !password.isEmpty()) {
            val progressDialog = ProgressDialog(this@LoginActivity)
            utils.setProgressDialog(progressDialog,resources.getString(R.string.progressDialogLoginTitle),resources.getString(R.string.progressDialogLoginMessage))
            //setProgressDialog(progressDialog,"Lütfen Bekleyiniz","Giriş işlemi yapılıyor")
            this.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    // Giriş işlemi başarılı ise dialog kapatılır ve bir sonraki aktiviteye gidilir. Gitmeden önce finish() methodu ile
                    // stackteki LoginActivity silinir.
                    progressDialog.dismiss()
                    startActivity(Intent(this, CategoryActivity::class.java))
                    finish()
                    toast(resources.getString(R.string.toastSuccessLogin),Durations.short)
                } else {
                    // progessDialog kapatılır giriş işlemi esnasında hata oluştuğu kullanıcıya toast mesaj ile bildirilir.
                    progressDialog.dismiss()
                    toast(resources.getString(R.string.toastFailLogin),Durations.short)
                }
            })

        }else {
            // Tüm alanlar doldurulmaz ise kullanıcı uyarılır.
            toast(resources.getString(R.string.toastFillallfields),Durations.short)
        }
    }
    // Toast fonksiyonu
    private fun toast(message:String,duration:Int){
        Toast.makeText(applicationContext,message,duration).show()
    }
    // Firebase initialize
    private fun firebaseInits(){
        auth = FirebaseAuth.getInstance()
    }


}