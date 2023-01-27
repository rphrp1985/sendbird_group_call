package com.prianshuprasad.webrtc.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sendbird.calls.EnterParams
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdException
import com.sendbird.calls.handler.CompletionHandler


class PreviewViewModel : ViewModel() {

    val enterResult: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val errorMessage: MutableLiveData<String> = MutableLiveData<String>(null)

    fun enter(roomId: String, isAudioEnabled: Boolean, isVideoEnabled: Boolean) {
        val room = SendBirdCall.getCachedRoomById(roomId) ?: return

        val enterParams = EnterParams()
            .setAudioEnabled(isAudioEnabled)
            .setVideoEnabled(isVideoEnabled)

        room.enter(enterParams, object : CompletionHandler {
            override fun onResult(e: SendBirdException?) {
                if (e == null) {
                    enterResult.value= true
//                    _enterResult.postValue(Resource.success(null))
                } else {

                    errorMessage.value= e.message.toString()
                    enterResult.value= false

//                    _enterResult.postValue(Resource.error(e.message, e.code, null))
                }
            }
        })
    }
}
