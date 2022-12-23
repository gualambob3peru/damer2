package com.example.damer2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Distrito
import com.example.damer2.data.Entities.Negocio

class DistritoAdapter:RecyclerView.Adapter<DistritoAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onItemClick: ((Distrito) -> Unit)? = null

    var distritos : List<Distrito> = emptyList()

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<Distrito>){
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
        var itemDistrito : Distrito
        var itemButton : Button


        init{
            itemDescripcion = itemView.findViewById(R.id.card_distrito_tDescripcion)
            itemDistrito = Distrito(0)
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