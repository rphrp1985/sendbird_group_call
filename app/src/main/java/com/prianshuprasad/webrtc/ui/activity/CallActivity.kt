package com.prianshuprasad.webrtc.ui.activity

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.prianshuprasad.socket.utils.epoxy.EpoxyController
import com.prianshuprasad.webrtc.GroupCallViewModel
import com.prianshuprasad.webrtc.R
import com.prianshuprasad.webrtc.databinding.ActivityCallBinding
import com.sendbird.calls.Participant
import com.sendbird.calls.SendBirdCall


class CallActivity : AppCompatActivity() {

    lateinit var binding: ActivityCallBinding
    lateinit var groupCallViewModel: GroupCallViewModel
    var roomId:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_call)

        supportActionBar?.hide()
       roomId = intent.getStringExtra("room").toString()

       groupCallViewModel= GroupCallViewModel(roomId)

        initView(roomId)

    }


    private fun initView(roomId: String) {
        val room = SendBirdCall.getCachedRoomById(roomId)

        // views
        binding.groupCallTextViewRoomId.text = roomId
        setAudioEnabledImage(room?.localParticipant?.isAudioEnabled ?: false)
        setVideoEnabledImage(room?.localParticipant?.isVideoEnabled ?: false)

        // RecyclerView
        initRecyclerView()

        // ViewModel
        observeViewModel()

        // events
        binding.groupCallImageViewSpeaker.setOnClickListener {
            showSelectingAudioDeviceDialog()
        }

        binding.groupCallImageViewCameraFlip.setOnClickListener {
//            viewModel.switchCamera()
        }

        binding.groupCallImageViewExit.setOnClickListener {
            groupCallViewModel.exit()
            finish()
        }


        binding.groupCallImageViewAudioOnOff.setOnClickListener {
            val isAudioEnabled = room?.localParticipant?.isAudioEnabled ?: return@setOnClickListener
            if (isAudioEnabled) {
                groupCallViewModel.muteMicrophone()
            } else {
                groupCallViewModel.unmuteMicrophone()
            }
        }

        binding.groupCallImageViewVideoOnOff.setOnClickListener {
            val isAudioEnabled = room?.localParticipant?.isAudioEnabled ?: return@setOnClickListener
            if (isAudioEnabled) {
                groupCallViewModel.stopLocalVideo()
            } else {
                groupCallViewModel.startLocalVideo()
            }
        }

        binding.groupCallImageViewExit.setOnClickListener {
            groupCallViewModel.exit()
        }
    }

    private fun initRecyclerView() {
        val width: Int = Resources.getSystem().displayMetrics.widthPixels
        val height: Int = (Resources.getSystem().displayMetrics.heightPixels*8)/10;
        val recyclerView = binding.groupCallRecyclerViewParticipants

        recyclerView.layoutManager = GridLayoutManager(this,1)
        val controller = EpoxyController(height,width)
        recyclerView.adapter= controller.adapter
        controller.requestModelBuild()

        groupCallViewModel.participants.observeForever {

            val x= ArrayList<Participant>()

            for(i in it)
            {    Toast.makeText(this@CallActivity,"${i.toString()}",Toast.LENGTH_SHORT).show()
                x.add(i);
           }
            controller.update(x)
            controller.requestModelBuild()

        }
    }

    private fun observeViewModel() {
        groupCallViewModel.localParticipant.observe(this) {
            if (it != null) {
                setAudioEnabledImage(it.isAudioEnabled)
                setVideoEnabledImage(it.isVideoEnabled)
            }
        }
//
//        groupCallViewModel.isExited.observe(viewLifecycleOwner) {
//            if (it.status == Status.SUCCESS) {
//                activity?.finish()
//            } else {
//                val message = it.message ?: return@observe
//                activity?.showToast(message)
//            }
//        }
//
//        viewModel.currentAudioDevice.observe(viewLifecycleOwner) {
//            if (it != null) {
//                val resource = when (it) {
//                    AudioDevice.EARPIECE -> R.drawable.icon_bluetooth_white
//                    AudioDevice.SPEAKERPHONE -> R.drawable.icon_speaker
//                    AudioDevice.WIRED_HEADSET -> R.drawable.icon_headset_white
//                    AudioDevice.BLUETOOTH -> R.drawable.icon_bluetooth_white
//                }
//
//                binding.groupCallImageViewSpeaker.setImageResource(resource)
//            }
//        }
    }

    private fun setAudioEnabledImage(isEnabled: Boolean) {
        binding.groupCallImageViewAudioOnOff.setImageResource(
            if (isEnabled) {
                R.drawable.ic_baseline_mic_24
            } else {
                R.drawable.ic_baseline_mic_off_24
            }
        )
    }

    private fun setVideoEnabledImage(isEnabled: Boolean) {
        binding.groupCallImageViewVideoOnOff.setImageResource(
            if (isEnabled) {
                R.drawable.ic_baseline_videocam_24
            } else {
                R.drawable.ic_baseline_videocam_off_24
            }
        )
    }

    private fun showSelectingAudioDeviceDialog() {
        val audioDevices = groupCallViewModel.getAvailableAudioDevices().toList()
        val currentAudioDevice = groupCallViewModel.currentAudioDevice.value
        val currentAudioDeviceIndex = audioDevices.indexOfFirst { it == currentAudioDevice }.let {
            if (it == -1) {
                return@let 0
            }

            it
        }
        val listItems = arrayOf(audioDevices.toTypedArray())

        var selectedIndex = 0

       val builder= AlertDialog.Builder(this)

            builder.setTitle("audio devices")
            .setNegativeButton(R.string.cancel, null)

            .create()
            .show()
    }




}