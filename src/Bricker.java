import java.util.LinkedList;

import brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

public class Bricker extends GameManager {
        public static float WINDOW_HEIGHT = 800, WINDOW_WIDTH = 900, HEARTS = 5;
        private final int THICKNESS = 25, BRICKS_COL = 3, BRICKS_ROW = 1;
        private final String FOLDER_ASSETS = "assets/",
                        FOLDER_AUDIO = "audio/",
                        FOLDER_IMG = "img/",
                        FOLDER_ITEMS = "items/",
                        FOLDER_OBJECT = "object/",
                        IMG_BALL = FOLDER_ASSETS + FOLDER_IMG + FOLDER_OBJECT + "ball.png",
                        IMG_BACKGROUND = FOLDER_ASSETS + FOLDER_IMG + FOLDER_OBJECT + "background.jpeg",
                        IMG_PADDLE = FOLDER_ASSETS + FOLDER_IMG + FOLDER_OBJECT + "paddle.png",
                        IMG_BRICK = FOLDER_ASSETS + FOLDER_IMG + FOLDER_OBJECT + "brick.png",
                        IMG_HEART = FOLDER_ASSETS + FOLDER_IMG + FOLDER_OBJECT + "heart.png",
                        SOND_BALL_BACKGROUND = FOLDER_ASSETS + FOLDER_AUDIO + "Bubble5_4.wav",
                        SOND_BALL_PADDLE = FOLDER_ASSETS + FOLDER_AUDIO + "knock.wav",
                        WINNER_TAG = "Win";
        private final float DISTANCE_BORDER = 80,
                        SIZE_BALL = 50,
                        SIZE_HEART = 50,
                        PADDLE_HEIGHT = 30,
                        PADDLE_WIDTH = 250,
                        DISTANCE_HEART = 5,
                        DISTANCE_FRAME = 5,
                        ROW_DISTANCE = 2,
                        COL_DISTANCE = 3;
        private Ball ball;
        private Vector2 windoeDimensions;
        private WindowController windowController;
        private Counter numberBrick;
        private Paddle usePaddle;
        private LinkedList<GameObject> hearts;

        public Bricker(String windowTitle, Vector2 windowDimensions) {
                super(windowTitle, windowDimensions);
        }

        @Override
        public void update(float arg0) {
                super.update(arg0);
                if (this.numberBrick.value() == 0)
                        if (this.windowController.openYesNoDialog("Winner!\n play agen?"))
                                windowController.resetGame();
                        else
                                windowController.closeWindow();
                else if (this.ball.getCenter().y() >= WINDOW_HEIGHT)
                        if (takesHeart())
                                if (this.windowController.openYesNoDialog("lost...\n play agen?"))
                                        windowController.resetGame();
                                else
                                        windowController.closeWindow();
                        else
                                this.ball.catchBall();
                else if (this.ball.getCenter().y() <= 0 || this.ball.getCenter().x() <= 0 || this.ball.getCenter()
                                .x() >= WINDOW_WIDTH)
                        this.ball.catchBall();
        }

        @Override
        public void initializeGame(ImageReader img, SoundReader soundReader,
                        UserInputListener inputListener, WindowController windowController) {

                this.windowController = windowController;
                windowController.setTargetFramerate(60);
                super.initializeGame(img, soundReader, inputListener, windowController);
                windoeDimensions = windowController.getWindowDimensions();

                // create baclgrund
                Renderable imgBackground = img.readImage(IMG_BACKGROUND, false);
                createBackground(imgBackground);
                // create border
                createBorders();

                // create heart
                Renderable heartImg = img.readImage(IMG_HEART, true);
                createHeart(heartImg);
                // creat paddle
                Renderable paddleImg = img.readImage(IMG_PADDLE, false);
                createPaddle(paddleImg, inputListener);

                // create ball
                Renderable ballIng = img.readImage(IMG_BALL, true);
                Sound collisionSoundBackground = soundReader.readSound(SOND_BALL_BACKGROUND);
                Sound collisionSoundPaddle = soundReader.readSound(SOND_BALL_PADDLE);
                createBall(ballIng, collisionSoundBackground, collisionSoundPaddle, inputListener);
                this.numberBrick = new Counter(BRICKS_COL * BRICKS_ROW);

                // create Brick
                Renderable brick = img.readImage(IMG_BRICK, false);

                createBrick(brick);
        }

