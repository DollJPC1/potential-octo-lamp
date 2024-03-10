package state_ball;

import java.util.Random;

import danogl.GameObject;
import danogl.util.Vector2;

public class ThrowBall implements statusBall{
    final float SPEED_BALL = 600;

    public ThrowBall(GameObject ball) {
        Random rand = new Random();
        ball.setVelocity(new Vector2(rand.nextBoolean() ? SPEED_BALL : -SPEED_BALL,
                -SPEED_BALL));
    }

    @Override
    public statusBall launchBall() {
        return this;
    }

    
}
