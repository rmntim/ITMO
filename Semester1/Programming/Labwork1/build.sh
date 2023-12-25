#!/usr/bin/bash

usage() {
    echo "Unknown command: $@"
    echo "Usage: build.sh build|run|clean"
}

build() {
    javac Main.java
    jar -cfe app.jar Main Main.class
}

run() {
    if [ ! -f ./app.jar ]; then
        build
    fi

    java -jar app.jar
}

clean() {
    rm app.jar Main.class
}

declare -A COMMANDS=(
    [main]=usage
    [build]=build
    [run]=run
    [clean]=clean
)

"${COMMANDS[${1:-main}]:-${COMMANDS[main]}}" "$@"
