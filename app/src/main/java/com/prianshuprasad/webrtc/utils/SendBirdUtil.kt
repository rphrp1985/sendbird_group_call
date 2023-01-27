package com.prianshuprasad.webrtc.utils

import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomHandler
import org.webrtc.ContextUtils

class SendBirdUtil {

    companion object {


        fun auth(userId:String,token:String,callback: MyCallback){
            // The USER_ID below should be unique to your Sendbird application.
            val params =  AuthenticateParams(userId)
                .setAccessToken(token)

            SendBirdCall.authenticate(params
            ) { user, e ->

                if(e!=null) {
                    callback.fail(e.toString())
//                    notify.value=(e.toString())
                }else
                {
                    callback.success("Authentication Successful ")
//                    notify.value="Authentication Successful "
                }


            }
        }






        fun createAndEnterRoom(callback: MyCallback) {

            val params = RoomParams(RoomType.SMALL_ROOM_FOR_VIDEO)
            SendBirdCall.createRoom(params, object : RoomHandler {
                override fun onResult(room: Room?, e: SendBirdException?) {
                    if (e != null) {

                       callback.fail(e.message.toString())
//                    _createdRoomId.postValue(Resource.error(e.message, e.code, null))

                    } else {

                        callback.success(room?.roomId.toString())

//                        notify("Room Created ${room?.roomId}")



                    }
                }
            })
        }



        fun fetchRoomById(roomId: String, callback: MyCallback) {
            if (roomId.isEmpty()) {
                return
            }
            SendBirdCall.fetchRoomById(roomId, object : RoomHandler {
                override fun onResult(room: Room?, e: SendBirdException?) {
                    if (e != null) {

                        callback.fail(e.message.toString())
                    } else {
                        callback.success(roomId)

                    }
                }
            })
        }




















    }


}