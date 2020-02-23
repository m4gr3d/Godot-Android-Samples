extends Node2D

var hello_world

func _ready():
	if Engine.has_singleton("HelloWorld"):
		hello_world = Engine.get_singleton("HelloWorld")


func _on_Button_pressed():
	if hello_world:
		hello_world.helloWorld()
