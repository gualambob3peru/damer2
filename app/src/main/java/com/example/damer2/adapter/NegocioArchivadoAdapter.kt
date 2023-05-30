package com.example.damer2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Negocio

class NegocioArchivadoAdapter:RecyclerView.Adapter<NegocioArchivadoAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var onItemClick: ((Negocio) -> Unit)? = null


    var negocios : List<Negocio> = emptyList()
    var num_vacios : MutableList<Int> = mutableListOf()
    var estado_enviados : MutableList<Int> = mutableListOf()
    var num_productos : MutableList<Int> = mutableListOf()
    var arr_canals : MutableList<String> = mutableListOf()
    var num : Int = 0

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<Negocio>,l5:MutableList<Int>,l6:MutableList<Int>,l7:MutableList<Int>, l8:MutableList<String>){
        this.codigos = miList1
        this.descripcions = miList2
        this.negocios = miList4
        this.num_vacios = l5
        this.estado_enviados = l6
        this.num_productos = l7
        this.arr_canals = l8
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_negocio_archivado,viewGroup,false)
        num++

        return ViewHolder(v,num,viewGroup.id)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.itemDescripcion.text  = this.descripcions[i]

        viewHolder.itemNegocio = this.negocios[i]

        if(this.negocios[i].estadoNuevo==1){
            viewHolder.itemCodigo.text  = "T - "+ this.negocios[i].codigo_negocio
        }else{
            viewHolder.itemCodigo.text  = this.negocios[i].codigo_negocio
        }
        viewHolder.itemCanal.text = this.arr_canals[i]
    }


    inner class ViewHolder(itemView: View,i :Int,id:Int) : RecyclerView.ViewHolder(itemView){

        var itemDescripcion:TextView
        var itemNegocio : Negocio
        var itemCodigo : TextView
        var itemCanal: TextView


        init{
            itemDescripcion = itemView.findViewById(R.id.card_negocio_tDescripcion)
            itemNegocio = Negocio(0)
            itemCodigo = itemView.findViewById(R.id.card_negocio_tDireccion)
            itemCanal= itemView.findViewById(R.id.card_negocio_tCanal)
        }
    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}