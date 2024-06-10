package com.example.instashare.Model;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instashare.Adapter.Comment;
import com.example.instashare.R;
import com.example.instashare.Utils.FirebaseUtils;
import com.example.instashare.Utils.InstaShareUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FriendFragment extends Fragment {
    private Uri uri;
    private User user, friend_user;
    private String uid, firstName, lastName, time;
    private Context context;
    private ImageView imgFriend;
    private TextView tvFriendName, tvTimeFriend;
    private ImageView  btn_cry, btn_wow, btn_laughing, btn_love;
    private ViewGroup container;
    private Button btnbinhluan;
    private ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.page_friend, container, false);
        imgFriend = rootView.findViewById(R.id.imgFriend);
        tvFriendName = rootView.findViewById(R.id.tvFriendName);
        tvTimeFriend = rootView.findViewById(R.id.tvTimeFriend);

        btn_cry = rootView.findViewById(R.id.emoji_cry);
        btn_laughing = rootView.findViewById(R.id.emoji_laughing);
        btn_love = rootView.findViewById(R.id.emoji_love);
        btn_wow = rootView.findViewById(R.id.emoji_wow);
        btnbinhluan = rootView.findViewById(R.id.btnComment);

        Glide.with(context).load(this.uri).into(imgFriend);
        getName();

        tvTimeFriend.setText(InstaShareUtils.getDistanceTime(time));

        btn_wow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji("3");
            }
        });
        btn_cry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji("4");
            }
        });
        btn_laughing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji("1");
            }
        });
        btn_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                emoji("2");
            }
        });

        btnbinhluan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentDialog();
            }
        });
        return rootView;
    }

    private void showCommentDialog() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.activity_comment, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        EditText edtReply = bottomSheetView.findViewById(R.id.edtReply);
        ImageButton imbReply = bottomSheetDialog.findViewById(R.id.imbReply);

        edtReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty())
                    imbReply.setVisibility(View.GONE);
                else
                    imbReply.setVisibility(View.VISIBLE);
            }
        });
        imbReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment(edtReply.getText().toString().trim());
                edtReply.setText("");
                bottomSheetView.setVisibility(View.GONE);
            }
        });
    }

    private void comment(String input){
        String idchatroom = InstaShareUtils.createId(user.getUid(), friend_user.getUid());
        Message newMessage = new Message(input, user.getUid(), Timestamp.now(), uri);
        FirebaseUtils.Instance().getChats(idchatroom)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        for (QueryDocumentSnapshot doc : value) {
                            Message message1= new Message(doc);
                            if(message1.getUri()==uri){
                                newMessage.setUri(null);
                                break;
                            }
                        }
                    }
                });
        if(newMessage!= null)
            FirebaseUtils.Instance().sendMessage(idchatroom, newMessage);
    }
    public FriendFragment(User user, Context context, Uri uri, String uid, String time)
    {
        this.user = user;
        this.context = context;
        this.uri = uri;
        this.uid = uid;
        this.time = time;
    }

    private void getName()
    {
        friend_user = new User();
        friend_user.setUid(uid);
        FirebaseUtils.getName(friend_user, context, new FirebaseUtils.UserNameCallback() {
            @Override
            public void onUserNameLoaded() {
                firstName = friend_user.getFirstName();
                lastName = friend_user.getLastName();
                tvFriendName.setText(firstName + " " + lastName);
            }
        });
    }

    public void flyEmoji(final int resId) {
        ZeroGravityAnimation animation = new ZeroGravityAnimation();
        animation.setCount(1);
        animation.setScalingFactor(2.0f);
        animation.setOriginationDirection(Direction.BOTTOM);
        animation.setDestinationDirection(Direction.TOP);
        animation.setImage(resId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        container = rootView.findViewById(R.id.animation_holder);
        animation.play((Activity) context,container);
    }
    public void emoji(String e){
        Comment comment = new Comment("0",Timestamp.now(), e, uri,  user.getUid());
        FirebaseUtils.Instance().sendIcon(friend_user.getUid(),comment );
        for (int i = 0; i < 15; i++) {
            switch(e){
                case "1":
                    flyEmoji(R.drawable.emoji_laughing);
                    break;
                case "2":
                    flyEmoji(R.drawable.emoji_love);
                    break;
                case "3":
                    flyEmoji(R.drawable.emoji_wow);
                    break;
                case "4":
                    flyEmoji(R.drawable.emoji_cry);
                    break;
            }
        }
    }
}
