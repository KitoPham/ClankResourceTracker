package co.kpham.clankresourcetracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Resources
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.round

const val NUM_OF_BUTTONS = 10
class MainActivity : AppCompatActivity() {


    val skillButtonArray = ArrayList<Button>()
    val moveButtonArray = ArrayList<Button>()
    val combatButtonArray = ArrayList<Button>()

    var selectArray = arrayOfNulls<Button>(3)

    var roundStats = arrayOf(0,0,0)
    var maxStats = arrayOf(0,0,0)


    private lateinit var viewModel: MyViewModel
    public var rounds : Int = 1

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().getDisplayMetrics().density).toInt()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        generateButtons(skillButtonArray, skillButtonBar, R.color.skillBlue, 0)
        generateButtons(moveButtonArray, moveButtonBar, R.color.movementYellow, 1)
        generateButtons(combatButtonArray, combatButtonBar, R.color.combatRed, 2)

        viewModel.getRound().observe(this, Observer{ roundNum : Int? ->
            roundNum?.let{
                roundNumber.text = it.toString()
                rounds = it
            }


        })

        viewModel.getAverages().observe(this, Observer { averages ->
            averages?.let {
                averageSkill.text = it[0].toString()
                averageMove.text = it[1].toString()
                averageCombat.text = it[2].toString()
            }
        })

        endButton.setOnClickListener{

            for(i in 0..(roundStats.count()-1))roundStats[i]=0
            for(i in 0..(maxStats.count() - 1)){
                viewModel.statTotals[i] = viewModel.statTotals[i] + maxStats[i]
                maxStats[i] = 0
            }
            viewModel.setAverages()
            rounds++
            viewModel.setRound(rounds)

            for (i in skillButtonArray)i.setBackgroundColor(Color.WHITE)
            for (i in moveButtonArray)i.setBackgroundColor(Color.WHITE)
            for (i in combatButtonArray)i.setBackgroundColor(Color.WHITE)
        }


    }

    //Generates an array of buttons with a background color of white
    //When clicked that button in it's row becomes the selected button for its index in the selected array
    //color changes to corresponding
    fun generateButtons(array: ArrayList<Button>, layout: LinearLayout, color1 : Int, selectedIndex : Int ){
        for (i in 0..NUM_OF_BUTTONS) {
            val button = Button(this)
            button.setBackgroundColor(Color.WHITE)
            button.text = "" + i
            button.setPadding(0, 0, 0, 0)

            val params = LinearLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT, 1f)
            val margin = dpToPx(2)
            params.setMargins(margin, margin, margin, margin)

            button.layoutParams = params

            array.add(button)
            button.setOnClickListener {
                if (selectArray[selectedIndex] != null){
                    selectArray[selectedIndex]?.setBackgroundColor(Color.WHITE)
                }
                val numberText = button.text.toString().toInt()
                if (numberText.toString().toInt() > roundStats[selectedIndex]){
                    roundStats[selectedIndex]= numberText
                }
                selectArray[selectedIndex] = button
                button.setBackgroundColor(ContextCompat.getColor(applicationContext, color1))

                if (maxStats[selectedIndex] < numberText){
                    maxStats[selectedIndex] = numberText
                }
            }
            layout.addView(button)
        }
    }
}
