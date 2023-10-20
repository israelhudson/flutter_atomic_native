package com.example.flutter_atomic_native.fragment.broadcast

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.BroadcastReceiver
import org.jitsi.meet.sdk.JitsiMeetOngoingConferenceService
import org.jitsi.meet.sdk.log.JitsiMeetLogger

open class MeetBroadcastManager(private val context: Context) : BroadcastReceiver(context) {

    private val localBroadcastManager = LocalBroadcastManager.getInstance(context)

    init {
        val intentFilter = IntentFilter()
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }
        localBroadcastManager.registerReceiver(this, intentFilter)
    }

    fun unregister() {
        localBroadcastManager.unregisterReceiver(this)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
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

    private fun onConferenceJoined(extraData: HashMap<String?, Any?>) {
        JitsiMeetLogger.i("Conference joined: $extraData")
        // Launch the service for the ongoing notification.
        JitsiMeetOngoingConferenceService.launch(context, extraData)

    }

    private fun onConferenceTerminated(extraData: HashMap<String?, Any?>) {
        JitsiMeetOngoingConferenceService.abort(context)
        JitsiMeetLogger.i("Conference terminated: $extraData")
    }

    private fun onConferenceWillJoin(extraData: HashMap<String?, Any?>) {
        JitsiMeetLogger.i("Conference will join: $extraData")
    }

    private fun onParticipantJoined(extraData: HashMap<String?, Any?>?) {
        try {
            JitsiMeetLogger.i("Participant joined: ", extraData)
        } catch (e: Exception) {
            JitsiMeetLogger.w("Invalid participant joined extraData", e)
        }
    }

    private fun onParticipantLeft(extraData: HashMap<String?, Any?>?) {
        try {
            JitsiMeetLogger.i("Participant left: ", extraData)
        } catch (e: Exception) {
            JitsiMeetLogger.w("Invalid participant left extraData", e)
        }
    }

    private fun onReadyToClose() {
        JitsiMeetLogger.i("SDK is ready to close")
    }
}
