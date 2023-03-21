package com.example.damer2.adapter

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.ProductoActivity
import com.example.damer2.R
import com.example.damer2.data.Entities.Categoria
import com.example.damer2.data.Entities.Contrato
import com.example.damer2.data.Entities.Producto
import com.example.damer2.data.Entities.ProductoMaster

class ProductoAgregarSkuAdapter:RecyclerView.Adapter<ProductoAgregarSkuAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var onItemClick: ((ProductoMaster) -> Unit)? = null
    var productos : List<ProductoMaster> = emptyList()


    fun setList(miList1: MutableList<String>,miList2: MutableList<String>, miList3 : List<ProductoMaster>){
        this.codigos = miList1
        this.descripcions = miList2
        this.productos = miList3

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_producto_agregar_sku,viewGroup,false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemProducto = this.productos[i]
        viewHolder.itemCodigo.text = this.codigos[i]
        viewHolder.itemDescripcion.text  = this.descripcions[i]
        viewHolder.itemProducto = this.productos[i]

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemCodigo: TextView
        var itemDescripcion:TextView
        var itemProducto : ProductoMaster

        //  var itemButton : Button

        init{
            itemCodigo = itemView.findViewById(R.id.card_pas_sku)
            itemDescripcion = itemView.findViewById(R.id.card_pas_tDescripcion)

            itemProducto = ProductoMaster()


            itemView.setOnClickListener {
                onItemClick?.invoke(productos[adapterPosition])
            }

        }

    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}