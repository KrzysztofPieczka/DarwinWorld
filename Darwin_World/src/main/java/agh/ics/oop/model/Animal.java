package agh.ics.oop.model;

import agh.ics.oop.model.util.Genes;
import agh.ics.oop.model.util.Parameters;

import java.util.*;


public class Animal implements WorldElement {

    protected int energy;
    protected MapDirection currentDirection = MapDirection.NORTH.change((int) Math.round(Math.random() * 7));
    protected Vector2d location;
    protected List<Integer> genes;
    protected int age = 0;
    protected int plantsEaten = 0;
    protected int offspring = 0;
    protected boolean dead = false;
    protected Parameters parameters;
    protected int startingGene = 0;
    protected Set<Animal> ancestors = new HashSet<>();
    protected int descendants = 0;

    // do spawnowania potomstwa
    public Animal(Vector2d location, List<Integer> genes, Parameters parameters, Set<Animal> ancestors) {
        this.location = location;
        this.genes = genes;
        this.energy = 2 * parameters.childEnergy();
        this.parameters = parameters;
        this.startingGene = (int) Math.round(Math.random() * (parameters.geneLength() - 1));
        this.ancestors = ancestors;
        updateDescendants();
    }

    // do spawnowania zwierzaków zupełnie nowych
    public Animal(Vector2d location, Parameters parameters) {
        this(location, Genes.makeGenes(parameters.geneLength()), parameters, new HashSet<>());
        this.energy = parameters.startingEnergy();
    }

    //do testów
    public Animal(Vector2d location, List<Integer> genes) {
        this.location = location;
        this.genes = genes;
        this.currentDirection = MapDirection.NORTH;
    }

    public Animal(Vector2d location, List<Integer> genes, Parameters parameters, int x) {
        this(location, genes);
        this.parameters = parameters;
    }


    // GETTERY
    @Override
    public Vector2d getPosition() {
        return location;
    }

    public MapDirection getCurrentDirection() {
        return currentDirection;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy() {
        return energy;
    }

    public int getPlantsEaten() {
        return plantsEaten;
    }

    public int getOffspring() {
        return offspring;
    }

    public List<Integer> getGenes() {
        return genes;
    }

    public int getActiveGene() {
        return genes.get((startingGene + age) % parameters.geneLength());
    }

    public boolean isDead() {
        return dead;
    }

    public int getDescendants(){
        return descendants;
    }
    //

    //SETTERY
    protected void setEnergy(int energy) {
        this.energy = energy;
    }

    protected void increaseOffspring() {
        this.offspring++;
    }

    public void setLocation(Vector2d location) {
        this.location = location;
    }
    //

    @Override
    public String toString() {
        return "%s".formatted(currentDirection);
    }

    public boolean isAt(Vector2d position) {
        return location.equals(position);
    }

    public void move(WorldMap map) {
        int currMove = genes.get((startingGene + age) % parameters.geneLength());
        currentDirection = currentDirection.change(currMove);
        Vector2d newLocation = location.add(currentDirection.toUnitVector());
        if (map.canMoveTo(newLocation)) {
            location = newLocation;
        } else {
            currentDirection = currentDirection.change(4);
        }
        age++;
        energy--;
        if (energy == 0) {
            dead = true;
        }
    }

    public void eat() {
        plantsEaten++;
        energy += parameters.eatingEnergy();
    }

    public Animal breed(Animal mate) {
        offspring++;
        mate.increaseOffspring();
        energy -= parameters.childEnergy();
        mate.setEnergy(mate.getEnergy() - parameters.childEnergy());

        List<Integer> newGenes = Genes.combineGenes(this, mate, parameters.geneLength());

        Set<Animal> childAncestors = new HashSet<>(Set.copyOf(ancestors));
        childAncestors.addAll(mate.ancestors);
        childAncestors.add(this);
        childAncestors.add(mate);

        return new Animal(location, newGenes, parameters, childAncestors);
    }

    private void updateDescendants(){
        for(Animal ancestor : ancestors){
            ancestor.descendants++;
        }
    }
}
