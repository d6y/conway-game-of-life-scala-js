import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.timers._
import org.scalajs.dom
import org.scalajs.dom.html

@JSExport
object ScalaJsGameOfLife {

  val frameRate = 100 // milliseconds

  @JSExport
  def main(canvas: html.Canvas): Unit = {

    import GameOfLife._

    // Starting with a random pattern of 30% Alive:
    val initial: Grid = Grid.random(30, 30, 0.3f, seed=100)

    // Starting with a glider:
    //val initial: Grid = Examples.glider

    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    // Each cell will be scalled to fill the canvas
    val xscale = canvas.width.toFloat / initial.width.toFloat
    val yscale = canvas.height.toFloat / initial.height.toFloat

    def plotCell(x: Int, y: Int, cell: Cell): Unit = {
      val colour = if (cell.state == Alive) "rgb(255,0,0)" else "rgb(0,0,0)"
      ctx.fillStyle = colour
      ctx.fillRect(x*xscale,y*yscale,xscale,yscale)
    }

    def plotGrid(grid: Grid): Unit = {
      val rows: Seq[List[Cell]] = for {
        y <- 0 until grid.height
        row = grid.cells.filter(_.y == y).toList.sortWith( _.x < _.x )
      } yield row

      for {
        y <- 0 until grid.height
        x <- 0 until grid.width
      } plotCell(x,y, rows(y)(x))
    }

    def draw(frames: Stream[Grid]): Unit =
      frames match {
        case Stream.Empty  => println("Done!")
        case grid #:: tail =>
          plotGrid(grid)
          setTimeout(frameRate)(draw(tail))
    }

    val game = Game(initial, conwaysRules, Stepper)
    draw(game)
  }

}
