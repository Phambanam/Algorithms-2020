package lesson7;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    public static String longestCommonSubSequence(String first, String second) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        List<Integer> result = new ArrayList<>();
        if (list.isEmpty()) return result;
        int m = list.size();
        int[] arrayLength = new int[m];
        int[] arrayTrace = new int[m];
        int max;
        int jMax;
        arrayLength[0] = 1;
        for (int i = 1; i < m; i++) {
            max = 0;
            jMax = 0;
            for (int j = 0; j < i; j++) {
                if (list.get(j) < list.get(i) && arrayLength[j] > max) {
                    max = arrayLength[j];
                    jMax = j;
                }
            }
            arrayLength[i] = max + 1;
            arrayTrace[i] = jMax;
        }
        int n = 0;
        for (int k = 0; k < m; k++) {
            if (arrayLength[k] > arrayLength[n]) n = k;
        }
        while (true) {
            if (n == 0) {
                result.add(list.get(n));
                break;
            }
            result.add(list.get(n));
            n = arrayTrace[n];
        }

        Collections.reverse(result);
        return result;
    }
    // трудоёмкост : O(n*n)
    // ресурсоёмкост : O(n) , здесь n- это размер списка


    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(inputName)));
        List<String> list = new ArrayList<>();
        String current = br.readLine();
        while (current != null) {
            list.add(current);
            current = br.readLine();
        }
        String[] array;
        Pair<Integer, Integer>[][] matrix = new Pair[list.size()][list.get(0).split(" ").length];
        int fromTop;
        int fromRight;
        int fromNorthWest;
        for (int i = 0; i < list.size(); i++) {
            array = list.get(i).split(" ");
            for (int j = 0; j < array.length; j++) {
                int element = Integer.parseInt(array[j]);
                if (i == 0 && j == 0) {
                    matrix[i][j] = new Pair(0, 0);
                } else if (i == 0) {
                    matrix[i][j] = new Pair<>(element, matrix[i][j - 1].component2() + matrix[i][j - 1].component1());
                } else if (j == 0) {
                    matrix[i][j] = new Pair<>(element, matrix[i - 1][j].component2() + matrix[i - 1][j].component1());
                } else {
                    fromTop = matrix[i - 1][j].component1() + matrix[i - 1][j].component2();
                    fromRight = matrix[i][j - 1].component1() + matrix[i][j - 1].component2();
                    fromNorthWest = matrix[i - 1][j - 1].component1() + matrix[i - 1][j - 1].component2();
                    matrix[i][j] = new Pair<>(element, Math.min(fromTop, Math.min(fromNorthWest, fromRight)));
                }
            }
        }
        return matrix[matrix.length - 1][matrix[0].length - 1].component2();
    }
    // трудоёмкост : O(n*m) , здесь n- ширина матрицы, m- рост матрицы
    // ресурсоёмкост : O(n*m) ,
    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
