@tool
extends EditorExportPlugin

func _supports_platform(platform):
	if platform is EditorExportPlatformAndroid:
		return true
	return false

func _get_android_libraries(platform, debug):
	if debug:
		return PackedStringArray(["hello_gdextension_plugin/.bin/debug/HelloGDExtension.debug.aar"])
	else:
		return PackedStringArray(["hello_gdextension_plugin/.bin/release/HelloGDExtension.release.aar"])

func _get_name():
	return "Hello GDExtension plugin"
