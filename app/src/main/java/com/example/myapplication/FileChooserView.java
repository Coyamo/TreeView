package com.example.myapplication;
import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import coyamo.treeview.TreeNode;
import coyamo.treeview.TreeViewAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 简易的文件选择demo
 */
public class FileChooserView extends RecyclerView{
	
	private File rootDir;
	final TreeViewAdapter adapter;
	
    public FileChooserView(Context ctx){
		this(ctx,null);
	}
	
	public FileChooserView(Context ctx,AttributeSet attr){
		super(ctx,attr);
		setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false));
		adapter=new TreeViewAdapter();
		setAdapter(adapter);

		adapter.addBinder(1,new FileBinder());

		
		adapter.setGap((int)ViewUtils.dp2px(ctx,24));
		adapter.setOnTreeViewClickListener(new TreeViewAdapter.OnTreeViewClickListener(){

				@Override
				public boolean onClick(RecyclerView.ViewHolder vh, TreeNode<?> node, int index) {
					if(node.getType()==1){
						FileBinder.VH holder=(FileBinder.VH) vh;
						File file=(File) node.getData(); 
						if(onFileItemClickListener!=null){
							boolean ret=onFileItemClickListener.onClick(holder,file);
							if(ret)return true;
						}
						if(file.isDirectory())
							if(node.isExpand()){
								//holder.state.setRotation(270);
								holder.state.animate().rotation(-90);
								holder.icon.setImageResource(R.drawable.ic_folder);
							}else{
								//holder.state.setRotation(0);
								holder.state.animate().rotation(0);
								holder.icon.setImageResource(R.drawable.ic_folder_open);
							}
						if(node.isExpand())return false;
						node.getChild().clear();
						if(file.isDirectory()){
							List<TreeNode<?>> list=new ArrayList<>();
							File[] fl=file.listFiles();
							if(fl!=null)
								for(File f:fl){
									list.add(new TreeNode<>(f, 1));
								}
							adapter.addChild(node,list);
						}
					}

					return false;
				}
			});
		
	}

	public void setRootDir(File rootDir) {
		this.rootDir = rootDir;
		List<TreeNode<?>> root=new ArrayList<>();
		File[] fs=rootDir.listFiles();
		if(fs==null)return;
		for(File f:fs){
			root.add(new TreeNode<File>(f,1));
		}
		adapter.removeRoot();
		adapter.addChild(null,root);
		if(onFileItemClickListener!=null){
			onFileItemClickListener.onRootPathChanged(rootDir);
		}
	}

	public File getRootDir() {
		return rootDir;
	}

	private OnFileItemClickListener onFileItemClickListener;

	public void setOnFileItemClickListener(OnFileItemClickListener onFileItemClickListener) {
		this.onFileItemClickListener = onFileItemClickListener;
	}

	public interface OnFileItemClickListener{
    	boolean onClick(FileBinder.VH vh,File file);
    	void onRootPathChanged(File file);
	}
    
}
