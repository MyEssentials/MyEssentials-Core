package myessentials.entities;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

    private T parent;
    private List<T> children = new ArrayList<T>();

    public TreeNode() {
        this(null);
    }

    public TreeNode(T parent) {
        this.parent = parent;
    }

    public T getParent() {
        return parent;
    }

    public void addChild(T child) {
        children.add(child);
    }


    public List<T> getChildren() {
        return children;
    }
}
