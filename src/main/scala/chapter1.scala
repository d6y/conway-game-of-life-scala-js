// Conway's Game of Life
// https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

object GameOfLife {

// The game takes places on a 2D grid made up of cells:
case class Grid(width: Int, height: Int, cells: Set[Cell])

// Every cell knows where it is in the grid...
case class Cell(x: Int, y: Int, state: CellState)

// ... and is either dead or alive:
sealed trait CellState
case object Dead  extends CellState
case object Alive extends CellState

// Each step of the game applies rules to the grid, producing new grid.
// The rules don't change.
type Stepper = Rules => Grid => Grid

// The whole game is a series of steps:
type Game = (Grid, Rules, Stepper) => Stream[Grid]

// The rules of the game define how a cell changes based on the cells around them:
type Neighbours = Set[Cell]
type Rules = (Cell, Neighbours) => Cell


// Conway's rules are:
val conwaysRules: Rules = (cell, neighbours) => {
  val numLivingNeighbours: Int = neighbours.filter(_.state == Alive).size
  (cell.state, numLivingNeighbours) match {
    case (Alive, n) if n < 2     => cell.copy(state=Dead)  // death by under-population
    case (Alive, 2) | (Alive, 3) => cell                   // surivor
    case (Alive, n) if n > 3     => cell.copy(state=Dead)  // death by over-population
    case (Dead,  3)              => cell.copy(state=Alive) // reproduction
    case _                       => cell
  }
}

object Stepper extends Stepper {
  def apply(rules: Rules): Grid => Grid =
    grid => grid.copy(
      cells = grid.cells.map(cell => rules(cell, Grid.neighboursOf(cell,grid)))
  )
}

object Game extends Game {
  def apply(initial: Grid, rules: Rules, stepper: Stepper): Stream[Grid] = {
    val oneStep: Grid => Grid = stepper(rules)
    Stream.iterate(initial)(oneStep)
  }
}



// We need a grid to start with:
object Grid {

  def create(width: Int, height: Int, createCell: (Int,Int) => Cell): Grid = {
    // We need neighbours to work with, so let's have at least a 3x3 grid
    require(width >= 3 && height >= 3)

    // The top left of the grid is (0,0)
    val cells = for {
      x <- 0 until width
      y <- 0 until height
    } yield createCell(x,y)

    Grid(width, height, cells.toSet)
  }

 // Here's one way to create a random grid:
 def random(width: Int, height: Int, probabilityAlive: Float, seed: Long=42): Grid = {
   val rnd = new scala.util.Random(seed)
   val createCell = (x: Int, y: Int) =>
     if (rnd.nextFloat <= probabilityAlive) Cell(x, y, Alive)
     else Cell(x, y, Dead)
   Grid.create(width, height, createCell)
 }

 // And here's a desert:
 def empty(width: Int, height: Int): Grid =
  create(width, height, Cell(_, _, Dead))

 // We will need a way to pick out neighbouring cells.
 // In paricular, the edges of the grid wrap (eg, the neighbours of the first cell
 // include cells from the bottow row and the last column)
 def neighboursOf(cell: Cell, grid: Grid): Set[Cell] = {

   def excludeSelf(neigh: Cell): Boolean = neigh != cell

   // Is the a "near" b? I.e., one either side, allowing for
   // wrapping around the bounds of the row or column
   def near(bounds: Int)(a: Int, b: Int): Boolean =
      a == b || a == ((bounds+b-1) % bounds) || a == ((b+1) % bounds)

   val aboveOrBelow = near(grid.height) _
   val leftOrRight  = near(grid.width) _

   val isNeighbour: Cell => Boolean = neigh =>
      excludeSelf(neigh) && leftOrRight(neigh.x, cell.x) && aboveOrBelow(neigh.y, cell.y)

   grid.cells.filter(isNeighbour)
 }
}


}
