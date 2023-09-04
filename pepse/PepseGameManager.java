package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.util.MathOrLogicOperations;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.Avatar;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import java.awt.*;

/**
 * A class that represents the game manager and the one the starts the game.
 */
public class PepseGameManager extends GameManager {
    private static final int SEED = 7;
    private static final float CYCLE_LENGTH_OF_NIGHT = 15;
    private static final float CYCLE_LENGTH_OF_SUN = 100;
    private static final Color COLOR_OF_SUN_HALO = new Color(255, 255, 0, 20);
    private static final int NUMBER_OF_FRAMES = 3;
    private static final float GET_CENTER_OF_DIMENSION = 2;
    private static final int THE_MIDDLE_FRAME = 1;
    private static final double MAKE_Y_LEFT_CORNER = 60;
    private static final int MAKE_X_LEFT_CORNER = 30;
    private static final int FRAME_0 = 0;
    private static final int FRAME_1 = 1;
    private static final int FRAME_2 = 2;
    private static final int PLACING_GROUND_LAYER = 5;
    private static final int PLACING_TOP_GROUND_LAYER = 20;
    private static final int PLACING_TRUNK_LAYER = 10;
    private static final int PLACING_STATIC_LEAVES_LAYER = 30;
    private static final int PLACING_FALLING_LEAVES_LAYER = 40;
    private static final int TWO = 2;
    private Avatar avatar;
    private Vector2 windowDimensions;
    private final static int SKY_LAYER = Layer.BACKGROUND;
    private final static int SUN_LAYER = Layer.BACKGROUND;
    private final static int NIGHT_LAYER = Layer.FOREGROUND;
    private int[] groundLayer;
    private int[] topGroundLayer;
    private int[] trunkLayer;
    private int[] staticLeafLayer;
    private int[] fallingLeafLayer;
    private final static int AVATAR_LAYER = Layer.DEFAULT;
    private Terrain[] terrains;
    private Tree[] trees;
    private int startFrame;
    private int endFrame;
    private int pointerToLeftLayers;
    private int pointerToMiddleLayers;
    private int pointerToRightLayers;
    private Sound sound;

