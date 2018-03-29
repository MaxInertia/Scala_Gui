package display

import scalafx.scene.canvas.GraphicsContext

trait Cartesian {
  def xComponent: Double
  def yComponent: Double
}

case class Position(var x: Double, var y: Double) extends Cartesian {
  def xComponent: Double = x
  def yComponent: Double = y
}

case class Velocity(var x: Double, var y: Double) extends Cartesian {
  def xComponent: Double = x
  def yComponent: Double = y
}

trait Boundaries {
  def top: Double
  def bottom: Double
  def right: Double
  def left: Double
  def isInside(x: Double, y: Double): Boolean
}

trait Temporal[Object] {
  def update(o: Object): Unit
}


trait Viewable[Object] {
  def draw(o: Object, gc: GraphicsContext): Unit
}


// P (subset) PSpace
// PSpace = NPSpace !!!
// NDTM operates in f(n) space, a DTM can do in (f(n))^2 space
// P < NP < PSPACE ('<' means "is contained in")

/*
# NP Reductions
To show a problem was NP-Complete
 1. Show Q is in NP
 2. Show Q is NP-Hard
      - Provide a polynomial time algorithm to convert it to another NP-Complete problem

# PSPACE Reductions
To show a problem Q is PSPACE-complete if:
  1. Q is in PSPACE
  2. Q is PSPACE-hard
  A problem is SPACE-hard if there is a polynomial time algorithm of the form A:

Cannot prove P != PSPACE

"Collapsing complexity classes"

DLOG: The set of problems that can be solved in logarithmic space on a DTM
NLOG: solvable in logarithmic space on NDTM

Theorem: DLOG < NLOG < P
- There is a compiler to turn a NLOG n space-bounded TM to a polynomial time TM

1900's David Hilbert thought it was possible to find an algorithm that could determine whether or not any math proposition was true
1931 Godel - The incompleteness theorem
  - Devised a theorem which could not be proven true or false
  - Says that

A language L is recursively enumerable if there is a TM such taht L = L(M)
- The recursively enumerable languages are everything we can compute on any method of computation

Recursive (Not "recursion" !!)
A language is recursive if L = L(M) for some TM M, and
1. if w in L, then M accepts
2. if w not in L, then M eventually stops moving (halts, no more transitions can be applied), and does not enter an accepting state

Are all Regular Languages recursive? Yes
- There is some DFA that accepts the RL, we can convert that DFA to a TM that accepts the same language

An algorithm has a worst case complexity of f(n) if, for every input of size n, it will never take longer than f(n) to finish.
All algorithms that has a worst case time complexity is 'recursive'.

Context-free Languages - Also recursive

Undecidable: A language (or problem) is undecidable if it is not a recursive language
- In this case, ther is no algorithm that always stops that can tell if a word is or is not in the language.
- This is kind of like O(infinity) time complexity (on any system equivalent to TM)

{w$w | {a,b}}
All context-free languages are recursive
Some recursive languages are not context-free

REG < DPDL < CFL < REC < RE
RE: Recursively enumerable languages
REC: Recursive languages
..: all languages accepted by TMs


*/