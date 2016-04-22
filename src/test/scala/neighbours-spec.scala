import org.scalatest._

import GameOfLife.{Grid,Cell,Alive,Dead}

class NeighboursSpec extends FlatSpec with Matchers {

  val grid4x4 = Grid(4, 4, Set[Cell](
    Cell(0,0,Dead), Cell(1,0,Dead), Cell(2,0,Dead), Cell(3,0,Dead),
    Cell(0,1,Dead), Cell(1,1,Dead), Cell(2,1,Dead), Cell(3,1,Dead),
    Cell(0,2,Dead), Cell(1,2,Dead), Cell(2,2,Dead), Cell(3,2,Dead),
    Cell(0,3,Dead), Cell(1,3,Dead), Cell(2,3,Dead), Cell(3,3,Dead) )
  )

  "A 4x4 grid" should "wrap neihbours for cell (0,0)" in {
    Grid.neighboursOf(Cell(0,0,Dead), grid4x4) should be(Set[Cell](
      Cell(3,3,Dead), Cell(0,3,Dead), Cell(1,3,Dead),
      Cell(3,0,Dead),                 Cell(1,0,Dead),
      Cell(3,1,Dead), Cell(0,1,Dead), Cell(1,1,Dead) )
    )
  }

}
