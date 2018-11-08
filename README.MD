# Android app bootstrap

An empty android (kotlin) app pre-configured with Dagger, Retrofit, Room and Jetpack.

## Requirements

As this project uses Jetpack (a.k.a AndroidX) it requires you to have:

- Android Studio 3.2+
- JDK 8+

## Usage

It's recommended that you clone the project and export it to an other folder

    git clone https://github.com/alphamu/android-app-bootstrap-kotlin.git
    cd android-app-bootstrap-kotlin
    git archive master | tar -x -C /somewhere/else

### Change package name

If you want to change the package name, it is recommended to do so
in Android Studio. First, create the package structure you want.
Then modify the files below to point to your new package.

- AndroidManifest.xml
    - package
- app/build.gradle
    - applicationId

Finally, using Android Studio's refactor feature, move the contents of
`com.alimuzaffar.blank` to your new package.

## Build the app

As always, you can just use `./gradlew assemble` to build all variants
of the app.

## Build using docker

- CD into the directory with the source

- If you want to download the existing docker image, run:

      docker pull alimuzaffar/android-build:28_0_3

- If you want to build or create the docker image, run:

      docker build -t android-build:android-gradle .

- Build the app in the docker container with:

      # Using the downloaded container
      docker run -it -v $PWD:/home/gradle/app -w /home/gradle/app alimuzaffar/android-build:28_0_3 ./gradlew assembleProdRelease

      # Using the built container
      docker run -it -v $PWD:/home/gradle/app -w /home/gradle/app android-build:android-gradle ./gradlew assembleProdRelease

NOTE: On Windows replace `$PWD` with `%cd%`

### IMPORTANT
Make sure local.properties does not contain sdk.dir. This will override the environment variable which points
to the android SDK and the app will not build.


Alternatively, you can drop down to bash in the docker container and run the commands manually.

    docker run -it -v $PWD:/home/gradle/app alimuzaffar/android-build:28_0_3 bash
    # OR
    docker run -it -v $PWD:/home/gradle/app android-build:dandroid-gradle bash


### Command explained

    -t              : Allocate a pseudo-tty
    -i              : Keep STDIN open even if not attached
    -v, --volume=[host-src:]container-dest[:<options>]: Bind mount a volume.
    -w=""           : Working directory inside the container

Basically, run a pseudo terminal, mount the current directory to an `app` folder in the docker container, move into that container and run the `./gradlew` command.