        private void createBackground(Renderable img) {
                GameObject background = new GameObject(Vector2.ZERO,
                                this.windowController.getWindowDimensions(), img);
                background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                gameObjects().addGameObject(background, Layer.BACKGROUND);

        }

        private void createBorders() {

                GameObject[] borders = {
                                // UP
                                new GameObject(
                                                Vector2.ZERO, new Vector2(
                                                                windoeDimensions.x(), THICKNESS),
                                                null),
                                // L
                                new GameObject(
                                                Vector2.ZERO, new Vector2(
                                                                THICKNESS, windoeDimensions.y()),
                                                null),
                                // R
                                new GameObject(
                                                Vector2.ZERO, new Vector2(
                                                                THICKNESS, windoeDimensions.y()),
                                                null),
                                // Done
                                // new GameObject(
                                // Vector2.ZERO, new Vector2(
                                // windoeDimensions.x(), THICKNESS),
                                // img)
                };

                borders[2].setCenter(new Vector2(windoeDimensions.x(), windoeDimensions.y() / 2));
                // borders[3].setCenter(new Vector2(windoeDimensions.x() / 2,
                // windoeDimensions.y()));

                borders[0].setTag(WINNER_TAG);
                // borders[3].setTag(lOST_TAG);

                for (GameObject obj : borders)
                        gameObjects().addGameObject(obj, Layer.STATIC_OBJECTS);

        }

        private void createBall(Renderable imgRenderable,
                        Sound collisionSoundBackground, Sound collisionSoundPaddle, UserInputListener inputListener) {

                this.ball = new Ball(Vector2.ZERO, new Vector2(SIZE_BALL, SIZE_BALL), imgRenderable,
                                collisionSoundBackground, collisionSoundPaddle, this.usePaddle,
                                inputListener);
                ball.catchBall();
                gameObjects().addGameObject(this.ball);
        }

        private void createPaddle(Renderable imgRenderable,
                        UserInputListener inputListener) {

                usePaddle = new Paddle(
                                Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                                imgRenderable, inputListener, windoeDimensions.x() - PADDLE_WIDTH);
                usePaddle.setCenter(
                                new Vector2(windoeDimensions.x() / 2, windoeDimensions.y() - DISTANCE_BORDER));
                gameObjects().addGameObject(usePaddle);
        }

        private void createBrick(Renderable img) {

                Brick[][] allBricks = new Brick[BRICKS_ROW][BRICKS_COL];

                final Vector2 sizeBrick = new Vector2(
                                ((this.windoeDimensions.x() - (BRICKS_COL - 1) * ROW_DISTANCE) - THICKNESS * 2)
                                                / BRICKS_COL,
                                20);

                for (int column = 0; column < BRICKS_ROW; column++)
                        for (int row = 0; row < BRICKS_COL; row++) {
                                allBricks[column][row] = new Brick(
                                                Vector2.ZERO, sizeBrick, img,
                                                new CollisionStrategy(gameObjects(), numberBrick));

                                allBricks[column][row].setCenter(new Vector2(
                                                (((row + 1) * ROW_DISTANCE + (sizeBrick.x() * row)) + sizeBrick.x() / 2)
                                                                + THICKNESS,
                                                DISTANCE_FRAME + ((column + 1) * COL_DISTANCE + sizeBrick.y() * column)
                                                                + THICKNESS));
                                gameObjects().addGameObject(allBricks[column][row], Layer.STATIC_OBJECTS);
                        }
        }

        private void createHeart(Renderable img) {
                this.hearts = new LinkedList<GameObject>();
                for (int i = 0; i < HEARTS; i++) {
                        GameObject heart = new GameObject(Vector2.ZERO, Vector2.ONES.mult(SIZE_HEART), img);
                        heart.setCenter(new Vector2(SIZE_HEART / 2 + i * (SIZE_HEART + DISTANCE_HEART),
                                        WINDOW_HEIGHT - SIZE_HEART / 2));
                        heart.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                        gameObjects().addGameObject(heart);
                        hearts.add(heart);
                }
        }

        private boolean takesHeart() {
                gameObjects().removeGameObject(this.hearts.removeLast());
                return this.hearts.isEmpty();
        }

        public static void main(String[] args) {
                new Bricker("Ping", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
        }

}
