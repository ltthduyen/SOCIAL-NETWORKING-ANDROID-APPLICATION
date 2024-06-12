package com.example.instashare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instashare.Model.User;
import com.example.instashare.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
    private List<User> listArray;
    private Context mcontext;
    private String cuid;
    public FriendAdapter(Context mcontext, String cuid) {
        this.mcontext = mcontext;
        this.cuid = cuid;
    }
    public void setData (List<User> List){
        this.listArray = List;
        notifyDataSetChanged();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_friends,parent,false );
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = listArray.get(position);
        holder.name.setText(user.getFullName());
        Glide.with(mcontext).load(user.getUri()).circleCrop().into(holder.img);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageButton btnState;
        CircleImageView img;
        String uid;
        String id;
        public MyViewHolder(View itemView) {
            super ( itemView );
            name = itemView.findViewById( R.id.tv_name);
            btnState = itemView.findViewById(R.id.btn_delete);
            img = itemView.findViewById(R.id.img_user);
        }

    }

    @Override
    public int getItemCount() {
        return listArray.size ();
    }
}
