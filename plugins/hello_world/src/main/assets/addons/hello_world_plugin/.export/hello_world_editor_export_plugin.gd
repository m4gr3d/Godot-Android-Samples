@tool
extends EditorExportPlugin

func _supports_platform(platform):
	if platform is EditorExportPlatformAndroid:
		return true
	return false
	
func _get_android_libraries(platform, debug):
	if debug:
		return PackedStringArray(["hello_world_plugin/.bin/debug/HelloWorld.debug.aar"])
	else:
		return PackedStringArray(["hello_world_plugin/.bin/release/HelloWorld.release.aar"])
	
func _get_name():
	return "HelloWorldPlugin"
