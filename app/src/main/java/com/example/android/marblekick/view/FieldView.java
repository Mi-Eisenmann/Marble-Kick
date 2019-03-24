package com.example.android.marblekick.view;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.SoundPool;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnTouchListener;

import com.example.android.marblekick.BallMovement.BallMovement;
import com.example.android.marblekick.MainActivity;
import com.example.android.marblekick.R;
import com.example.android.marblekick.VectorCalculationsCollision.Mathematics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;


public class FieldView extends View {

    // Connection to other classes
    Mathematics mathematics = new Mathematics();
    BallMovement ballMovement = new BallMovement();

    public static final int INITIAL_ANIMATION_DURATION = 6000; //6 SECONDS
    public static final Random random = new Random();
    public static final int SPOT_DIAMETER = 200;
    public int LIVES = 3;
    //Static instance variables
    //Collections types for our circles/spots (imageviews) and Animators
    private final Queue<ImageView> spots = new ConcurrentLinkedDeque<>();
    private final Queue<Animator> animators = new ConcurrentLinkedDeque<>();
    private SharedPreferences preferences;
    //Variables that manage the game
    private int spotsTouched;
    public static int level = 1;
    private int viewWidth;
    private int viewHeight;
    private long animationTime;
    private boolean dialogDisplayed;
    private TextView levelTextView;
    private LinearLayout livesLinearLayout;
    public static RelativeLayout relativeLayout;
    private Resources resources;
    private LayoutInflater layoutInflater;
    private Handler spotHandler;

    private long startTime = 0;
    private int last_x; // Coordinates of the finger when starting the movement
    private int last_y;
    public static ImageView Ballspot;
    public static ImageView Hitspot1;
    public static ImageView Hitspot2;
    public static ImageView Hitspot3;
    public static ImageView Hitspot4;
    public static ImageView Hitspot5;

    private float massBall = 1;
    private float massTarget = 1;

    public static float currentBallVelX = 0;
    public static float currentBallX;
    public static float currentBallVelY = 0;
    public static float currentBallY;

    public static float hitBall1VelX = 0;
    public static float hitBall1X;
    public static float hitBall1VelY = 0;
    public static float hitBall1Y;

    public static float hitBall2VelX = 0;
    public static float hitBall2X;
    public static float hitBall2VelY = 0;
    public static float hitBall2Y;

    public static float hitBall3VelX = 0;
    public static float hitBall3X;
    public static float hitBall3VelY = 0;
    public static float hitBall3Y;

    public static float hitBall4VelX = 0;
    public static float hitBall4X;
    public static float hitBall4VelY = 0;
    public static float hitBall4Y;

    public static float hitBall5VelX = 0;
    public static float hitBall5X;
    public static float hitBall5VelY = 0;
    public static float hitBall5Y;

    private ArrayList hittingList;
    private int toRemove = -1;
    private TextView dataTextView;



    @SuppressLint("ClickableViewAccessibility")
    public FieldView(Context context, SharedPreferences sharedPreferences, RelativeLayout parentLayout) {
        super(context);

        preferences = sharedPreferences;

        //save resources for loading external values
        resources = context.getResources();

        //save LayoutInflater
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Setup UI components
        relativeLayout = parentLayout;
        livesLinearLayout = relativeLayout.findViewById(R.id.lifeLinearLayout);
        levelTextView = relativeLayout.findViewById(R.id.levelTextview);
        //dataTextView = relativeLayout.findViewById(R.id.data);

        spotHandler = new Handler();
    }

    // Time Procedure
    Handler timerHandler = new Handler();
    final Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            int[] posOffset = new int [ 2 ];
            relativeLayout.getLocationInWindow(posOffset);

            // Movement of the Balls
            ballMovement.Movement();

            // Check for Collisions
            if (collisionConditions()){
                collision();
            }

            // Checking for Wall contact
            if (wallContact()){
                removeSpot();
                toRemove = -1;
            }

            // Checking for Level-Up
            if (determineVictory()){
                level += 1;
                LIVES += 1;
                resetGame();
            }

            // Checking for a Loss
            if (determineLoss()){
                LIVES -= 1;
                resetGame();
            }


            // Reset the Game when Lives fall to 0
            if (LIVES == 0){
                level = 1;
                LIVES = 3;
                fullRemoveAllSpots();
                resetGame();
            }

