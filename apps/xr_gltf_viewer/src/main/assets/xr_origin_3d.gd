extends XROrigin3D

var xr_interface: XRInterface = null

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	xr_interface = XRServer.find_interface("OpenXR")
	if xr_interface and xr_interface.is_initialized():
		var vp := get_viewport()
		vp.use_xr = true


func _on_left_hand_button_pressed(name: String) -> void:
	if name == "menu_button":
		var app_plugin := Engine.get_singleton("XrAppPlugin")
		if app_plugin:
			print("Invoking GLTF menu")
			app_plugin.showItemsSelection()
		else:
			printerr("Unable to invoke GLTF menu")
		pass
