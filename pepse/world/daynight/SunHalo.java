package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Component;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class that creates the halo that follows the sun object game.
 * It has only one public static function that creates it.
 */
public class SunHalo {

    private static final Vector2 HALO_SIZE = Vector2.of(250, 250);

    /**
     * A public static function that creates the halo for the sun.
     * @param gameObjects the collection to add the halo to.
     * @param layer the layer we should put the new object to.
     * @param sun the sun that the halo should follow.
     * @param color the color of the halo we want it to be.
     * @return the new game object, the halo.
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    GameObject sun, Color color){
        Renderable haloCircle = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(Vector2.ZERO,HALO_SIZE, haloCircle);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.addComponent( (deltaTime)-> sunHalo.setCenter(sun.getCenter()) );
        gameObjects.addGameObject(sunHalo, layer);
        return sun;
    }
}
