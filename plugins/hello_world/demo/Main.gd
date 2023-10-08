extends Node2D

var hello_world_plugin : HelloWorldPlugin

func _ready():
	hello_world_plugin = preload("res://addons/hello_world_plugin/hello_world_plugin_interface.gd").new()


func _on_Button_pressed():
	if hello_world_plugin:
		hello_world_plugin.helloWorld()
