#!/usr/bin/env bash
# Runs all JUnit 5 tests. Uses Maven when available, otherwise the JUnit console launcher.
# Force the launcher: USE_CONSOLE=1 ./test.sh   (jar path: JUNIT_CONSOLE=/path/to/junit-platform-console-standalone.jar)
set -e
cd "$(dirname "$0")"
if command -v mvn >/dev/null 2>&1 && [ -z "$USE_CONSOLE" ]; then
  mvn -q test
else
  JAR="${JUNIT_CONSOLE:-/usr/share/java/junit-platform-console-standalone.jar}"
  rm -rf out && mkdir -p out/main out/test
  javac -d out/main $(find src/main/java -name '*.java')
  javac -cp "$JAR:out/main" -d out/test $(find src/test/java -name '*.java')
  java -jar "$JAR" --class-path out/main:out/test --scan-class-path out/test --disable-banner --disable-ansi-colors
fi
