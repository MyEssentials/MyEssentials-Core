package myessentials.test.entities;

import junit.framework.Assert;
import myessentials.entities.api.Tree;
import myessentials.entities.api.TreeNode;
import myessentials.test.MECTest;
import org.junit.Test;

public class TreeTest extends MECTest {

    @Test
    public void shouldInitTreeWithRoot() {
        TreeNode node = new TreeNode();
        Tree tree = new Tree(node);
        Assert.assertEquals("Tree root has not been instantiated properly", node, tree.getRoot());
    }

    @Test
    public void shouldAddParentsToNode() {
        TreeNode nodeRoot = new TreeNode();
        TreeNode nodeChild1 = new TreeNode();
        TreeNode nodeChild2 = new TreeNode();
        TreeNode nodeChild3 = new TreeNode();
        TreeNode nodeChild21 = new TreeNode();

        nodeRoot.addChild(nodeChild1);
        nodeRoot.addChild(nodeChild2);
        nodeRoot.addChild(nodeChild3);
        nodeChild2.addChild(nodeChild21);

        Assert.assertTrue("Child has not been added to the node", nodeRoot.getChildren().contains(nodeChild1));
        Assert.assertTrue("Child has not been added to the node", nodeRoot.getChildren().contains(nodeChild2));
        Assert.assertTrue("Child has not been added to the node", nodeRoot.getChildren().contains(nodeChild3));
        Assert.assertTrue("Child has not been added to the node", nodeChild2.getChildren().contains(nodeChild21));

        Assert.assertTrue("Failed to get parent from a node with parent", nodeChild1.getParent().equals(nodeRoot));
        Assert.assertTrue("Failed to get parent from a node with parent", nodeChild2.getParent().equals(nodeRoot));
        Assert.assertTrue("Failed to get parent from a node with parent", nodeChild3.getParent().equals(nodeRoot));
        Assert.assertTrue("Failed to get parent from a node with parent", nodeChild21.getParent().equals(nodeChild2));
    }

}
