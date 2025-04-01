package com.sugardaddy.colegioapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.PromedioEstudiante

class PromedioAdapter(private var lista: List<PromedioEstudiante>) :
    RecyclerView.Adapter<PromedioAdapter.PromedioViewHolder>() {

    inner class PromedioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.tvNombre)
        val promedio = itemView.findViewById<TextView>(R.id.tvPromedio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromedioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promedio_estudiante, parent, false)
        return PromedioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromedioViewHolder, position: Int) {
        val estudiante = lista[position]
        holder.nombre.text = estudiante.nombreCompleto
        holder.promedio.text = "Promedio: %.2f".format(estudiante.promedio)
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nueva: List<PromedioEstudiante>) {
        lista = nueva
        notifyDataSetChanged()
    }
}
