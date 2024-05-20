class_name HelloWorldPlugin extends RefCounted

## Interface used to access the functionality provided by this plugin

var _hello_world_singleton

func _init():
	if Engine.has_singleton("HelloWorld"):
		_hello_world_singleton = Engine.get_singleton("HelloWorld")
	else:
		printerr("Initialization error: unable to access the java logic")

## Toggle between showing and hiding the hello world text
func helloWorld():
	if _hello_world_singleton:
		_hello_world_singleton.helloWorld()
	else:
		printerr("Initialization error")
