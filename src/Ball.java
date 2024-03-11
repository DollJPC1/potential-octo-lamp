
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import state_ball.*;

public class Ball extends GameObject {
    final float SPEED_BALL = 600;

    private Sound collisionSoundBackground;
    private Sound collisionSoundPaddle;
    private statusBall stateBallNow;
    private CatchBall ballHolder;

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
            Sound collisionSoundBackground, Sound collisionSoundPaddle, Paddle paddle,
            UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        
        this.collisionSoundBackground = collisionSoundBackground;
        this.collisionSoundPaddle = collisionSoundPaddle;
        this.ballHolder = new CatchBall(this, paddle, inputListener);
        catchBall();
    }

    public void catchBall() {
        setVelocity(Vector2.ZERO);
        this.stateBallNow = this.ballHolder;
        ballHolder.launchBall();
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        setVelocity(getVelocity().flipped(collision.getNormal()));

        if (other.getClass() == Paddle.class)
            this.collisionSoundPaddle.play();
        else 
            this.collisionSoundBackground.play();
        

    }

    @Override
    public void update(float arg0) {
        super.update(arg0);
        stateBallNow = this.stateBallNow.launchBall();
    }

}
