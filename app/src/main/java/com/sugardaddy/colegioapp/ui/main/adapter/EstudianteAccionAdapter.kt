package com.sugardaddy.colegioapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sugardaddy.colegioapp.R
import com.sugardaddy.colegioapp.data.model.Estudiante

class EstudianteAccionAdapter(
    private var lista: List<Estudiante>,
    private val onEditar: (Estudiante) -> Unit,
    private val onEliminar: (Estudiante) -> Unit
) : RecyclerView.Adapter<EstudianteAccionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.tvNombre)
        val grado = itemView.findViewById<TextView>(R.id.tvGrado)
        val materia = itemView.findViewById<TextView>(R.id.tvMateria)
        val nota = itemView.findViewById<TextView>(R.id.tvNota)
        val btnEditar = itemView.findViewById<Button>(R.id.btnEditar)
        val btnEliminar = itemView.findViewById<Button>(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estudiante_accion, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estudiante = lista[position]
        holder.nombre.text = "${estudiante.nombre} ${estudiante.apellido}"
        holder.grado.text = "Grado: ${estudiante.grado}"
        holder.materia.text = "Materia: ${estudiante.materia}"
        holder.nota.text = "Nota: ${estudiante.nota}"

        holder.btnEditar.setOnClickListener { onEditar(estudiante) }
        holder.btnEliminar.setOnClickListener { onEliminar(estudiante) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Estudiante>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
