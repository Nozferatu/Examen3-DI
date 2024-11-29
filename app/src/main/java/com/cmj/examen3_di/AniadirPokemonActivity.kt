package com.cmj.examen3_di

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.SpinnerAdapter
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.text.isDigitsOnly
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cmj.examen3_di.databinding.ActivityAniadirPokemonBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.DateFormat
import java.util.Date

class AniadirPokemonActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAniadirPokemonBinding
    private var contexto = this

    private lateinit var pokemon: Pokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAniadirPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding){
            //Spinner
            val tipos = listOf("Fuego", "Planta", "Eléctrico", "Agua", "Oscuro", "Psíquico", "Hada", "Oscuro", "Tierra", "Volador")
            val adapter = ArrayAdapter(contexto, android.R.layout.simple_spinner_item, tipos)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinnerTipos.adapter = adapter

            //Fecha
            fechaAtrapadoInput.setOnClickListener {
                val builder = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Selecciona una fecha")
                val datePicker = builder.build()

                datePicker.addOnPositiveButtonClickListener {
                    fechaAtrapadoInput.setText(datePicker.headerText)
                }

                datePicker.show(supportFragmentManager, "ola")
            }

            //Botón para añadir el Pokémon
            botonAniadir.setOnClickListener {
                var nombre = nombreInput.text.toString()
                var entrenador = entrenadorInput.text.toString()
                var tipo = spinnerTipos.selectedItem.toString()
                var estatura = estaturaInput.text.toString()
                var comentarios = comentariosInput.text.toString()
                var fechaAtrapado = fechaAtrapadoInput.text.toString()

                var fecha = DateFormat.getDateInstance(DateFormat.MEDIUM).parse(fechaAtrapado)
                println(fecha)

                var valido = validarDatosPokemon(nombre, entrenador, estatura, fecha)

                if(valido){
                    hacerTostada(contexto, "Bien hecho", 3)
                    nombre = nombre.replaceFirstChar { it.uppercase() }

                    pokemon = Pokemon(nombre, entrenador, tipo, estatura.toInt(), comentarios, fechaAtrapado)
                }
            }
        }
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        intent.putExtra("pokemon", pokemon)

        return super.getOnBackInvokedDispatcher()
    }

    fun hacerTostada(contexto: Context, mensaje: String, tiempo: Int){
        Toast.makeText(
            contexto,
            mensaje,
            tiempo
        ).show()
    }

    fun validarDatosPokemon(
        nombre: String,
        entrenador: String,
        estatura: String,
        fecha: Date
    ): Boolean{
        if(nombre.length < 3){
            hacerTostada(contexto, "El nombre debe de tener al menos 3 caracteres", 3)
            return false
        }

        if(contieneVocales(entrenador)){
            if(entrenador.length > 25){
                hacerTostada(contexto, "El nombre del entrenador no debe de tener más de 25 caracteres", 3)
                return false
            }
        }else {
            hacerTostada(contexto, "El nombre del entrenador debe tener una vocal", 3)
            return false
        }

        if(!estatura.isDigitsOnly() || estatura.isEmpty()){
            return false
        }

        val fechaActual = Date()

        if(fechaActual.before(fecha)){
            hacerTostada(contexto, "No puedes capturar un Pokémon en el futuro", 3)
            return false
        }

        return true
    }

    fun contieneVocales(cadena: String): Boolean{
        val vocales = "AEIOU"

        for(char in cadena){
            if(vocales.contains(char.uppercase())) return true
        }

        return false
    }
}