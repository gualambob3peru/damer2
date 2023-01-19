package com.example.damer2.adapter

import android.graphics.Color
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damer2.ProductoActivity
import com.example.damer2.R
import com.example.damer2.data.Entities.Contrato
import com.example.damer2.data.Entities.Producto

class ProductoAdapter:RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {
    var codigos : MutableList<String> = mutableListOf()
    var miContrato :Contrato = Contrato()
    var descripcions : MutableList<String> = mutableListOf()
    var buttons : MutableList<String> = mutableListOf()
    var onCompraKey: ((Int, Int, EditText, EditText,Float, TextView) -> Unit)? = null
    var onInventarioKey: ((Int, Int, EditText, EditText,Float, TextView) -> Unit)? = null
    var onPrecioKey: ((Int, Int, EditText) -> Unit)? = null
    var onVeKey: ((Int, Int, EditText) -> Unit)? = null
    var onItemClick: ((Int,TextView,TextView,TextView,TextView) -> Unit)? = null
    var productos : List<Producto> = emptyList()
    var listText : List<TextView> = mutableListOf()
    var tipoDato:String="0"

    fun setList(miList1: MutableList<String>,miList2: MutableList<String>,miList4:List<Producto>,miList5:Contrato,tipoDato : String){
        this.codigos = miList1
        this.descripcions = miList2
        this.productos = miList4
        this.miContrato = miList5
        this.tipoDato = tipoDato
    }

    fun getList(viewHolder: ViewHolder):TextView{
        return viewHolder.itemCompra
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_producto,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemProducto = this.productos[i]
        viewHolder.itemCodigo.text = this.codigos[i]
        viewHolder.itemDescripcion.text  = this.descripcions[i]
        viewHolder.itemProducto = this.productos[i]


        viewHolder.itemCompra.setText(ProductoActivity.listSkus[i][0])
        viewHolder.itemInventario.setText(ProductoActivity.listSkus[i][1])
        viewHolder.itemPrecio.setText(ProductoActivity.listSkus[i][2])
        viewHolder.itemVe.setText(ProductoActivity.listSkus[i][3])

        viewHolder.itemVant.text = productos[i].vant
        viewHolder.itemCoant.text = productos[i].compra_ant

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var itemCodigo: TextView
        var itemDescripcion:TextView
        var itemProducto : Producto
        var itemCompra : EditText
        var itemInventario : EditText
        var itemPrecio : EditText
        var itemVe : EditText
        var itemVant : TextView
        var itemCoant : TextView
        var itemMsg : TextView

        //  var itemButton : Button

        init{
            itemCodigo = itemView.findViewById(R.id.tCodigoProducto)
            itemDescripcion = itemView.findViewById(R.id.tDescripcionProducto)
            itemProducto = Producto()
            itemCompra = itemView.findViewById(R.id.inputCompra)
            itemVant = itemView.findViewById(R.id.txtVant)
            itemCoant = itemView.findViewById(R.id.txtcoant)

            itemInventario = itemView.findViewById(R.id.inputInventario)
            itemPrecio = itemView.findViewById(R.id.inputPrecio)
            itemVe = itemView.findViewById(R.id.inputVe)
            itemMsg = itemView.findViewById(R.id.producto_msgRegla1)

            if(miContrato.cod_categoria!=""){
                //Iniciando
                if(tipoDato=="1"){
                    itemInventario.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
                    itemCompra.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
                    itemVe.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)
                }else{
                    itemCompra.setInputType(InputType.TYPE_CLASS_NUMBER)
                    itemInventario.setInputType(InputType.TYPE_CLASS_NUMBER)
                    itemVe.setInputType(InputType.TYPE_CLASS_NUMBER)
                }


                itemPrecio.setInputType(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED)


                itemCompra.isEnabled = false
                itemCompra.setBackgroundColor(Color.parseColor("#EEEEEE"))
                itemInventario.isEnabled = false
                itemInventario.setBackgroundColor(Color.parseColor("#EEEEEE"))
                itemPrecio.isEnabled = false
                itemPrecio.setBackgroundColor(Color.parseColor("#EEEEEE"))
                itemVe.isEnabled = false
                itemVe.setBackgroundColor(Color.parseColor("#EEEEEE"))

                //Verificando variables
                var variables = miContrato.variables
                var arr_variables= variables.split(",")


                for (varia in arr_variables){
                    if(varia=="1"){
                        itemCompra.isEnabled = true
                        itemCompra.setBackgroundColor(Color.WHITE)
                    }else if(varia=="2"){
                        itemInventario.isEnabled = true
                        itemInventario.setBackgroundColor(Color.WHITE)
                    }else if(varia=="3"){
                        itemPrecio.isEnabled = true
                        itemPrecio.setBackgroundColor(Color.WHITE)
                    }else if(varia=="4"){
                        itemVe.isEnabled = true
                        itemVe.setBackgroundColor(Color.WHITE)
                    }
                }
            }else{

            }



            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition,itemCompra,itemInventario,itemPrecio,itemVe)
            }
            itemCompra.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {


                    val inputCompraV = itemView.findViewById<EditText>(R.id.inputCompra)
                    val inputInventarioV = itemView.findViewById<EditText>(R.id.inputInventario)


                    onCompraKey?.invoke(adapterPosition,0,inputCompraV,inputInventarioV,productos[adapterPosition].vant.toFloat(),itemMsg)
                }
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {

                }
            })


            itemInventario.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {

                    val inputCompraV = itemView.findViewById<EditText>(R.id.inputCompra)
                    val inputInventarioV = itemView.findViewById<EditText>(R.id.inputInventario)

                    onInventarioKey?.invoke(adapterPosition,0,inputCompraV,inputInventarioV,productos[adapterPosition].vant.toFloat(),itemMsg)
                }
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {}
            })


            itemPrecio.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    onPrecioKey?.invoke(adapterPosition,2,itemPrecio)
                }
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {}
            })


            itemVe.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    onVeKey?.invoke(adapterPosition,3,itemVe)
                }
                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {}
            })


        }

    }

    override fun getItemCount(): Int {
        return codigos.size
    }
}