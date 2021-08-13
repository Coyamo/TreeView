package com.example.myapplication;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import coyamo.treeview.TreeBinder;
import coyamo.treeview.TreeNode;
import java.io.File;

public class FileBinder implements TreeBinder<FileBinder.VH> {

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int type) {
		View v=LayoutInflater.from(p1.getContext()).inflate(R.layout.simple_file_chooser_item,p1,false);
		return new VH(v);
	}

	@Override
	public void onBindViewHolder(FileBinder.VH holder, TreeNode node) {
		File file=(File) node.getData();
		if(file.isDirectory()){
			holder.state.setVisibility(View.VISIBLE);
			holder.state.setImageResource(R.drawable.ic_expand);
			
			if(node.isExpand()){
				holder.state.setRotation(0);
				//holder.state.animate().rotation(0);
				holder.icon.setImageResource(R.drawable.ic_folder_open);
			}else{
				holder.state.setRotation(-90);
				//holder.state.animate().rotation(270);
				holder.icon.setImageResource(R.drawable.ic_folder);
			}
			
		}else{
			holder.state.setVisibility(View.INVISIBLE);
			holder.icon.setImageResource(R.drawable.ic_file);
		}
		holder.text.setText(file.getName());
	}

    public static class VH extends RecyclerView.ViewHolder{
		public ImageView state,icon;
		public TextView text;
		public VH(View v){
			super(v);
			state=v.findViewById(R.id.simple_file_chooser_item_state);
			icon=v.findViewById(R.id.simple_file_chooser_item_icon);
			text=v.findViewById(R.id.simple_file_chooser_item_text);
			
			
			
		}
	}
}
