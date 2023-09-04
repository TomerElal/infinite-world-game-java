package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import pepse.util.ColorSupplier;
import pepse.util.MathOrLogicOperations;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Function;

/**
 * A class that creates trees.
 * It has a single method that creates tress in the given range.
 */
public class Tree {

    private static final int NOT_TO_PLANT = 0;
    private final int staticLeafLayer;
    private final GameObjectCollection gameObjects;
    private final int trunkLayer;
    private final Function<Float, Float> getHeight;
    private Random random;
    private final int seed;
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private int groundLayer;
    private final int fallingLeafLayer;

    /**
     * A constructor for Tree class.
     * In case we get the same seed for randomizing we can create the exact same trees, as before.
     * @param gameObjects the collection of all game's objects, to add the new tree's parts.
     * @param trunkLayer the layer of the new trunks.
     * @param getHeight a function reference that get an x coordinate and returns the ground height there.
     * @param seed the seed for the random instance.
     * @param fallingLeafLayer the layer for the falling leaves.
     * @param staticLeafLayer the layer for the static leaves of the tree.
     */
    public Tree(
            GameObjectCollection gameObjects,
            int trunkLayer,
            Function<Float, Float> getHeight,
            int seed, int fallingLeafLayer, int staticLeafLayer){
        this.gameObjects = gameObjects;
        this.trunkLayer = trunkLayer;
        this.getHeight = getHeight;
        this.seed = seed;
        random = new Random(seed);
        this.fallingLeafLayer = fallingLeafLayer;
        this.staticLeafLayer = staticLeafLayer;
    }
    /**
     * A public method the creates trees in a given range.
     * Each column in the range is randomized whither to plant there a tree or not.
     * In case we plant, we call Trunk and leaves static method for the real creation.
     * @param minX the start index of the range.
     * @param maxX the end index of the range.
     */
    public void createInRange(int minX, int maxX){

        maxX = MathOrLogicOperations.getTheNumInMultipliesOf30(maxX, true);
        minX = MathOrLogicOperations.getTheNumInMultipliesOf30(minX, false);
        Renderable renderableTrunkBlock =
                new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));
        // The reason for using int and not boolean for the flag, is because we want to use the
        // information of the tree size that the trunk method returns.
        int treeHasCreated = NOT_TO_PLANT;
        for (float i = minX; i < maxX; i += Block.SIZE) {

            int height = (int) MathOrLogicOperations.normalizeHeight(getHeight.apply(i));
            // For making them not to close to each other.
            if(treeHasCreated > NOT_TO_PLANT){
                treeHasCreated = NOT_TO_PLANT;
                continue;
            }
            treeHasCreated = Trunk.create(i, trunkLayer, gameObjects, height, seed, renderableTrunkBlock);
            Leaves.create(i, staticLeafLayer, gameObjects, height, seed, treeHasCreated, fallingLeafLayer);
        }
    }
}
