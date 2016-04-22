import org.scalatest._

import GameOfLife.{Game,Stepper,conwaysRules}

class StepSpec extends FlatSpec with Matchers {
    
  "Stepper" should "cause blink to change" in {
    val game = Game.apply(Examples.blinker1, conwaysRules, Stepper)
    game.drop(1).head should be(Examples.blinker2)
  }

}
