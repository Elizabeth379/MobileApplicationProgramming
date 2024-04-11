package com.example.engineeringcalculator.fragments

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.engineeringcalculator.calculations.Calculator
import com.example.engineeringcalculator.calculations.Operations
import com.example.engineeringcalculator.Connector
import com.example.engineeringcalculator.R
import android.view.KeyEvent

class BaseModeFragment : Fragment() {
    private lateinit var btn_0: Button
    private lateinit var btn_1: Button
    private lateinit var btn_2: Button
    private lateinit var btn_3: Button
    private lateinit var btn_4: Button
    private lateinit var btn_5: Button
    private lateinit var btn_6: Button
    private lateinit var btn_7: Button
    private lateinit var btn_8: Button
    private lateinit var btn_9: Button
    private lateinit var btn_dot: Button
    private lateinit var btn_back: Button
    private lateinit var btn_ac: Button
    private lateinit var btn_plus: Button
    private lateinit var btn_minus: Button
    private lateinit var btn_multiply: Button
    private lateinit var btn_division: Button
    private lateinit var btn_equal: Button
    private lateinit var btn_parenthesis_l: Button
    private lateinit var btn_parenthesis_r: Button

    private lateinit var calculator : Calculator
    private lateinit var vibrator : Vibrator
    private var isDemo : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_base_mode, container, false)
        setupLayoutFor(view)

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && event.action == KeyEvent.ACTION_DOWN) {
                calculator.delete()
                true
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && event.action == KeyEvent.ACTION_DOWN) {
                calculator.equals()
                true
            } else {
                false
            }

        }

        return view
    }

    private fun setupLayoutFor(view: View) {
        //Numbers
        btn_0 = view.findViewById(R.id.btn_0)
        btn_0.setOnClickListener{
            calculator.appendNumber("0")
        }
        btn_1 = view.findViewById(R.id.btn_1)
        btn_1.setOnClickListener{
            calculator.appendNumber("1")
        }
        btn_2 = view.findViewById(R.id.btn_2)
        btn_2.setOnClickListener{
            calculator.appendNumber("2")
        }
        btn_3 = view.findViewById(R.id.btn_3)
        btn_3.setOnClickListener{
            calculator.appendNumber("3")
        }
        btn_4 = view.findViewById(R.id.btn_4)
        btn_4.setOnClickListener{
            calculator.appendNumber("4")
        }
        btn_5 = view.findViewById(R.id.btn_5)
        btn_5.setOnClickListener{
            calculator.appendNumber("5")
        }
        btn_6 = view.findViewById(R.id.btn_6)
        btn_6.setOnClickListener{
            calculator.appendNumber("6")
        }
        btn_7 = view.findViewById(R.id.btn_7)
        btn_7.setOnClickListener{
            calculator.appendNumber("7")
        }
        btn_8 = view.findViewById(R.id.btn_8)
        btn_8.setOnClickListener{
            calculator.appendNumber("8")
        }
        btn_9 = view.findViewById(R.id.btn_9)
        btn_9.setOnClickListener{
            calculator.appendNumber("9")
        }

        //Dot, Back, AC, (, )
        btn_dot = view.findViewById(R.id.btn_dot)
        btn_dot.setOnClickListener {
            calculator.setFloat()
        }
        btn_back = view.findViewById(R.id.btn_back)
        btn_back.setOnClickListener{
            calculator.delete()
        }
        btn_ac = view.findViewById(R.id.btn_ac)
        btn_ac.setOnClickListener{
            calculator.clear()
        }
        btn_parenthesis_l = view.findViewById(R.id.btn_parenthesis_l)
        btn_parenthesis_l.setOnClickListener{
            calculator.appendFunction("(") { n1 : Double -> n1}
        }
        btn_parenthesis_r = view.findViewById(R.id.btn_parenthesis_r)
        btn_parenthesis_r.setOnClickListener{
            calculator.complete()
        }

        //Math operations
        btn_equal = view.findViewById(R.id.btn_equal)
        btn_equal.setOnClickListener{
            calculator.equals()
        }
        btn_plus = view.findViewById(R.id.btn_plus)
        btn_plus.setOnClickListener {
            if (isDemo) {
                vibrator.vibrate(100)
                Toast.makeText(getContext(), "Sorry, you need to buy full version :(",
                    Toast.LENGTH_SHORT).show()
            }
            else
                calculator.appendOperation(Operations.ADD)
        }
        btn_minus = view.findViewById(R.id.btn_minus)
        btn_minus.setOnClickListener{
            calculator.appendOperation(Operations.SUBTRACT)
        }
        btn_multiply = view.findViewById(R.id.btn_multiply)
        btn_multiply.setOnClickListener{
            calculator.appendOperation(Operations.MULTIPLY)
        }
        btn_division = view.findViewById(R.id.btn_division)
        btn_division.setOnClickListener{
            calculator.appendOperation(Operations.DIVIDE)
        }

        this.calculator = activ!!.getCalculator()
        this.vibrator = activ!!.getVibrator()
        this.isDemo = activ!!.isDemo()
    }

    var activ: Connector? = null  //ссылка на MainActivity

    fun getInstance(): BaseModeFragment? {
        val args = Bundle()
        val fragment = BaseModeFragment()
        fragment.setArguments(args)
        return fragment
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activ = getActivity() as Connector?
    }
}