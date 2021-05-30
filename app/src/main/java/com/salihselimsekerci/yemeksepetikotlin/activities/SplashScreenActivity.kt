/*
 * Handler sınıfından tanımladığımız bir handler nesnesi ile delay olarak 3000 ms vererek aktivitemizin bu süre zarfında çalışmasını
 * süre bittiğinde belirlenen aktiviteye gitmesini sağladık.
 * InternetConnection sınıfımızdan tanımladığımız nesne ile de internet bağlantımızı kontrol ediyoruz. Bı sınıf bize internetin
 * olup olmamasına göre bir boolean değer döndürüyor. Eğer internet yok ise kullanıcıya internetini açması içinalertdialog gösteriyoruz
 * eğer kabul ederse internet ayarlarına gidiyor etmez ise uygulamayı kapatıyoruzç
 */
package com.salihselimsekerci.yemeksepetikotlin.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.salihselimsekerci.yemeksepetikotlin.classes.InternetConnection
import com.salihselimsekerci.yemeksepetikotlin.R

class SplashScreenActivity : AppCompatActivity() {
    // handler tanımı
    private lateinit var handler: Handler
    // internetConnection nesnesi
    private lateinit var internetConnection:InternetConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        // handler nesne initialize
        handler = Handler()

        // InternetConnection nesnemize bulunduğumuz aktivitenin context inin geçilmesi
        internetConnection = InternetConnection(applicationContext)
        // internet kontrolü
        internetConnection.observe(this, Observer { isConnected ->
            if (isConnected){
                // handler.postDelayed ile aktivitenin bir süre zarfında çalışması için THREAD oluşturma.
                handler.postDelayed({
                    val intent = Intent(this@SplashScreenActivity, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                },3000)
            }else {
                // Eğer internet yok ise
                // Dialog ayarlarını yap ve dialogu göster
                val builder = AlertDialog.Builder(this)
                builder.setTitle(resources.getString(R.string.splashBuilderTitle))
                builder.setMessage(resources.getString(R.string.splashBuilderMessage))
                builder.setCancelable(false)
                // positiveButton a tıklanırsa:
                builder.setPositiveButton(resources.getString(R.string.YES)){dialog: DialogInterface?, which: Int ->
                    // uyarı mesajı göster
                    Toast.makeText(applicationContext, R.string.internetError,Toast.LENGTH_SHORT).show()
                    // internet ayarlarına götür
                    val intent = Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
                    startActivity(intent)
                }

                builder.setNegativeButton(resources.getString(R.string.NO)){dialog: DialogInterface?, which: Int ->
                    // negativeButton a tıklanırsa uygulamadan çık.
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
                // dialog ekranını göster
                builder.show()
            }
        })
    }
}