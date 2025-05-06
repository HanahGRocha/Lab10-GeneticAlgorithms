import java.util.Random;

public class Individual {
    private static final int CHROMOSOME_LENGTH = 32;
    private boolean[] chromosome = new boolean[CHROMOSOME_LENGTH];
    private int fitness = 0;

    public Individual(Random rand) {
        for (int i = 0; i < CHROMOSOME_LENGTH; i++) {
            chromosome[i] = rand.nextBoolean();
        }
        calculateFitness();
    }

    public Individual(boolean[] chromosome) {
        this.chromosome = chromosome;
        calculateFitness();
    }

    public void calculateFitness() {
        fitness = 0;
        for (boolean gene : chromosome) {
            if (gene) fitness++;
        }
    }

    public int getFitness() {
        return fitness;
    }

    public boolean[] getChromosome() {
        return chromosome;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean bit : chromosome) {
            sb.append(bit ? '1' : '0');
        }
        return sb.toString();
    }

    public Individual clone() {
        return new Individual(chromosome.clone());
    }
}