            /*dataTextView.setText(
                    String.valueOf(currentBallX) + "   " + String.valueOf(currentBallY) + "   " + String.valueOf(currentBallVelX) + "   " + String.valueOf(currentBallVelY) + "\n"
                    + String.valueOf(hitBall1X) + "   " + String.valueOf(hitBall1Y) + "   " + String.valueOf(hitBall1VelX) + "   " + String.valueOf(hitBall1VelY) + "\n"
                    + String.valueOf(hitBall2X) + "   " + String.valueOf(hitBall2Y) + "   " + String.valueOf(hitBall2VelX) + "   " + String.valueOf(hitBall2VelY) + "\n"
                    + String.valueOf(hitBall3X) + "   " + String.valueOf(hitBall3Y) + "   " + String.valueOf(hitBall3VelX) + "   " + String.valueOf(hitBall3VelY) + "\n"
                    + String.valueOf(hitBall4X) + "   " + String.valueOf(hitBall4Y) + "   " + String.valueOf(hitBall4VelX) + "   " + String.valueOf(hitBall4VelY) + "\n"
                    + String.valueOf(hitBall5X) + "   " + String.valueOf(hitBall5Y) + "   " + String.valueOf(hitBall5VelX) + "   " + String.valueOf(hitBall5VelY)
            );*/

