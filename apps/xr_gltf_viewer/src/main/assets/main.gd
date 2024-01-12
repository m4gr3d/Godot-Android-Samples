extends Node3D

@onready var gltf_holder: Node3D = $GLTFHolder
# Reference to the gltf model that's currently being shown.
var current_gltf_node: Node3D = null

# Called when the node enters the scene tree for the first time.
func _ready() -> void:
	var default_selection := "turkey"
	var app_plugin := Engine.get_singleton("XrAppPlugin")
	if app_plugin:
		print("App plugin is available")
		
		default_selection = app_plugin.getCurrentGLTFSelection()
		
		# Signal fired from the app logic to update the gltf model being shown
		app_plugin.connect("show_gltf", _load_gltf_model)
		
		if app_plugin.isXREnabled():
			_initialize_xr_interface()
		else:
			_initialize_pancake_interface()
	else:
		print("App plugin is not available")
	
	# Default asset to load when the app starts
	_load_gltf_model("res://gltfs/food_kit/" + default_selection + ".glb")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta: float) -> void:
	# Make the gltf model slowly rotate
	gltf_holder.rotate_y(0.001)


func _initialize_pancake_interface() -> void:
	print("Initializing pancake view")
	var pancake_view := load("res://pancake_view.tscn")
	var instance : Node = pancake_view.instantiate()
	add_child(instance)


func _initialize_xr_interface() -> void:
	print("Initializing xr view")
	var xr_scene := load("res://xr_origin_3d.tscn")
	var instance : Node = xr_scene.instantiate()
	add_child(instance)


func _load_gltf_model(gltf_path: String) -> void:
	if current_gltf_node != null:
		gltf_holder.remove_child(current_gltf_node)
	
	current_gltf_node = load(gltf_path).instantiate()
	gltf_holder.add_child(current_gltf_node)

