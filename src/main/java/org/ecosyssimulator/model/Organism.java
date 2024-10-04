package org.ecosyssimulator.model;

public class Organism {
    private final Species species;
    private boolean isAlive;
    private int age;
    private double size;

    public Organism(Species species) {
        this.species = species;
        this.isAlive = true;
        this.age = 1;
        this.size = 1.0;
    }

    public void update() {
        if (isAlive) {
            age++;
            grow();
            checkSurvival();
        }
    }

    private void grow() {
        size *= (1 + 0.1 * (1.0 / species.getLifespan()));
    }

    private void checkSurvival() {
        if (age >= species.getLifespan()) {
            isAlive = false;
        }
    }

    public boolean canReproduce() {
        if (!isAlive) {
            return false;
        }
        // Casting integer en double avec ()
        double relativeAge = (double) age / species.getLifespan();

        // Plage de reproduction
        double minReproductiveAge = 0.2;
        double maxReproductiveAge = 0.8;

        if (relativeAge < minReproductiveAge || relativeAge > maxReproductiveAge) {
            return false;
        }

        double reproductionChance = species.getReproductionRate();

        double ageFactor;
        if (relativeAge <= 0.5) {
            ageFactor = relativeAge * 2;
        } else {
            ageFactor = (1 - relativeAge) * 2;
        }
        reproductionChance *= ageFactor;

        return Math.random() < reproductionChance;
    }

    // Getters and setters
    public Species getSpecies() { return species; }
    public boolean isAlive() { return isAlive; }
    public void setAlive(boolean alive) { isAlive = alive; }
    public int getAge() { return age; }
    public double getSize() { return size; }

    public String getEmoticon() {
        return species.getEmoticon();
    }
}
