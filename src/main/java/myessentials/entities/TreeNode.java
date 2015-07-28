package myessentials.entities;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {

    protected T parent;
    protected List<T> children = new ArrayList<T>();

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
