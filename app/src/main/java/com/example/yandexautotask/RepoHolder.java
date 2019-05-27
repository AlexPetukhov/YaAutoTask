package com.example.yandexautotask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RepoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView userName, description;
    ImageView userImage;

    RepoHolder(View view){
        super(view);

        userName = view.findViewById(R.id.userName);
        description = view.findViewById(R.id.description);
        userImage = view.findViewById(R.id.userImage);
    }

    @Override
    public void onClick(View v) {

    }

}