    /**
     * A public static function main that runs the game.
     * @param args the user arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
    /**
     * A public method that creates all the game objects of the game, for playing.
     * Its purpose is to initialize all the background scenery for the user to run the game.
     * Since the world is infinite we save every moment 3 frames of the screen, that each one continue the
     * previous.
     * The avatar is standing on the middle one, and for every move to the next frame - we change the one
     * from the other side to continue the sequence.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.sound = soundReader.readSound("assets/backgroundSong.wav");
        windowDimensions = windowController.getWindowDimensions();
        initialization();
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, CYCLE_LENGTH_OF_NIGHT);
        GameObject sun = Sun.create(gameObjects(), SUN_LAYER, windowDimensions, CYCLE_LENGTH_OF_SUN);
        SunHalo.create(gameObjects(), SUN_LAYER, sun, COLOR_OF_SUN_HALO);
        createNumberOfFrame(NUMBER_OF_FRAMES);
        int avatarXCoord = (int) (windowDimensions.x()/GET_CENTER_OF_DIMENSION) - MAKE_X_LEFT_CORNER;
        double avatarYCoord =
                MathOrLogicOperations.normalizeHeight(terrains[THE_MIDDLE_FRAME].
                        groundHeightAt(avatarXCoord)) - MAKE_Y_LEFT_CORNER;
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER, Vector2.of(avatarXCoord,
                (float) avatarYCoord), inputListener, imageReader);
        handleAllCollisionsBetweenObjects();
        setCamera(new Camera(avatar, Vector2.ZERO, windowDimensions, windowDimensions));
    }
    /**
     * An override to the update method that handle the recreation and deletion of the world, when the avatar
     * moves.
     * When the avatar leave the middle frame, we delete the farther frame and create a new frame in the
     * avatar move direction, so that the avatar will always be in the middle frame.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        int xDiversionToAdd = (int) windowDimensions.x();
        if(avatar.getCenter().x() > endFrame){
            startFrame += xDiversionToAdd;
            endFrame += xDiversionToAdd;
            deleteObjectsInFrame(pointerToLeftLayers);
            changePointerByMovingRight();
            int i = pointerToRightLayers;
            createFrame(i, startFrame + xDiversionToAdd, endFrame + xDiversionToAdd);
        }else if(avatar.getCenter().x() < startFrame){
            startFrame -= xDiversionToAdd;
            endFrame -= xDiversionToAdd;
            deleteObjectsInFrame(pointerToRightLayers);
            changePointerByMovingLeft();
            int i = pointerToLeftLayers;
            createFrame(i, startFrame - xDiversionToAdd, endFrame - xDiversionToAdd);
        }
    }
    private void initialization(){
        pointerToLeftLayers = FRAME_0;
        pointerToMiddleLayers = FRAME_1;
        pointerToRightLayers = FRAME_2;
        groundLayer = new int[NUMBER_OF_FRAMES];
        topGroundLayer = new int[NUMBER_OF_FRAMES];
        trunkLayer = new int[NUMBER_OF_FRAMES];
        staticLeafLayer = new int[NUMBER_OF_FRAMES];
        fallingLeafLayer = new int[NUMBER_OF_FRAMES];
        for (int i = 0; i < NUMBER_OF_FRAMES; i++) {
            groundLayer[i] = Layer.STATIC_OBJECTS + i + PLACING_GROUND_LAYER;
            topGroundLayer[i] = Layer.STATIC_OBJECTS + PLACING_TOP_GROUND_LAYER + i;
            trunkLayer[i] = Layer.STATIC_OBJECTS + PLACING_TRUNK_LAYER + i;
            staticLeafLayer[i] = Layer.STATIC_OBJECTS + PLACING_STATIC_LEAVES_LAYER + i;
            fallingLeafLayer[i] = Layer.STATIC_OBJECTS + PLACING_FALLING_LEAVES_LAYER + i;
        }
        terrains = new Terrain[NUMBER_OF_FRAMES];
        trees = new Tree[NUMBER_OF_FRAMES];
        sound.playLooped();
        startFrame = (int) -windowDimensions.x();
        endFrame = FRAME_0;
    }
    private void createFrame(int frameNumber, int startFrame, int endFrame){
        terrains[frameNumber] = new Terrain(gameObjects(), groundLayer[frameNumber], windowDimensions, SEED);
        terrains[frameNumber].createInRange(startFrame, endFrame);
        trees[frameNumber] = new Tree(gameObjects(), trunkLayer[frameNumber],
                terrains[frameNumber]::groundHeightAt, SEED,
                fallingLeafLayer[frameNumber], staticLeafLayer[frameNumber]);
        trees[frameNumber].createInRange(startFrame, endFrame);
    }
    private void createNumberOfFrame(int numberOfFrame){
        for (int i = 0; i < numberOfFrame; i++) {
            createFrame(i, startFrame, endFrame);
            startFrame += windowDimensions.x();
            endFrame += windowDimensions.x();
        }
        startFrame -= windowDimensions.x()*TWO; // make the start frame in the middle
        endFrame -= windowDimensions.x()*TWO; // same for end frame
    }
    private void handleAllCollisionsBetweenObjects(){
        for(int i = 0; i < NUMBER_OF_FRAMES; i++){
            gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, topGroundLayer[i], true);
            gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, trunkLayer[i], true);
            // This way we handle the collisions between all the falling leaves in any frame to every
            // ground in any frame.
            // This because leaves can travel to other frames.
            gameObjects().layers().shouldLayersCollide(fallingLeafLayer[i],
                    topGroundLayer[i], true);
            gameObjects().layers().shouldLayersCollide(fallingLeafLayer[(i+1)%3],
                    topGroundLayer[i], true);
            gameObjects().layers().shouldLayersCollide(fallingLeafLayer[(i+2)%3],
                    topGroundLayer[i], true);
        }
    }
    private void changePointerByMovingRight() {
        int tmp =pointerToRightLayers;
        pointerToRightLayers = pointerToLeftLayers;
        pointerToLeftLayers = pointerToMiddleLayers;
        pointerToMiddleLayers = tmp;
    }
    private void changePointerByMovingLeft(){
        int tmp = pointerToLeftLayers;
        pointerToLeftLayers = pointerToRightLayers;
        pointerToRightLayers = pointerToMiddleLayers;
        pointerToMiddleLayers = tmp;
    }
    private void deleteObjectsInFrame(int frameNum){
        for(GameObject gameObject : gameObjects().objectsInLayer(groundLayer[frameNum])){
            gameObjects().removeGameObject(gameObject, groundLayer[frameNum]);
        }
        for(GameObject gameObject : gameObjects().objectsInLayer(trunkLayer[frameNum])){
            gameObjects().removeGameObject(gameObject, trunkLayer[frameNum]);
        }
        for(GameObject gameObject : gameObjects().objectsInLayer(fallingLeafLayer[frameNum])){
            gameObjects().removeGameObject(gameObject, fallingLeafLayer[frameNum]);

        }
        for(GameObject gameObject : gameObjects().objectsInLayer(staticLeafLayer[frameNum])){
            gameObjects().removeGameObject(gameObject, staticLeafLayer[frameNum]);
        }
        for(GameObject gameObject : gameObjects().objectsInLayer(topGroundLayer[frameNum])){
            gameObjects().removeGameObject(gameObject, topGroundLayer[frameNum]);

        }
    }
}
