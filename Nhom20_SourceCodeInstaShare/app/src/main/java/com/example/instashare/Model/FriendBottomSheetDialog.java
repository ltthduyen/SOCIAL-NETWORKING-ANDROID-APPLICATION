package com.example.instashare.Model;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class FriendBottomSheetDialog extends BottomSheetDialog {
    private Context context;
    public FriendBottomSheetDialog(Context context) {
        super(context);
        this.context = context;
    }
}
