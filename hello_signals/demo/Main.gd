extends Node2D

var timerCount = 0
var timerRunning = false
var helloSignals

func _ready():
	if Engine.has_singleton("HelloSignals"):
		helloSignals = Engine.get_singleton("HelloSignals")
		helloSignals.connect("TikTok", self, "_on_tiktok")
		
		$Button.connect("pressed", self, "_on_Button_pressed")
	else:
		print("Couldn't find HelloSignals singleton")


func _on_tiktok():
	print("TikTok signal received")
	timerCount = timerCount + 1
	$Label.text = str(timerCount)

func _on_Button_pressed():
	print("on button pressed from GDScript")
	helloSignals.onButtonPressed()
	timerRunning = !timerRunning
	if (timerRunning):
		$Button.text = "Stop Timer"
	else:
		$Button.text = "Start Timer"
