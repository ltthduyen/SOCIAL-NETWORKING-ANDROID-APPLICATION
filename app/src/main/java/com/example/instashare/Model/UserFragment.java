package com.example.instashare.Model;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instashare.R;
import com.example.instashare.Utils.InstaShareUtils;

public class UserFragment extends Fragment {
    private Uri uri;
    private User user;
    private Context context;
    private ImageView imgUser;
    private String time;
    private TextView tvTimeUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_user, container, false);
        imgUser = rootView.findViewById(R.id.imgUser);
        tvTimeUser = rootView.findViewById(R.id.tvTimeUser);

        Glide.with(context).load(uri).into(imgUser);
        tvTimeUser.setText(InstaShareUtils.getDistanceTime(time));
        return rootView;
    }

    public UserFragment(User user, Context context, Uri uri, String time)
    {
        this.user = user;
        this.context = context;
        this.uri = uri;
        this.time = time;
    }
}
