
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import state_ball.*;

public class Ball extends GameObject {
    final float SPEED_BALL = 600;
    final String lOST_TAG = "Lost";
    // final String[] IMG_COLOR_BORDERS = { "assets/Red.png", "assets/Yelow.png" };

    private Sound collisionSoundBackground;
    private Sound collisionSoundPaddle;
    private String prompt;
    private byte attempts;
    private statusBall stateBallNow;
    private CatchBall ballHolder;
    private float heightOfBallCatch;

    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
            Sound collisionSoundBackground, Sound collisionSoundPaddle, Vector2 windoeDimensions, Paddle paddle,
            UserInputListener inputListener, float maxWinsow) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSoundBackground = collisionSoundBackground;
        this.collisionSoundPaddle = collisionSoundPaddle;
        this.prompt = "";
        this.attempts = 3;
        this.ballHolder = new CatchBall(this, paddle, inputListener);
        this.heightOfBallCatch = paddle.getCenter().y() - paddle.getDimensions().y() -
                (dimensions.y() - paddle.getDimensions().y()) / 2;
        catchBall();
    }

    public void catchBall() {
        setVelocity(Vector2.ZERO);
        setCenter(Vector2.DOWN.mult(this.heightOfBallCatch));
        this.stateBallNow = this.ballHolder;
    }


    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newSpeed = getVelocity().flipped(collision.getNormal());
        setVelocity(newSpeed);

        if (other.getClass() == Paddle.class)
            this.collisionSoundPaddle.play();
        else {
            this.collisionSoundBackground.play();
            if (other.getTag().equals(lOST_TAG))
                if ((this.attempts -= 1) == 0)
                    this.prompt = "lost";
                else
                    catchBall();

        }

    }

    public String getPrompt() {

        return prompt;
    }

    @Override
    public void update(float arg0) {
        super.update(arg0);
        stateBallNow = this.stateBallNow.launchBall();
    }

}
