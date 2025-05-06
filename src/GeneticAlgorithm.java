import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 25;
    private static final int TOURNAMENT_SIZE = 3;
    private static final double MUTATION_RATE = 0.01;
    private Random rand = new Random();
    private List<Individual> population = new ArrayList<>();
    private BufferedWriter writer;

    public GeneticAlgorithm() {
        try {
            writer = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            System.err.println("Error opening file: " + e.getMessage());
        }

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(new Individual(rand));
        }
    }

    public void run() {
        int generation = 0;
        while (true) {
            printPopulation(generation);

            if (hasSolution()) {
                System.out.println("Solution found in generation " + generation + "!");
                break;
            }

            List<Individual> newPopulation = new ArrayList<>();

            while (newPopulation.size() < POPULATION_SIZE) {
                Individual parent1 = tournamentSelection();
                Individual parent2 = tournamentSelection();

                Individual[] offspring = onePointCrossover(parent1, parent2);

                mutate(offspring[0]);
                mutate(offspring[1]);

                newPopulation.add(offspring[0]);
                if (newPopulation.size() < POPULATION_SIZE)
                    newPopulation.add(offspring[1]);
            }

            population = newPopulation;
            generation++;
        }
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Error closing file: " + e.getMessage());
        }
    }

    private boolean hasSolution() {
        return population.stream().anyMatch(ind -> ind.getFitness() == 32);
    }

    private Individual tournamentSelection() {
        List<Individual> tournament = new ArrayList<>();
        while (tournament.size() < TOURNAMENT_SIZE) {
            Individual candidate = population.get(rand.nextInt(POPULATION_SIZE));
            if (!tournament.contains(candidate)) {
                tournament.add(candidate);
            }
        }
        return Collections.max(tournament, Comparator.comparing(Individual::getFitness));
    }

    private Individual[] onePointCrossover(Individual parent1, Individual parent2) {
        boolean[] p1 = parent1.getChromosome();
        boolean[] p2 = parent2.getChromosome();
        boolean[] c1 = new boolean[p1.length];
        boolean[] c2 = new boolean[p1.length];

        int point = rand.nextInt(p1.length);

        for (int i = 0; i < p1.length; i++) {
            if (i < point) {
                c1[i] = p1[i];
                c2[i] = p2[i];
            } else {
                c1[i] = p2[i];
                c2[i] = p1[i];
            }
        }

        return new Individual[]{ new Individual(c1), new Individual(c2) };
    }

    private void mutate(Individual ind) {
        boolean[] genes = ind.getChromosome();
        for (int i = 0; i < genes.length; i++) {
            if (rand.nextDouble() < MUTATION_RATE) {
                genes[i] = !genes[i];
            }
        }
        ind.calculateFitness();
    }

    private void printPopulation(int generation) {
        StringBuilder sb = new StringBuilder();
        sb.append("**************************************************\n");
        int i = 1;
        for (Individual ind : population) {
            sb.append("Individual ").append(i).append(": [")
                    .append(ind).append("] and fitness is ")
                    .append(ind.getFitness()).append("\n");
            i++;
        }
        int bestFitness = population.stream().mapToInt(Individual::getFitness).max().orElse(0);
        sb.append("Generation ").append(generation)
                .append(": Best individual’s fitness is ")
                .append(bestFitness).append(", continuing with next generation!\n");
        sb.append("**************************************************\n");

        // Print to console
        System.out.print(sb);

        // Write to file
        try {
            writer.write(sb.toString());
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
