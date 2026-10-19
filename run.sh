#!/usr/bin/env bash
# Compiles the sources with plain javac (no Maven needed) and runs the demo.
set -e
cd "$(dirname "$0")"
rm -rf out/main && mkdir -p out/main
javac -d out/main $(find src/main/java -name '*.java')
java -cp out/main telemedicine.demo.Main
