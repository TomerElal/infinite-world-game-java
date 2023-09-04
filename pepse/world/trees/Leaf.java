package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;
import java.util.Random;
/**
 * A gameObject that extends the block, since it has the same features and more.
 * Its duty in the game is to be a leaf.
 * There are two behaviors the one leaf can have - a static leaf that only vibrates in its
 * location, and a dropout leaf that after a random time falls to the ground while fading out,
 * stays there a random time again, returns to its starting position, fading in, and start its
 * journey again.
 */
public class Leaf extends Block {
    private static final Float START_ANGLE = -7f;
    private static final Float END_ANGLE = 7f;
    private static final float ANGLE_TRANSITION_TIME = 1.5f;
    private static final float Y_ZERO_VELOCITY = 0;
    private static final float FALLING_Y_VELOCITY = 30;
    private static final float FACTOR_MULT_FOR_DIMENSION_SIZE = 0.85f;
    private static final float DIMENSION_TRANSITION_TIME = 1.5f;
    private static final int MIN_REBORN_WAIT_TIME = 2;
    private static final int MAX_REBORN_WAIT_TIME = 6;
    private static final int MAX_FADE_IN_WAIT_TIME = 5;
    private static final float FADE_IN_WAIT_TIME_MULT_FACTOR = 2;
    private static final float STATIC_SCHEDULEDTASK_WAIT_TIME_MULT_FACTOR = 4;
    private static final int MAX_SCHEDULEDTASK_WAIT_TIME = 5;
    private static final float FALLING_SCHEDULEDTASK_WAIT_TIME_MULT_FACTOR = 2;
    private static final float LEAF_MASS = 0;
    private static final float FADING_OUT_TIME = 15;
    private static final Float X_VELOCITY_START_VALUE = -20f;
    private static final Float X_VELOCITY_END_VALUE = 20f;
    private static final float VELOCITY_X_TRANSITION_TIME = 2;
    private static final float MULT_FACTOR_FOR_DOWN_VELOCITY = 40;
    private final Random random;
    private final Vector2 initialLocation;
    private Transition<Float> transition;
    /**
     * A constructor for the leaf instance.
     * @param topLeftCorner the location of the new leaf.
     * @param renderable the way that we want to render this object.
     * @param seed the seed for creating random numbers.
     *             by getting it from outside, we can recreate leaf the same way exactly by
     *             using the same seed.
     * @param strategy this determines whither strategy this leaf will hold - static or falling.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable, int seed, boolean strategy) {
        super(topLeftCorner, renderable);
        random = new Random(seed);
        this.initialLocation = getCenter();
        this.activateLeafStrategy(strategy);
    }
    /**
     * A public method that handle collisions, when entering them by using the super
     * functionality, removing the transition component, and setting the velocity to zero.
     * So the leaf won't continue to fall.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.removeComponent(transition);
        setVelocity(Vector2.ZERO);
    }
    /**
     * A public method that handle collisions, by using the super
     * functionality, and setting the y velocity to zero, so the leaf won't
     * continue to fall.
     * @param other The collision partner.
     * @param collision Information regarding this collision.
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        super.onCollisionStay(other, collision);
        this.transform().setVelocityY(Y_ZERO_VELOCITY);
    }
    /**
     * A public method that handle collisions, when exiting them by using the super
     * functionality, and setting the y velocity to keep falling when the leaf did not return
     * to its initial location yet.
     * @param other The former collision partner.
     */
    @Override
    public void onCollisionExit(GameObject other) {
        super.onCollisionExit(other);
        if(this.getCenter().y() != initialLocation.y()){ // in order to let the leaves wait in the tree
            // when returning there after the fall
            this.transform().setVelocityY(FALLING_Y_VELOCITY);
        }
    }
    private void leafVibration() {
        new Transition<>(this, this.renderer()::setRenderableAngle,
                START_ANGLE, END_ANGLE, Transition.CUBIC_INTERPOLATOR_FLOAT, ANGLE_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        new Transition<>(this, this::setDimensions, Vector2.of(Block.SIZE, Block.SIZE),
                Vector2.of(Block.SIZE, Block.SIZE).mult(FACTOR_MULT_FOR_DIMENSION_SIZE),
                Transition.CUBIC_INTERPOLATOR_VECTOR, DIMENSION_TRANSITION_TIME,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }
    private void repeatLeafLifeCycle(){
        this.setCenter(initialLocation);
        float rebornWaitTime = Math.max(random.nextInt(MAX_REBORN_WAIT_TIME),MIN_REBORN_WAIT_TIME);
        float fadeInWaitTime =
                (random.nextFloat() + random.nextInt(MAX_FADE_IN_WAIT_TIME))*FADE_IN_WAIT_TIME_MULT_FACTOR;
        this.setVelocity(Vector2.ZERO);
        new ScheduledTask(this, rebornWaitTime, false,
                ()->this.renderer().fadeIn(fadeInWaitTime));
        new ScheduledTask(this, rebornWaitTime + fadeInWaitTime,
                false, this::leafFall);

    }
    private void activateLeafStrategy(boolean vibrateStrategy){
        if(vibrateStrategy){
            new ScheduledTask(this,
                    random.nextFloat()* STATIC_SCHEDULEDTASK_WAIT_TIME_MULT_FACTOR,
                    false, this::leafVibration);
        }else {
            new ScheduledTask(this,
                    (random.nextFloat() + random.nextInt(MAX_SCHEDULEDTASK_WAIT_TIME))*
                            FALLING_SCHEDULEDTASK_WAIT_TIME_MULT_FACTOR, false, this::leafFall);
        }

    }
    private void leafFall(){
        physics().setMass(LEAF_MASS); // to prevent the falling leaf from moving the ground
        this.renderer().fadeOut(FADING_OUT_TIME, this::repeatLeafLifeCycle);
        this.transition = new Transition<>(this,this.transform()::setVelocityX,
                X_VELOCITY_START_VALUE, X_VELOCITY_END_VALUE,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                VELOCITY_X_TRANSITION_TIME, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        this.setVelocity(Vector2.DOWN.mult(MULT_FACTOR_FOR_DOWN_VELOCITY));
    }
}
