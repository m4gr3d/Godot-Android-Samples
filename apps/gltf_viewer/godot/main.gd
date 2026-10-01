extends Node3D

# Reference to the gltf model that's currently being shown.
var current_gltf_node: Node3D = null

func _ready() -> void:
	# Default asset to load when the app starts
	_load_gltf("res://gltfs/food_kit/turkey.glb")
	
	var appPlugin := Engine.get_singleton("AppPlugin")
	if appPlugin:
		print("App plugin is available")

		# Signal fired from the app logic to update the gltf model being shown
		appPlugin.connect("show_gltf", _load_gltf)
	else:
		print("App plugin is not available")


func _process(_delta: float) -> void:
	if current_gltf_node == null:
		return
		
	# Make the gltf model slowly rotate
	current_gltf_node.rotate_y(0.001)


func _unhandled_input(event: InputEvent) -> void:
	if current_gltf_node == null:
		return
	
	if event is InputEventMagnifyGesture:
		# Scale the gltf model based on pinch / zoom gestures
		current_gltf_node.scale = current_gltf_node.scale * event.factor
		
	if event is InputEventMouseMotion and event.button_mask == MOUSE_BUTTON_MASK_LEFT:
		# Rotate the gltf model based on swipe gestures
		var relative_drag: Vector2 = event.relative
		current_gltf_node.rotate_y(relative_drag.x / 100)
		current_gltf_node.rotate_x(relative_drag.y / 100)


# Load the gltf model specified by the given path
func _load_gltf(gltf_path: String) -> void:
	if current_gltf_node != null:
		remove_child(current_gltf_node)
	
	current_gltf_node = load(gltf_path).instantiate()

	add_child(current_gltf_node)
