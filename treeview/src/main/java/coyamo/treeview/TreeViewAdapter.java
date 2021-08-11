package coyamo.treeview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<TreeNode<?>> data = new ArrayList<>();
    private final Map<Integer, TreeBinder<? extends RecyclerView.ViewHolder>> binderMap = new HashMap<>();
    private int gap=20;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup p1, int p2) {
        RecyclerView.ViewHolder vh = getBinder(p2).onCreateViewHolder(p1, p2);
        vh.itemView.setOnClickListener(v -> {
            TreeNode<?> node= (TreeNode<?>) v.getTag(R.id.tag_node);
            boolean preventDefault=false;
            if(onTreeViewClickListener!=null){
                int pos= (int) v.getTag(R.id.tag_position);
                preventDefault=onTreeViewClickListener.onClick(vh,node,pos);
            }
            if(!preventDefault){
                if(node.isExpand())hideChild(node);
                else expandChild(node);
            }


        });
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TreeNode<?> node =getNodeAt(position);
        holder.itemView.setPadding(node.getDepth()*gap,0,0,0);
        holder.itemView.setTag(R.id.tag_node,node);
        holder.itemView.setTag(R.id.tag_position,position);
        getBinder(getItemViewType(position)).onBindViewHolder(holder,node );
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

    public TreeNode<?> getNodeAt(int pos) {
        return data.get(pos);
    }

    public void addChild(TreeNode<?> node, List<TreeNode<?>> child) {
        if (node == null) {
            for (TreeNode<?> tn : child) {
                tn.setDepth(0);
            }
            int index = data.size();
            data.addAll(child);
            notifyItemRangeInserted(index, child.size());

        } else {
            node.setHasChild(true);
            int depth = node.getDepth();
            for (TreeNode<?> tn : child) {
                tn.setDepth(depth + 1);
            }
            node.child.addAll(child);
            if (node.isExpand()) {
                int index = data.indexOf(node) + 1;
                //这种情况是父节点没有显示 所以设置为expand 也暂时不能更新view
                if(index==0)return;
                data.addAll(index, child);
                notifyItemRangeInserted(index, child.size());
            }
        }

        //更新child的node
        for(TreeNode<?> nd:child){
            if(nd.isExpand())expandChild(nd);
        }
    }
/*
    public void removeChild(TreeNode<?> parentNode) {
        if (!parentNode.hasChild() || parentNode.child.size() == 0) return;
        int count= removeNodeChild(parentNode,true);
        int index=data.indexOf(parentNode) + 1;
        if(index!=0) notifyItemRangeRemoved(index, count);
        parentNode.setHasChild(false);
        parentNode.child.clear();
    }
*/
    public void removeRoot(){
        int count =data.size();
        data.clear();
        notifyItemRangeRemoved(0,count);
    }
    public void hideChild(TreeNode<?> parentNode) {
        if (!parentNode.hasChild() || !parentNode.isExpand()) return;
        int index=data.indexOf(parentNode) + 1;
        int count=removeNodeChild(parentNode,false);
        if(index!=0) notifyItemRangeRemoved(index, count);
        parentNode.setExpand(false);
    }

    private int removeNodeChild(TreeNode<?> parentNode,boolean delete){
        int count=0;
        for(TreeNode<?> nd:parentNode.child){
            if(nd.child.size()!=0){
                if(nd.isExpand())  count+=removeNodeChild(nd,delete);
                else removeNodeChild(nd,delete);
            }
            count++;

        }
        data.removeAll(parentNode.child);
        if(delete)parentNode.child.clear();
        return count;
    }
    public void expandChild(TreeNode<?> parentNode) {
        //if (parentNode.isExpand()) return;
        int index = data.indexOf(parentNode) + 1;
        data.addAll(index, parentNode.child);
        notifyItemRangeInserted(index, parentNode.child.size());
        parentNode.setExpand(true);

        //更新node的node
        for(TreeNode<?> nd:parentNode.child){
            if(nd.isExpand())expandChild(nd);
        }
    }

    public <T extends RecyclerView.ViewHolder> void addBinder(int type, TreeBinder<T> binder) {
        binderMap.put(type, binder);
    }

    public TreeBinder getBinder(int type) {
        return binderMap.get(type);
    }

    public TreeBinder removeBinder(int type) {
        return binderMap.remove(type);
    }



    private OnTreeViewClickListener onTreeViewClickListener;

    public void setOnTreeViewClickListener(OnTreeViewClickListener onTreeViewClickListener) {
        this.onTreeViewClickListener = onTreeViewClickListener;
    }

    public interface OnTreeViewClickListener{
        boolean onClick(RecyclerView.ViewHolder vh,TreeNode<?> node,int index);
    }
}
