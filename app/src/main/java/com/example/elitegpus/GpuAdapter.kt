package com.example.elitegpus

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.firestore.FirebaseFirestore

class GpuAdapter(private val lista: MutableList<GpuModel>) :
    RecyclerView.Adapter<GpuAdapter.GpuViewHolder>() {

    class GpuViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val ivFoto: ImageView = v.findViewById(R.id.ivItemFoto)
        val tvNombre: TextView = v.findViewById(R.id.tvItemNombre)
        val tvPrecio: TextView = v.findViewById(R.id.tvItemPrecio)
        val btnEliminar: Button = v.findViewById(R.id.btnEliminarGpu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GpuViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_gpu, parent, false)
        return GpuViewHolder(v)
    }

    override fun onBindViewHolder(holder: GpuViewHolder, position: Int) {
        val gpu = lista[position]
        holder.tvNombre.text = gpu.nombre
        holder.tvPrecio.text = "$${gpu.precio} USD"

        if (gpu.imagenBase64.isNotEmpty()) {
            try {
                val bytes = Base64.decode(gpu.imagenBase64, Base64.DEFAULT)
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                Glide.with(holder.itemView.context)
                    .load(bmp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivFoto)
            } catch (e: Exception) {
                holder.ivFoto.setImageResource(R.mipmap.ic_launcher)
            }
        }

        holder.btnEliminar.setOnClickListener {
            if (gpu.id.isNotEmpty()) {
                FirebaseFirestore.getInstance().collection("Gpus").document(gpu.id)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context, "GPU eliminada", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(holder.itemView.context, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun getItemCount(): Int = lista.size
}