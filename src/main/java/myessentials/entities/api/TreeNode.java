package myessentials.entities.api;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T extends TreeNode> {

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
        child.parent = this;
    }


    public List<T> getChildren() {
        return children;
    }
}
