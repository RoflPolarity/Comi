
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import com.mxgraph.model.mxCell;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class TSPGeneticAlgorithm  {
    private static int[][] distances; // Матрица расстояний между городами
    private int populationSize; // Размер популяции
    private double mutationRate; // Вероятность мутации
    private int tournamentSize; // Размер турнира
    private int elitismCount; // Количество элитных особей
    private int generationCount; // Количество поколений

    public static int[][] getDistances() {
        return distances;
    }

    public void setDistances(int[][] distances) {
        TSPGeneticAlgorithm.distances = distances;
    }

    public TSPGeneticAlgorithm(int[][] distances, int populationSize, double mutationRate, int tournamentSize, int elitismCount, int generationCount) {
        this.distances = distances;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.tournamentSize = tournamentSize;
        this.elitismCount = elitismCount;
        this.generationCount = generationCount;
    }

    // Создание начальной популяции
    private List<int[]> createInitialPopulation() {
        List<int[]> population = new ArrayList<int[]>();
        for (int i = 0; i < populationSize; i++) {
            int[] individual = new int[distances.length];
            for (int j = 0; j < distances.length; j++) {
                individual[j] = j;
            }
            shuffleArray(individual);
            population.add(individual);
        }
        return population;
    }

    // Турнирная селекция
    private int[] tournamentSelection(List<int[]> population) {
        Random random = new Random();
        List<int[]> tournament = new ArrayList<int[]>();
        for (int i = 0; i < tournamentSize; i++) {
            int index = random.nextInt(population.size());
            tournament.add(population.get(index));
        }
        int[] fittest = null;
        int minDistance = Integer.MAX_VALUE;
        for (int[] individual : tournament) {
            int distance = calculateDistance(individual);
            if (distance < minDistance) {
                minDistance = distance;
                fittest = individual;
            }
        }
        return fittest;
    }

    // Расчет расстояния между городами в маршруте
    private int calculateDistance(int[] individual) {
        int distance = 0;
        for (int i = 0; i < individual.length - 1; i++) {
            distance += distances[individual[i]][individual[i + 1]];
        }
        distance += distances[individual[individual.length - 1]][individual[0]];
        return distance;
    }

    // Мутация
    private void mutate(int[] individual) {
        Random random = new Random();
        for (int i = 0; i < individual.length; i++) {
            if (random.nextDouble() < mutationRate) {
                int swapIndex = random.nextInt(individual.length);
                int temp = individual[i];
                individual[i] = individual[swapIndex];
                individual[swapIndex] = temp;
            }
        }
    }

    // Проверка, содержит ли массив заданный элемент
    private boolean contains(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return true;
            }
        }
        return false;
    }

    // Перемешивание элементов массива в случайном порядке
    private void shuffleArray(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private int[] cycleCrossover(int[] parent1, int[] parent2) {
        Random random = new Random();
        int[] child = new int[parent1.length];
        Arrays.fill(child, -1);
        int cycleStartIndex = random.nextInt(parent1.length);
        int currentIndex = cycleStartIndex;
        while (child[currentIndex] == -1) {
            child[currentIndex] = parent1[currentIndex];
            currentIndex = indexOf(parent2, parent1[currentIndex]);
        }
        for (int i = 0; i < child.length; i++) {
            if (child[i] == -1) {
                child[i] = parent2[i];
            }
        }
        return child;
    }

    private int indexOf(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return i;
            }
        }
        return -1;
    }
    private int[] orderedCrossover(int[] parent1, int[] parent2) {
        Random random = new Random();
        int[] child = new int[parent1.length];
        Arrays.fill(child, -1);
        int startPoint = random.nextInt(parent1.length);
        int endPoint = random.nextInt(parent1.length);
        if (startPoint > endPoint) {
            int temp = startPoint;
            startPoint = endPoint;
            endPoint = temp;
        }
        for (int i = startPoint; i <= endPoint; i++) {
            child[i] = parent1[i];
        }
        int currentIndex = endPoint;
        for (int i = endPoint + 1; i < parent2.length; i++) {
            int gene = parent2[i];
            if (!containsGene(child, gene)) {
                child[currentIndex] = gene;
                currentIndex++;
                if (currentIndex == child.length) {
                    currentIndex = 0;
                }
            }
        }
        for (int i = 0; i < endPoint + 1; i++) {
            int gene = parent2[i];
            if (!containsGene(child, gene)) {
                child[currentIndex] = gene;
                currentIndex++;
                if (currentIndex == child.length) {
                    currentIndex = 0;
                }
            }
        }
        return child;
    }

    private boolean containsGene(int[] array, int gene) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == gene) {
                return true;
            }
        }
        return false;
    }
    // Генетический алгоритм
    public int[] solveCycleCrossover() {
        List<int[]> population = createInitialPopulation();
        for (int i = 0; i < generationCount; i++) {
            List<int[]> newPopulation = new ArrayList<int[]>();
            for (int j = 0; j < elitismCount; j++) {
                int[] fittest = tournamentSelection(population);
                newPopulation.add(fittest);
            }
            for (int j = 0; j < populationSize - elitismCount; j++) {
                int[] parent1 = tournamentSelection(population);
                int[] parent2 = tournamentSelection(population);
                int[] child = cycleCrossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
            }
            population = newPopulation;
        }
        int[] bestIndividual = null;
        int minDistance = Integer.MAX_VALUE;
        for (int[] individual : population) {
            int distance = calculateDistance(individual);
            if (distance < minDistance) {
                minDistance = distance;
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }

    public int[] solveOrderedCrossover() {
        List<int[]> population = createInitialPopulation();
        for (int i = 0; i < generationCount; i++) {
            List<int[]> newPopulation = new ArrayList<int[]>();
            for (int j = 0; j < elitismCount; j++) {
                int[] fittest = tournamentSelection(population);
                newPopulation.add(fittest);
            }
            for (int j = 0; j < populationSize - elitismCount; j++) {
                int[] parent1 = tournamentSelection(population);
                int[] parent2 = tournamentSelection(population);
                int[] child = orderedCrossover(parent1, parent2);
                mutate(child);
                newPopulation.add(child);
            }
            population = newPopulation;
        }
        int[] bestIndividual = null;
        int minDistance = Integer.MAX_VALUE;
        for (int[] individual : population) {
            int distance = calculateDistance(individual);
            if (distance < minDistance) {
                minDistance = distance;
                bestIndividual = individual;
            }
        }
        return bestIndividual;
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
    public static boolean saveGraph(int[][] arr, String name) throws IOException {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        for (int i = 0; i < arr.length; i++) {
            graph.addVertex(String.valueOf(i));
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (arr[i][j] != 0) { // Проверяем наличие ребра между вершинами
                    graph.addEdge(String.valueOf(i), String.valueOf(j));
                }
            }
        }
        // Создание объекта для отображения графа
        mxGraph mxGraph = new mxGraph();
        Object parent = mxGraph.getDefaultParent();

        mxGraph.getModel().beginUpdate();
        try {
            for (int i = 0; i < arr.length; i++) {
                // Добавление вершин на графическое поле
                mxGraph.insertVertex(parent, null, String.valueOf(i), i + 20, i + 20, 50, 50);
            }
            for (DefaultEdge edge : graph.edgeSet()) {
                // Получение вершин, соединенных ребром
                String source = graph.getEdgeSource(edge);
                String target = graph.getEdgeTarget(edge);
                // Поиск соответствующих объектов вершин в графическом представлении
                //Object sourceVertex = mxGraph.getChild(parent, source);
                //Object targetVertex = mxGraph.getVertexToCellMap().get(target);
                // Добавление ребра на графическое поле
                //mxGraph.insertEdge(parent, null, "", sourceVertex, targetVertex);
            }
        } finally {
            mxGraph.getModel().endUpdate();
            }
        // Создание компонента для отображения графа в окне
        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);

        return true;
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

    public int[] setCrossover(char c){
        GraphVisualizer gv = new GraphVisualizer();
        if (c=='1'){
            var res = solveCycleCrossover();
            gv.saveGraph(distances,res);
            return res;
        } else if (c=='2') {
            var res = solveOrderedCrossover();
            gv.saveGraph(distances,res);
            return res;
        }else throw new IllegalArgumentException();
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
        if (pop>=num){
            System.err.println("Размер популяции должен быть меньше числа городов!");
            while (pop>=num){
                System.out.println("Повторите ввод размера популяции.");
                pop = new Scanner(System.in).nextInt();
            }
        }
        System.out.println("Введите верятность мутации");
        double mut = new Scanner(System.in).nextDouble();
        // Генерируем случайную матрицу расстояний
        int[][] distanceMatrix = generateRandomDistanceMatrix(num,a,b);
        // Создаём экземпляр генетического алгоритма
        TSPGeneticAlgorithm ga = new TSPGeneticAlgorithm(distanceMatrix, pop, mut, 2, 5,5000);

        while (true){
            System.out.println("1.Сгенерировать новую случайную матрицу расстояний");
            System.out.println("2.Прочитать матрицу расстояний из файла");
            System.out.println("3.Цикличный кроссовер");
            System.out.println("4.Упорядоченный кроссовер");
            System.out.println("5.Выход");
            System.out.println("\nВведите номер пункта меню");
            int menuNum = new Scanner(System.in).nextInt();
            if (menuNum==1) ga.setDistances(generateRandomDistanceMatrix(num,a,b));
            else if (menuNum==2){
                int[][] data = new int[num][num];
                final File[] file = {null};
                EventQueue.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        JFrame jf = new JFrame( "Dialog" ); // added
                        jf.setAlwaysOnTop( true ); // added
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showOpenDialog( jf );  // changed
                        if (result == JFileChooser.APPROVE_OPTION) {
                            file[0] = fileChooser.getSelectedFile();}
                        jf.dispose(); // added
                    }
                });
                try {
                    InputStreamReader isr = new InputStreamReader(new FileInputStream(file[0]),StandardCharsets.UTF_8);
                    for (int i = 0; i < data.length; i++) {
                        for (int j = 0; j < data.length; j++) {
                            data[i][j] = isr.read();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ga.setDistances(data);
                } else if (menuNum==3) {
                var resultRoute = ga.setCrossover('1');
                System.out.println("Цикличный кроссовер");
                System.out.println("Best route found:");
                System.out.println(Arrays.toString(resultRoute));
                System.out.println("Length of best route found: " + resultRoute.length);
            } else if (menuNum==4) {
                var resultRoute = ga.setCrossover('2');
                System.out.println("Упорядоченный кроссовер");
                System.out.println("Best route found:");
                System.out.println(Arrays.toString(resultRoute));
                System.out.println("Length of best route found: " + resultRoute.length);
            }
        }
        }
    }
