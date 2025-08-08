package com.example.lengthunitconverter_kotlin

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lengthunitconverter_kotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val conversionFactors = mapOf(
        "Metre" to 1.0,
        "Millimetre" to 0.001,
        "Mile" to 1609.34,
        "Foot" to 0.3048
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Spinners
        val units = resources.getStringArray(R.array.length_units)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.fromSpinner.adapter = adapter
        binding.toSpinner.adapter = adapter

        // Set up Convert button click listener
        binding.convertButton.setOnClickListener {
            val inputText = binding.inputValue.text.toString()
            if (inputText.isEmpty()) {
                binding.inputValue.error = "Please enter a value"
                return@setOnClickListener
            }
            val inputValue = inputText.toDoubleOrNull()
            if (inputValue == null) {
                binding.inputValue.error = "Invalid number"
                return@setOnClickListener
            }

            val fromUnit = binding.fromSpinner.selectedItem.toString()
            val toUnit = binding.toSpinner.selectedItem.toString()
            val result = convertLength(inputValue, fromUnit, toUnit)
            binding.resultText.text = String.format("Result: %.2f %s", result, toUnit)
        }
    }

    private fun convertLength(value: Double, fromUnit: String, toUnit: String): Double {
        val fromFactor = conversionFactors[fromUnit] ?: 1.0
        val toFactor = conversionFactors[toUnit] ?: 1.0
        return value * fromFactor / toFactor
    }
}