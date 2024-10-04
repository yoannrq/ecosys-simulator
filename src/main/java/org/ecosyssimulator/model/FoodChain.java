package org.ecosyssimulator.model;

public class FoodChain {
    public static boolean canEat(Species predator, Species prey) {
        if (predator == Species.LAPIN) {
            return prey == Species.CAROTTE;
        } else if (predator == Species.RENARD) {
            return prey == Species.LAPIN;
        } else if (predator == Species.LOUP) {
            return prey == Species.LAPIN || prey == Species.RENARD;
        }
        return false;
    }
}
