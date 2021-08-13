package coyamo.treeview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<TreeNode> data = new ArrayList<>();
    private final Map<Integer, TreeBinder<? extends RecyclerView.ViewHolder>> binderMap = new HashMap<>();
    private int gap = 20;
    private OnTreeViewClickListener onTreeViewClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
        RecyclerView.ViewHolder vh = getBinder(p2).onCreateViewHolder(p1, p2);
        vh.itemView.setOnClickListener(v -> {
            TreeNode node = (TreeNode) v.getTag(R.id.tag_node);
            boolean preventDefault = false;
            if (onTreeViewClickListener != null) {
                int pos = (int) v.getTag(R.id.tag_position);
                preventDefault = onTreeViewClickListener.onClick(vh, node, pos);
            }
            if (!preventDefault) {
                if (node.isExpand()) hideChild(node);
                else expandChild(node);
            }


        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TreeNode node = getNodeAt(position);
        holder.itemView.setPadding(node.getDepth() * gap, 0, 0, 0);
        holder.itemView.setTag(R.id.tag_node, node);
        holder.itemView.setTag(R.id.tag_position, position);
        getBinder(getItemViewType(position)).onBindViewHolder(holder, node);
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getNodeAt(position).getType();
    }

    /**
     * 获取指定位置的node
     *
     * @param pos 相对于整个树的位置
     * @return node
     */
    public TreeNode getNodeAt(int pos) {
        return data.get(pos);
    }

    /**
     * 相对于整个树的位置
     *
     * @param node node
     * @return 位置
     */
    public int getNodeIndex(TreeNode node) {
        return data.indexOf(node);
    }
    //////////////////////////////

    /**
     * 返回root层 在同一层的位置
     *
     * @param node
     * @return
     */
    public int getRootNodeRelativeIndex(TreeNode node) {
        //if(node.getParent()!=null)return -1;
        int index = 0;
        for (TreeNode tn : data) {
            if (tn.getParent() == null) {
                if (tn == node) return index;
                index++;
            }
        }
        return index;
    }

    /**
     * 通知更新node
     *
     * @param node node
     */
    public void notifyNode(TreeNode node) {
        if (data.contains(node)) {
            int index = data.indexOf(node);
            if (index == data.size() - 1) {
                if (node.isExpand())
                    expandChild(node);
                return;
            }
            TreeNode temp;
            int i = index + 1;
            List<TreeNode> list = new ArrayList<>();
            while (i != data.size() && (temp = data.get(i)).getDepth() > node.getDepth()) {
                i++;
                list.add(temp);
            }
            data.removeAll(list);
            notifyItemRangeRemoved(index + 1, list.size());
            if (node.isExpand())
                expandChild(node);
        } else {
            TreeNode parent = node.getParent();
            if (parent != null) notifyNode(parent);
        }

    }

    //////////////////////////////
    public void addRoot(int pos,List<TreeNode> child) {
        for (TreeNode tn : child) {
            tn.setDepth(0);
        }
        int index = data.size();
        data.addAll(pos,child);
        notifyItemRangeInserted(index, child.size());

        //更新child的node
        for (TreeNode nd : child) {
            if (nd.isExpand()) expandChild(nd);
        }
    }
    public void addRoot(List<TreeNode> child) {
        for (TreeNode tn : child) {
            tn.setDepth(0);
        }
        int index = data.size();
        data.addAll(child);
        notifyItemRangeInserted(index, child.size());

        //更新child的node
        for (TreeNode nd : child) {
            if (nd.isExpand()) expandChild(nd);
        }
    }
    public void setRoot(List<TreeNode> child) {
        removeRoot();
        addRoot(child);
    }

    @Deprecated
    public void addChild(TreeNode node, List<TreeNode> child) {
        if (node == null) {
            for (TreeNode tn : child) {
                tn.setDepth(0);
            }
            int index = data.size();
            data.addAll(child);
            notifyItemRangeInserted(index, child.size());

        } else {
            node.setHasChild(true);
            int depth = node.getDepth();
            for (TreeNode tn : child) {
                tn.setDepth(depth + 1);
                tn.setParent(node);
            }
            node.children.addAll(child);
            if (node.isExpand()) {
                int index = data.indexOf(node) + 1;
                //这种情况是父节点没有显示 所以设置为expand 也暂时不能更新view
                if (index == 0) return;
                data.addAll(index, child);
                notifyItemRangeInserted(index, child.size());
            }
        }

        //更新child的node
        for (TreeNode nd : child) {
            if (nd.isExpand()) expandChild(nd);
        }
    }

    public void removeRoot() {
        int count = data.size();
        data.clear();
        notifyItemRangeRemoved(0, count);
    }

    public void hideChild(TreeNode parentNode) {
        if (!parentNode.hasChild() || !parentNode.isExpand()) return;
        int index = data.indexOf(parentNode) + 1;
        int count = removeNodeChild(parentNode, false);
        if (index != 0) notifyItemRangeRemoved(index, count);
        parentNode.setExpand(false);
    }

    /**
     * 从rv的数据中移除node及其所有显示了的node
     *
     * @param parentNode 待移除
     * @param delete     是否清除引用
     * @return 从rv data 移除的数据的数量
     */
    private int removeNodeChild(TreeNode parentNode, boolean delete) {
        int count = 0;
        for (TreeNode nd : parentNode.children) {
            if (nd.children.size() != 0) {
                if (nd.isExpand()) count += removeNodeChild(nd, delete);
                else removeNodeChild(nd, delete);
            }
            count++;

        }
        data.removeAll(parentNode.children);
        if (delete) parentNode.children.clear();
        return count;
    }

    public void expandChild(TreeNode parentNode) {
        //if (parentNode.isExpand()) return;
        int index = data.indexOf(parentNode) + 1;
        //如果parentNode没有显示 递归先展开他的父节点
        if (index == 0) {
            TreeNode parent = parentNode.getParent();
            if (parent != null) expandChild(parent);
            else return;
        }
        data.addAll(index, parentNode.children);
        notifyItemRangeInserted(index, parentNode.children.size());
        parentNode.setExpand(true);

        //更新node的node
        for (TreeNode nd : parentNode.children) {
            if (nd.isExpand()) expandChild(nd);
        }
    }

    public boolean isShowInTree(TreeNode node) {
        TreeNode parent = node.getParent();
        if (parent != null) {
            if (parent.isExpand()) {
                return isShowInTree(parent);
            } else return false;
        } else {
            for (TreeNode tn : data) {
                if (tn == node) return true;
            }
            return false;
        }
    }

    //////////////////////////////
    public <T extends RecyclerView.ViewHolder> void addBinder(int type, TreeBinder<T> binder) {
        binderMap.put(type, binder);
    }

    public TreeBinder getBinder(int type) {
        return binderMap.get(type);
    }

    public TreeBinder removeBinder(int type) {
        return binderMap.remove(type);
    }

    public void setOnTreeViewClickListener(OnTreeViewClickListener onTreeViewClickListener) {
        this.onTreeViewClickListener = onTreeViewClickListener;
    }

    public interface OnTreeViewClickListener {
        boolean onClick(RecyclerView.ViewHolder vh, TreeNode node, int index);
    }
}
