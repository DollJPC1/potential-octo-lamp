package state_ball;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import java.awt.event.KeyEvent;

public class CatchBall implements statusBall {

    private GameObject paddle;
    private GameObject ball;
    private UserInputListener inputListener;

    public CatchBall(GameObject ball, GameObject paddle, UserInputListener inputListener) {
        this.ball = ball;
        this.paddle = paddle;
        this.inputListener = inputListener;
    }

    @Override
    public statusBall launchBall() {
        this.ball.setCenter(ball.getCenter().multX(0).add(paddle.getCenter().multY(0)));
        if (this.inputListener.isKeyPressed(KeyEvent.VK_SPACE))
            return new ThrowBall(this.ball);
    
        return this;
    }

}
