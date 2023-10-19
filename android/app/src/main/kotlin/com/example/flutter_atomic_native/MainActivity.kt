package com.example.flutter_atomic_native

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.modules.core.PermissionListener
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface

class MainActivity: FlutterFragmentActivity(), JitsiMeetActivityInterface {
    override fun configureFlutterEngine(flutterEngine: io.flutter.embedding.engine.FlutterEngine) {
//        flutterEngine
//            .platformViewsController
//            .registry
//            .registerViewFactory("com.example.app/my-fragment", MyFragmentViewFactory())
        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("com.example.app/jitsi-fragment", JitsiMeetFragmentViewFactory())
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(this, requestCode, resultCode, data)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        JitsiMeetActivityDelegate.onBackPressed()
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        JitsiMeetActivityDelegate.onNewIntent(intent)
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        if (p0 != null) {
            super.requestPermissions(p0, p1)
        }
    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }
}

class JitsiMeetFragmentViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        return JitsiMeetFragmentView(context)
    }
}

class JitsiMeetFragmentView(private val context: Context) : PlatformView {
    private val frameLayout = FrameLayout(context)

    init {
        frameLayout.id = View.generateViewId()  // Gere e defina um ID para o FrameLayout
        val fragmentManager = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            is ContextWrapper -> (context.baseContext as? FragmentActivity)?.supportFragmentManager
            else -> null
        }

        fragmentManager?.beginTransaction()?.replace(frameLayout.id, JitsiMeetFragment())?.commit()
    }

    override fun getView(): View {
        return frameLayout
    }

    override fun dispose() {}

}

class MyFragmentViewFactory : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        return MyFragmentView(context)
    }
}

class MyFragmentView(private val context: Context) : PlatformView {
    private val frameLayout = FrameLayout(context)

    init {
        frameLayout.id = View.generateViewId()  // Gere e defina um ID para o FrameLayout
        val fragmentManager = when (context) {
            is FragmentActivity -> context.supportFragmentManager
            is ContextWrapper -> (context.baseContext as? FragmentActivity)?.supportFragmentManager
            else -> null
        }

        fragmentManager?.beginTransaction()?.replace(frameLayout.id, ExampleFragment())?.commit()
    }

    override fun getView(): View {
        return frameLayout
    }

    override fun dispose() {}

}
