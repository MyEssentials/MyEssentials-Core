package myessentials.entities;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Container<T> {

    protected List<T> items = new ArrayList<T>();

    public void add(T item) {
        items.add(item);
    }

    public void add(Collection<T> items) {
        this.items.addAll(items);
    }

    public void remove(T item) {
        items.remove(item);
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public List<T> asList() {
        return ImmutableList.copyOf(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
