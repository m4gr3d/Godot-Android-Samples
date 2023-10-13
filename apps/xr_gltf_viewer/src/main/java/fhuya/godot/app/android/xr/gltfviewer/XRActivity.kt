package fhuya.godot.app.android.xr.gltfviewer

import android.os.Bundle
import org.godotengine.godot.GodotActivity
import org.godotengine.godot.xr.XRMode

class XRActivity : GodotActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getCommandLine() : List<String> {
        val commandLines = mutableListOf<String>()
        commandLines.addAll(super.getCommandLine())

        commandLines.add(XRMode.OPENXR.cmdLineArg)
        commandLines.add("--xr-mode")
        commandLines.add("on")
        return commandLines
    }
}
