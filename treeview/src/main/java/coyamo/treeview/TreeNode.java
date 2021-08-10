package coyamo.treeview;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
    List<TreeNode<?>> child = new ArrayList<>();
    private T data;
    private int depth;
    private Object tag;
    private int type;
    private boolean expand, hasChild;

    public TreeNode(T data, int type) {
        this.data = data;
        this.type = type;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean hasChild() {
        return hasChild;
    }

    public List<TreeNode<?>> getChild() {
        return child;
    }

    public void setChild(List<TreeNode<?>> child) {
        this.child = child;
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

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
