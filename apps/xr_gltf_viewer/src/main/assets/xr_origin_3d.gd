extends XROrigin3D

var xr_interface: XRInterface = null
var world_environment: WorldEnvironment = null

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	world_environment = get_node("/root/World/WorldEnvironment")
	
	xr_interface = XRServer.find_interface("OpenXR")
	if xr_interface and xr_interface.is_initialized():
		var vp := get_viewport()
		vp.use_xr = true
		
		_enable_passthrough()


func _on_left_hand_button_pressed(button_name: String) -> void:
	if button_name == "menu_button":
		var app_plugin := Engine.get_singleton("XrAppPlugin")
		if app_plugin:
			print("Invoking GLTF menu")
			app_plugin.showItemsSelection()
		else:
			printerr("Unable to invoke GLTF menu")
		pass


func _enable_passthrough() -> bool:
	if xr_interface == null or !xr_interface.is_passthrough_supported():
		return false
	
	var vp := get_viewport()	
	vp.transparent_bg = true
	if !xr_interface.start_passthrough():
		vp.transparent_bg = false
		return false
	
	if world_environment:
		world_environment.environment.background_mode = Environment.BG_CLEAR_COLOR
		
	print("Passthrough enabled")
	return true


func _disable_passthrough() -> void:
	if xr_interface == null:
		return
	
	if xr_interface.is_passthrough_enabled():
		xr_interface.stop_passthrough()
	
	if world_environment:
		world_environment.environment.background_mode = Environment.BG_SKY
	
	get_viewport().transparent_bg = false
	print("Passthrough disabled")
