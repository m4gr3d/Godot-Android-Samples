extends Node3D

@onready var gltf_holder: Node3D = $GLTFHolder


func _unhandled_input(event: InputEvent) -> void:
	if gltf_holder == null:
		print("Invalid reference to gltf holder")
		return
	
	if event is InputEventMagnifyGesture:
		# Scale the gltf model based on pinch / zoom gestures
		gltf_holder.scale = gltf_holder.scale * event.factor
		
	if event is InputEventMouseMotion and event.button_mask == MOUSE_BUTTON_MASK_LEFT:
		# Rotate the gltf model based on swipe gestures
		var relative_drag: Vector2 = event.relative
		gltf_holder.rotate_y(relative_drag.x / 100)
		gltf_holder.rotate_x(relative_drag.y / 100)
