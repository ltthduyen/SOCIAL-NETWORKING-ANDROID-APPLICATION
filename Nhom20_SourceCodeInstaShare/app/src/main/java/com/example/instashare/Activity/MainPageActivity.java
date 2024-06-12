package com.example.instashare.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.example.instashare.Adapter.VPagerAdapter;
import com.example.instashare.Model.AccountFragment;
import com.example.instashare.Model.FriendFragment;
import com.example.instashare.Model.Request;
import com.example.instashare.Model.UserFragment;
import com.example.instashare.Model.User;
import com.example.instashare.Model.VerticalViewPager;
import com.example.instashare.Model.Widget;
import com.example.instashare.R;
import com.example.instashare.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.checkerframework.checker.units.qual.A;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPageActivity extends AppCompatActivity {
    private VerticalViewPager verticalViewPager;
    private PagerAdapter pagerAdapter;
    private CircleImageView cmvProfile;
    private LinearLayout iclHeading, iclCustomTakePhoto;
    private User user;
    private StorageReference storageRef;
    private StorageReference imagesRef;
    private Map<Date, StorageReference> listStorage;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<Date> keys;
    private List<String> listUid = new ArrayList<>();
    private List<String> listTemp = new ArrayList<>();
    private List<String> listName = new ArrayList<>();
    private Map<String, String> listMap = new HashMap<>();
    private List<Fragment> list;
    private ImageButton imbBackCapture, imbWidgetCus, imbChat;
    private Spinner spinFriend;
    private ArrayAdapter<String> adapterName;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        iclHeading = findViewById(R.id.iclheading);
        iclCustomTakePhoto = findViewById(R.id.icltakephoto);
        iclHeading.setVisibility(View.GONE);
        iclCustomTakePhoto.setVisibility(View.GONE);

        cmvProfile = findViewById(R.id.cmvProfileAcc);
        imbBackCapture = findViewById(R.id.imbBackCapture);
        imbWidgetCus = findViewById(R.id.imbWidgetCus);
        spinFriend = findViewById(R.id.spinFriend);

        imbChat = findViewById(R.id.imbChat);

        user = (User) getIntent().getParcelableExtra("user");
        Glide.with(this).load(user.getUri()).circleCrop().into(cmvProfile);

        list = new ArrayList<>();
        list.add(new AccountFragment(user, this));

        verticalViewPager = findViewById(R.id.vvpMainPage);
        pagerAdapter = new VPagerAdapter(getSupportFragmentManager(),list);

        imbChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
        verticalViewPager.addOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    iclHeading.setVisibility(View.GONE);
                    iclCustomTakePhoto.setVisibility(View.GONE);
                } else {
                    iclHeading.setVisibility(View.VISIBLE);
                    iclCustomTakePhoto.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        verticalViewPager.setAdapter(pagerAdapter);

        storageRef = FirebaseStorage.getInstance().getReference();
        imagesRef = storageRef.child("Images");
        listStorage = new TreeMap<>(new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return o2.compareTo(o1);
            }
        });
        listName.add("Mọi người");
        getListFriend();

        imbBackCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToAccount();
            }
        });

        cmvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        
        imbWidgetCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWidget();
            }
        });
    }
    private void openChat() {
        Intent intent = new Intent(MainPageActivity.this, ListChatRoomsActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        finish();
    }
    private void openWidget() {
        Intent intent = new Intent(MainPageActivity.this, WidgetActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        finish();
    }

    private void openProfile() {
        Intent intent = new Intent(MainPageActivity.this, ProfileActivity.class);
        if(user == null)
        {
            Toast.makeText(this, "Mạng không ổn định", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("user", user);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        finish();
    }

    private void backToAccount() {
        verticalViewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed()
    {
        int currentFragmentIndex = verticalViewPager.getCurrentItem();
        if (currentFragmentIndex == 0) {
            super.onBackPressed();
            finish();
        } else {
            verticalViewPager.setCurrentItem(0);
        }
    }

    private void getListFriend() {
        FirebaseUtils.Instance().getAllRequest()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Request request = ds.getValue(Request.class);
                            if(request.getState().equals("2")){
                                String uid = request.getIdreceive();
                                if (request.getIdreceive().equals(user.getUid())){
                                    uid = request.getIdsend();
                                }
                                listUid.add(uid);
                            }
                        }
                        Set<String> set = new HashSet<>(listUid);
                        listUid.clear();
                        listUid.addAll(set);
                        listUid.add(user.getUid());
                        listTemp.addAll(listUid);
                        createUserName();
                        getAdapter();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void createUserName() {
        for (String uid: listUid)
        {
            User nuser = new User();
            nuser.setUid(uid);
            FirebaseUtils.getName(nuser, MainPageActivity.this, new FirebaseUtils.UserNameCallback() {
                @Override
                public void onUserNameLoaded() {
                    listName.add(nuser.getFullName());
                    listMap.put(listName.get(listName.size()-1), uid);
                }
            });
        }

        if(list != null && list.get(0) instanceof AccountFragment) {
            ((AccountFragment) list.get(0)).setListName(listName);
        }

        adapterName = new ArrayAdapter<String>(this, R.layout.spinner_dropdown, listName);
        adapterName.setDropDownViewResource(R.layout.colorspinnerlayout);
        spinFriend.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!flag) {
                    flag = true;
                    return;
                }
                listUid.clear();
                listStorage.clear();
                while (list.size() > 1) {
                    list.remove(list.size() - 1);
                    pagerAdapter.notifyDataSetChanged();
                }
                if(position != 0) {
                    String name = spinFriend.getSelectedItem().toString();
                    listUid.add(listMap.get(name));
                }
                else if(position == 0)
                    listUid.addAll(listTemp);
                verticalViewPager.setCurrentItem(0);
                getAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinFriend.setAdapter(adapterName);
    }

    private void getAdapter() {
        imagesRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            // Lấy đường dẫn URL của ảnh
                            String[] parts = item.getName().split("_");
                            if(listUid.contains(parts[1])) {
                                try {
                                    Date date = format.parse(parts[0]);
                                    listStorage.put(date, item);
                                } catch (ParseException e) {
                                    Log.d("TAG_DATE", e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }
                        }

                        getImage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xảy ra lỗi khi lấy danh sách ảnh
                    }
                });
    }

    private void getImage() {
        keys = new ArrayList<>(listStorage.keySet());
        startFetchingUrls(0);
    }

    private void startFetchingUrls(final int index) {
        if (index >= keys.size()) {
            verticalViewPager.setAdapter(pagerAdapter);
            Integer position = getIntent().getIntExtra("index", 0);
            verticalViewPager.setCurrentItem(position);
            return;
        }
        listStorage.get(keys.get(index)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String[] parts = listStorage.get(keys.get(index)).getName().split("_");
                String uid = parts[1];
                String time = parts[0];
                if(uid.equals(user.getUid()))
                    list.add(new UserFragment(user, MainPageActivity.this, uri, time));
                else
                    list.add(new FriendFragment(user, MainPageActivity.this, uri, uid, time));
                pagerAdapter.notifyDataSetChanged();
                startFetchingUrls(index+1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Xảy ra lỗi khi lấy đường dẫn ảnh
            }
        });
    }
}
