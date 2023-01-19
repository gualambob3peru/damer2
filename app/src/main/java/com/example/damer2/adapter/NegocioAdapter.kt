package com.example.damer2.adapter

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Negocio

class NegocioAdapter:RecyclerView.Adapter<NegocioAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onItemClick: ((Negocio) -> Unit)? = null
    var onItemButtonClick: ((Negocio, Button) -> Unit)? = null

    var negocios : List<Negocio> = emptyList()
    var num_vacios : MutableList<Int> = mutableListOf()
    var estado_enviados : MutableList<Int> = mutableListOf()
    var num : Int = 0

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<Negocio>,l5:MutableList<Int>,l6:MutableList<Int>){
        this.codigos = miList1
        this.descripcions = miList2
        this.negocios = miList4
        this.num_vacios = l5
        this.estado_enviados = l6
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_negocio,viewGroup,false)
        num++

        return ViewHolder(v,num,viewGroup.id)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.itemDescripcion.text  = this.descripcions[i]
        viewHolder.itemNegocio = this.negocios[i]

        val n_v = num_vacios[i]
        val e_e = estado_enviados[i]

        var estadoenvio = 0

        if(e_e==1){//verde
            estadoenvio=1
            viewHolder.itemButton.setBackgroundColor(Color.parseColor( "#06D6A0") )
        }else{
            if(n_v>0){//Rojo
                estadoenvio=2
                viewHolder.itemButton.setBackgroundColor(Color.parseColor("#ff6542") )
            }else if(n_v==0){//Amarillo
                estadoenvio=3
                viewHolder.itemButton.setBackgroundColor(Color.parseColor("#ffd166"))
            }
        }

    }


    inner class ViewHolder(itemView: View,i :Int,id:Int) : RecyclerView.ViewHolder(itemView){

        var itemDescripcion:TextView
        var itemNegocio : Negocio
        var itemButton : Button
        var estadoenvio : Int = 0

        init{
            itemDescripcion = itemView.findViewById(R.id.card_negocio_tDescripcion)
            itemNegocio = Negocio(0)
            itemButton = itemView.findViewById(R.id.card_negocio_enviar)

            itemDescripcion.setOnClickListener{
                onItemClick?.invoke(negocios[adapterPosition])
            }
            itemButton.setOnClickListener{
                onItemButtonClick?.invoke(negocios[adapterPosition],itemButton)
            }
        }
    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}