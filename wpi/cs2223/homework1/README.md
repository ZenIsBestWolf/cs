# Double Trouble
*A quick and easy Nim game!*

## About
Double Trouble is a [Nim](https://en.wikipedia.org/wiki/Nim) game.
Players take turns removing items from piles and the last person to take the last item wins.
In this version of Nim, there are three piles.
- 3 green markers
- 7 yellow markers
- 5 orange markers

Take as many as you'd like from any one pile! But you must at least take one.

A computer will be your opponent in this game, and be weary, it will play perfectly!

## Instructions
In order to play, you need [Java](https://openjdk.org/) installed.
Then just run the jar file in your terminal.
#### Debug Mode
There is a debug mode available for examining features of the game, both for TAs testing it
and for the developer to root out issues and insure correct play.
Debug mode can be accessed by appending `--debug SEED` replacing "SEED" with any integer.
This sets the random seed of the computer player, so reliable output can be tested when the computer
is forced into a random move. Please only enable this for debugging, as otherwise it will make the game
appear quite messy (extra logging) and leak what the computer is "thinking", as well as strategy for the player.

### Windows
On Windows, check if you have Java properly installed by doing the following:
1. Press the Windows Key + R at the same time to open the "Run" dialog box.
2. Type `cmd` and hit Run.
3. In the Command Prompt window, run `java --version`.

If you get an error or are running a very old version of Java, you should install Java 17.
1. Reopen the Command Prompt if you closed it.
2. Run `winget install OpenJDK.Java17.Java`

Now that you have Java installed, just run `java -jar DTGame.jar` to play.

### macOS
On macOS, check if you have Java properly installed by doing the following:
1. Using Spotlight or by navigating to your Applications folder in Finder, open Terminal.
2. Run `java --version`.

If you get an error or are running a very old version of Java, you should install a newer version.
You can do this either by acquiring an installer on your own, or with Homebrew.
`brew install zulu17` will install Zulu Java 17 from Azul.
Once you have Java installed, just run `java -jar DTGame.jar` to play.

### Linux
If you are running Linux you're expected to know what you're doing. I don't need to explain this to you.
1. Check if you have Java with `java --version`.
    - If you don't, install it with `apt`, `pacman`, or whatever your package manager of choice is.
2. Once you have Java installed, just run `java -jar DTGame.jar` to play.

## Compiling
In order to compile a fresh copy on macOS and Linux, just run `./make.sh`.
On Windows, run `make.bat` (although it is untested).

## Explore!
The source code is available in the `src/dev/dignan/DoubleTrouble` directory for your enjoyment.