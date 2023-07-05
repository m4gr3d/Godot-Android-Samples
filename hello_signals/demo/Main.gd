extends Node2D

var timerCount = 0
var timerRunning = false
var helloSignalsPlugin : HelloSignalsPlugin

func _ready():
	helloSignalsPlugin = preload("res://addons/hello_signals_plugin/interface/hello_signals_plugin.gd").new()
	helloSignalsPlugin.registerForTikTok(Callable(self, "_on_tiktok"))
		
	$Button.connect("pressed", Callable(self, "_on_Button_pressed"))


func _on_tiktok():
	print("TikTok signal received")
	timerCount = timerCount + 1
	$Label.text = str(timerCount)


func _on_Button_pressed():
	print("on button pressed from GDScript")
	helloSignalsPlugin.toggleTikTok()
	timerRunning = !timerRunning
	if (timerRunning):
		$Button.text = "Stop Timer"
	else:
		$Button.text = "Start Timer"
