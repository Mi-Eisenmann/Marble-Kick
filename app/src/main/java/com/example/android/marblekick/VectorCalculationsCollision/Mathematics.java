package com.example.android.marblekick.VectorCalculationsCollision;

import java.util.ArrayList;
import java.util.Arrays;

public class Mathematics {

    // Vector-Product
    public float vecProd(ArrayList vec1, ArrayList vec2){
        if (vec1.size() != 2 && vec2.size() != 2){
            return -1000;
        }

        float res = (float) vec1.get(0) * (float) vec2.get(0) + (float) vec1.get(1) * (float) vec2.get(1);

        return res;
    }

    // Vector-Subtraction
    public ArrayList vecSub(ArrayList vec1, ArrayList vec2){
        float entryX = (float) vec1.get(0) - (float) vec2.get(0);
        float entryY = (float) vec1.get(1) - (float) vec2.get(1);
        ArrayList result = new ArrayList(Arrays.asList(entryX,entryY));
        return result;
    }

    // Calculate the collision
    public ArrayList collisionCalculation(float m1, float m2, float x1, float y1, float vx1, float vy1, float x2, float y2, float vx2, float vy2){
    ArrayList P1 = new ArrayList(Arrays.asList(x1,y1));
    ArrayList P2 = new ArrayList(Arrays.asList(x2,y2));
    ArrayList V1 = new ArrayList(Arrays.asList(vx1,vy1));
    ArrayList V2 = new ArrayList(Arrays.asList(vx2,vy2));

    // following the wiki-article https://en.wikipedia.org/wiki/Elastic_collision
    float newVX1 = vx1 - (2*m2)/(m1+m2) * (vecProd(vecSub(V1,V2),vecSub(P1,P2))) / (vecProd(vecSub(P1,P2),vecSub(P1,P2))) * (x1-x2);
    float newVY1 = vy1 - (2*m2)/(m1+m2) * (vecProd(vecSub(V1,V2),vecSub(P1,P2))) / (vecProd(vecSub(P1,P2),vecSub(P1,P2))) * (y1-y2);
    float newVX2 = vx2 - (2*m1)/(m1+m2) * (vecProd(vecSub(V2,V1),vecSub(P2,P1))) / (vecProd(vecSub(P2,P1),vecSub(P2,P1))) * (x2-x1);
    float newVY2 = vy2 - (2*m1)/(m1+m2) * (vecProd(vecSub(V2,V1),vecSub(P2,P1))) / (vecProd(vecSub(P2,P1),vecSub(P2,P1))) * (y2-y1);

    ArrayList result = new ArrayList(Arrays.asList(newVX1,newVY1,newVX2,newVY2));
    return result;
    }
}
