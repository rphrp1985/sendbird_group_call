package com.prianshuprasad.webrtc.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prianshuprasad.webrtc.utils.MyCallback
import com.prianshuprasad.webrtc.utils.RoomDetails
import com.prianshuprasad.webrtc.utils.SendBirdUtil

class MainActivityViewModel:ViewModel() {

    val notify:MutableLiveData<String> = MutableLiveData(null)
    val authDetails: MutableLiveData<RoomDetails> = MutableLiveData<RoomDetails>(RoomDetails(false,""))




    fun auth(userId:String,token:String){

       SendBirdUtil.auth(userId,token,object :MyCallback{
           override fun success(msg: String) {
              notify.value=msg
           }

           override fun fail(msg: String) {
               notify.value= msg
           }
       })
    }



    fun createAndEnterRoom() {

        SendBirdUtil.createAndEnterRoom(object :MyCallback{
            override fun success(msg: String) {

                authDetails.value= RoomDetails(true,msg);

            }

            override fun fail(msg: String) {

                notify.value=msg
            }

        })

    }


    fun fetchRoomById(roomId: String) {
      SendBirdUtil.fetchRoomById(roomId,object : MyCallback{
          override fun success(msg: String) {
              authDetails.value= RoomDetails(true,msg)
          }

          override fun fail(msg: String) {
              notify.value=msg
          }

      })
    }























}