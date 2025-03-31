package com.sugardaddy.colegioapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.Estudiante

class EstudianteAdapter(private var estudiantes: List<Estudiante>) :
    RecyclerView.Adapter<EstudianteAdapter.EstudianteViewHolder>() {

    inner class EstudianteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.tvNombre)
        val grado = itemView.findViewById<TextView>(R.id.tvGrado)
        val materia = itemView.findViewById<TextView>(R.id.tvMateria)
        val nota = itemView.findViewById<TextView>(R.id.tvNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstudianteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estudiante, parent, false)
        return EstudianteViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstudianteViewHolder, position: Int) {
        val estudiante = estudiantes[position]
        holder.nombre.text = "${estudiante.nombre} ${estudiante.apellido}"
        holder.grado.text = "Grado: ${estudiante.grado}"
        holder.materia.text = "Materia: ${estudiante.materia}"
        holder.nota.text = "Nota: ${estudiante.nota}"
    }

    override fun getItemCount(): Int = estudiantes.size

    fun actualizarLista(nuevaLista: List<Estudiante>) {
        estudiantes = nuevaLista
        notifyDataSetChanged()
    }
}
