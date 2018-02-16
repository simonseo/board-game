# Homework 1: Connect Four

The primary objective of this homework is to develop the game of
[Connect Four](https://en.wikipedia.org/wiki/Connect_Four). This is a
two-player game in which players take turns putting chips on a
grid. Once a player has four chips in-a-row they win. See the game's
original [television commercial](https://youtu.be/KN3nohBw_CE) for a
visual summary.

Aside from game development, this homework should get you comfortable
with unit testing---you'll notice a JUnit file buried in the test
package---and with the [observer design
pattern](https://en.wikipedia.org/wiki/Observer_pattern).

## Classes

Package | Class | Extends | Implements | Summary
 --- | --- | --- | --- | ---
api  | View | | Observer | Handles game I/O.
   | | Game | Observable | | Manages the internal game state.
   | | Chip | | | Represents chips on a board (type contained with board cells).
impl.game | ConnectFour | Game | | ConnectFour implementation of `Game`.
impl.view | Console | View | | Game interaction through the console.
exc | GameIndexOutOfBoundsException | IndexOutOfBoundsException | | Bad placement requests.
  | | GameStateException | Exception | | Requests of the model that cannot be answered due to the state of the game.

Your job is to implement classes in `impl.game` and `impl.view` using
the classes in API. You may also add files to `test.unit` if you like
(more below). *Do not* alter files in any other package, or add files
to any other package.

## Behavioral Specification

### Game

Connect Four is game involving two players played on a six-row,
seven-column board. Initially, the board is empty. Players then take
turns adding chips of their respective color to the board. Once four
chips in-a-row have been made by a single player---horizontally,
diagonally, or vertically---the game is over. If there are no empty
cells on the board, the game is declared a tie.

#### `placeChip`

Chips can be placed in columns zero through six inclusive. Given an
integer in this range, the chip carried by the current player should
"fall" to the first open row closest to the boards bottom. Requests
for places outside of the valid range, or to columns that are full,
should throw a `GameIndexOutOfBoundsException`. Requests to a game
that is over should throw a `GameStateException`.

This method should not only place chips on the board, but alter
players depending on the state of the game. In particular it should
change the current player and, if necessary, the winning player as
well.

#### `getChip`

Return the chip currently placed at a given coordinate. If a player
were physically sitting at a table viewing an upright Connect Four
board, the upper left corner would correspond to row zero, column
zero; while the lower right corner would correspond to row five,
column six.

#### `getWinningPlayer`

Return the player who has been declared the winner of this
game. Throws a `GameStateException` if the request has been made when
no winner has been determined.

#### `getCurrentPlayer`

Return the current player. If the game is a tie, the return value
should correspond to the empty chip; it should be the non-empty chip
otherwise.

#### `isGameOver`

The game is over if it is a tie or a winner has been determined.

### View

The user should interact with the game through the view via the
command line. The view observes the game, updating what it shows to
the user based its observations (notifications). The view maintains an
[*aggregate*](https://en.wikipedia.org/wiki/Object_composition#Aggregation)
relationship with the game.

#### `render`

Draw the board on the screen in a manner that resembles a standing
board. It should be clear which player occupies a given cell.

## Expectations

### View (30 points)

The view must accurately update the visualization of the board and
give players turns. The system test takes care of announcing the end
result of the game; which should run as expected, annoucing ties or
wins. The view and the model should interact entirely via the Java
Observer infrastructure. Specifically, this means no loops should be
involved.

### Model (60 points)

The game must pass a collection of unit tests based on the specified
behavior. Fractional points will be deducted for each failed test. A
basic test is provided to get your started. You should at least pass
this!

No points will awarded for code that fails to compile during the
testing phase.

### Practices (10 points)

We continue to expect "good general habits" and good "usage of
GitHub." Please see the [previous
homework](https://github.abudhabi.nyu.edu/jsw7/blackjack/tree/hw1#grading)
for what this entails.

## Testing

The `test` package comes with an overall system test and a series of
unit tests. You can use the system test for sanity checking, but your
focus should be the unit tests. There are several standard tests you
must pass, but you should also create your own tests for areas that
might not be covered.

Within the test directory, there is a sub-directory for unit tests
(src/tests/unit). This directory is meant to contain directories in
which individuals can house their unit tests. Notice that by default
there is a single directory for
[jsw7](https://github.abudhabi.nyu.edu/jsw7):

```
$> ls src/test/unit/
jsw7
```

To add your own tests, create a directory in this location
corresponding to your NetID. Populate that directory with your
tests. For example, if [Kobe
Bryant](http://www.nba.com/lakers/news/8_2_24.html) were in our class,
he would do the following:

```
$> cd src/test/unit/
$> mkdir kb24
$> cat <<BUZZER > kb24/ConnectFourGameTest.java
> package test.unit.kb24
> public class ConnectFourGameTest {}
> BUZZER
$> git add kb24
```

If you add the directory (and all Java files it holds) to your
repository, we will consider these tests when grading. There are no
unit tests for the view (`ConnectFourView`) nor are you required to
create any (although, feel free to if you're so inclined).

## Build/test/run

You can use whatever you like for development. If you choose to remain
in the command line, the [ant](http://ant.apache.org) build script
recognizes the following commands:

Command | Description
--- | ---
clean   | Removes extraneous directories; most notably, existing class files. Use this command if Java's giving you strange errors.
compile | Compiles all Java files under src, except the unit tests.
compile-tests | Compiles files in `impl.game` and `test.unit`.
test    | Engages JUnit to run the unit tests.
console | Runs the full game.

## Adding files

It is okay to add additional Java files as you deem necessary. Those
files should be reserved for impl; most likely impl/game.

This is important because of testing: the only methods that can be
tested are those defined in the API. As such, although we checkout
your entire repository, when we test we move your src/impl into a
stock directory that contains a default api and a test/unit with our
suite of tests (which may including yours). Thus, if you add files to
any other location, your code will likely not even build during our
testing phase.

Further, if the test(s) you provide test things not in the standard
API, nor specified explicitly herein, those tests will be ignored. So
while it's good practice to test everything, know that when it comes
to our "universal" test suite, we can only take into account things
that are universal.

## Workflow

It is expected that you will use GitHub religiously. You should fork
this project, then branch/commit/push freely.

See the assignment listing in NYU Classes for the due date. We will
grade the code that is on your master branch at the exact deadline. We
are willing to consider material on your master branch after the
deadline, however, doing so will incur a 10% penalty for 24 hours
after the deadine, and a 30% penalty thereafter. There are no
extensions.
