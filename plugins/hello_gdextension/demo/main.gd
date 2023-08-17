extends Node2D

func _ready():	
	var hello_gdextension_plugin = preload("res://addons/hello_gdextension_plugin/interface/hello_gdextension_plugin.gd").new()
	hello_gdextension_plugin.add_gdexample_node(get_path())
