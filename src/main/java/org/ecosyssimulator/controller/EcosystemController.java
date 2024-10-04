package org.ecosyssimulator.controller;

import org.ecosyssimulator.model.FoodChain;
import org.ecosyssimulator.model.Organism;
import org.ecosyssimulator.model.Species;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class EcosystemController {
    private int currentDay;
    private List<Organism> organisms;
    private List<String> events;
    private FoodChain foodChain;

    public EcosystemController() {
        this.currentDay = 1;
        this.organisms = new ArrayList<>();
        this.events = new ArrayList<>();
        this.foodChain = new FoodChain();
    }

    public void simulateDay() {
        events.add("\uD83C\uDF05 Jour " + currentDay + " : Le soleil se lève sur la simulation. \uD83C\uDF05");
        currentDay++;
        updateOrganisms();
        simulateReproduction();
        simulatePredation();
        removeDeadOrganisms();
        events.add("\uD83C\uDF19 Jour " + currentDay + " : Fin de la journee. \uD83C\uDF19");
    }

    public void simulateMonth() {
        for (int i = 1; i < 31; i++) {
            simulateDay();
        }
    }

    private void updateOrganisms() {
        for (Organism organism : organisms) {
            organism.update();
            if (!organism.isAlive()) {
                events.add("Un(e) " + organism.getEmoticon() + organism.getSpecies() + " est mort de vieillesse.");
            }

        }
    }

    private void simulateReproduction() {
        List<Organism> newborns = new ArrayList<>();
        for (Organism organism : organisms) {
            if (organism.canReproduce()) {
                Organism newborn = new Organism(organism.getSpecies());
                newborns.add(newborn);
                events.add("Un(e) " + organism.getEmoticon() + organism.getSpecies() + " est né(e) !");
            }
        }
        organisms.addAll(newborns);
    }

    private void simulatePredation() {
        for (Organism predator : organisms) {
            if (!predator.isAlive()) continue;

            for (Organism prey : organisms) {
                if (!prey.isAlive()) continue;

                if (FoodChain.canEat(predator.getSpecies(), prey.getSpecies())) {
                    double survivalChance = calculateSurvivalChance(predator, prey);
                    if (Math.random() > survivalChance) {
                        prey.setAlive(false);
                        events.add("Un(e) " + predator.getEmoticon() + predator.getSpecies() + " a mangé(e) un(e) " + prey.getEmoticon() + prey.getSpecies() + ".");
                        break;
                    } else {
                        events.add("Un(e) " + prey.getEmoticon() + prey.getSpecies() + " a échappé(e) à un(e) " + predator.getEmoticon() + predator.getSpecies() + ".");
                    }
                }
            }
        }
    }

    private double calculateSurvivalChance(Organism predator, Organism prey) {
        double preyLifeRatio = (double) prey.getAge() / prey.getSpecies().getLifespan();
        double predatorLifeRatio = (double) predator.getAge() / predator.getSpecies().getLifespan();

        // Plus la proie est jeune, plus elle a de chances de survivre
        double baseChance = 0.5 - preyLifeRatio * 0.3;

        // Plus le prédateur est vieux, moins il a de chances de réussir sa chasse
        baseChance += predatorLifeRatio * 0.2;

        // Assurer que la chance reste entre 0 et 1
        return Math.max(0, Math.min(1, baseChance));
    }

    private void removeDeadOrganisms() {
        organisms.removeIf(organism -> !organism.isAlive());
    }

    public void addOrganism(Species species) {
        Organism organism = new Organism(species);
        organisms.add(organism);
        events.add("Un(e) " + species.getEmoticon() + species + " a été ajouté(e) à l'écosysteme.");
    }

    public void resetSimulation() {
        currentDay = 0;
        organisms.clear();
        events.clear();
        events.add("La simulation a été reinitialisée.");
    }

    public Map<Species, Integer> getPopulationStats() {
        Map<Species, Integer> stats = new HashMap<>();
        for (Organism organism : organisms) {
            stats.merge(organism.getSpecies(), 1, Integer::sum);
        }
        return stats;
    }

    // Getters
    public int getCurrentDay() { return currentDay; }
    public List<Organism> getOrganisms() { return new ArrayList<>(organisms); }
    public int getTotalPopulation() { return organisms.size(); }
    public List<String> getEvents() { return new ArrayList<>(events); }

    // Setters
    public void setEvent(String content) {
        events.add(content);
    }
}
