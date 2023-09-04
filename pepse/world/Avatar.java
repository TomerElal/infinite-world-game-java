package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.awt.*;
import java.awt.event.KeyEvent;
/**
 * A public class that represents an avatar objects for the game.
 * It has also a static function that can creates one and adding it to the game.
 */
public class Avatar extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private static final int NOT_MOVING = 0;
    private static final float ACCELERATION_FOR_Y = 700;
    private static final int START_ANIMATION = 0;
    private static final float AVATAR_MASS = 0;
    private static final float ENERGY_MAX_COUNTER = 100;
    private static final float CHANGE_OF_ENERGY = 0.5f;
    private static final int MOVING_ANIMATION = 2;
    private static final int NO_ENERGY = 0;
    private static final int JUMPING_ANIMATION = 1;
    private static final int FLYING_ANIMATION = 3;
    private static final Vector2 LOCATION_OF_ENERGY_TEXT = Vector2.ZERO;
    private static final Vector2 SIZE_OF_ENERGY_TEXT = Vector2.of(30,30);
    private static final String ENERGY_TEXT_FONT = "algerian";
    private static final String ENERGY_TEXT_START_LINE = "energy: ";
    private static final int NUMBER_OF_ANIMATIONS_FOR_AVATAR_STATE = 4;
    private static final int STANDING_AND_MOVING_ANIMATIONS = 2;
    private static final int NUMBER_OF_IMAGES_FOR_ANIMATIONS_1 = 4;
    private static final int NUMBER_OF_IMAGES_FOR_ANIMATIONS_2 = 6;
    private static final double TIME_BETWEEN_CLIPS = 0.1;
    private static final Vector2 AVATAR_SIZE = Vector2.of(40, 80);
    private static final int RUNNIG_ANIMATION = 2;
    private float energyCounter;
    private final AnimationRenderable[] animations;
    private final GameObject energyPresentation;
    private final UserInputListener inputListener;
    /**
     * Construct a new GameObject instance.
     * It gets all the animation as renderable object for showing different versions of it.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param animations    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions,
                  AnimationRenderable[] animations, UserInputListener inputListener,
                  GameObject energyPresentation) {
        super(topLeftCorner, dimensions, animations[START_ANIMATION]);
        this.animations = animations;
        physics().setMass(AVATAR_MASS);
        this.energyPresentation = energyPresentation;
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.transform().setAccelerationY(ACCELERATION_FOR_Y);
        this.inputListener = inputListener;
        this.energyCounter = ENERGY_MAX_COUNTER;
    }
    /**
     * An override for the update method that add functional to the game when the avatar moves.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateTextEnergy();
        if (this.getVelocity().y() == NOT_MOVING && energyCounter < ENERGY_MAX_COUNTER){
            energyCounter += CHANGE_OF_ENERGY;
        }
        int xVelocity = NOT_MOVING;
        int currentAnimation = START_ANIMATION;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVelocity -= MOVEMENT_SPEED;
            currentAnimation = MOVING_ANIMATION;
            this.renderer().setIsFlippedHorizontally(true);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVelocity += MOVEMENT_SPEED;
            currentAnimation = MOVING_ANIMATION;
            this.renderer().setIsFlippedHorizontally(false);
        }
        this.transform().setVelocityX(xVelocity);
        this.renderer().setRenderable(animations[currentAnimation]);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == NOT_MOVING) {
            this.transform().setVelocityY(-MOVEMENT_SPEED);
            this.renderer().setRenderable(animations[JUMPING_ANIMATION]);
        } else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energyCounter > NO_ENERGY) {
            this.transform().setVelocityY(-MOVEMENT_SPEED);
            this.renderer().setRenderable(animations[FLYING_ANIMATION]);
            energyCounter -= CHANGE_OF_ENERGY;
        }
    }

    /**
     * A public static function that creates an avatar object for the game.
     * @param gameObjects the collection to add the new avatar object to.
     * @param layer the layer to add the avatar object to.
     * @param topLeftCorner the top left corner to place the avatar object to.
     * @param inputListener the object that can gives information about the user interaction.
     * @param imageReader the object that can read an image by a given path.
     * @return the new avatar object.
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader){
        GameObject energyText = new GameObject(LOCATION_OF_ENERGY_TEXT, SIZE_OF_ENERGY_TEXT,
                new TextRenderable(ENERGY_TEXT_START_LINE, ENERGY_TEXT_FONT));
        gameObjects.addGameObject(energyText);
        energyText.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Renderable[][] avatarStates = new Renderable[NUMBER_OF_ANIMATIONS_FOR_AVATAR_STATE][];
        AnimationRenderable[] animations = new AnimationRenderable[NUMBER_OF_ANIMATIONS_FOR_AVATAR_STATE];
        createsAnimationRenderForAvatar(imageReader, avatarStates, animations);
        Avatar myAvatar = new Avatar(topLeftCorner,AVATAR_SIZE, animations, inputListener,
                energyText);
        gameObjects.addGameObject(myAvatar, layer);
        return myAvatar;
    }
    private static void createsAnimationRenderForAvatar(ImageReader imageReader,
                                                        Renderable[][] avatarStates,
                                                        AnimationRenderable[] animations) {
        for (int i = 0; i < NUMBER_OF_ANIMATIONS_FOR_AVATAR_STATE; i++) {
            if (i < STANDING_AND_MOVING_ANIMATIONS){
                avatarStates[i] = new Renderable[NUMBER_OF_IMAGES_FOR_ANIMATIONS_1];
            }else{
                avatarStates[i] = new Renderable[NUMBER_OF_IMAGES_FOR_ANIMATIONS_2];
            }
        }
        for (int i = 0; i < NUMBER_OF_ANIMATIONS_FOR_AVATAR_STATE; i++) {
            avatarStates[START_ANIMATION][i]  = imageReader.readImage("assets/avatar/idle_" + i + ".png",
                    true);
            avatarStates[JUMPING_ANIMATION][i]  = imageReader.readImage("assets/avatar/jump_" + i + ".png",
                    true);
            avatarStates[RUNNIG_ANIMATION][i]  = imageReader.readImage("assets/avatar/run_" + i + ".png",
                    true);
            avatarStates[FLYING_ANIMATION][i]  = imageReader.readImage("assets/avatar/swim_" + i + ".png",
                    true);
        }
        for (int i = 4; i < NUMBER_OF_IMAGES_FOR_ANIMATIONS_2; i++) {
            avatarStates[RUNNIG_ANIMATION][i]  = imageReader.readImage("assets/avatar/run_" + i + ".png",
                    true);
            avatarStates[FLYING_ANIMATION][i]  = imageReader.readImage("assets/avatar/swim_" + i + ".png",
                    true);
        }
        for (int j = 0; j < NUMBER_OF_IMAGES_FOR_ANIMATIONS_1; j++) {
            animations[j] = new AnimationRenderable(avatarStates[j], TIME_BETWEEN_CLIPS);
        }
    }
    private void updateTextEnergy() {
        TextRenderable textRenderable = (TextRenderable) energyPresentation.renderer().getRenderable();
        textRenderable.setColor(Color.BLACK);
        textRenderable.setString("Energy = " + energyCounter);
    }
}
