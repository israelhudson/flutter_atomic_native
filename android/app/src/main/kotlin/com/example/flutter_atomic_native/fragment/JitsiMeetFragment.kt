package com.example.flutter_atomic_native.fragment

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.flutter_atomic_native.R
import com.facebook.react.modules.core.PermissionListener
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.BroadcastReceiver
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetOngoingConferenceService
import org.jitsi.meet.sdk.JitsiMeetView
import org.jitsi.meet.sdk.log.JitsiMeetLogger
import java.net.URL

class JitsiMeetFragment (context: Context): Fragment(), JitsiMeetActivityInterface {

    private var jitsiMeetView: JitsiMeetView? = null

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver(context) {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> onConferenceJoined(event.data)
                BroadcastEvent.Type.CONFERENCE_WILL_JOIN -> onConferenceWillJoin(event.data)
                BroadcastEvent.Type.CONFERENCE_TERMINATED -> onConferenceTerminated(event.data)
                BroadcastEvent.Type.PARTICIPANT_JOINED -> onParticipantJoined(event.data)
                BroadcastEvent.Type.PARTICIPANT_LEFT -> onParticipantLeft(event.data)
                BroadcastEvent.Type.READY_TO_CLOSE -> onReadyToClose()
                else -> {}
            }
        }
    }

    protected fun onConferenceJoined(extraData: HashMap<String?, Any?>) {
        JitsiMeetLogger.i("Conference joined: $extraData")
        // Launch the service for the ongoing notification.
        JitsiMeetOngoingConferenceService.launch(this.context, extraData)

    }

    protected fun onConferenceTerminated(extraData: HashMap<String?, Any?>) {
        JitsiMeetLogger.i("Conference terminated: $extraData")
    }

    protected fun onConferenceWillJoin(extraData: HashMap<String?, Any?>) {
        JitsiMeetLogger.i("Conference will join: $extraData")
    }

    protected fun onParticipantJoined(extraData: HashMap<String?, Any?>?) {
        try {
            JitsiMeetLogger.i("Participant joined: ", extraData)
        } catch (e: Exception) {
            JitsiMeetLogger.w("Invalid participant joined extraData", e)
        }
    }

    protected fun onParticipantLeft(extraData: HashMap<String?, Any?>?) {
        try {
            JitsiMeetLogger.i("Participant left: ", extraData)
        } catch (e: Exception) {
            JitsiMeetLogger.w("Invalid participant left extraData", e)
        }
    }

    protected fun onReadyToClose() {
        JitsiMeetLogger.i("SDK is ready to close")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflating layout for the fragment
        return inflater.inflate(R.layout.fragment_jitsi_meet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jitsiMeetView = JitsiMeetView(requireContext())
        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.minhacademia.com.br/"))
            .setRoom("poctestesisrael2222")
            .setAudioMuted(true)
            .setVideoMuted(true)
            .setFeatureFlag("welcomepage.enabled", false)
            .setFeatureFlag("resolution", 360)
            //.setFeatureFlag("toolbox.enabled", false)
            .setFeatureFlag("server-url-change.enabled", true)
            .build()
        jitsiMeetView?.join(options)

        // Add JitsiMeetView to the layout
        val jitsiMeetContainer: FrameLayout = view.findViewById(R.id.jitsi_meet_container)
        jitsiMeetContainer.addView(jitsiMeetView)

        val intentFilter = IntentFilter()

        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(view.context).registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Dispose of JitsiMeetView
        jitsiMeetView?.dispose()
        jitsiMeetView = null
    }

    override fun checkPermission(p0: String?, p1: Int, p2: Int): Int {
        TODO("Not yet implemented")
    }

    override fun checkSelfPermission(p0: String?): Int {
        TODO("Not yet implemented")
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        TODO("Not yet implemented")
    }
}
