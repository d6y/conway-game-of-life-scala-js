import GameOfLife.{Grid,Cell,Alive,Dead}
import shapeless._, syntax.sized._, ops.sized._
import ops.nat._, LT._

object Examples {

  def grid[C <: Nat]
    (rows: Sized[IndexedSeq[Int], C]*)
    (implicit
      ev: _0 < C,
      gen: ToHList[IndexedSeq[Int],C]): Grid = {
    val height = rows.length
    val width = rows.head.toHList.runtimeLength
    Grid.create(width, height, (x,y) => Cell(x, y, if (1 == rows(y).toIndexedSeq(x)) Alive else Dead))
  }

  //
  // Static patterns
  //

  val block = grid(
    Sized(0,0,0,0),
    Sized(0,1,1,0),
    Sized(0,1,1,0),
    Sized(0,0,0,0)
  )

  val beehive = grid(
    Sized(0,0,0,0,0,0),
    Sized(0,0,1,1,0,0),
    Sized(0,1,0,0,1,0),
    Sized(0,0,1,1,0,0),
    Sized(0,0,0,0,0,0)
  )

  //
  // Oscillators
  //

  // blinker flips between two states (has period 2)
  val blinker1 = grid(
    Sized(0,0,0,0,0),
    Sized(0,0,1,0,0),
    Sized(0,0,1,0,0),
    Sized(0,0,1,0,0),
    Sized(0,0,0,0,0)
  )

  val blinker2 = grid(
    Sized(0,0,0,0,0),
    Sized(0,0,0,0,0),
    Sized(0,1,1,1,0),
    Sized(0,0,0,0,0),
    Sized(0,0,0,0,0)
  )

  //
  // Spaceships
  //
  val glider = grid(
    Sized(0,0,0,0,0,0,0),
    Sized(0,0,0,0,0,0,0),
    Sized(0,0,1,0,1,0,0),
    Sized(0,0,0,1,1,0,0),
    Sized(0,0,0,1,0,0,0),
    Sized(0,0,0,0,0,0,0),
    Sized(0,0,0,0,0,0,0)
  )
}
