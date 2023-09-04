package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import java.util.Objects;
import java.util.Random;

/**
 * A class that only crate one trunk, by using a static function "create".
 */
public class Trunk{
    private static final double MAKING_THE_TREE_NOT_TOO_SMALL = 0.6;
    private static final float THRESHOLD_FOR_PLANTING_TREE = 0.1f;
    private static final int NO_PLANT = 0;

    /**
     * A public static function that create a trunk by using some block's objects, one above the other.
     * Its height is randomized by the given seed.
     * @param xCoordinate the column in the screen we want to add trunk to.
     * @param layer the layer to add the trunk to.
     * @param gameObjects the collection for all the block's gameObjects, that represents that trunk, to add.
     * @param groundHeight the height of the ground on the given x coordinate.
     * @param seed the seed for initialize the random instance.
     * @param renderableTrunkBlock the renderable object for the blocks of the trunk.
     * @return the size of the trunk that created or zero if not.
     */
    public static int create(float xCoordinate, int layer, GameObjectCollection gameObjects, int groundHeight,
                             int seed, Renderable renderableTrunkBlock) {
        Random random = new Random(Objects.hash(xCoordinate, seed));
        if (random.nextFloat() < THRESHOLD_FOR_PLANTING_TREE){
            int treeSize = (int) Math.min(random.nextInt(groundHeight),
                    groundHeight*MAKING_THE_TREE_NOT_TOO_SMALL);
            for (int j = groundHeight; j > treeSize; j -= Block.SIZE) {
                GameObject trunkBlock = new Block(Vector2.of(xCoordinate,j), renderableTrunkBlock);
                trunkBlock.setTag("trunkBlock");
                gameObjects.addGameObject(trunkBlock, layer);
            }
            return treeSize;
        }
        return NO_PLANT;
    }
}
