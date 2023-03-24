import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TSPGeneticAlgorithmWithoutElitism {

    public static int[] solveForOnePointCrossover(int[][] distances, int populationSize, int maxGenerations, double mutationProbability) {
        Random random = new Random();
        List<int[]> population = initializePopulation(distances.length, populationSize, random);
        for (int i = 0; i < maxGenerations; i++) {
            List<int[]> offspring = generateOffspringForOnePointCrossover(population, distances, random);
            mutateOffspring(offspring, mutationProbability);
            population = offspring;
        }
        return getBestIndividual(population, distances);
    }

    public static int[] solveForCycleCrossover(int[][] distances, int populationSize, int maxGenerations, double mutationProbability) {
        Random random = new Random();
        List<int[]> population = initializePopulation(distances.length, populationSize, random);
        for (int i = 0; i < maxGenerations; i++) {
            List<int[]> offspring = generateOffspringForCycledCrossover(population, distances, random);
            mutateOffspring(offspring, mutationProbability);
            population = offspring;
        }
        return getBestIndividual(population, distances);
    }

    private static List<int[]> initializePopulation(int numCities, int populationSize, Random random) {
        List<int[]> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            ArrayList<Integer> individual = new ArrayList<>();
            for (int j = 0; j < numCities; j++) {
                individual.add(j);
            }
            Collections.shuffle(individual, random);
            int[] indArr = new int[numCities];
            for (int j = 0; j < individual.size(); j++)indArr[j] = individual.get(j);
            population.add(indArr);
        }
        return population;
    }

    private static List<int[]> generateOffspringForOnePointCrossover(List<int[]> population, int[][] distances, Random random) {
        List<int[]> offspring = new ArrayList<>();
        while (offspring.size() < population.size()) {
            int[] parent1 = selectParent(population, distances, random);
            int[] parent2 = selectParent(population, distances, random);
            List<int[]> children = OnePointCrossover(parent1, parent2);
            for (int[] child : children) {
                offspring.add(child);
                if (offspring.size() == population.size()) {
                    break;
                }
            }
        }
        return offspring;
    }

    public static List<int[]> OnePointCrossover(int[] parent1, int[] parent2) {
        int[] child = new int[parent1.length];

        // Choose a random crossover point
        int crossoverPoint = (int) (Math.random() * (parent1.length - 1)) + 1;

        // Copy the first part of parent1 into the child
        System.arraycopy(parent1, 0, child, 0, crossoverPoint);

        // Copy the remaining part of parent2 into the child
        for (int i = crossoverPoint; i < child.length; i++) {
            // Check if the value is already in the child
            if (!contains(child, parent2[i])) {
                child[i] = parent2[i];
            } else {
                // Find the first unused value in parent2 and add it to the child
                for (int j = 0; j < parent2.length; j++) {
                    if (!contains(child, parent2[j])) {
                        child[i] = parent2[j];
                        break;
                    }
                }
            }
        }

        return List.of(child);
    }

    private static boolean contains(int[] arr, int value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) {
                return true;
            }
        }
        return false;
    }

    private static int[] selectParent(List<int[]> population, int[][] distances, Random random) {
        int tournamentSize = 5;
        int[] bestIndividual = population.get(random.nextInt(population.size()));
        int bestFitness = evaluateFitness(bestIndividual, distances);
        for (int i = 1; i < tournamentSize; i++) {
            int[] individual = population.get(random.nextInt(population.size()));
            int fitness = evaluateFitness(individual, distances);
            if (fitness < bestFitness) {
                bestIndividual = individual;
                bestFitness = fitness;
            }
        }
        return bestIndividual;
    }

    private static List<int[]> generateOffspringForCycledCrossover(List<int[]> population, int[][] distances, Random random) {
        List<int[]> offspring = new ArrayList<>();
        while (offspring.size() < population.size()) {
            int[] parent1 = selectParent(population, distances, random);
            int[] parent2 = selectParent(population, distances, random);
            List<int[]> children = CycledCrossover(parent1, parent2);
            for (int[] child : children) {
                offspring.add(child);
                if (offspring.size() == population.size()) {
                    break;
                }
            }
        }
        return offspring;
    }
    // Performs cyclic crossover on two parent solutions
    public static List<int[]> CycledCrossover(int[] parent1, int[] parent2) {
        int[] child = new int[parent1.length];
        Arrays.fill(child, -1); // Initialize child with -1

        // Choose a random starting index
        int index = (int) (Math.random() * parent1.length);
        int cycleStart = index;

        // Create a cycle by copying elements from parent1 to child
        // until the cycle is closed
        do {
            child[index] = parent1[index];
            index = indexOf(parent2, parent1[index]);
        } while (index != cycleStart);

        // Fill in the remaining elements with values from parent2
        for (int i = 0; i < parent2.length; i++) {
            if (child[i] == -1) {
                child[i] = parent2[i];
            }
        }

        return List.of(child);
    }

    private static void mutateOffspring(List<int[]> offspring, double mutationProbability) {
        for (int[] individual : offspring) {
            if (Math.random() < mutationProbability) {
                int index1 = (int) (Math.random() * individual.length);
                int index2 = (int) (Math.random() * individual.length);
                swap(individual, index1, index2);
            }
        }
    }

    private static int evaluateFitness(int[] individual, int[][] distances) {
        int fitness = 0;
        for (int i = 0; i < individual.length - 1; i++) {
            fitness += distances[individual[i]][individual[i + 1]];
        }
        fitness += distances[individual[individual.length - 1]][individual[0]];
        return fitness;
    }

    private static int[] getBestIndividual(List<int[]> population, int[][] distances) {
        int[] bestIndividual = population.get(0);
        int bestFitness = evaluateFitness(bestIndividual, distances);
        for (int[] individual : population) {
            int fitness = evaluateFitness(individual, distances);
            if (fitness < bestFitness) {
                bestIndividual = individual;
                bestFitness = fitness;
            }
        }
        return bestIndividual;
    }

    private static void swap(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public static int[][] generateRandomDistanceMatrix(int num, int a, int b){
        int[][] res = new int[num][num];
        for (int i = 0; i < res.length; i++) {
            for (int j = i; j < res.length; j++) {
                int c = ThreadLocalRandom.current().nextInt(a,b);
                if (i==j) c = 0;
                res[i][j] = c;
                res[j][i] = c;
            }
        }
        try {
            saveDistanceMatrix(res);
            GraphVisualizer graphVisualizer = new GraphVisualizer();
            graphVisualizer.saveGraph(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    public static boolean saveDistanceMatrix(int[][] arr) throws IOException {
        File file = new File("InitDistanceMatrix.txt");
        if (!file.exists()) file.createNewFile();
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File("InitDistanceMatrix.txt")));
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                osw.write(arr[i][j]+" ");
                osw.flush();
            }
            osw.write('\n');
            osw.flush();
        }
        osw.close();
        return true;
    }

    public static int getDistance(int[] route ,int[][] distances){
        int distance = 0;
        for (int i = 0; i < route.length-1; i++) {
            distance+=distances[route[i]][route[i+1]];
        }
        return distance;
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        System.out.println("Введите кол-во городов");
        int num = new Scanner(System.in).nextInt();
        System.out.println("Введите нижний порог генерации");
        int a = new Scanner(System.in).nextInt();
        System.out.println("Введите верхний порог генерации");
        int b = new Scanner(System.in).nextInt();
        System.out.println("Введите размер популяции");
        int pop = new Scanner(System.in).nextInt();
        System.out.println("Введите верятность мутации");
        double mut = new Scanner(System.in).nextDouble();
        // Генерируем случайную матрицу расстояний
        int[][] distanceMatrix = generateRandomDistanceMatrix(num,a,b);
        // Создаём экземпляр генетического алгоритма
        GraphVisualizer ga = new GraphVisualizer();
        while (true) {
            System.out.println("1.Сгенерировать новую случайную матрицу расстояний");
            System.out.println("2.Цикличный кроссовер");
            System.out.println("3.Упорядоченный кроссовер");
            System.out.println("4.Выход");
            System.out.println("\nВведите номер пункта меню");
            int menuNum = new Scanner(System.in).nextInt();
            if (menuNum == 1) distanceMatrix = generateRandomDistanceMatrix(num,a,b);
            else if (menuNum==2) {
                var bestRoute = solveForCycleCrossover(distanceMatrix,pop,50000,mut);
                System.out.println("Цикличный кроссовер");
                System.out.println("Best route found:");
                System.out.println(Arrays.toString(bestRoute));
                ga.saveGraph(distanceMatrix,bestRoute);
                System.out.println("Length of best route found: " + getDistance(bestRoute,distanceMatrix));
            } else if (menuNum==3) {
                var resultRoute = solveForOnePointCrossover(distanceMatrix,pop,5000,mut);
                System.out.println("Одноточный кроссовер");
                System.out.println("Best route found:");
                System.out.println(Arrays.toString(resultRoute));
                ga.saveGraph(distanceMatrix,resultRoute);
                System.out.println("Length of best route found: " + getDistance(resultRoute,distanceMatrix));
            } else if (menuNum==4) {
                break;
            }else throw new IllegalArgumentException();
        }
    }
}
