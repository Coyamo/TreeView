package coyamo.treeview;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    List<TreeNode> children = new ArrayList<>();
    private Object data;
    private int depth;
    private int type;
    private boolean expand, hasChild = true;
    private TreeNode parent;

    public TreeNode(Object data, int type) {
        this.data = data;
        this.type = type;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean hasChild() {
        return hasChild;
    }

    private List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(List<TreeNode> children) {
        for (TreeNode tn : children) {
            tn.parent = this;
            tn.depth = depth + 1;
        }
        this.children.addAll(children);
    }

    public void clearChild() {
        children.clear();
    }

    /**
     * 在父节点中的位置，最顶层node需要在adaper获取
     *
     * @param node node
     * @return 位置 没有则为-1
     */
    public int getIndex(TreeNode node) {
        if (parent == null) return -1;
        return parent.children.indexOf(node);
    }

    public TreeNode getChildAt(int index) {
        return children.get(index);
    }

    public int getChildCount() {
        return children.size();
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void insert(TreeNode child, int index) {
        child.parent = this;
        child.setDepth(depth + 1);
        children.add(index, child);
    }

    public void remove(int index) {
        children.remove(children.get(index));
    }

    public void remove(TreeNode node) {
        children.remove(node);
    }

    public void removeFromParent() {
        if (parent != null) parent.remove(this);
    }
}
