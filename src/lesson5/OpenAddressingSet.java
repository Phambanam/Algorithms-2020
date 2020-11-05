package lesson5;

import lesson3.BinarySearchTree;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OpenAddressingSet<T> extends AbstractSet<T> {
    private T t = (T) "PHAMNAM";
    List<T> elementData = new ArrayList<>();

    private final int bits;

    private final int capacity;


    private final Object[] storage;

    public Object[] getStorage() {
        return storage;
    }

    private int size = 0;

    public int startingIndex(Object element) {

        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }
    public T DD (){
        return t;
    }
    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current == DD() || current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != DD() && current != null) {
            if (current.equals(t)) {

                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        elementData.add(t);
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    @Override
    public boolean remove(Object o) {
        if(!contains(o)) {
            return false;
        }
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != DD() && current != null) {
            if (current.equals(o)) {
                break;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }

        storage[index] = DD();
        size--;
        return true;

    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        // TODO
        return new myIterator();
    }
    public class myIterator implements Iterator<T>{
        private int location = 0;
        int  lastRet = -1;


        @Override
        public boolean hasNext() {
            return location != size ;
        }

        @Override
        public T next() {
            int i = location;
            if(i >= size) throw new IllegalStateException();
            List<T> elementData = OpenAddressingSet.this.elementData;
            location = i + 1;
            return (T) elementData.get(lastRet = i );
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            OpenAddressingSet.this.remove(elementData.get(lastRet ));
            elementData.remove(lastRet );
            location = lastRet;
            lastRet = -1;
        }
    }
}

