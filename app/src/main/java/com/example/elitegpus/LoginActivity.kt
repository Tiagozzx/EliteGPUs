package com.example.elitegpus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        prefs = getSharedPreferences("ElitePrefs", Context.MODE_PRIVATE)

        if (prefs.getBoolean("isLogged", false) && auth.currentUser != null) {
            navegarAlMain()
            return
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            // Autenticación formal en Firebase para obtener token de sesión real
            auth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    prefs.edit()
                        .putBoolean("isLogged", true)
                        .putString("rol", "Administrador")
                        .apply()
                    navegarAlMain()
                } else {
                    Toast.makeText(
                        this,
                        "Error de autenticación: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun navegarAlMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}