class_name HelloSignalsPlugin extends RefCounted

## Interface used to access the functionality provided by the HelloSignals plugin

var _hello_signals_singleton

func _init():
	if Engine.has_singleton("HelloSignals"):
		_hello_signals_singleton = Engine.get_singleton("HelloSignals")
	else:
		printerr("Couldn't find HelloSignals singleton")


## Register for the tiktok signals emitted
func registerForTikTok(callback: Callable) -> void:
	if _hello_signals_singleton:
		_hello_signals_singleton.connect("TikTok", callback)
	else:
		printerr("Unable to register for tiktok")


## Start tiktok if not started, otherwise stop it
func toggleTikTok():
	if _hello_signals_singleton:
		_hello_signals_singleton.onButtonPressed()
	else:
		printerr("Unable to toggle tiktok")
