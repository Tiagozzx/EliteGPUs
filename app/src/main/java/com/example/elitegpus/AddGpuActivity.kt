package com.example.elitegpus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class AddGpuActivity : AppCompatActivity() {

    private lateinit var ivFoto: ImageView
    private var base64Img: String = ""

    private val takePic = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp ->
        if (bmp != null) {
            ivFoto.setImageBitmap(bmp)

            // Redimensionar a tamaño web optimizado
            val scaledBmp = Bitmap.createScaledBitmap(bmp, 350, 350, true)
            val stream = ByteArrayOutputStream()
            scaledBmp.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            val byteArray = stream.toByteArray()
            base64Img = Base64.encodeToString(byteArray, Base64.NO_WRAP)

            Toast.makeText(this, "Foto capturada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gpu)

        crearCanal()
        ivFoto = findViewById(R.id.ivGpuFoto)

        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnTomarFoto).setOnClickListener {
            takePic.launch(null)
        }

        val etNombre = findViewById<TextInputEditText>(R.id.etGpuNombre)
        val etPrecio = findViewById<TextInputEditText>(R.id.etGpuPrecio)
        val tilNombre = findViewById<TextInputLayout>(R.id.tilGpuNombre)

        findViewById<Button>(R.id.btnGuardarGpu).setOnClickListener {
            val nom = etNombre.text.toString().trim()
            val pre = etPrecio.text.toString().trim()

            if (nom.isEmpty()) {
                tilNombre.error = "Ingresa el modelo"
                return@setOnClickListener
            }

            if (base64Img.isEmpty()) {
                Toast.makeText(this, "Debes tomar la foto primero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Subiendo a Firebase...", Toast.LENGTH_SHORT).show()

            val datosGpu = hashMapOf(
                "nombre" to nom,
                "precio" to pre,
                "imagenBase64" to base64Img
            )

            // Envío a la colección en la nube de Firebase
            FirebaseFirestore.getInstance().collection("Gpus").add(datosGpu)
                .addOnSuccessListener {
                    lanzarNotif(nom)
                    Toast.makeText(this, "¡GPU subida a Firebase!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Fallo al subir: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun crearCanal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel("elite_ch", "Elite Channel", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        }
    }

    private fun lanzarNotif(nom: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "elite_ch")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Nueva GPU Publicada")
            .setContentText("El modelo $nom ya está disponible.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pi)
            .setAutoCancel(true)

        NotificationManagerCompat.from(this).notify(System.currentTimeMillis().toInt(), builder.build())
    }
}