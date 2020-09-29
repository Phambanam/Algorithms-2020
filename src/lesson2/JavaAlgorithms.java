package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(inputName)));
        List<Integer> list = new ArrayList<>();
        String str = br.readLine();
        while (str != null) {
            list.add(Integer.parseInt(str));
            str = br.readLine();
        }
        Pair<Integer, Integer> res = null;
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i != 0 && list.get(i) > list.get(i - 1)) {
                continue;
            }
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) - list.get(i) > max) {
                    max = list.get(j) - list.get(i);
                    res = new Pair<>(i, j);
                }
            }
        }
        assert res != null;
        return new Pair<>(res.component1() + 1, res.component2() + 1);
        // T =O(n^2) n размер list
        //R = O(n)
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     * <p>
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        int res = 0;
        for(int i = 2; i <= menNumber; i++){
            res = (choiceInterval + res) % i;
        }
        return res +1;
        //T = O(n) n : menNumber
        // R = O(1)
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    static public String longestCommonSubstring(String firs, String second) {
        String res ="";
        int fLength = firs.length();
        int sLength = second.length();
        for(int i = 0; i < fLength; i++)
            for(int j = 0; j < sLength; j++ ){
               if(firs.charAt(i) == second.charAt(j)){
                   int m = 1;
                   String str = String.valueOf(firs.charAt(i));
                   while(i + m < fLength && j+ m < sLength && firs.charAt(i+m) == second.charAt(j+m))
                   {
                       str +=  String.valueOf(firs.charAt(i+m));
                       m++;
                   }
                if (str.length() > res.length()) res = str;
               }
            }
        return res;
            // n = max(fLength,sLength)
        //Трудоемкость алгоритм - O(n^3)
        // R = O(1)
    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    static public int calcPrimesNumber(int limit) {
        if (limit <= 1) return 0;
        int count = 0;
        int[] list = new int[limit+1];
        list[0]=0;
        list[1]=0;
        for(int i = 2; i <= limit; i++) list[i] = 1;
        for(int i = 2; i <= (int) Math.sqrt(limit); i++){
            if(list[i] == 1)
                for(int j = 2; j <= limit /i; j++ ) list[i*j]=0;
        }
        for (int e : list){
            if(e ==1) count++;
        }
        return count;
    }
    // T = O(nlogn) n= limit
    // R = O(n)
}
