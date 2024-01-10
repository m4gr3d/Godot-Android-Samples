extends Node3D

var xr_interface: XRInterface = null

@onready var gltf_holder: Node3D = $GLTFHolder
# Reference to the gltf model that's currently being shown.
var current_gltf_node: Node3D = null

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	_initialize_xr_interface()
	
	# Default asset to load when the app starts
	_load_gltf_model("res://gltfs/food_kit/turkey.glb")
	
	_connect_to_app_plugin()


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta: float) -> void:
	# Make the gltf model slowly rotate
	gltf_holder.rotate_y(0.001)


func _initialize_xr_interface() -> void:
	xr_interface = XRServer.find_interface("OpenXR")
	if xr_interface and xr_interface.is_initialized():
		var vp := get_viewport()
		vp.use_xr = true


func _load_gltf_model(gltf_path: String) -> void:
	if current_gltf_node != null:
		gltf_holder.remove_child(current_gltf_node)
	
	current_gltf_node = load(gltf_path).instantiate()
	gltf_holder.add_child(current_gltf_node)


func _connect_to_app_plugin() -> void:
	var app_plugin := Engine.get_singleton("XrAppPlugin")
	if app_plugin:
		print("App plugin is available")
		
		# Signal fired from the app logic to update the gltf model being shown
		app_plugin.connect("show_gltf", _load_gltf_model)
	else:
		print("App plugin is not available")


func _on_left_hand_button_pressed(name: String) -> void:
	if name == "menu_button":
		var app_plugin := Engine.get_singleton("XrAppPlugin")
		if app_plugin:
			print("Invoking GLTF menu")
			app_plugin.showItemsSelection()
		else:
			printerr("Unable to invoke GLTF menu")
		pass
