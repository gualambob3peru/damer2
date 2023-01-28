package com.example.damer2.components.producto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.damer2.MainActivity
import com.example.damer2.R
import com.example.damer2.data.Database.AuditoriaDb


class ProductoCeroDialog : DialogFragment(){




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val db = AuditoriaDb(requireContext())

        var rootView : View = inflater.inflate(R.layout.dialog_producto_creo, container,false)
        val login_activity = Intent(requireContext(),MainActivity::class.java)

        var btnDiaProductoAceptarCreo = rootView.findViewById<Button>(R.id.btnDiaProductoAceptarCreo)
        var btnDiaProductoCancelarCreo = rootView.findViewById<Button>(R.id.btnDiaProductoCancelarCreo)

        btnDiaProductoAceptarCreo.setOnClickListener {


        }

        btnDiaProductoCancelarCreo.setOnClickListener {
            dismiss()
        }



        return rootView
    }



    interface algo {
       fun paso(){

       }
    }
}