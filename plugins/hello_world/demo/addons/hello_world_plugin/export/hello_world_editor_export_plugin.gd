@tool
extends EditorExportPlugin

func _supports_platform(platform):
	if platform is EditorExportPlatformAndroid:
		return true
	return false
	
func _get_android_libraries(platform, debug):
	if debug:
		return PackedStringArray(["hello_world_plugin/export/HelloWorld.debug.aar"])
	else:
		return PackedStringArray(["hello_world_plugin/export/HelloWorld.release.aar"])
	
func _get_name():
	return "HelloWorldPlugin"
