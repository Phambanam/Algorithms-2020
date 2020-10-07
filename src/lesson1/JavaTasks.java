package lesson1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaTasks {
    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     * <p>
     * Пример:
     * <p>
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) throws Exception {

        FileReader fr = new FileReader(new File(inputName));
        FileWriter fw = new FileWriter(new File(outputName));
        BufferedReader br = new BufferedReader(fr);
        String line;
        String test = "12:41:30 AM";
        String sample = "(([01]\\d)|(2[0-4])):[0-5]\\d:[0-5]\\d\\s((PM)|(AM))";
        List<Integer> lTime = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            int timeIn =
                    Integer.parseInt(line.substring(3, 5)) * 60 +
                            Integer.parseInt(line.substring(6, 8));
            if (!line.matches(sample)) throw new Exception(" loi ");
            if (!line.startsWith("12")) {
                timeIn += Integer.parseInt(line.substring(0, 2)) * 3600;
            }
            if (!line.startsWith("AM", 9)) {
                timeIn += 24 * 3600;
            }
            lTime.add(timeIn);
        }
        int[] listT = lTime.stream().mapToInt(Integer::intValue).toArray();
        Sorts.quickSort(listT);
        for (int e : listT) {
            {
                if (e < 3600) {
                    fw.write(String.format("%02d:%02d:%02d", 12, (e % 3600) / 60, e % 60) + " AM\n");
                }
            }
            if (e > 3600 && e < 24 * 3600)
                fw.write(String.format("%02d:%02d:%02d", e / 3600, (e % 3600) / 60, e % 60) + " AM\n");
            if (e > 24 * 3600) {
                e -= 24 * 3600;
                if (e < 3600) {
                    e += 12 * 3600;
                }
                fw.write(String.format("%02d:%02d:%02d", e / 3600, (e % 3600) / 60, e % 60) + " PM\n");
            }
        }

        fr.close();
        fw.close();
        br.close();
        //=>Трудоемкость алгоритма T = O(nlog(n)) n размер listT
        // R = O(n)
    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) throws IOException {
        Scanner sc = new Scanner(Paths.get(inputName), StandardCharsets.UTF_8);
        PrintWriter printWriter = new PrintWriter(outputName, StandardCharsets.UTF_8);
        Map<String, List<String>> addressNames = new HashMap<>();
        Pattern pattern = Pattern.compile("(([а-яёА-ЯЁ]+)|(([а-яёА-ЯЁ]+)-([а-яёА-ЯЁ]+)))\\s\\d+");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String l = line.substring(matcher.start(), matcher.end());
                List<String> lStr = new ArrayList<>();
                addressNames.putIfAbsent(l, lStr);
                addressNames.get(l).add(line.substring(0, line.length() - 3 - l.length()));
            }
        }
        for (List<String> e : addressNames.values()) {
            Collections.sort(e);
        }
        Map<String, List<Integer>> map = new HashMap<>();
        for (String e : addressNames.keySet()) {
            Integer i = Integer.parseInt(e.split("\\s")[1]);
            String str = e.split("\\s")[0];
            List<Integer> lStr = new ArrayList<>();
            map.putIfAbsent(str, lStr);
            map.get(str).add(i);
        }
        List<String> lConvert = new ArrayList<>(map.keySet());
        Collections.sort(lConvert);
        for (List<Integer> e : map.values()) {
            Collections.sort(e);
        }
        for (String e : lConvert) {
            for (Integer i : map.get(e)) {
                printWriter.println(e + " " + i + " - " + String.join(", ", addressNames.get(e + " " + i)));
            }
        }//O(n)
        printWriter.close();
        // => T = O(n^2) n = размер map
        // R = O(n)
    }


    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File(inputName)));
        FileWriter fw = new FileWriter(new File(outputName));
        List<Integer> positiveList = new ArrayList<>();
        String tem = br.readLine();
        while (tem != null) {
            int a =(int) (Double.parseDouble(tem) * 10) + 2730;
            positiveList.add(a);
            tem = br.readLine();
        }
        int[] arrayPositive = positiveList.stream().mapToInt(i -> i).toArray();
        arrayPositive = Sorts.countingSort(arrayPositive,7730);//O(n/2)
        for (int e : arrayPositive) fw.write((double)( e -2730)/ 10 + "\n");//O(n/2)
        fw.close();
        br.close();
        //=> T= O(n)n = max( 7730, PositiveInt.size)
        // R = O(n)
    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(new File(inputName)));
        FileWriter fw = new FileWriter(new File(outputName));
        List<Integer> sequence = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            sequence.add(Integer.parseInt(line));
        }
        int[] arrSequence = sequence.stream().mapToInt(i -> i).toArray();
        Sorts.quickSort(arrSequence);
        int maxCount = 1;
        int minValue = arrSequence[0];
        int countValue = 1;
        for (int i = 1; i < arrSequence.length; i++) {
            if (arrSequence[i] != arrSequence[i - 1] || i == arrSequence.length - 1) {
                if (i == arrSequence.length - 1) countValue++;
                if ((minValue > arrSequence[i - 1] && maxCount == countValue) || countValue > maxCount) {
                    minValue = arrSequence[i - 1];
                    maxCount = countValue;
                }
                countValue = 0;
            }
            countValue++;
        }
        for (int e : sequence) {
            if (e != minValue) fw.write(e + "\n");
        }
        while (maxCount > 0) {
            fw.write(minValue + "\n");
            maxCount--;
        }
        fw.close();
        br.close();
        // => T = O(n^2) n = arrSequence.length;
        // R = O(n)
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     *
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
       int indexSecond = -1;
       int indexFirst = 0;
       int lengthFirst = first.length;
       while(indexFirst < first.length && indexSecond < second.length){
           indexSecond++;
           if(lengthFirst >= second.length){
               second[indexSecond] = first[indexFirst];
               indexFirst++;
               continue;
           }
           if(first[indexFirst].compareTo(second[lengthFirst]) > 0){
              second[indexSecond] = second[lengthFirst];
              lengthFirst++;
           }else{
               second[indexSecond] = first[indexFirst];
               indexFirst++;
           }
       }
       //T = O(n) n размер массива second
       // R = O(n)
}
}
