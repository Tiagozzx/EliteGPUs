package com.example.elitegpus

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var rvGpus: RecyclerView
    private lateinit var adapter: GpuAdapter
    private val listaGpus = mutableListOf<GpuModel>()
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("ElitePrefs", Context.MODE_PRIVATE)

        val btnSalir = findViewById<Button>(R.id.btnLogout)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAddGpu)
        rvGpus = findViewById(R.id.rvGpus)

        val rol = prefs.getString("rol", "Usuario")
        if (rol != "Administrador") {
            fabAdd.visibility = View.GONE
        } else {
            fabAdd.visibility = View.VISIBLE
        }

        adapter = GpuAdapter(listaGpus)
        rvGpus.layoutManager = LinearLayoutManager(this)
        rvGpus.adapter = adapter

        val db = FirebaseFirestore.getInstance()
        db.collection("Gpus").addSnapshotListener { snap, error ->
            if (error != null) {
                Toast.makeText(this, "Error de red: ${error.message}", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            if (snap != null) {
                listaGpus.clear()
                for (doc in snap.documents) {
                    val g = doc.toObject(GpuModel::class.java)
                    if (g != null) {
                        g.id = doc.id
                        listaGpus.add(g)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddGpuActivity::class.java))
        }

        btnSalir.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            prefs.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}