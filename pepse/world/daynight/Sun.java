package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that create a sun object to the game.
 * It has only one public static function that used for the creation.
 */
public class Sun {

    private static final float PART_OF_FOR_X = 0.5F;
    private static final float PART_OF_FOR_Y = 0.6F;
    private static final float START_ANGLE_FOR_TRANSITION = 0f;
    private static final double END_ANGLE_FOR_TRANSITION = 2*Math.PI;
    private static final float TRANSITION_TIME = 30f;
    private static final double HALF_PI = 0.5*Math.PI;
    private static final float DEVIATION_FOR_LOCATION = 350;

    /**
     * A public static function that create the sun for the game.
     * @param gameObjects the collection of the game objects to add the sun to.
     * @param layer the layer of the sun, the new game object.
     * @param windowDimensions the window dimensions of the game.
     * @param cycleLength the length of the sun to do one cycle.
     * @return the new sun object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength){
        Renderable yellowCircle = new OvalRenderable(Color.YELLOW);
        Vector2 initialLocation = Vector2.of(PART_OF_FOR_X * windowDimensions.x(),
                PART_OF_FOR_Y * windowDimensions.y());
        GameObject sun = new GameObject(initialLocation,Vector2.of(cycleLength, cycleLength),
                yellowCircle);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        new Transition<>(sun, (angle) -> sun.setCenter(computePoint(initialLocation, angle)),
                START_ANGLE_FOR_TRANSITION, (float) (END_ANGLE_FOR_TRANSITION),
                Transition.CUBIC_INTERPOLATOR_FLOAT,TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_LOOP, null);
        sun.setTag("sun");
        return sun;
    }

    private static Vector2 computePoint(Vector2 initialLocation, double angle){
        return Vector2.of((float) Math.cos(angle - HALF_PI)*DEVIATION_FOR_LOCATION + initialLocation.x(),
                (float) Math.sin(angle - HALF_PI)*DEVIATION_FOR_LOCATION + initialLocation.y());
    }
}
