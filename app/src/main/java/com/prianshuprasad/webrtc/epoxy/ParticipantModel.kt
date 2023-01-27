package com.prianshuprasad.socket.utils.epoxy

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.*
import com.prianshuprasad.webrtc.R
import com.sendbird.calls.Participant


@EpoxyModelClass( layout = R.layout.item_users)
abstract class ParticipantModel :   EpoxyModelWithHolder<ParticipantModel.Holder>(){


    @EpoxyAttribute
    lateinit var participant:Participant

    @EpoxyAttribute
    var height:Int=0


    @EpoxyAttribute
    var width:Int=0


    override fun bind(holder: Holder) {

        holder.userid.text= participant.participantId
        participant.videoView= holder.sendbird
        holder.layouts.layoutParams.height= height
        holder.layouts.layoutParams.width= width
//        holder.sendbird.requestFocus()

//        holder.layouts.layoutParams.height= height.toInt()
//        holder.layouts.layoutParams.width= width

    }


    inner class Holder : EpoxyHolder(){

        lateinit var sendbird:com.sendbird.calls.SendBirdVideoView
        lateinit var userid:TextView
        lateinit var profile:ImageView
        lateinit var layouts: LinearLayout




        override fun bindView(itemView: View) {

            sendbird= itemView?.findViewById(R.id.participant_sendbird_video_view)!!
            userid = itemView?.findViewById(R.id.participant_text_view_user_id)!!
            profile = itemView?.findViewById(R.id.participant_image_view_profile)!!
            layouts= itemView?.findViewById(R.id.participant_linear_layout)!!




        }

    }

    override fun getDefaultLayout(): Int {
       return R.layout.item_users
    }



}

