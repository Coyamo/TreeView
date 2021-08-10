package coyamo.treeview;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface TreeBinder<VH extends RecyclerView.ViewHolder> {
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int type);
    void onBindViewHolder(VH holder, TreeNode<?> node);
}
