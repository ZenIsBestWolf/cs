#!/bin/zsh

rm -rf bin
javac -d bin src/dev/dignan/DoubleTrouble/*.java
cd bin
jar cvfe DTGame.jar dev.dignan.DoubleTrouble.Main dev/dignan/DoubleTrouble/*.class
mv DTGame.jar ..
exit