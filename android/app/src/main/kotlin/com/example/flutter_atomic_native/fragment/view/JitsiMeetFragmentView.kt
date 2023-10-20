package com.example.flutter_atomic_native.fragment.view

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.example.flutter_atomic_native.fragment.JitsiMeetFragment
import io.flutter.plugin.platform.PlatformView

class JitsiMeetFragmentView(private val context: Context) : PlatformView {
    private val frameLayout = FrameLayout(context)

    init {
        frameLayout.id = View.generateViewId()  // Gere e defina um ID para o FrameLayout
        val fragmentManager = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            is ContextWrapper -> (context.baseContext as? FragmentActivity)?.supportFragmentManager
            else -> null
        }

        fragmentManager?.beginTransaction()?.replace(frameLayout.id, JitsiMeetFragment(context))?.commit()
    }

    override fun getView(): View {
        return frameLayout
    }

    override fun dispose() {}

}
