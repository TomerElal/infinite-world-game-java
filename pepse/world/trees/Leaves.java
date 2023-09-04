package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;
import java.util.Objects;
import java.util.Random;
/**
 * A public class that creates leaves for one tree with static method.
 */
public class Leaves{
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int MIN_MATRIX_LEAVES_LIMIT = 10;
    private static final int TWO = 2;
    private static final float BOOLEAN_TERM_WHITHER_TO_PLANT_TREE = 0.1f;
    private static final int MAX_MATRIX_LEAVES_LIMIT = 5;
    private static final int MAKE_IT_ODD = 1;
    private static final float THRESHOLD_FOR_CREATING_LEAF = 0.4f;
    private static final float THRESHOLD_FOR_FALLING_LEAVES = 0.1f;

    /**
     * A public static function that crate leaves for one tree.
     * @param xCoordinate the x coordinate of the tree's trunk that the leaves should cover.
     * @param staticLeafLayer the layer of the static leaves.
     * @param gameObjects the game's object collection that can manipulate the
     * @param groundHeight the height of the ground where the tree's position at.
     * @param seed the seed for creating random numbers.
     *             by getting it from outside scope, we can recreate leaf the same way exactly by
     *             using the same seed.
     * @param trunkHeight the height of the tree's trunk.
     * @param fallingLeafLayer the layer of the falling leaves.
     */
    public static void create(float xCoordinate, int staticLeafLayer, GameObjectCollection gameObjects,
                              int groundHeight, int seed, int trunkHeight, int fallingLeafLayer) {
        int matrixLimit = Math.min(MIN_MATRIX_LEAVES_LIMIT, (groundHeight - trunkHeight)/TWO);
        Random random = new Random(Objects.hash(xCoordinate, seed));
        // in order to be consistent with the random creation of the trunks.
        if (random.nextFloat() < BOOLEAN_TERM_WHITHER_TO_PLANT_TREE){
            int matrixSize = Math.max(random.nextInt(matrixLimit), MAX_MATRIX_LEAVES_LIMIT);
            int diversion = ((matrixSize/TWO) + MAKE_IT_ODD)*Block.SIZE;
            for (int i = (int) xCoordinate - diversion ;i <= xCoordinate + diversion; i += Block.SIZE) {
                for (int j = trunkHeight - diversion; j <= trunkHeight + diversion; j+= Block.SIZE) {
                    if (random.nextFloat() > THRESHOLD_FOR_CREATING_LEAF) {
                        boolean isStaticLeaf = random.nextFloat() > THRESHOLD_FOR_FALLING_LEAVES;
                        Leaf leaf = createOneLeaf(isStaticLeaf, i, j, seed);
                        if(isStaticLeaf){
                            gameObjects.addGameObject(leaf, staticLeafLayer);
                        }else{
                            gameObjects.addGameObject(leaf, fallingLeafLayer);
                        }
                    }
                }
            }
        }
    }
    private static Leaf createOneLeaf(boolean isStaticLeaf, int xCoord, int yCoord, int seed) {
        Renderable renderableLeafBlock =
                new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR));
        Leaf leaf = new Leaf(Vector2.of(xCoord, yCoord), renderableLeafBlock,
                Objects.hash(seed,xCoord,yCoord), isStaticLeaf);
        leaf.setTag("leafBlock");
        return leaf;
    }
}