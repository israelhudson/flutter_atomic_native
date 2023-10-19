package com.example.flutter_atomic_native.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.flutter_atomic_native.R
import com.facebook.react.modules.core.PermissionListener
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView
import java.net.URL

class JitsiMeetFragment : Fragment(), JitsiMeetActivityInterface {

    private var jitsiMeetView: JitsiMeetView? = null

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
