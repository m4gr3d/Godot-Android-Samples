extends Node3D

var xr_interface: XRInterface = null

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
	if current_gltf_node == null:
		return
	
	# Make the gltf model slowly rotate
	current_gltf_node.rotate_y(0.001)


func _initialize_xr_interface() -> void:
	xr_interface = XRServer.find_interface("OpenXR")
	if xr_interface and xr_interface.is_initialized():
		var vp := get_viewport()
		vp.use_xr = true


func _load_gltf_model(gltf_path: String) -> void:
	if current_gltf_node != null:
		remove_child(current_gltf_node)
	
	current_gltf_node = load(gltf_path).instantiate()
	add_child(current_gltf_node)


func _connect_to_app_plugin() -> void:
	var app_plugin := Engine.get_singleton("AppPlugin")
	if app_plugin:
		print("App plugin is available")
		
		# Signal fired from the app logic to update the gltf model being shown
		app_plugin.connect("show_gltf", _load_gltf_model)
	else:
		print("App plugin is not available")
