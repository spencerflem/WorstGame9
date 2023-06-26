# Summary

Our contribution to [Game Breaker's Toolkit Jam 9](https://itch.io/jam/game-breakers-toolkit-jam-9).

# More Details

This is a platformer infested with glorious, awful ads and other nonsense. Good luck.

# Running the Project
To run the project, do the following:
* Clone the repository.
* Install OpenJDK 17+. We recommend [Adoptium's](https://adoptium.net/temurin/releases/?version=18).
* Open the project in your IDE. We recommend IntelliJ, Android Studio, or VS Code with java extensions installed.
* Run the `desktop:run` Gradle task.

# TODO
* some way of moving between levels
* an intro screen?
* enimies?
* spikes
* what happens on death?
* multiple windows
* add cool effects
* add cool sfx
* .. add more here if you think of anything

# WorstGame9

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

This project was generated with a template including simple application launchers and a main class extending `Game` that sets the first screen.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `desktop`: Primary desktop platform using LWJGL3.

## Gradle

This project uses [Gradle](http://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `desktop:jar`: builds application's runnable jar, which can be found at `desktop/build/libs`.
- `desktop:run`: starts the application.
- `desktop:jpackageImage`: packages as an platform specific executable.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
