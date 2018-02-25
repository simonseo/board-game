# New Models

The objective of this assignment is to add two additional games to the
framework.

## Games

### Complica

Complica is similar to Connect Four in that players attempt achieve
four chips in a row vertically, horizontally, or diagonally. There are
two major differences:

1. Complica is played on a seven-row, four-column board; and

2. players are allowed to add chips to columns that are seemingly
   full.

The second difference has major implications for what end-of-game
means in Complica. In cases where the column is full, that column
behaves like a queue: the first chip in, now at the bottom of the
column, is pushed out; all other chips are lowered; and the new chip
is placed at the top of the column.

This change implies that the game is over only when one player has
more four-in-a-rows than their opponent. To this end, blocks of
consecutive chips that are longer than four should be treated as
separate chunks of four. For example, consider a player who has
achieved five chips in-a-row; such a condition should be treated as
two instances of four-in-a-row.

### Tic-Tac-Toe

Tic-Tac-Toe is played on a 3-by-3 grid. Like Connect Four and
Complica, it is a turn based game in which the first player to lay
consecutively placed chips wins. In this case, the magic consecutive
number is three. Different cultures have given this game different
names -- [Wikipedia](https://en.wikipedia.org/wiki/Tic-tac-toe) gives a
good overview of what those names are, along with the general rules.

The biggest difference between this and the other two games is that
chips do not "drop" in place. Instead, a placement specification must
be a 2-tuple specifying the row and column; and the subsequent
placement must be to that exact location.

## Integration

Adding these two games will require you to fill out the two new Java
files in the `impl.game` package. You are free to add more files to
this package if you like. These games must be integrated into your
existing code base using the [template design
pattern](https://sourcemaking.com/design_patterns/template_method). It
is probably best if you first add the games without using the pattern,
then think through the refactorization required for it to be achieved.

You may also require alterations to your view. If your view was well
designed in the previous version, however, those changes should be
minimal.

## Run

The game to run should be specified at run-time. Specifically, this
means passing the relevant game to Ant. The following, would execute
Complica:

```
ant console -Dgame=complica
```

When started this way, Ant essentially passes the string to
`test.system.ConsoleTest`. ConsoleTest has been augmented to recognize
this string and instantiate the correct Game implementation; see its
implementation for the universe of recognized values.

## Working with Git

First, "tag" your current version:

```bash
$> git tag -a v1 -m "Homework 1 final"
$> git push origin v1
```

Tagging allows you to "bookmark" a milestone in your projects
development.

Next, [sync your current
fork](https://help.github.com/articles/syncing-a-fork/) with this
one. Syncing should update your README and add the new class structure
to your existing work.

You are free to develop this homework on a new branch. However, for
grading we will only take into consideration what is on master at the
deadline.

## Expectations

### Usability (20 points)

Usability and programmatic aspects of the previous assignment should
be maintained. That means there should be a single console view that
is capable of displaying and handling interaction of a specified
model.

### Correctness (50 points)

As was the case in the first edition of Board Games, there is a suite
of tests we have developed that we will use for grading. However, you
are strongly encouraged to build your own unit tests during
development. Again, those tests should reside in `src/test/unit/foo`,
where `foo` is your NetID. Please differentiate tests as follows:

* Game specific tests should go in files beginning with that games
  name. For example, ConnectFour test should go in
  `ConnectFour*Test.java`, where `*` can be whatever name you choose.

* Game agnostic tests should go in `Common*Test.java`

Consider using JUnit [parameterized
tests](https://github.com/junit-team/junit4/wiki/Parameterized-tests)
to save time.

The number of points you receive for this aspect will be based on the
fraction of tests passed. Questions about testing semantics, and thus
ambiguities to this specification, should be resolved using GitHub
issues (mark the "assignee" as
[@jsw7](https://github.abudhabi.nyu.edu/jsw7) and
[@kqm1](https://github.abudhabi.nyu.edu/jsw7)).

### Design (20 points)

These games should be added using the template design pattern. Your
code should clearly document how and where this has been done. Note
that there are multiple places where this pattern can be applied --
for full credit we will be looking for all of them.


### Practices (10 points)

We continue to expect "good general habits" and good "usage of
GitHub." Please see the [previous
homework](https://github.abudhabi.nyu.edu/jsw7/blackjack/tree/hw1#grading)
for what this entails.
