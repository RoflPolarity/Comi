import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class tests {
    @Test
    public void test1() throws IOException {
        int city = 11;
        int a = 10,b =20;
        double mut = 1;
        TSPGeneticAlgorithmWithoutElitism tspGeneticAlgorithmWithoutElitism = new TSPGeneticAlgorithmWithoutElitism();
        int[][] distances = TSPGeneticAlgorithmWithoutElitism.generateRandomDistanceMatrix(city,a,b);
        double[] resultsOfCycled = new double[50];
        for (int i = 0; i < 50; i++) {
                resultsOfCycled[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForCycleCrossover(distances,11,50000,mut),distances));
        }
        double[] resultsOfOnePoint = new double[50];
        for (int i = 0; i < 50; i++) {
            resultsOfOnePoint[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForOnePointCrossover(distances,11,50000,mut),distances));
        }
        resultsOfCycled = new double[50];
        resultsOfOnePoint = new double[50];
        for (int i = 0; i < 50; i++) {
            resultsOfCycled[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForCycleCrossover(distances,11,50000,mut),distances));
        }
        for (int i = 0; i < 50; i++) {
            resultsOfOnePoint[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForOnePointCrossover(distances,11,50000,mut),distances));
        }
        double[] CycledOverage = new double[1], OnePointOverage = new double[1];
        double sum = Arrays.stream(resultsOfCycled).sum();
        CycledOverage[0] = sum/resultsOfCycled.length;
        sum = Arrays.stream(resultsOfOnePoint).sum();
        OnePointOverage[0] = sum/resultsOfOnePoint.length;
        var dataset = new XYSeriesCollection();
        var seriesCycled = new XYSeries("Циклический кроссовер");
        seriesCycled.add(1,CycledOverage[0]);
        var seriesOnePoint = new XYSeries("Одноточечный кроссовер");
        seriesOnePoint.add(8,OnePointOverage[0]);
        dataset.addSeries(seriesCycled);
        dataset.addSeries(seriesOnePoint);
        JFreeChart histogram = ChartFactory.createHistogram("Запуск для 11 вершин","","",dataset);
        ChartUtils.saveChartAsJPEG(new File("Chart11.jpeg"),histogram,800,600);

    }
    @Test
    public void test2() throws IOException {
        int city = 11;
        int a = 10,b =20;
        double mut = 1;
        int[][] distances = TSPGeneticAlgorithmWithoutElitism.generateRandomDistanceMatrix(city,a,b);
        double[] resultsOfCycled = new double[50];
        for (int i = 0; i < 50; i++) {
            resultsOfCycled[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForCycleCrossover(distances,11,50000,mut),distances));
        }
        double[] resultsOfOnePoint = new double[50];
        for (int i = 0; i < 50; i++) {
            resultsOfOnePoint[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForOnePointCrossover(distances,11,50000,mut),distances));
        }
        double[] CycledOverage = new double[1], OnePointOverage = new double[1];
        double sum = Arrays.stream(resultsOfCycled).sum();
        CycledOverage[0] = sum/resultsOfCycled.length;
        sum = Arrays.stream(resultsOfOnePoint).sum();
        OnePointOverage[0] = sum/resultsOfOnePoint.length;
        var dataset = new XYSeriesCollection();
        var seriesCycled = new XYSeries("Циклический кроссовер");
        seriesCycled.add(1,CycledOverage[0]);
        var seriesOnePoint = new XYSeries("Одноточечный кроссовер");
        seriesOnePoint.add(8,OnePointOverage[0]);
        dataset.addSeries(seriesCycled);
        dataset.addSeries(seriesOnePoint);
        JFreeChart histogram = ChartFactory.createHistogram("Запуск для 9 вершин","","",dataset);
        ChartUtils.saveChartAsJPEG(new File("Chart9.jpeg"),histogram,800,600);

    }
    @Test
    public void test3() throws IOException {
        int city = 10;
        int a = 10,b =20;
        double mut = 1;
        int[][] distances = TSPGeneticAlgorithmWithoutElitism.generateRandomDistanceMatrix(city,a,b);
        double[] resultsOfCycled = new double[50];
        for (int i = 0; i < 50; i++) {
            resultsOfCycled[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForCycleCrossover(distances,11,50000,mut),distances));
        }
        double[] resultsOfOnePoint = new double[50];
        for (int i = 0; i < 50; i++) {
            resultsOfOnePoint[i] = (TSPGeneticAlgorithmWithoutElitism.getDistance(TSPGeneticAlgorithmWithoutElitism.solveForOnePointCrossover(distances,11,50000,mut),distances));
        }
        double[] CycledOverage = new double[1], OnePointOverage = new double[1];
        double sum = Arrays.stream(resultsOfCycled).sum();
        CycledOverage[0] = sum/resultsOfCycled.length;
        sum = Arrays.stream(resultsOfOnePoint).sum();
        OnePointOverage[0] = sum/resultsOfOnePoint.length;
        var dataset = new XYSeriesCollection();
        var seriesCycled = new XYSeries("Циклический кроссовер");
        seriesCycled.add(1,CycledOverage[0]);
        var seriesOnePoint = new XYSeries("Одноточечный кроссовер");
        seriesOnePoint.add(8,OnePointOverage[0]);
        dataset.addSeries(seriesCycled);
        dataset.addSeries(seriesOnePoint);
        JFreeChart histogram = ChartFactory.createHistogram("Запуск для 10 вершин","","",dataset);
        ChartUtils.saveChartAsJPEG(new File("Chart10.jpeg"),histogram,800,600);

    }
}
