package myessentials.entities.api;

public class Tree<T extends TreeNode> {
    private T root;

    public Tree(T root) {
        this.root = root;
    }

    public T getRoot() {
        return root;
    }
}
