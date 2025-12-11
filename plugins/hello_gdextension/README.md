## Hello GDExtension plugin

Showcase how to build a Godot Android GDExtension plugin

### Setup

Clone the project using `git`:

```
git clone https://github.com/m4gr3d/Godot-Android-Samples.git
```

The sample has a dependency on the `godot-cpp` library, which is included as a Git submodule. 
After cloning the sample, you'll need to execute the following commands to set up the submodule:

```
cd Godot-Android-Samples
git submodule update --init
```

### Building the C++ bindings

To generate and compile the C++ bindings, use the following commands.

```
cd Godot-Android-Samples/plugins/hello_gdextension/godot-cpp
scons platform=android target=template_debug
scons platform=android target=template_release
```

When it's completed, you should have static libraries stored in 
`plugins/hello_gdextension/godot-cpp/bin/` that will be used for 
compilation by the plugin.

### Building the Hello GDExtension plugin

Use the following commands to build the plugin:

```
cd Godot-Android-Samples
./gradlew :plugins:hello_gdextension:assemble
```

The generated artifact can be found under [`demo/addons`](demo/addons).

### Usage

Open the [`demo`](demo) project in the Godot Editor

**Note:**

It's recommended to generate a version of the gdextension binary for the platform you're running 
the Godot Editor onto. To do so:

```
cd Godot-Android-Samples/plugins/hello_gdextension
scons -j4
```

Update [`src/main/assets/addons/hello_gdextension_plugin/hello_gdextension.gdextension`](src/main/assets/addons/hello_gdextension_plugin/hello_gdextension.gdextension) 
with the path to the generated binary. 
