package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    List<T> elementData = new ArrayList<>();
    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     * <p>
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * <p>
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     * <p>
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        elementData.add(t);
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     * <p>
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     * <p>
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     * <p>
     * Средняя
     */
    Boolean check = true;

    @Override
    public boolean remove(Object o) {
        root = delete(root, o);
        if (!check) return false;
        size--;
        return true;
        // трудоёмкост : O(log(n)) в средним случае, O(n) в худшем случае n- количество узлов дерева
        // ресурсоёмкост : O(1)
    }

    private Node<T> delete(Node<T> node, Object o) {
        if (node == null) {
            check = false;
            return node;
        }
        if (node.value.compareTo((T) o) == 0) {
            if (node.left == null) {
                return node.right;
            }
            Node<T> maxNode = node.left;

            while (maxNode.right != null) {
                maxNode = maxNode.right;
            }
            Node<T> nNew = new Node<>(maxNode.value);
            nNew.right = node.right;
            nNew.left = delete(node.left, maxNode.value);
            return nNew;
        }
        if (node.value.compareTo((T) o) < 0) {
            node.right = delete(node.right, o);
        } else {
            node.left = delete(node.left, o);
        }
        return node;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        private T lastElement  ;
        private Queue<T> elementData = new LinkedList<>();
        public BinarySearchTreeIterator(){
            if(root != null) addQueue(root,elementData);
        }
        private void addQueue(Node<T> node, Queue<T> elementData){
            if(node.left != null) addQueue(node.left,elementData);
            elementData.add(node.value);
            if(node.right != null) addQueue(node.right,elementData);
        }

        /**
         * Проверка наличия следующего элемента
         * <p>
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         * <p>
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         * <p>
         * Средняя
         */
        @Override
        public boolean hasNext() {
            return elementData.peek() != null;
        }

        /**
         * Получение следующего элемента
         * <p>
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         * <p>
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         * <p>
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         * <p>
         * Средняя
         */
        @Override
        public T next() {
            if(elementData.peek() == null) throw new IllegalStateException();
           lastElement = elementData.remove();
           return  lastElement;
        }

        /**
         * Удаление предыдущего элемента
         * <p>
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         * <p>
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         * <p>
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         * <p>
         * Сложная
         */
        @Override
        public void remove() {
            if(lastElement == null) throw new IllegalStateException();
            if( !BinarySearchTree.this.remove(lastElement)) throw new IllegalStateException();

        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    int sizeSubSet(T from, T toEnd) {
        count = 0;
        sizeSubSet(from, toEnd, root);
        return count;
    }

    private int count;

    private void sizeSubSet(T from, T toEnd, Node<T> node) {
        if (node != null) {
            sizeSubSet(from, toEnd, node.left);
            if (from == null) {
                if (node.value.compareTo(toEnd) < 0) count++;
            } else if (toEnd == null) {
                if (node.value.compareTo(from) >= 0) count++;
            } else {
                if (node.value.compareTo(from) >= 0 && node.value.compareTo(toEnd) < 0) count++;
            }
            sizeSubSet(from, toEnd, node.right);
        }
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new mySortedSet<>(this,
                false, fromElement,
                false, toElement);
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new mySortedSet<>(this,
                true, null, false,
                toElement);
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new mySortedSet<>(this, false, fromElement, true, null);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;

    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }
}

final class mySortedSet<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    final BinarySearchTree<T> m;
    final T eStart, eEnd;
    final boolean fromStart, toEnd;

    mySortedSet(BinarySearchTree<T> m,
                boolean fromStart, T eStart,
                boolean toEnd, T eEnd) {
        this.m = m;
        this.fromStart = fromStart;
        this.eStart = eStart;
        this.toEnd = toEnd;
        this.eEnd = eEnd;
    }

    final boolean myCompare(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        if (eStart != null && eEnd != null) {
            return t.compareTo(eStart) >= 0 && t.compareTo(eEnd) < 0;
        } else {
            if (eStart == null) {
                return t.compareTo(eEnd) < 0;
            } else return t.compareTo(eStart) >= 0;
        }
    }

    @Override
    public boolean add(T t) {
        if (!myCompare(t)) throw new IllegalArgumentException();
        return myCompare(t) && m.add(t);
    }

    @Override
    public boolean remove(Object o) {
        if (!myCompare(o)) throw new IllegalArgumentException();
        return myCompare(o) && m.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return myCompare(o) && m.contains(o);
    }

    @Nullable
    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public int size() {
        return m.sizeSubSet(eStart, eEnd);
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }


    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (fromElement.compareTo(toElement) > 0) throw new NoSuchElementException();
        return new mySortedSet<>(m, false, fromElement,
                false, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new mySortedSet<>(m, fromStart, eStart, false, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new mySortedSet<>(m, false, fromElement, toEnd, eEnd);
    }

    @Override
    public T first() {

        if (size() == 0) throw new NoSuchElementException();
        if (eStart == null) {
            return m.first();
        } else if (toEnd) {
            return eStart;
        } else
            {
            Iterator<T> bIterator = m.iterator();
            T current = null;
            while (bIterator.hasNext()) {
                current = bIterator.next();
                if (current.compareTo(eStart) >= 0) {
                    break;
                }
            }
            return current;
        }

      }

    @Override
    public T last() {

        if (size() == 0) throw new NoSuchElementException();
        if (eEnd == null) {
            return m.last();
        } else {
            Iterator<T> bIterator = m.iterator();
            T current;
            T sCurrent = null;
            while (bIterator.hasNext()) {
                current = bIterator.next();
                if (current.compareTo(eEnd) >= 0) {
                    break;
                }
                sCurrent = current;
            }
            return sCurrent;
        }
    }


    @Override
    public int height() {
        return 0;
    }

    @Override
    public boolean checkInvariant() {
        return false;
    }
}