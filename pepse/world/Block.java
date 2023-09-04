package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class that represents a Block as gameObject.
 * The block is prevented from moving.
 */
public class Block extends GameObject {

    /**
     * the size of the block.
     */
    public static final int SIZE = 30;

    /**
     * A constructor of the block that initialized it.
     * @param topLeftCorner the position for placing the top left corner of the block.
     * @param renderable the renderable object for rendering the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
