import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.html

@JSExport
object ScalaJsGameOfLife extends JSApp {

  def main(): Unit = {
    import GameOfLife._

    def consolePlot(grid: Grid): String = {
      val rows: Seq[List[Cell]] = for {
        y <- 0 until grid.height
        row = grid.cells.filter(_.y == y).toList.sortWith( _.x < _.x )
      } yield row
      rows.map(row => row.toList.map { case Cell(_,_, Alive) => "O" ; case _ => "-" }.mkString).mkString("\n")
    }

    println(
      Game.apply(Examples.blinker1, conwaysRules, Stepper).take(2).map(consolePlot).toList
    )
  }

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

    ctx.fillStyle = "rgb(255,0,0)"
    ctx.fillRect(0,0, 100, 200)

  }

}
