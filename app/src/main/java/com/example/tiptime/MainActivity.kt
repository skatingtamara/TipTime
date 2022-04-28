package com.example.tiptime

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

// Added for logging
private const val TAG = "MainActivity"

/**
 * Activity that displays a tip calculator.
 */
class MainActivity : AppCompatActivity() {

    // Binding object instance with access to the views in the activity_main.xml layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout XML file and return a binding object instance
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set the content view of the Activity to be the root view of the layout
        setContentView(binding.root)

        // Setup a click listener on the calculate button to calculate the tip and total cost
        binding.calculateButton.setOnClickListener {
            val tipFinal = calculateTip()
            val totalCost = calculateTotal()
            displayTip(tipFinal)
            displayTotal(totalCost)
        }

        // Set up a key listener on the EditText field to listen for "enter" button presses
        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode)
        }
    }

    /**
     * Calculates the tip based on the user input.
     */
    private fun calculateTip():Double {
        // Get the decimal value from the cost of service EditText field
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        //  If the cost if null or 0, then display 0 and exit.
        if (cost == null || cost == 0.0) {
            //displayTip(0.0)
            return 0.0
        }

        // Get the tip percentage based on which radio button is selected
        val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }

        // Calculate the tip
        var tip = tipPercentage * cost

        // If the switch for rounding up the tip toggled on (isChecked is true), then round up the
        // tip. Otherwise do not change the tip value.
        if (binding.roundUpSwitch.isChecked) {
            tip = kotlin.math.ceil(tip)
        }

        // Display the formatted tip value onscreen
        //displayTip(tip)
        return tip

    }

    /**
     * Format the tip amount according to the local currency and display it onscreen.
     * Example would be "Tip Amount: $10.00".
     */
    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }


    /**
     * Key listener for hiding the keyboard when the "Enter" button is tapped.
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }



    /**
     * ************** Tamara's addition*******************
     * Format the Total (cost of service + tip amount) according to the local currency
     * and display it onscreen.
     * Example would be "Total: $20.00".
     */

    // Adding logging functionality
    //private val TAG = "MainActivity"  // ??@Tamara. was:     private const val TAG = "MainActivity"  // @Tamara

    /**
     * Calculates the tip based on the user input.
     */
    private fun calculateTotal():Double {
        // Get the decimal value from the cost of service EditText field
        val stringInTextField = binding.costOfServiceEditText.text.toString()
        val cost = stringInTextField.toDoubleOrNull()

        //  If the cost if null or 0, then display 0 and exit.
        if (cost == null || cost == 0.0) {
            //displayTotal(0.0)
            return 0.0
        }

        // Calculate the tip from code above
        val tipFinal = calculateTip()
        Log.v(TAG, "Tip is calculated as ${tipFinal}")


        // Calculate the total
        val totalCost = cost + tipFinal

        // Display the formatted tip value onscreen
        //displayTotal(totalCost)
        return totalCost
    }

    /**
     * Format the total amount according to the local currency and display it onscreen.
     * Example would be "Total: $20.00".
     */
    private fun displayTotal(total: Double) {
        val formattedTotal = NumberFormat.getCurrencyInstance().format(total)
        binding.totalCost.text = getString(R.string.total_cost, formattedTotal)
    }


}