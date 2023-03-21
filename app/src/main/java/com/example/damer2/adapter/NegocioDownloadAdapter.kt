package com.example.damer2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Negocio

class NegocioDownloadAdapter:RecyclerView.Adapter<NegocioDownloadAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onItemClick: ((Negocio) -> Unit)? = null

    var negocios : List<Negocio> = emptyList()

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<Negocio>){
        this.codigos = miList1
        this.descripcions = miList2
        this.negocios = miList4
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_negocio_download,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.itemDescripcion.text  = this.descripcions[i]
        viewHolder.itemNegocio = this.negocios[i]

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var itemDescripcion:TextView
        var itemNegocio : Negocio
        var itemButton : Button


        init{
            itemDescripcion = itemView.findViewById(R.id.negocio_descarga_tDescripcion)
            itemNegocio = Negocio(0)
            itemButton = itemView.findViewById(R.id.negocio_descarga_btnDescargar)

            itemButton.setOnClickListener{
                onItemClick?.invoke(negocios[adapterPosition])

            }
        }
    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}