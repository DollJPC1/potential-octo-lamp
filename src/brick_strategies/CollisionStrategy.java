package brick_strategies;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class CollisionStrategy {

    private GameObjectCollection gameObjects;
    private Counter numberBrick;

    public CollisionStrategy(GameObjectCollection gameObjects, Counter numberBrick) {
        this.gameObjects = gameObjects;
        this.numberBrick = numberBrick;
    }

    public void onCollision(GameObject thisObj, Collision collision) {
        numberBrick.decrement();
        this.gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);

    }

}
