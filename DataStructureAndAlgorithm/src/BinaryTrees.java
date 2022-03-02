import java.util.*;

class BinaryTrees { // 二叉树
    private TreeNode root; // rootnode

    public BinaryTrees() {
    }

    public BinaryTrees(TreeNode root) {
        this.setRoot(root);
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<Integer> preOrderTraversal() { // 前序遍历封装方法
        List<Integer> list = new ArrayList<>();
        if (root != null) {
            root.preOrderTraversal(list);
        }
        return list;
    }

    public List<Integer> infixOrderTraversal() { // 前序遍历封装方法
        List<Integer> list = new ArrayList<>();
        if (root != null) {
            root.infixOrderTraversal(list);
        }
        return list;
    }

    public List<Integer> suffixOrderTraversal() { // 前序遍历封装方法
        List<Integer> list = new ArrayList<>();
        if (root != null) {
            root.suffixOrderTraversal(list);
        }
        return list;
    }
}

class TreeNode { // 二叉树节点
    private int val;
    private TreeNode left;
    private TreeNode right;

    public TreeNode() {
    }

    public TreeNode(int val) {
        this.setVal(val);
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.setVal(val);
        this.setLeft(left);
        this.setRight(right);
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public void preOrderTraversal(List<Integer> list) { // 前序遍历核心程序
        list.add(val);
        if (left != null) {
            left.preOrderTraversal(list);
        }
        if (right != null) {
            right.preOrderTraversal(list);
        }
    }

    public void infixOrderTraversal(List<Integer> list) { // 前序遍历核心程序
        if (left != null) {
            left.infixOrderTraversal(list);
        }
        list.add(val);
        if (right != null) {
            right.infixOrderTraversal(list);
        }
    }

    public void suffixOrderTraversal(List<Integer> list) { // 前序遍历核心程序
        if (left != null) {
            left.suffixOrderTraversal(list);
        }
        if (right != null) {
            right.suffixOrderTraversal(list);
        }
        list.add(val);
    }
}
