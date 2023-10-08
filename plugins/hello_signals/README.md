## Hello Signals plugin

Showcases how to build a Godot Android plugin which is invoked from gdscript and emit signals 
from the java logic.

### Building the Hello Signals plugin

Use the following commands to build the plugin:

```
cd Godot-Android-Samples
./gradlew :plugins:hello_signals:assemble
```

The generated artifact can be found under [`demo/addons`](demo/addons).

### Usage

Open the [`demo`](demo) project in the Godot Editor
