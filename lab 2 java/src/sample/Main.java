package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class Main extends Application {
    private static final int AMOUNT = 10000;
    private double a = 7;
    private double b = 10;

    private Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<Double> elements = random.doubles(AMOUNT, a, b).boxed().collect(Collectors.toList());// генерируем случ числа

        Integer[] segments = getSegments(elements);
        BarChart<String, Number> barChart = getStringNumberBarChart(segments);
        barChart.setPrefHeight(400);
        barChart.setPrefWidth(400);

        LineChart<Number, Number> lineChart = getLineChart(elements);
        lineChart.setPrefWidth(400);
        lineChart.setPrefHeight(400);

        LineChart<Number, Number> lineChart2 = getLineChart2(elements);
        lineChart2.setPrefWidth(400);
        lineChart2.setPrefHeight(400);


        BorderPane pane = new BorderPane();
        pane.setLeft(barChart);
        pane.setCenter(lineChart);
        pane.setRight(lineChart2);


        Group root = new Group(pane);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Lab #2");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param elements - random elements
     * @return array of segments разбитие по колонкам
     */
    private Integer[] getSegments(List<Double> elements) {
        double[] doubles = elements.stream().mapToDouble(i -> i).toArray();
        System.out.println(Arrays.toString(doubles));

        //определяем сегменты
        List<Double> columns = new ArrayList<>();
        for (int i = 1; i < (b - a) * 10 + 1; i++) {
            columns.add(a + i * 0.1);
        }
        System.out.println(columns);

        //разбитие по сегментам
        Integer[] segments = new Integer[columns.size()];
        Arrays.fill(segments, 0);
        for (Double integer : doubles) {
            for (int i = 0; i < columns.size(); i++) {
                if (integer < columns.get(i)) {
                    segments[i] += 1;
                    break;
                }
            }
        }
        System.out.println(Arrays.toString(segments));
        return segments;
    }

    /**
     * create bar chart depends on values and name
     *
     * @param segments - values
     * @return bar chart создание гистограммы
     */
    private BarChart<String, Number> getStringNumberBarChart(Integer[] segments) {
        CategoryAxis xAxis = new CategoryAxis();
        List<String> col = new ArrayList<>();

        for (int i = 0; i < (b - a) * 10; i++) {
            double start = a + i * 0.1;
            double end = a + (i + 1) * 0.1;
            col.add(start + " - " + end);
        }

        xAxis.setCategories(observableArrayList(col));
        xAxis.setLabel("interval");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("frequency");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Число попаданий в отрезки");
        List<XYChart.Series<String, Number>> xColumns = new ArrayList<>();
        for (int i = 0; i < col.size(); i++) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(col.get(i), segments[i]));
            xColumns.add(series);
        }
        barChart.getData().addAll(xColumns);
        return barChart;
    }

    /**
     * отображает график функции распределения
     *
     * @param elements
     * @return график
     */
    private LineChart<Number, Number> getLineChart(List<Double> elements) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<>(x, y);
        numberLineChart.setTitle("Функция распределения");
        XYChart.Series series = new XYChart.Series();
        series.setName("F(x)");
        ObservableList<XYChart.Data> data = observableArrayList();

        elements.sort(Double::compareTo);
        for (int i = 0; i < elements.size(); i += 10) {
            data.add(new XYChart.Data((double) (i / 10), elements.get(i)));
        }

        series.setData(data);
        numberLineChart.getData().add(series);
        return numberLineChart;
    }

    /**
     * отображает график плотности распределения
     *
     * @param elements
     * @return график
     */
    private LineChart<Number, Number> getLineChart2(List<Double> elements) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        LineChart<Number, Number> numberLineChart = new LineChart<>(x, y);
        numberLineChart.setTitle("Плотность распределения");
        XYChart.Series series = new XYChart.Series();
        series.setName("F(x)");
        ObservableList<XYChart.Data> data = observableArrayList();

        for (int i = 0; i < elements.size(); i += 10) {
            data.add(new XYChart.Data((double) (i / 10), (double) 1 / (b - a)));
        }

        series.setData(data);
        numberLineChart.getData().add(series);
        return numberLineChart;
    }

}
