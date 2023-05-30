package com.example.damer2.adapter

import android.content.Context
import android.graphics.Color
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getDrawable

import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Negocio
import org.w3c.dom.Text


class NegocioAdapter:RecyclerView.Adapter<NegocioAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onItemClick: ((Negocio) -> Unit)? = null
    var onItemButtonClick: ((Negocio, Button) -> Unit)? = null
    var onItemBtnTemporalClick: ((Negocio, Button) -> Unit)? = null
    var onItemBtnArchivarClick: ((Negocio) -> Unit)? = null

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
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_negocio,viewGroup,false)
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

        val n_v = num_vacios[i]
        val e_e = estado_enviados[i]
        val n_p = num_productos[i]

        var estadoenvio = 0

        if(e_e==1){//verde
            estadoenvio=1
            viewHolder.itemButton.setBackgroundColor(Color.parseColor( "#06D6A0") )
            viewHolder.itemBtnArchivar.visibility = View.VISIBLE
        }else{
            viewHolder.itemBtnArchivar.visibility = View.INVISIBLE
            if(n_v>0 || n_p==0){//Rojo
                estadoenvio=2
                viewHolder.itemButton.setBackgroundColor(Color.parseColor("#ff6542") )
            }else if(n_v==0){//Amarillo
                estadoenvio=3
                viewHolder.itemButton.setBackgroundColor(Color.parseColor("#ffd166"))
            }
        }

        if(this.negocios[i].estadoExcluido==2){
            viewHolder.itemDescripcion.setTextColor(Color.parseColor("#ec3a27"))
        }else if(this.negocios[i].estadoExcluido==3){
            viewHolder.itemDescripcion.setTextColor(Color.parseColor("#ffd166"))
        }else{
            viewHolder.itemDescripcion.setTextColor(Color.parseColor("#FFFFFF"))
        }

        if(this.negocios[i].estadoTemporal==0){
            viewHolder.itemBtnTemporal.visibility = View.VISIBLE
        }else if(this.negocios[i].estadoTemporal==2){
            viewHolder.itemBtnTemporal.visibility = View.VISIBLE
            viewHolder.itemBtnTemporal.text = "BAJA"
            viewHolder.itemBtnTemporal.setBackgroundColor(Color.parseColor("#ff6542") ) //ROJO
        }




    }


    inner class ViewHolder(itemView: View,i :Int,id:Int) : RecyclerView.ViewHolder(itemView){

        var itemDescripcion:TextView
        var itemNegocio : Negocio
        var itemButton : Button
        var estadoenvio : Int = 0
        var itemCodigo : TextView
        var itemTextoExcluido: TextView
        var itemBtnTemporal: Button
        var itemCanal: TextView
        var itemBtnArchivar: ImageView

        init{
            itemDescripcion = itemView.findViewById(R.id.card_negocio_tDescripcion)
            itemNegocio = Negocio(0)
            itemButton = itemView.findViewById(R.id.card_negocio_enviar)
            itemCodigo = itemView.findViewById(R.id.card_negocio_tDireccion)
            itemTextoExcluido= itemView.findViewById(R.id.negocio_excluido)
            itemBtnTemporal= itemView.findViewById(R.id.btn_baja)
            itemCanal= itemView.findViewById(R.id.card_negocio_tCanal)
            itemBtnArchivar= itemView.findViewById(R.id.btn_archivar)

            itemDescripcion.setOnClickListener{
                onItemClick?.invoke(negocios[adapterPosition])
            }
            itemButton.setOnClickListener{
                onItemButtonClick?.invoke(negocios[adapterPosition],itemButton)
            }
            itemBtnTemporal.setOnClickListener{
                onItemBtnTemporalClick?.invoke(negocios[adapterPosition],itemBtnTemporal)
            }
            itemBtnArchivar.setOnClickListener{
                onItemBtnArchivarClick?.invoke(negocios[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}