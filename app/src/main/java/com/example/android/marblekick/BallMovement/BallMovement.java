package com.example.android.marblekick.BallMovement;

import com.example.android.marblekick.MainActivity;
import com.example.android.marblekick.view.FieldView;

public class BallMovement {

    // Calculating the sphere movements
    public void Movement(){

        int effectiveLevel;
        if(FieldView.level <= 4){effectiveLevel = FieldView.level;}
        else{effectiveLevel = 5;}

        switch(effectiveLevel){
            case 1: ballMovement(); target1Movement(); break;
            case 2: ballMovement(); target1Movement(); target2Movement(); break;
            case 3: ballMovement(); target1Movement(); target2Movement(); target3Movement(); break;
            case 4: ballMovement(); target1Movement(); target2Movement(); target3Movement(); target4Movement(); break;
            case 5: ballMovement(); target1Movement(); target2Movement(); target3Movement(); target4Movement(); target5Movement(); break;
        }
    }

    public void ballMovement(){
        int[] posOffset = new int [ 2 ];
        FieldView.relativeLayout.getLocationInWindow(posOffset);

        if (FieldView.currentBallX <= 0 || FieldView.currentBallX >= MainActivity.xWidth - FieldView.SPOT_DIAMETER) {
            FieldView.currentBallVelX *= -1;
        }
        if (FieldView.currentBallY <= 0 || FieldView.currentBallY >= MainActivity.yHeight - posOffset[1] - FieldView.SPOT_DIAMETER) {
            FieldView.currentBallVelY *= -1;
        }
        FieldView.currentBallX += FieldView.currentBallVelX;
        FieldView.currentBallY -= FieldView.currentBallVelY;

        FieldView.Ballspot.setX(FieldView.currentBallX);
        FieldView.Ballspot.setY(FieldView.currentBallY);

        FieldView.currentBallVelX *= 0.98;
        FieldView.currentBallVelY *= 0.98;
    }

    public void target1Movement(){
        int[] posOffset = new int [ 2 ];
        FieldView.relativeLayout.getLocationInWindow(posOffset);

        if (FieldView.hitBall1X <= 0 || FieldView.hitBall1X >= MainActivity.xWidth - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall1VelX *= -1;
        }
        if (FieldView.hitBall1Y <= 0 || FieldView.hitBall1Y >= MainActivity.yHeight - posOffset[1] - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall1VelY *= -1;
        }
        FieldView.hitBall1X += FieldView.hitBall1VelX;
        FieldView.hitBall1Y -= FieldView.hitBall1VelY;

        FieldView.Hitspot1.setX(FieldView.hitBall1X);
        FieldView.Hitspot1.setY(FieldView.hitBall1Y);

        FieldView.hitBall1VelX *= 0.98;
        FieldView.hitBall1VelY *= 0.98;
    }

    public void target2Movement(){
        int[] posOffset = new int [ 2 ];
        FieldView.relativeLayout.getLocationInWindow(posOffset);

        if (FieldView.hitBall2X <= 0 || FieldView.hitBall2X >= MainActivity.xWidth - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall2VelX *= -1;
        }
        if (FieldView.hitBall2Y <= 0 || FieldView.hitBall2Y >= MainActivity.yHeight - posOffset[1] - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall2VelY *= -1;
        }
        FieldView.hitBall2X += FieldView.hitBall2VelX;
        FieldView.hitBall2Y -= FieldView.hitBall2VelY;

        FieldView.Hitspot2.setX(FieldView.hitBall2X);
        FieldView.Hitspot2.setY(FieldView.hitBall2Y);

        FieldView.hitBall2VelX *= 0.98;
        FieldView.hitBall2VelY *= 0.98;
    }

    public void target3Movement(){
        int[] posOffset = new int [ 2 ];
        FieldView.relativeLayout.getLocationInWindow(posOffset);

        if (FieldView.hitBall3X <= 0 || FieldView.hitBall3X >= MainActivity.xWidth - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall3VelX *= -1;
        }
        if (FieldView.hitBall3Y <= 0 || FieldView.hitBall3Y >= MainActivity.yHeight - posOffset[1] - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall3VelY *= -1;
        }
        FieldView.hitBall3X += FieldView.hitBall3VelX;
        FieldView.hitBall3Y -= FieldView.hitBall3VelY;

        FieldView.Hitspot3.setX(FieldView.hitBall3X);
        FieldView.Hitspot3.setY(FieldView.hitBall3Y);

        FieldView.hitBall3VelX *= 0.98;
        FieldView.hitBall3VelY *= 0.98;
    }

    public void target4Movement(){
        int[] posOffset = new int [ 2 ];
        FieldView.relativeLayout.getLocationInWindow(posOffset);

        if (FieldView.hitBall4X <= 0 || FieldView.hitBall4X >= MainActivity.xWidth - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall4VelX *= -1;
        }
        if (FieldView.hitBall4Y <= 0 || FieldView.hitBall4Y >= MainActivity.yHeight - posOffset[1] - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall4VelY *= -1;
        }
        FieldView.hitBall4X += FieldView.hitBall4VelX;
        FieldView.hitBall4Y -= FieldView.hitBall4VelY;

        FieldView.Hitspot4.setX(FieldView.hitBall4X);
        FieldView.Hitspot4.setY(FieldView.hitBall4Y);

        FieldView.hitBall4VelX *= 0.98;
        FieldView.hitBall4VelY *= 0.98;
    }

    public void target5Movement(){
        int[] posOffset = new int [ 2 ];
        FieldView.relativeLayout.getLocationInWindow(posOffset);

        if (FieldView.hitBall5X <= 0 || FieldView.hitBall5X >= MainActivity.xWidth - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall5VelX *= -1;
        }
        if (FieldView.hitBall5Y <= 0 || FieldView.hitBall5Y >= MainActivity.yHeight - posOffset[1] - FieldView.SPOT_DIAMETER) {
            FieldView.hitBall5VelY *= -1;
        }
        FieldView.hitBall5X += FieldView.hitBall5VelX;
        FieldView.hitBall5Y -= FieldView.hitBall5VelY;

        FieldView.Hitspot5.setX(FieldView.hitBall5X);
        FieldView.Hitspot5.setY(FieldView.hitBall5Y);

        FieldView.hitBall5VelX *= 0.98;
        FieldView.hitBall5VelY *= 0.98;
    }
}
