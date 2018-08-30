package co.kpham.clankresourcetracker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import kotlin.math.round

class MyViewModel : ViewModel() {

    private val numberOfButtons : MutableLiveData<Int> = MutableLiveData()
    private val currentRound : MutableLiveData<Int> = MutableLiveData()
    private val statAverages : MutableLiveData<Array<Double>> = MutableLiveData()
    var statTotals:Array<Int> = arrayOf(0,0,0)

    fun setButtons(num : Int) {
        numberOfButtons.value = num
        numberOfButtons.postValue(numberOfButtons.value)
    }

    fun getButtons() : MutableLiveData<Int>{
        return numberOfButtons
    }

    fun getRound() : MutableLiveData<Int>{
        if(currentRound.value == null){
            currentRound.value = 1
        }

        return currentRound
    }

    fun setRound(round : Int){
        currentRound.value = round
    }

    fun getAverages() : MutableLiveData<Array<Double>>{
        if(statAverages.value == null){
            statAverages.value = arrayOf(0.0,0.0,0.0)
        }
        return statAverages
    }

    fun setAverages(){
        for(i in 0 until statTotals.count()){
            currentRound.value?.let{
                statAverages.value?.run{
                    this[i] = (Math.round((statTotals[i].toDouble() / it.toDouble())*100)/100.00)
                }
            }
        }
        statAverages.postValue(statAverages.value)
    }

}