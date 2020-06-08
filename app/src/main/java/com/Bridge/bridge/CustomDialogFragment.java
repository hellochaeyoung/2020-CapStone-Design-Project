package com.Bridge.bridge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.victor.loading.rotate.RotateLoading;

public class CustomDialogFragment extends DialogFragment {

    String dialogMessage = "no";
    private static final String TAG = "CustomDialogFragment";
    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";

    public static CustomDialogFragment newInstance(String mainMsg){
        Bundle bundle = new Bundle();
        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(getArguments()!=null){
            dialogMessage = getArguments().getString(ARG_DIALOG_MAIN_MSG);
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Loading").setIcon(R.drawable.powerpoint);

        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
       // ((TextView)view.findViewById(R.id.dialogText)).setText(dialogMessage);
        RotateLoading rotateLoading = view.findViewById(R.id.rotateloading);
        rotateLoading.start();
        builder.setView(view);

        return builder.create();
       // Bundle bundle = getArguments();
//        if (bundle != null) {
//            dialogMessage = bundle.getString("dialog");
//
//
//        }



    }
    public void dismissDialog(){
        this.dismiss();;
    }

}
