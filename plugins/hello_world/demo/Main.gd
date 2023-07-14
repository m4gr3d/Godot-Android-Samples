extends Node2D

var hello_world_plugin : HelloWorldPlugin

func _ready():
	hello_world_plugin = preload("res://addons/hello_world_plugin/interface/hello_world_plugin.gd").new()


func _on_Button_pressed():
	if hello_world_plugin:
		hello_world_plugin.helloWorld()
