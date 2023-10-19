package com.example.flutter_atomic_native

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class MainActivity: FlutterFragmentActivity() {
    override fun configureFlutterEngine(flutterEngine: io.flutter.embedding.engine.FlutterEngine) {
        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("com.example.app/my-fragment", MyFragmentViewFactory())
    }
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
