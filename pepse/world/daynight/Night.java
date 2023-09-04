package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that creates the night object of the game.
 * The class has only one static function that create the new object.
 */
public class Night {
    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final Float INITIAL_OPAQUENESS_VALUE = 0f;
    /**
     * A public static function that creates the new night object.
     * It also adding it a transition for making its opaqueness accordingly to the day's state.
     * @param gameObjects the collection to add the new game's object.
     * @param layer the layer to add the night new object.
     * @param windowDimensions the dimensions of the game screen.
     * @param cycleLength the length of the cycle the night should do.
     * @return the new night object.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength){
        Renderable blackRectangle = new RectangleRenderable(Color.black);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, blackRectangle);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("night");
        gameObjects.addGameObject(night, layer);
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                INITIAL_OPAQUENESS_VALUE, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null); // nothing further to execute upon reaching final value
        return night;
    }

}
