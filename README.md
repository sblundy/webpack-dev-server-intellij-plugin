## Webpack Dev Server Plugin for IntelliJ IDEA

This a plugin to integrate [webpack](https://webpack.github.io/) into [IntelliJ](https://www.jetbrains.com/idea/).

## Motivation

This was inspired by the [Webpack-Dashboard](https://github.com/FormidableLabs/webpack-dashboard) project. You'll be able
to run a webpack dev server within IntelliJ complete with stats, status, and othe useful information.

## Developing

Download the project
```shell
git clone https://github.com/sblundy/webpack-dev-server-intellij-plugin.git
```

1. Now create a new IntelliJ project using File -> New -> Project from Existing Sources...
1. Select `webpack-dev-server-intellij-plugin/build.gradle`
1. Click Ok

Alternatively, you can create it directly by File -> New -> Project from VCS -> Github

### Building

To compile, test, and package the plugin, simply run:
```shell
./gradlew build
```

We're using [Gradle](https://gradle.org/), however it is not necessary you download it. The 
[wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) handles everything. 

### Run Local Sandbox

The Gradle [plugin](https://github.com/JetBrains/gradle-intellij-plugin) were using has a task that builds and runs the 
plugin in a sandbox environment. To run the sandbox, it's just one command.

```shell
./gradlew runIdea
```

This will start a new, default IntelliJ instance with this plugin installed. This will be a completely different 
environment from your day-to-day IntelliJ. This is for developing and testing the plugin out.

### Installation from Local

If you wish to install this plugin for actual use:

1. Build the project by `./gradlew build`
1. In IntelliJ, open Preferences.
1. Goto 'Plugins'
1. Click on 'Install plugin from disk...'
1. Select zip file from `WebpackDevServer/build/distributions/WebpackDevServer.zip`

## License

MIT