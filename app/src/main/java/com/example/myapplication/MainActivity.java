package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import coyamo.treeview.TreeBinder;
import coyamo.treeview.TreeNode;
import coyamo.treeview.TreeViewAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));


        TreeViewAdapter adapter=new TreeViewAdapter();
        adapter.addBinder(1, new TextBinder());
        adapter.addBinder(2, new ImageBinder());

        List<TreeNode<?>> root=new ArrayList<>();
        TreeNode<String> node1=new TreeNode<>("目录 1",1);
        root.add(node1);
        root.add(new TreeNode<>("目录 2",1));
        root.add(new TreeNode<>("目录 3",1));
        root.add(new TreeNode<>("目录 4",1));
        adapter.addChild(null,root);

        List<TreeNode<?>> sub1=new ArrayList<>();
        TreeNode<String> node2 =new TreeNode<>("目录 1-1",1);
        node2.setExpand(true);
        sub1.add(node2);
        sub1.add(new TreeNode<>("目录 1-2",1));
        sub1.add(new TreeNode<>(R.mipmap.ic_launcher,2));
        adapter.addChild(node1,sub1);

        List<TreeNode<?>> sub1_1=new ArrayList<>();
        sub1_1.add(new TreeNode<>("目录 1-1-1",1));
        sub1_1.add(new TreeNode<>(R.mipmap.ic_launcher,2));
        adapter.addChild(node2,sub1_1);

        adapter.setGap(50);
        adapter.setOnTreeViewClickListener((view, node, index) -> {
                Toast.makeText(MainActivity.this, node.getData()+" "+index, Toast.LENGTH_SHORT).show();
                return false;
            }
        );
        recyclerView.setAdapter(adapter);
    }

    public static class TextBinder implements TreeBinder<TextBinder.VH> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int type) {
            return new VH(new TextView(p1.getContext()));
        }

        @Override
        public void onBindViewHolder(VH holder, TreeNode<?> node) {
            String str= (String) node.getData();
            holder.text.setText(str);
        }

        public static class VH extends RecyclerView.ViewHolder{
            TextView text;
            public VH(@NonNull View itemView) {
                super(itemView);
                text= (TextView) itemView;
            }
        }
    }

    public static class ImageBinder implements TreeBinder<ImageBinder.VH> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int type) {
            return new VH(new ImageView(p1.getContext()));
        }

        @Override
        public void onBindViewHolder(VH holder, TreeNode<?> node) {
            int id= (int) node.getData();
            holder.image.setImageResource(id);
        }

        public static class VH extends RecyclerView.ViewHolder{
            ImageView image;
            public VH(@NonNull View itemView) {
                super(itemView);
                image= (ImageView) itemView;
            }
        }
    }

}