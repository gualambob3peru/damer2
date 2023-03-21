package com.example.damer2.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.R
import com.example.damer2.data.Entities.Categoria

class CategoriaAdapter:RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onItemClick: ((Categoria) -> Unit)? = null
    var onItemExcluirClick: ((Categoria) -> Unit)? = null
    var categorias : List<Categoria> = emptyList()
    var excluirs : List<Int> = emptyList()

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<Categoria>,miList5: MutableList<Int>){
        this.codigos = miList1
        this.descripcions = miList2
       // this.buttons = miList3
        this.categorias = miList4
        this.excluirs = miList5
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_categoria,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemCodigo.text = this.codigos[i]
        viewHolder.itemDescripcion.text  = this.descripcions[i]
        viewHolder.itemCategoria = this.categorias[i]

        if(this.excluirs[i]==2){ //excluido
            viewHolder.itemDescripcion.setTextColor(Color.parseColor("#ec3a27"))
        }else if(this.excluirs[i]==3){ //excluido y enviado a otro negocio
            viewHolder.itemDescripcion.setTextColor(Color.parseColor("#ffd166"))
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemCodigo: TextView
        var itemCategoria : Categoria
        var itemExcluir : Button
        var itemDescripcion:TextView
        var itemTextoExcluir:TextView

        init{
            itemCodigo = itemView.findViewById(R.id.card_categoria_codigo)
            itemDescripcion = itemView.findViewById(R.id.card_categoria_tDescripcion)
            itemExcluir = itemView.findViewById(R.id.categoria_btnExcluir)
            itemTextoExcluir = itemView.findViewById(R.id.categoria_textoExcluido)

            itemCategoria = Categoria()
            //itemButton = itemView.findViewById(R.id.btnVerCate)

            itemView.setOnClickListener {
                onItemClick?.invoke(categorias[adapterPosition])
            }

            itemExcluir.setOnClickListener {
                onItemExcluirClick?.invoke(categorias[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}