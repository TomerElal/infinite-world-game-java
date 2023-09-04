package pepse.util;

import pepse.world.Block;

public class MathOrLogicOperations {
    public static int getTheNumInMultipliesOf30(int num, boolean reduce) {
        int tmp = Math.abs(num);
        while (tmp%30 != 0){
            if(reduce){
                tmp--;
            }else{
                tmp++;
            }
        }
        if(num < 0) {
            return -tmp;
        }
        return tmp;
    }

    public static double normalizeHeight(float xCoord) {
        return Math.floor((xCoord) / Block.SIZE) * Block.SIZE;
    }
}
