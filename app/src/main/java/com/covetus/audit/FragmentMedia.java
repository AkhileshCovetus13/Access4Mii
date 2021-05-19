package com.covetus.audit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ABS_CUSTOM_VIEW.TextViewSemiBold;
import ABS_GET_SET.Audit;
import ABS_HELPER.DatabaseHelper;
import ABS_HELPER.FlowLayout;
import ABS_HELPER.PreferenceManager;

import static Modal.AuditListModal.funGetAllAudit;

public class FragmentMedia extends Fragment {

    FlowLayout mMediaFlowView;
    ImageView mImageBack;
    ArrayList<Audit> mListItems = new ArrayList<>();
    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        mMediaFlowView = view.findViewById(R.id.mMediaFlowView);
        mImageBack = view.findViewById(R.id.mImageBack);
        mImageBack.setVisibility(View.GONE);
        db = new DatabaseHelper(getActivity());
        mListItems = funGetAllAudit(PreferenceManager.getFormiiId(getActivity()), "1", "", "1", db);
        for (int i = 0; i < mListItems.size(); i++) {
            final View Imghidden = getActivity().getLayoutInflater().inflate(R.layout.item_media_grid, mMediaFlowView, false);
            ImageView mMediaImage = Imghidden.findViewById(R.id.mMediaImage);
            TextViewSemiBold mTxtFolderName = Imghidden.findViewById(R.id.mTxtFolderName);
            Glide.with(getActivity()).load(R.drawable.ic_folder).into(mMediaImage);
            mTxtFolderName.setText(mListItems.get(i).getmTitle());
            final int finalI = i;
            mMediaImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ActivityMediaList.class);
                    intent.putExtra("mAuditId", mListItems.get(finalI).getmAuditId());
                    startActivity(intent);
                }
            });

            mMediaFlowView.addView(Imghidden);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageBack.setVisibility(View.GONE);
    }
}