class_name HelloGDExtensionPlugin extends RefCounted

## Interface used to access the functionality provided by the HelloGDExtension plugin

var _hello_gdextension_singleton

func _init():
	if Engine.has_singleton("HelloGDExtension"):
		_hello_gdextension_singleton = Engine.get_singleton("HelloGDExtension")
	else:
		printerr("Couldn't find HelloGDExtension singleton")


## Add a GDExample node
func add_gdexample_node(parent_node_path: NodePath):
	if _hello_gdextension_singleton:
		_hello_gdextension_singleton.addGDExampleNode(parent_node_path)
	else:
		printerr("Unable to add gdexample node")

## Update the visibility of the gdexample node
func set_gdexample_visible(visible: bool):
	if _hello_gdextension_singleton:
		_hello_gdextension_singleton.setGDExampleVisible(visible)
	else:
		printerr("Unable to update gdexample visibility")
