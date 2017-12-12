package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;
import static sample.RandomGenerator.generateUsingCongruentialMethod;
import static sample.RandomGenerator.generateUsingMidSquareMethod;

public class Main extends Application {
    private static final String INITIAL_VALUE = "16852278";
    private static final String K = "12347";
    private static final String M = "12345678";
    private static final int S = 10;
    private static final int AMOUNT = 100;
    private static final int TYPE = 2; //1 - mid square, 2 - congruential

    private static Double mz;
    private static Double dz;
    private static Double r;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BarChart<String, Number> barChart;
        if (TYPE == 1) {
            List<Integer> elements2 = getSquareRandomElements(AMOUNT); // генерируем случ числа
            Integer[] segments2 = getSegments(elements2, 100_000_000);
            barChart = getStringNumberBarChart(segments2, "Square");
        } else {
            List<Integer> elements = getCongruentialRandomElements(AMOUNT);
            Integer[] segments = getSegments(elements, 10_000_000);
            barChart = getStringNumberBarChart(segments, "Congruential");
        }

        Group root = new Group(barChart);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Lab #1");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param elements - random elements
     * @param delimiter - delimiter
     * @return array of segments разьитие по колонкам
     */
    private Integer[] getSegments(List<Integer> elements, int delimiter) {
        double[] doubles = elements.stream().mapToDouble(i -> i).map(d -> d / delimiter).toArray();
        calcMzAndDz(doubles);
        independentTest(doubles);
        System.out.println(Arrays.toString(doubles));
        double[] columns = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        Integer[] segments = new Integer[10];
        Arrays.fill(segments, 0);
        for (Double integer : doubles) {
            for (int i = 0; i < 10; i++) {
                if (integer < columns[i]) {
                    segments[i] += 1;
                    break;
                }
            }
        }
        return segments;
    }

    /**
     * create bar chart depends on values and name
     * @param segments - values
     * @param name - name of method type
     * @return bar chart созжание гистограммы
     */
    private BarChart<String, Number> getStringNumberBarChart(Integer[] segments, String name) {
        CategoryAxis xAxis = new CategoryAxis();
        List<String> col = Arrays.asList("0.1-0.2", "0.2-0.3", "0.3-0.4", "0.4-0.5", "0.5-0.6",
                "0.6-0.7", "0.7-0.8", "0.8-0.9", "0.9-1.0");
        xAxis.setCategories(observableArrayList(col));
        xAxis.setLabel("interval");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("frequency");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(name + " Mz = " + mz + " Dz = " + dz + "\n R = " + r);
        List<XYChart.Series<String, Number>> xColumns = new ArrayList<>(10);
        for (int i = 0; i < col.size(); i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(col.get(i), segments[i]));
            xColumns.add(series);
        }
        barChart.getData().addAll(xColumns);
        return barChart;
    }

    /**
     * generate random numbers using сongruential method
     * @param amount - amount of elements
     * @return list of numbers Мультипликативный конгруэнтный метод
     */
    private List<Integer> getCongruentialRandomElements(int amount) {
        List<Integer> result = new ArrayList<>(amount);
        String s = generateUsingCongruentialMethod(INITIAL_VALUE, M, K);
        for (int i = 0; i < amount; i++) {
            s = generateUsingCongruentialMethod(s, M, K);
            result.add(Integer.parseInt(s));
        }
        return result;
    }

    /**
     * generate random numbers using mid square method
     * @param amount - amount of elements
     * @return list of numbers  метод середины квадрата
     */
    private List<Integer> getSquareRandomElements(int amount) {
        List<Integer> result = new ArrayList<>(amount);
        String s = generateUsingMidSquareMethod(INITIAL_VALUE);
        for (int i = 0; i < amount; i++) {
            s = generateUsingMidSquareMethod(s);
            result.add(Integer.parseInt(s));
        }
        return result;
    }

    /**
     * calculate Mz and Dz
     * @param numbers - array of numbers
     */
    private void calcMzAndDz(double[] numbers) {
        Double sumOfNumbers = Arrays.stream(numbers).sum(); // сумма всех чисел для вычисл. M(z) и D(z)
        Double sumOfSquares = Arrays.stream(numbers).map(el -> el * el).sum();
        mz = sumOfNumbers / numbers.length; // M = 1/n * sumOfALlNumbers
        dz = sumOfSquares / numbers.length - mz * mz; // D = 1/n * sumOfSquares - M^2
    }

    /**
     * calculate R - correlation
     * @param numbers - array of numbers
     */
    private void independentTest(double[] numbers) {
        int n = numbers.length;
        double sum = 0;
        for (int i = 0; i < n - S; i++) {
            sum += numbers[i] * numbers[i + S];
        }
        r = (12 * sum / (n - S)) - 3;
        if (r < -1) r = -1d;
    }
}
