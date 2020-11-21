
package lesson6;

import kotlin.NotImplementedError;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {


    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
       List<Graph.Edge> result = new ArrayList<>();
       if(graph.getEdges().size() == 0) return result;
        Map<Graph.Vertex,Set<Graph.Vertex>> connect = new HashMap<>();
        for(Graph.Vertex vertex : graph.getVertices()){
            connect.put(vertex, graph.getNeighbors(vertex));
            if(connect.get(vertex).size() % 2 != 0) return result;
        }
        Deque<Graph.Vertex> currentPath = new ArrayDeque<>();

        Graph.Vertex firstVertex = graph.getVertices().iterator().next();
        currentPath.add(firstVertex);
        List<Graph.Vertex> way = new ArrayList<>();
        while(currentPath.size() != 0){
           Graph.Vertex current = currentPath.getLast();
           if(connect.get(current).size() != 0){
               Graph.Vertex  next = connect.get(current).iterator().next();
               connect.get(current).remove(next);
               connect.get(next).remove(current);
               currentPath.addLast(next);

           }else{
               currentPath.removeLast();
               way.add(current);
           }
        }
        for(Graph.Vertex vertex : connect.keySet()){
            if(connect.get(vertex).size() != 0) return result;
        }
        for(int i = 0; i < way.size() - 1; i++){
            result.add(graph.getConnection(way.get(i),way.get(i+1)));
        }
        return result;
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    public static Path bestPath;
    private static void findBestPath(Graph graph, Graph.Vertex currVertex,Path currPath){
        Graph.Vertex lastVertex = currPath.getVertices().get(currPath.getVertices().size() - 1);
        for(Graph.Vertex vertex : graph.getNeighbors(lastVertex)){
            if(currPath.contains(vertex)) continue;
            Path newPath = new Path(currPath,graph,vertex);
            if(bestPath.getLength() < newPath.getLength()) {
                bestPath = newPath;
            }
            findBestPath(graph,vertex,newPath);
        }
    }
    public static Path longestSimplePath(Graph graph) {
        bestPath = new Path();
        for(Graph.Vertex vertex : graph.getVertices()){
            if (bestPath.getVertices().size() == graph.getVertices().size()) break;
             findBestPath(graph,vertex, new Path(vertex));
        }
        return bestPath;
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static private boolean test(boolean[][] booleans, String[][] array, String word, int row, int col) {
        if (word.length() == 1) return true;
        String str = String.valueOf(word.charAt(1));
        String substring = word.substring(1);
        if (row > 0 && array[row - 1][col].equals(str) && !booleans[row - 1][col]) {
            booleans[row][col] = true;
            if (test(booleans, array, substring, row - 1, col)) return true;
        }
        if (row + 1 < array.length && array[row + 1][col].equals(str) && !booleans[row + 1][col]) {
            booleans[row][col] = true;
            if (test(booleans, array, substring, row + 1, col)) return true;
        }
        if (col > 0 && array[row][col - 1].equals(str) && !booleans[row][col - 1]) {
            booleans[row][col] = true;
            if (test(booleans, array, substring, row, col - 1)) return true;
        }
        if (col + 1 < array[0].length && array[row][col + 1].equals(str) && !booleans[row][col + 1]) {
            booleans[row][col] = true;
            if (test(booleans, array, substring, row, col + 1)) return true;
        }
        booleans[row][col] = false;
        return false;
    }

    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws IOException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(inputName)));
        String str = br.readLine();
        List<String> list = new ArrayList<>();
        while (str != null) {
            list.add(str);
            str = br.readLine();
        }
        String[][] arrayWords = new String[list.size()][list.get(0).length() / 2 + 1];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).length(); j += 2) {
                arrayWords[i][j / 2] = String.valueOf(list.get(i).charAt(j));
            }
        }
        Set<String> result = new HashSet<>();
        boolean[][] booleans = new boolean[arrayWords.length][arrayWords[0].length];
        int count;
        for (String element : words) {
            for (int i = 0; i < arrayWords.length; i++) {
                count = 0;
                for (int j = 0; j < arrayWords[0].length; j++) {
                    if (arrayWords[i][j].equals(String.valueOf(element.charAt(0)))) {
                        if (test(booleans, arrayWords, element, i, j)) {
                            result.add(element);
                            booleans = new boolean[arrayWords.length][arrayWords[0].length];
                            count++;
                            break;
                        }
                    }
                }
                if (count == 1) break;
            }
        }
        return result;
    }

    // трудоёмкост : O(n*m) - n это число строк и m это число букв в одной строке
    // ресурсоёмкост : O(n*m)
}