/*
 * Author(s): Jacob Dixon @jacobrdixon.com
 * Date: 6/10/2018 - 13/10/2018
 */

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Player extends ImageView {

    // Player is in jump animation
    private static boolean inJumpAnimation = false;

    // Player Speed When Moved
    private static double playerSpeed = 5.0;

    // Player Velocity
    private static double vX = 0.0;
    private static double vY = 0.0;

    private boolean isMovingRight = false;
    private boolean isMovingLeft = false;

    // Gravity
    static double g = 0.5;

    Collision playerCollision = new Collision();

    // Temporary object created fore testing collision.
    Rectangle box = MainApplication.box;

    Player(double x, double y, double width, double height, Image sprite) {
        this.setX(x);
        this.setY(y);
        this.setFitWidth(width);
        this.setFitHeight(height);
        this.setImage(sprite);

    }

    /**
     * TODO Add descriptions
     * Check Collision Top
     * @return
     */
    public boolean checkStageCollisionTop(){
        return(this.getY() <= 0);
    }

    /**
     * TODO Add descriptions
     * Check Collision Bottom
     * @return
     */
    public boolean checkStageCollisionBottom(){
        return(this.getY()  + this.getFitHeight() >= MainApplication.WINDOW_SIZE_Y);
    }

    /**
     * TODO Add descriptions
     * Check Collision Left
     * @return Whether the player is colliding with the left edge of the stage or not.
     */
    public boolean checkStageCollisionLeft() {
        return (this.getX() <= 0);
    }

    /**
     * TODO Add descriptions
     * Check Collision Right
     * @return Returns whether the player is colliding with the right edge of the stage or not.
     */
    public boolean checkStageCollisionRight(){
        return(this.getX() + this.getFitWidth() >= MainApplication.WINDOW_SIZE_X);
    }

    /**
     * TODO update description
     * Player Movement
     * Moves the player every frame.
     */
    public void move(){
        this.setX(this.getX() + vX);
        this.setY(this.getY() + vY);
    }

    /**
     * TODO Add descriptions
     *
     * Position and size can be called fromm ImageView.
     *
     * @return Returns value of Y velocity.
     */
    public double getvY() {
        return vY;
    }

    /**
     * TODO Add descriptions
     * @return Returns value of X velocity.
     */
    public double getvX() {
        return vX;
    }

    /**
     * Player update method called every frame.
     */
    public void onUpdate(boolean up, boolean left, boolean right) {

        vX = 0;

        if(!inJumpAnimation) {
            vY = 0;
        }

        // Player Controls

        //  |- L&R Controls

        if (right &&
                !checkStageCollisionRight() &&
                !( this.getX() + this.getFitWidth() + playerSpeed >= MainApplication.WINDOW_SIZE_X ) &&
                !( playerCollision.isCollidingRight( box,this ) ) ) {

            vX += playerSpeed;
            isMovingRight = true;

        } else if ( isMovingRight && this.getX() + this.getFitWidth() + playerSpeed >= MainApplication.WINDOW_SIZE_X ) {
            setX( MainApplication.WINDOW_SIZE_X - this.getFitWidth() );
            isMovingRight = false;

        }

        if (left &&
                !checkStageCollisionLeft() &&
                !( this.getX() - playerSpeed <= 0 ) &&
                !( playerCollision.isCollidingLeft( box,this ) ) ) {

            vX += playerSpeed * -1;
            isMovingLeft = true;

        } else if ( isMovingLeft && this.getX() - playerSpeed <= 0 ) {
            setX(0);
            isMovingLeft = false;

        }

        // |- Jumping Controls

        if(inJumpAnimation){

            if(!(this.getY() + this.getFitHeight() >= MainApplication.WINDOW_SIZE_Y ) ) {
                vY += g;
            }else if( this.getY() + this.getFitHeight() >= MainApplication.WINDOW_SIZE_Y){
                vY = 0;
                inJumpAnimation = false;
            }

            // ?
            if (playerCollision.willCollide(box, this)) {
                vY  = 0.0;

                double diff = box.getY() - (this.getY() + this.getFitHeight());
                this.setY(this.getY() + diff);

                inJumpAnimation = false;
            }

        }

        // Jump
        if (up && !inJumpAnimation){
            vY += -10.0;
            inJumpAnimation = true;


        }

        // Set inJumpAnimation false when player collides with stage bottom.
        if(this.getY() + this.getFitHeight() >= MainApplication.WINDOW_SIZE_Y){
            inJumpAnimation = false;
            this.setY(MainApplication.WINDOW_SIZE_Y - this.getFitHeight());
        }

        // TODO : Update player falling.
        if( !inJumpAnimation && !playerCollision.isCollidingBottom(box, this) && !checkStageCollisionBottom()){
            inJumpAnimation = true;

        }

        move();

    }

}