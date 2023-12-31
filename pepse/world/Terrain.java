package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.MathOrLogicOperations;

import java.awt.*;
import java.util.Random;

/**
 * A class that creates the ground of the game by creating all the ground blocks.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final float DEVIATION_FOR_LOCATION = 2 / 3F;
    private static final int FIRST_LAYER_OF_GROUND = 0;
    private static final int SECOND_LAYER_OF_GROUND = 1;
    private static final double PARAMETER_1 = 0.008;
    private static final double PARAMETER_2 = 50;
    private static final int PARAMETER_3 = 70;
    private static final double PARAMETER_4 = 0.003;
    private static final int PARAMETER_5 = 40;
    private static final int DEVIATION_FOR_COLLISION_LAYER = 15;
    private final float groundHeightAtX0;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Random random;
    private final int collisionLayer;

    /**
     * Constructor for the Terrain class.
     *
     * @param gameObjects       The GameObjectCollection where the ground objects will be added.
     * @param groundLayer       The layer where the ground objects will be placed.
     * @param windowDimensions  The dimensions of the game window.
     * @param seed              The seed for randomization.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions, int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.groundHeightAtX0 = windowDimensions.y() * DEVIATION_FOR_LOCATION;
        this.random = new Random(seed);
        this.collisionLayer = groundLayer + DEVIATION_FOR_COLLISION_LAYER;
    }

    /**
     * Calculate the ground height at a given column using mathematical calculations and randomization.
     *
     * @param x The column.
     * @return The height in this column.
     */
    public float groundHeightAt(float x) {
        return (float) (Math.sin((Math.sin(x * PARAMETER_1)) * (PARAMETER_2 + random.nextInt(PARAMETER_3)))) +
                (float) (Math.sin(x * PARAMETER_4)) * (PARAMETER_3 + random.nextInt(PARAMETER_5))
                + groundHeightAtX0;
    }

    /**
     * Create the ground for columns within a specified range.
     *
     * @param minX The start index of the range.
     * @param maxX The end index of the range.
     */
    public void createInRange(int minX, int maxX) {
        double height;
        float xCoord, yCoord;
        maxX = MathOrLogicOperations.getTheNumInMultipliesOf30(maxX, true);
        minX = MathOrLogicOperations.getTheNumInMultipliesOf30(minX, false);
        for (int i = 0; i <= (maxX - minX); i += Block.SIZE) {
            xCoord = minX + i;
            height = MathOrLogicOperations.normalizeHeight(groundHeightAt(xCoord));
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                Renderable groundBlockRender =
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                yCoord = (float) height + (Block.SIZE * j);
                GameObject singleGroundBlock = new Block(new Vector2(xCoord, yCoord), groundBlockRender);
                singleGroundBlock.setTag("ground");
                // Make only the first two layers collisional.
                int layer = (j == FIRST_LAYER_OF_GROUND || j == SECOND_LAYER_OF_GROUND)
                        ? collisionLayer : groundLayer;
                gameObjects.addGameObject(singleGroundBlock, layer);
            }
        }
    }
}
