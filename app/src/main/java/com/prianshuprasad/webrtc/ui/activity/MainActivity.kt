package com.prianshuprasad.webrtc.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.prianshuprasad.webrtc.R
import com.prianshuprasad.webrtc.ui.fragmnet.DashboardFragment
import com.prianshuprasad.webrtc.ui.viewmodel.MainActivityViewModel
import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomHandler
import org.webrtc.ContextUtils

class MainActivity : AppCompatActivity() {
    private val dashboardFragment= DashboardFragment()
    private val mainActivityViewModel =MainActivityViewModel()


//    val userId= "jj"
//    val token = "536c698a1390e4d39970875d834f2faf1d3a811a";

    val userId= "sendbird_desk_agent_id_fd4986a8-19a8-4958-bd9f-c136a60e73e0";
    val token ="30692c5d372e1ae2f7e3bb625276929a44427ff4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SendBirdCall.init(getApplicationContext(), "093728BC-C4B3-4F01-9203-9426BB7DB46D");

        observe()

        mainActivityViewModel.auth(userId,token)

        openDashBoard()

    }

    fun createAndEnterRoom(){
        mainActivityViewModel.createAndEnterRoom()
    }

    fun fetchRoomById(roomId: String) {

        mainActivityViewModel.fetchRoomById(roomId)


    }


    fun openDashBoard(){

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame,
            dashboardFragment).commit()
    }



    fun observe(){
        mainActivityViewModel.notify.observeForever {
            if(it!=null)
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()

        }

        mainActivityViewModel.authDetails.observeForever {
            if(it.bool)
             openPreview(it.data)
        }

    }
    fun openPreview(roomId: String){
        val intent= Intent(this@MainActivity, PreviewActivity::class.java)
        intent.putExtra("room",roomId)
        startActivity(intent)
    }


}