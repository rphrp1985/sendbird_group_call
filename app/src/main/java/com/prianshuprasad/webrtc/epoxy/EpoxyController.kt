package com.prianshuprasad.socket.utils.epoxy

import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.sendbird.calls.Participant


class EpoxyController(val SHeight:Int, val SWidth:Int) : EpoxyController(){


    var items : ArrayList<Participant>

    init {
        items= ArrayList()
    }

    fun update(x:ArrayList<Participant>){
        items= x

    }

    override fun buildModels() {
        var i:Long =0

        items.forEach {participant ->


            ParticipantModel_().id(i).participant(participant).height(getHeight()).width(getWidth(i.toInt())).addTo(this)
          i++

        }
    }


    override fun getSpanSizeLookup(): GridLayoutManager.SpanSizeLookup {

        if (items.size == 1 || items.size % 2 == 0)
            return super.getSpanSizeLookup()

        val span = object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {


                when (position) {
                    0 -> return 2
                    else -> return 1
                }


            }

        }
        return span
    }



    private fun getWidth( position: Int): Int {
       val size= items.size
        if(size <= 2)
            return SWidth
        else if (size%2!=0) {
            if (position == 0)
                return SWidth;
            else
                return SWidth / 2
        } else {
            return SWidth / 2
        }
    }

    private fun getHeight(): Int {
        val size=items.size
        when(size)
        {
            1->
                return SHeight
            2,3,4 ->
                return SHeight/2
            else ->
            {
                val rows= (size+1)/2;
                return SHeight/(rows)
            }
        }
    }








}


