import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import javax.swing.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class GraphVisualizer {

    public boolean saveGraph(int[][] arr) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (int i = 0; i < arr.length; i++) {
            graph.addVertex(String.valueOf(i));
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) { // идем только по верхнему треугольнику матрицы смежности
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
            // Добавление вершин на графическое поле
            Map<String, Object> vertexMap = new HashMap<>();
            for (int i = 0; i < arr.length; i++) {
                Object vertex = mxGraph.insertVertex(parent, null, String.valueOf(i), 0, 0, 50, 50);
                vertexMap.put(String.valueOf(i), vertex);
            }

            // Добавление ребер на графическое поле
            for (DefaultEdge edge : graph.edgeSet()) {
                // Получение вершин, соединенных ребром
                String source = graph.getEdgeSource(edge);
                String target = graph.getEdgeTarget(edge);
                // Поиск соответствующих объектов вершин в графическом представлении
                Object sourceVertex = vertexMap.get(source);
                Object targetVertex = vertexMap.get(target);
                // Добавление ребра на графическое поле
                mxGraph.insertEdge(parent, null, arr[Integer.parseInt(source)][Integer.parseInt(target)], sourceVertex, targetVertex);
            }
        } finally {
            mxGraph.getModel().endUpdate();
        }

        // Расстановка вершин с помощью алгоритма раскладки
        mxHierarchicalLayout layout = new mxHierarchicalLayout(mxGraph);
        layout.execute(parent);

        // Создание компонента для отображения графа в окне
        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);

        return true;
    }

    public boolean saveGraph(int[][] arr,int[] marsh) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        for (int i = 0; i < arr.length; i++) {
            graph.addVertex(String.valueOf(i));
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) { // идем только по верхнему треугольнику матрицы смежности
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
            // Добавление вершин на графическое поле
            Map<String, Object> vertexMap = new HashMap<>();
            for (int i = 0; i < arr.length; i++) {
                Object vertex = mxGraph.insertVertex(parent, null, String.valueOf(i), 0, 0, 50, 50);
                vertexMap.put(String.valueOf(i), vertex);
            }

            // Добавление ребер на графическое поле
            for (DefaultEdge edge : graph.edgeSet()) {
                // Получение вершин, соединенных ребром
                String source = graph.getEdgeSource(edge);
                String target = graph.getEdgeTarget(edge);
                // Поиск соответствующих объектов вершин в графическом представлении
                Object sourceVertex = vertexMap.get(source);
                Object targetVertex = vertexMap.get(target);
                // Добавление ребра на графическое поле
                mxGraph.insertEdge(parent, null, arr[Integer.parseInt(source)][Integer.parseInt(target)], sourceVertex, targetVertex);
            }
            for (int i = 0; i < marsh.length-1; i++) {
                String source = String.valueOf(marsh[i]);
                String target = String.valueOf(marsh[i+1]);
                Object edge = mxGraph.getEdgesBetween(vertexMap.get(source),vertexMap.get(target))[0];
                mxStylesheet stylesheet = mxGraph.getStylesheet();
                Hashtable<String, Object> edgeStyle = new Hashtable<String, Object>();
                edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#FF0000");
                stylesheet.putCellStyle("redEdge", edgeStyle);

// Применение стиля к ребру
                mxCell edgeCell = (mxCell) edge;
                edgeCell.setStyle("redEdge");
            }
        } finally {
            mxGraph.getModel().endUpdate();
        }
        // Расстановка вершин с помощью алгоритма раскладки
        mxHierarchicalLayout layout = new mxHierarchicalLayout(mxGraph);
        layout.execute(parent);

        // Создание компонента для отображения графа в окне
        mxGraphComponent graphComponent = new mxGraphComponent(mxGraph);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);

        return true;
    }

}