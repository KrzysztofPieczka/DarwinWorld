package agh.ics.oop.model.util;

import agh.ics.oop.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class Genes {

    public static List<Integer> makeGenes(int size){
        List<Integer> genes = new ArrayList<>();
        for(int i = 0; i < size; i++){
            genes.add((int) Math.round((Math.random()*7)));
        }
        return genes;
    }

    public static List<Integer> combineGenes(Animal mate1, Animal mate2, int size){
        float energy1 = mate1.getEnergy();
        float energy2 = mate2.getEnergy();
        int genInput1 = Math.round(energy1*(size/(energy1+energy2)));
        int genInput2 = size-genInput1;

        List<Integer> genes1 = mate1.getGenes();
        List<Integer> genes2 = mate2.getGenes();
        int side = (int) Math.round(Math.random());
        List<Integer> newGenes = new ArrayList<>();

        if(side == 0){
            for(int i = 0; i < size; i++){
                if(i < genInput1) {
                    newGenes.add(genes1.get(i));
                } else {
                    newGenes.add(genes2.get(i));
                }
            }
        } else {
            for(int i = 0; i < size; i++){
                if(i < genInput2) {
                    newGenes.add(genes2.get(i));
                } else {
                    newGenes.add(genes1.get(i));
                }
            }
        }

        return mutation(newGenes, size);
    }

    private static List<Integer> mutation(List<Integer> newGenes, int size){
        int mutatedGenes = (int)Math.round(Math.random()*size);
        for(int i = 0; i < mutatedGenes; i++){
            int whichGene = (int)Math.round(Math.random()*(size-1));
            newGenes.set(whichGene, (int)Math.round(Math.random()*7));
        }
        return newGenes;
    }
}
