package com.example.flutter_atomic_native.fragment.factory

import android.content.Context
import com.example.flutter_atomic_native.fragment.view.JitsiMeetFragmentView
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class JitsiMeetFragmentViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        return JitsiMeetFragmentView(context)
    }
}