            timerHandler.postDelayed(this, 10);
        }
    };


    // Check for spheres hitting the wall of the display/field
    public boolean wallContact(){

        // Get the parameters for the walls
        int[] posOffset = new int [ 2 ];
        relativeLayout.getLocationInWindow(posOffset);

        int leftWall = 0;
        int rightWall = MainActivity.xWidth - SPOT_DIAMETER;
        int topWall = 0;
        int bottomWall = MainActivity.yHeight - posOffset[1] - SPOT_DIAMETER;

        // On the right side we don't check for too big x-coordinates, as we save the removed spots there
        if(currentBallX < leftWall || (currentBallX > rightWall && currentBallX < (rightWall + 100)) || currentBallY > bottomWall || currentBallY < topWall){ toRemove = 0; return true;}
        else if(hitBall1X < leftWall || (hitBall1X > rightWall && hitBall1X < (rightWall + 100)) || hitBall1Y > bottomWall || hitBall1Y < topWall){ toRemove = 1; return true;}
        else if(hitBall2X < leftWall || (hitBall2X > rightWall && hitBall2X < (rightWall + 100)) || hitBall2Y > bottomWall || hitBall2Y < topWall){ toRemove = 2; return true;}
        else if(hitBall3X < leftWall || (hitBall3X > rightWall && hitBall3X < (rightWall + 100)) || hitBall3Y > bottomWall || hitBall3Y < topWall){ toRemove = 3; return true;}
        else if(hitBall4X < leftWall || (hitBall4X > rightWall && hitBall4X < (rightWall + 100)) || hitBall4Y > bottomWall || hitBall4Y < topWall){ toRemove = 4; return true;}
        else if(hitBall5X < leftWall || (hitBall5X > rightWall && hitBall5X < (rightWall + 100)) || hitBall5Y > bottomWall || hitBall5Y < topWall){ toRemove = 5; return true;}

        return false;
    }

    // Remove a specific spot from the field and palce outside the field
    public void removeSpot(){

        switch(toRemove){
            case 0: relativeLayout.removeView(Ballspot); currentBallX = 2000; currentBallY = 0; currentBallVelX = 0; currentBallVelY = 0; break;
            case 1: relativeLayout.removeView(Hitspot1); hitBall1X = 2000; hitBall1Y = 300; hitBall1VelX = 0; hitBall1VelY = 0; break;
            case 2: relativeLayout.removeView(Hitspot2); hitBall2X = 2000; hitBall2Y = 600; hitBall2VelX = 0; hitBall2VelY = 0; break;
            case 3: relativeLayout.removeView(Hitspot3); hitBall3X = 2000; hitBall3Y = 900; hitBall3VelX = 0; hitBall3VelY = 0; break;
            case 4: relativeLayout.removeView(Hitspot4); hitBall4X = 2000; hitBall4Y = 1200; hitBall4VelX = 0; hitBall4VelY = 0; break;
            case 5: relativeLayout.removeView(Hitspot5); hitBall5X = 2000; hitBall5Y = 1500; hitBall5VelX = 0; hitBall5VelY = 0; break;
        }

    }

    // Remove all spots
    public void fullRemoveAllSpots(){
        toRemove = 0; removeSpot();
        toRemove = 1; removeSpot();
        toRemove = 2; removeSpot();
        toRemove = 3; removeSpot();
        toRemove = 4; removeSpot();
        toRemove = 5; removeSpot();
        toRemove = -1;
    }


    // Reset all velocities
    public void resetAllVelocities(){
        currentBallVelX = 0;
        currentBallVelY = 0;
        hitBall1VelX = 0;
        hitBall1VelY = 0;
        hitBall2VelX = 0;
        hitBall2VelY = 0;
        hitBall3VelX = 0;
        hitBall3VelY = 0;
        hitBall4VelX = 0;
        hitBall4VelY = 0;
        hitBall5VelX = 0;
        hitBall5VelY = 0;
    }


    // Condition for Sphere-Collision
    public boolean collisionsOfTwoSpheres(float x1, float y1, float x2, float y2){

        float distanceX = x1 - x2;
        float distanceY = y1 - y2;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance <= SPOT_DIAMETER){
            return true;
        } else {
            return false;
        }
    }


    // Check whether two spheres are colliding
    public boolean collisionConditions(){

        hittingList = new ArrayList();

        int effectiveLevel;
        if(level <= 4){effectiveLevel = level;}
        else{effectiveLevel = 5;}

        switch (effectiveLevel){
            case 1:
                if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall1X, hitBall1Y)) {
                    hittingList.add(0); hittingList.add(1); return true;
                } else {return false;}
            case 2:
                if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall1X, hitBall1Y)) {
                    hittingList.add(0); hittingList.add(1); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall2X, hitBall2Y)) {
                    hittingList.add(0); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall2X, hitBall2Y)) {
                    hittingList.add(1); hittingList.add(2); return true;
                } else {return false;}
            case 3:
                if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall1X, hitBall1Y)) {
                    hittingList.add(0); hittingList.add(1); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall2X, hitBall2Y)) {
                    hittingList.add(0); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall3X, hitBall3Y)) {
                    hittingList.add(0); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall2X, hitBall2Y)) {
                    hittingList.add(1); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall3X, hitBall3Y)) {
                    hittingList.add(1); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(hitBall2X, hitBall2Y, hitBall3X, hitBall3Y)) {
                    hittingList.add(2); hittingList.add(3); return true;
                } else {return false;}
            case 4:
                if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall1X, hitBall1Y)) {
                    hittingList.add(0); hittingList.add(1); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall2X, hitBall2Y)) {
                    hittingList.add(0); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall3X, hitBall3Y)) {
                    hittingList.add(0); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall4X, hitBall4Y)) {
                    hittingList.add(0); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall2X, hitBall2Y)) {
                    hittingList.add(1); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall3X, hitBall3Y)) {
                    hittingList.add(1); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall4X, hitBall4Y)) {
                    hittingList.add(1); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(hitBall2X, hitBall2Y, hitBall3X, hitBall3Y)) {
                    hittingList.add(2); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(hitBall2X, hitBall2Y, hitBall4X, hitBall4Y)) {
                    hittingList.add(2); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(hitBall3X, hitBall3Y, hitBall4X, hitBall4Y)) {
                    hittingList.add(3); hittingList.add(4); return true;
                } else {return false;}
            case 5:
                if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall1X, hitBall1Y)) {
                    hittingList.add(0); hittingList.add(1); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall2X, hitBall2Y)) {
                    hittingList.add(0); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall3X, hitBall3Y)) {
                    hittingList.add(0); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall4X, hitBall4Y)) {
                    hittingList.add(0); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(currentBallX, currentBallY, hitBall5X, hitBall5Y)) {
                    hittingList.add(0); hittingList.add(5); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall2X, hitBall2Y)) {
                    hittingList.add(1); hittingList.add(2); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall3X, hitBall3Y)) {
                    hittingList.add(1); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall4X, hitBall4Y)) {
                    hittingList.add(1); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(hitBall1X, hitBall1Y, hitBall5X, hitBall5Y)) {
                    hittingList.add(1); hittingList.add(5); return true;
                } else if (collisionsOfTwoSpheres(hitBall2X, hitBall2Y, hitBall3X, hitBall3Y)) {
                    hittingList.add(2); hittingList.add(3); return true;
                } else if (collisionsOfTwoSpheres(hitBall2X, hitBall2Y, hitBall4X, hitBall4Y)) {
                    hittingList.add(2); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(hitBall2X, hitBall2Y, hitBall5X, hitBall5Y)) {
                    hittingList.add(2); hittingList.add(5); return true;
                } else if (collisionsOfTwoSpheres(hitBall3X, hitBall3Y, hitBall4X, hitBall4Y)) {
                    hittingList.add(3); hittingList.add(4); return true;
                } else if (collisionsOfTwoSpheres(hitBall3X, hitBall3Y, hitBall5X, hitBall5Y)) {
                    hittingList.add(3); hittingList.add(5); return true;
                } else if (collisionsOfTwoSpheres(hitBall4X, hitBall4Y, hitBall5X, hitBall5Y)) {
                    hittingList.add(4); hittingList.add(5); return true;
                } else {return false;}
        }

        return false;
    }


    // Calculate the velocities after the collision of two spheres
    public void collision() {

        int Col1 = (int) hittingList.get(0);
        int Col2 = (int) hittingList.get(1);

        ArrayList calculationResult = new ArrayList();

        if (Col1 == 0 && Col2 == 1) {
            calculationResult = mathematics.collisionCalculation(massBall, massTarget, currentBallX, currentBallY, currentBallVelX, currentBallVelY, hitBall1X, hitBall1Y, hitBall1VelX, hitBall1VelY);
            currentBallVelX = (float) calculationResult.get(0);
            currentBallVelY = (float) calculationResult.get(1);
            hitBall1VelX = (float) calculationResult.get(2);
            hitBall1VelY = (float) calculationResult.get(3);
        } else if (Col1 == 0 && Col2 == 2) {
            calculationResult = mathematics.collisionCalculation(massBall, massTarget, currentBallX, currentBallY, currentBallVelX, currentBallVelY, hitBall2X, hitBall2Y, hitBall2VelX, hitBall2VelY);
            currentBallVelX = (float) calculationResult.get(0);
            currentBallVelY = (float) calculationResult.get(1);
            hitBall2VelX = (float) calculationResult.get(2);
            hitBall2VelY = (float) calculationResult.get(3);
        } else if (Col1 == 0 && Col2 == 3) {
            calculationResult = mathematics.collisionCalculation(massBall, massTarget, currentBallX, currentBallY, currentBallVelX, currentBallVelY, hitBall3X, hitBall3Y, hitBall3VelX, hitBall3VelY);
            currentBallVelX = (float) calculationResult.get(0);
            currentBallVelY = (float) calculationResult.get(1);
            hitBall3VelX = (float) calculationResult.get(2);
            hitBall3VelY = (float) calculationResult.get(3);
        } else if (Col1 == 0 && Col2 == 4) {
            calculationResult = mathematics.collisionCalculation(massBall, massTarget, currentBallX, currentBallY, currentBallVelX, currentBallVelY, hitBall4X, hitBall4Y, hitBall4VelX, hitBall4VelY);
            currentBallVelX = (float) calculationResult.get(0);
            currentBallVelY = (float) calculationResult.get(1);
            hitBall4VelX = (float) calculationResult.get(2);
            hitBall4VelY = (float) calculationResult.get(3);
        } else if (Col1 == 0 && Col2 == 5) {
            calculationResult = mathematics.collisionCalculation(massBall, massTarget, currentBallX, currentBallY, currentBallVelX, currentBallVelY, hitBall5X, hitBall5Y, hitBall5VelX, hitBall5VelY);
            currentBallVelX = (float) calculationResult.get(0);
            currentBallVelY = (float) calculationResult.get(1);
            hitBall5VelX = (float) calculationResult.get(2);
            hitBall5VelY = (float) calculationResult.get(3);
        } else if (Col1 == 1 && Col2 == 2) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall1X, hitBall1Y, hitBall1VelX, hitBall1VelY, hitBall2X, hitBall2Y, hitBall2VelX, hitBall2VelY);
            hitBall1VelX = (float) calculationResult.get(0);
            hitBall1VelY = (float) calculationResult.get(1);
            hitBall2VelX = (float) calculationResult.get(2);
            hitBall2VelY = (float) calculationResult.get(3);
        } else if (Col1 == 1 && Col2 == 3) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall1X, hitBall1Y, hitBall1VelX, hitBall1VelY, hitBall3X, hitBall3Y, hitBall3VelX, hitBall3VelY);
            hitBall1VelX = (float) calculationResult.get(0);
            hitBall1VelY = (float) calculationResult.get(1);
            hitBall3VelX = (float) calculationResult.get(2);
            hitBall3VelY = (float) calculationResult.get(3);
        } else if (Col1 == 1 && Col2 == 4) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall1X, hitBall1Y, hitBall1VelX, hitBall1VelY, hitBall4X, hitBall4Y, hitBall4VelX, hitBall4VelY);
            hitBall1VelX = (float) calculationResult.get(0);
            hitBall1VelY = (float) calculationResult.get(1);
            hitBall4VelX = (float) calculationResult.get(2);
            hitBall4VelY = (float) calculationResult.get(3);
        } else if (Col1 == 1 && Col2 == 5) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall1X, hitBall1Y, hitBall1VelX, hitBall1VelY, hitBall5X, hitBall5Y, hitBall5VelX, hitBall5VelY);
            hitBall1VelX = (float) calculationResult.get(0);
            hitBall1VelY = (float) calculationResult.get(1);
            hitBall5VelX = (float) calculationResult.get(2);
            hitBall5VelY = (float) calculationResult.get(3);
        } else if (Col1 == 2 && Col2 == 3) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall2X, hitBall2Y, hitBall2VelX, hitBall2VelY, hitBall3X, hitBall3Y, hitBall3VelX, hitBall3VelY);
            hitBall2VelX = (float) calculationResult.get(0);
            hitBall2VelY = (float) calculationResult.get(1);
            hitBall3VelX = (float) calculationResult.get(2);
            hitBall3VelY = (float) calculationResult.get(3);
        } else if (Col1 == 2 && Col2 == 4) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall2X, hitBall2Y, hitBall2VelX, hitBall2VelY, hitBall4X, hitBall4Y, hitBall4VelX, hitBall4VelY);
            hitBall2VelX = (float) calculationResult.get(0);
            hitBall2VelY = (float) calculationResult.get(1);
            hitBall4VelX = (float) calculationResult.get(2);
            hitBall4VelY = (float) calculationResult.get(3);
        } else if (Col1 == 2 && Col2 == 5) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall2X, hitBall2Y, hitBall2VelX, hitBall2VelY, hitBall5X, hitBall5Y, hitBall5VelX, hitBall5VelY);
            hitBall2VelX = (float) calculationResult.get(0);
            hitBall2VelY = (float) calculationResult.get(1);
            hitBall5VelX = (float) calculationResult.get(2);
            hitBall5VelY = (float) calculationResult.get(3);
        } else if (Col1 == 3 && Col2 == 4) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall3X, hitBall3Y, hitBall3VelX, hitBall3VelY, hitBall4X, hitBall4Y, hitBall4VelX, hitBall4VelY);
            hitBall3VelX = (float) calculationResult.get(0);
            hitBall3VelY = (float) calculationResult.get(1);
            hitBall4VelX = (float) calculationResult.get(2);
            hitBall4VelY = (float) calculationResult.get(3);
        } else if (Col1 == 3 && Col2 == 5) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall3X, hitBall3Y, hitBall3VelX, hitBall3VelY, hitBall5X, hitBall5Y, hitBall5VelX, hitBall5VelY);
            hitBall3VelX = (float) calculationResult.get(0);
            hitBall3VelY = (float) calculationResult.get(1);
            hitBall5VelX = (float) calculationResult.get(2);
            hitBall5VelY = (float) calculationResult.get(3);
        } else if (Col1 == 4 && Col2 == 5) {
            calculationResult = mathematics.collisionCalculation(massTarget, massTarget, hitBall4X, hitBall4Y, hitBall4VelX, hitBall4VelY, hitBall5X, hitBall5Y, hitBall5VelX, hitBall5VelY);
            hitBall4VelX = (float) calculationResult.get(0);
            hitBall4VelY = (float) calculationResult.get(1);
            hitBall5VelX = (float) calculationResult.get(2);
            hitBall5VelY = (float) calculationResult.get(3);
        }

    }


    // Check whether the level is finished succesfully
    public boolean determineVictory(){

        int effectiveLevel;

        if (level <= 4){effectiveLevel = level;}
        else {effectiveLevel = 5;}

        switch (effectiveLevel){
            case 1: if(/*currentBallX == 2000 &&*/ hitBall1X == 2000){return true;} break;
            case 2: if(/*currentBallX == 2000 &&*/ hitBall1X == 2000 && hitBall2X == 2000){return true;} break;
            case 3: if(/*currentBallX == 2000 &&*/ hitBall1X == 2000 && hitBall2X == 2000 && hitBall3X == 2000){return true;} break;
            case 4: if(/*currentBallX == 2000 &&*/ hitBall1X == 2000 && hitBall2X == 2000 && hitBall3X == 2000 && hitBall4X == 2000){return true;} break;
            case 5: if(/*currentBallX == 2000 &&*/ hitBall1X == 2000 && hitBall2X == 2000 && hitBall3X == 2000 && hitBall4X == 2000 && hitBall5X == 2000){return true;}
        }

        return false;
    }


    // Check whether the level is finished unsuccesfully
    public boolean determineLoss(){

        int effectiveLevel;

        if (level <= 4){effectiveLevel = level;}
        else {effectiveLevel = 5;}

        switch (effectiveLevel){
            case 1: if(currentBallX == 2000 &&
                    hitBall1X != 2000 && hitBall1VelX <= 0.01 && hitBall1VelY <= 0.01){return true;} break;
            case 2: if(currentBallX == 2000 && (
                    (hitBall1X != 2000 && hitBall1VelX <= 0.01 && hitBall1VelY <= 0.01) ||
                    (hitBall2X != 2000 && hitBall2VelX <= 0.01 && hitBall2VelY <= 0.01) ) ){return true;} break;
            case 3: if(currentBallX == 2000 && (
                    (hitBall1X != 2000 && hitBall1VelX <= 0.01 && hitBall1VelY <= 0.01) ||
                    (hitBall2X != 2000 && hitBall2VelX <= 0.01 && hitBall2VelY <= 0.01) ||
                    (hitBall3X != 2000 && hitBall3VelX <= 0.01 && hitBall3VelY <= 0.01) ) ){return true;} break;
            case 4: if(currentBallX == 2000 && (
                    (hitBall1X != 2000 && hitBall1VelX <= 0.01 && hitBall1VelY <= 0.01) ||
                    (hitBall2X != 2000 && hitBall2VelX <= 0.01 && hitBall2VelY <= 0.01) ||
                    (hitBall3X != 2000 && hitBall3VelX <= 0.01 && hitBall3VelY <= 0.01) ||
                    (hitBall4X != 2000 && hitBall4VelX <= 0.01 && hitBall4VelY <= 0.01) ) ){return true;} break;
            case 5: if(currentBallX == 2000 && (
                    (hitBall1X != 2000 && hitBall1VelX <= 0.01 && hitBall1VelY <= 0.01) ||
                    (hitBall2X != 2000 && hitBall2VelX <= 0.01 && hitBall2VelY <= 0.01) ||
                    (hitBall3X != 2000 && hitBall3VelX <= 0.01 && hitBall3VelY <= 0.01) ||
                    (hitBall4X != 2000 && hitBall4VelX <= 0.01 && hitBall4VelY <= 0.01) ||
                    (hitBall5X != 2000 && hitBall5VelX <= 0.01 && hitBall5VelY <= 0.01) ) ){return true;}
        }

        return false;
    }


    // store SpotOnView's width/height
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh)
    {
        viewWidth = width; // save the new width
        viewHeight = height; // save the new height
    } // end method onSizeChanged

    // called by the SpotOn Activity when it receives a call to onPause
    public void pause()
    {
        cancelAnimations(); // cancel all outstanding animations
    } // end method pause

    // cancel animations and remove ImageViews representing spots
    private void cancelAnimations()
    {
        // cancel remaining animations
        for (Animator animator : animators)
            animator.cancel();

        // remove remaining spots from the screen
        for (ImageView view : spots)
            relativeLayout.removeView(view);

        timerHandler.removeCallbacks(timerRunnable);
        animators.clear();
        spots.clear();
    } // end method cancelAnimations

    // called by the SpotOn Activity when it receives a call to onResume
    public void resume(Context context) {

        if (!dialogDisplayed)
            resetGame(); // start the game
    }

    // Reload the Game
    public void resetGame() {

        spots.clear(); // empty the List of spots
        animators.clear(); // empty the List of Animators
        livesLinearLayout.removeAllViews(); // clear old lives from screen

        // Remove all the remaining spots and velocities
        timerHandler.removeCallbacks(timerRunnable);

        // Remove all the spots from the field and reset all velocities to zero
        fullRemoveAllSpots();
        resetAllVelocities();

        animationTime = INITIAL_ANIMATION_DURATION; // init animation length
        spotsTouched = 0; // reset the number of spots touched


        displayScores(); // display scores and level

        // Add lives
        for (int i = 0; i < LIVES; i++)
        {
            // add life indicator to screen
            livesLinearLayout.addView(
                    (ImageView) layoutInflater.inflate(R.layout.life, null));
        }

        // Add new Spots
        addTarget();
        addNewSpotBall();

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }


    // display scores and level
    private void displayScores()
    {
        levelTextView.setText(
                resources.getString(R.string.level) + " " + level);
    }


    // Adds the targets
    public void addTarget()
    {
        // Defining the number of spots to position
        int range;
        if (level <= 4){range = level;}
        else {range = 5;}
        float x;
        float y;

        // Place them
        for (int i = 1; i <= range; i++){
            ArrayList coordinates = findSuitableSpot(i);
            x = (float) coordinates.get(0);
            y = (float) coordinates.get(1);

            placeHitSpots(x,y,i);
        }
    }

    // Place the spots at the formely determined positions
    public void placeHitSpots(float x, float y, int number){
        switch (number) {
            case 1: Hitspot1 = (ImageView) layoutInflater.inflate(R.layout.untouched, null);
                spots.add(Hitspot1); // add the new spot to our list of spots
                Hitspot1.setLayoutParams(new RelativeLayout.LayoutParams(
                        SPOT_DIAMETER, SPOT_DIAMETER));
                Hitspot1.setImageResource(R.drawable.red_spot);
                Hitspot1.setX(x); // set spot's starting x location
                Hitspot1.setY(y); // set spot's starting y location
                relativeLayout.addView(Hitspot1); // add spot to the screen
                hitBall1X = x;
                hitBall1Y = y;
                //Toast.makeText(getContext(),"Spawn 1",Toast.LENGTH_SHORT).show();
                break;
            case 2: Hitspot2 = (ImageView) layoutInflater.inflate(R.layout.untouched, null);
                spots.add(Hitspot2); // add the new spot to our list of spots
                Hitspot2.setLayoutParams(new RelativeLayout.LayoutParams(
                        SPOT_DIAMETER, SPOT_DIAMETER));
                Hitspot2.setImageResource(R.drawable.red_spot);
                Hitspot2.setX(x); // set spot's starting x location
                Hitspot2.setY(y); // set spot's starting y location
                relativeLayout.addView(Hitspot2); // add spot to the screen
                hitBall2X = x;
                hitBall2Y = y;
                //Toast.makeText(getContext(),"Spawn 2",Toast.LENGTH_SHORT).show();
                break;
            case 3: Hitspot3 = (ImageView) layoutInflater.inflate(R.layout.untouched, null);
                spots.add(Hitspot1); // add the new spot to our list of spots
                Hitspot3.setLayoutParams(new RelativeLayout.LayoutParams(
                        SPOT_DIAMETER, SPOT_DIAMETER));
                Hitspot3.setImageResource(R.drawable.red_spot);
                Hitspot3.setX(x); // set spot's starting x location
                Hitspot3.setY(y); // set spot's starting y location
                relativeLayout.addView(Hitspot3); // add spot to the screen
                hitBall3X = x;
                hitBall3Y = y;
                //Toast.makeText(getContext(),"Spawn 3",Toast.LENGTH_SHORT).show();
                break;
            case 4: Hitspot4 = (ImageView) layoutInflater.inflate(R.layout.untouched, null);
                spots.add(Hitspot4); // add the new spot to our list of spots
                Hitspot4.setLayoutParams(new RelativeLayout.LayoutParams(
                        SPOT_DIAMETER, SPOT_DIAMETER));
                Hitspot4.setImageResource(R.drawable.red_spot);
                Hitspot4.setX(x); // set spot's starting x location
                Hitspot4.setY(y); // set spot's starting y location
                relativeLayout.addView(Hitspot4); // add spot to the screen
                hitBall4X = x;
                hitBall4Y = y;
                //Toast.makeText(getContext(),"Spawn 4",Toast.LENGTH_SHORT).show();
                break;
            case 5: Hitspot5 = (ImageView) layoutInflater.inflate(R.layout.untouched, null);
                spots.add(Hitspot5); // add the new spot to our list of spots
                Hitspot5.setLayoutParams(new RelativeLayout.LayoutParams(
                        SPOT_DIAMETER, SPOT_DIAMETER));
                Hitspot5.setImageResource(R.drawable.red_spot);
                Hitspot5.setX(x); // set spot's starting x location
                Hitspot5.setY(y); // set spot's starting y location
                relativeLayout.addView(Hitspot5); // add spot to the screen
                hitBall5X = x;
                hitBall5Y = y;
                //Toast.makeText(getContext(),"Spawn 5",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // Finds a location to place the targets in the upper half of the field without overlapps
    public ArrayList findSuitableSpot(int range){

        boolean coordinatesFitting = false;
        float x = 0;
        float y = 0;

        do {
            // choose two random coordinates for the spawn point in the upper half of the playing field
            ArrayList newSpawn = randomCoordinates();
            x = (float) newSpawn.get(0);
            y = (float) newSpawn.get(1);

            coordinatesFitting = checkCoordinatesValidity(x,y,range);
        } while (coordinatesFitting == false);

        ArrayList result = new ArrayList(Arrays.asList(x,y));
        return result;

    }

    // Creates random coordinates in the upper half of the playfield
    public ArrayList randomCoordinates(){
        float x = random.nextInt(MainActivity.xWidth - SPOT_DIAMETER);
        float y = random.nextInt(MainActivity.yHeight/2 - SPOT_DIAMETER);
        ArrayList result = new ArrayList(Arrays.asList(x,y));
        return result;
    }

    // Checks for overlapps with formerly placed targets
    public boolean checkCoordinatesValidity(float x, float y, int range){

        // Choosing a minimal distance between the spheres of 1.42 ~ 1.414... = sqrt(2) , so that they don't overlapp
        switch (range) {
            case 1: return true;
            case 2: if (Math.sqrt((hitBall1X-x)*(hitBall1X-x)+(hitBall1Y-y)*(hitBall1Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    break;
            case 3: if (Math.sqrt((hitBall1X-x)*(hitBall1X-x)+(hitBall1Y-y)*(hitBall1Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    else if (Math.sqrt((hitBall2X-x)*(hitBall2X-x)+(hitBall2Y-y)*(hitBall2Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    break;
            case 4: if (Math.sqrt((hitBall1X-x)*(hitBall1X-x)+(hitBall1Y-y)*(hitBall1Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    else if (Math.sqrt((hitBall2X-x)*(hitBall2X-x)+(hitBall2Y-y)*(hitBall2Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    else if (Math.sqrt((hitBall3X-x)*(hitBall3X-x)+(hitBall3Y-y)*(hitBall3Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    break;
            case 5: if (Math.sqrt((hitBall1X-x)*(hitBall1X-x)+(hitBall1Y-y)*(hitBall1Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    else if (Math.sqrt((hitBall2X-x)*(hitBall2X-x)+(hitBall2Y-y)*(hitBall2Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    else if (Math.sqrt((hitBall3X-x)*(hitBall3X-x)+(hitBall3Y-y)*(hitBall3Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    else if (Math.sqrt((hitBall4X-x)*(hitBall4X-x)+(hitBall4Y-y)*(hitBall4Y-y)) <= 1.42*SPOT_DIAMETER){return false;}
                    break;
        }
        return true;
    }

    // Add the play Ball
    public void addNewSpotBall()
    {
        // choose two random coordinates for the starting and ending points
        //int x = random.nextInt(viewWidth - SPOT_DIAMETER);
        float x = (MainActivity.xWidth/2 - SPOT_DIAMETER/2);
        float y = (3*MainActivity.yHeight/4 - SPOT_DIAMETER/2);

        // create new spot
        /*final ImageView spot =
                (ImageView) layoutInflater.inflate(R.layout.untouched, null);*/
        Ballspot = (ImageView) layoutInflater.inflate(R.layout.untouched, null);
        spots.add(Ballspot); // add the new spot to our list of spots
        Ballspot.setLayoutParams(new RelativeLayout.LayoutParams(
                SPOT_DIAMETER, SPOT_DIAMETER));
        Ballspot.setImageResource(R.drawable.green_spot);
        Ballspot.setX(x); // set spot's starting x location
        Ballspot.setY(y); // set spot's starting y location
        currentBallX = x;
        currentBallY = y;

        relativeLayout.addView(Ballspot); // add spot to the screen

        Ballspot.setOnTouchListener(onTouchListener());

    } // end addNewSpotAim method


    // called when the user touches the screen, but not a spot
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        return true;
    }

    public OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                int[] posOffset = new int [ 2 ];
                relativeLayout.getLocationInWindow(posOffset);

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        //ball_moving = true;
                        break;

                    case MotionEvent.ACTION_UP:
                        last_x = x;
                        last_y = y;
                        //ball_moving = false;
                        //movingBall();

                        currentBallVelX = (float) 0.2 * ( currentBallX + SPOT_DIAMETER/2 - last_x);
                        currentBallVelY = (float) 0.2 * (-currentBallY - posOffset[1] - SPOT_DIAMETER/2 + last_y);

                        //Toast.makeText(getContext(),"Here",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(),String.valueOf(currentBallVelY),Toast.LENGTH_SHORT).show();

                    case MotionEvent.ACTION_MOVE:

                        // Let the Spot follow the finger and compensate the offsets of the header and the spotsize
                        /*int[] posOffset = new int [ 2 ];
                        relativeLayout.getLocationInWindow(posOffset);
                        Ballspot.setX(x - posOffset[0] - Ballspot.getWidth()/2);
                        Ballspot.setY(y - posOffset[1] - Ballspot.getHeight()/2);*/

                        //movingBall();
                        break;
                }
                //mainLayout.invalidate();
                return true;
            }
        };
    }

}
