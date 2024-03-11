package state_ball;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class CatchBall implements statusBall {

    private GameObject paddle;
    private GameObject ball;
    private UserInputListener inputListener;
    private float ballHeight;

    public CatchBall(GameObject ball, GameObject paddle, UserInputListener inputListener) {
        this.ball = ball;
        this.paddle = paddle;
        this.inputListener = inputListener;
        this.ballHeight = (paddle.getDimensions().y() + ball.getDimensions().y())/2;
    }

    @Override
    public statusBall launchBall() {
        ball.setCenter(paddle.getCenter().add(Vector2.UP.mult(ballHeight)));
        if (this.inputListener.isKeyPressed(KeyEvent.VK_SPACE))
            return new ThrowBall(this.ball);
        return this;
    }

}
