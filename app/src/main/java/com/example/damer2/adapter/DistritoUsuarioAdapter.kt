package com.example.damer2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Distrito
import com.example.damer2.data.Entities.DistritoUsuario
import com.example.damer2.data.Entities.Negocio

class DistritoUsuarioAdapter:RecyclerView.Adapter<DistritoUsuarioAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onItemClick: ((DistritoUsuario) -> Unit)? = null

    var distritos : List<DistritoUsuario> = emptyList()

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<DistritoUsuario>){
        this.codigos = miList1
        this.descripcions = miList2
        this.distritos = miList4
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_distrito,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.itemDescripcion.text  = this.descripcions[i]
        viewHolder.itemDistrito = this.distritos[i]

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var itemDescripcion:TextView
        var itemDistrito : DistritoUsuario
        var itemButton : Button


        init{
            itemDescripcion = itemView.findViewById(R.id.card_distrito_tDescripcion)
            itemDistrito = DistritoUsuario(0)
            itemButton = itemView.findViewById(R.id.card_distrito_btnDescargar)

            itemButton.setOnClickListener{
                onItemClick?.invoke(distritos[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}