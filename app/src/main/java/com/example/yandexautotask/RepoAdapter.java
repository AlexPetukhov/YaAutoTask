package com.example.yandexautotask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoHolder>{
    private List<Repo> repoList = new ArrayList<>();
    private Context context;


    public RepoAdapter(Context context, List<Repo> repos) {
        repoList.addAll(repos);
        this.context = context;
    }

    public RepoAdapter(Context context) {
        this.context = context;
    }

    public void addAll(List<Repo> repos){
        for(Repo o : repos){
            this.add(o);
        }
    }

    private void add(Repo repo){
        repoList.add(repo);
        notifyItemInserted(repoList.size() - 1);
    }

    @Override
    public RepoHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_adapter, parent, false);
        return new RepoHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoHolder holder, int position) {
        final Repo repository = repoList.get(position);
        holder.userName.setText(repository.getFull_name());
        if(repository.getDescription()!=null)
            holder.description.setText(repository.getDescription());
        Glide.with(context)
                .load(repository.getOwner().getAvatar_url())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = repository.getHtml_url();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

}
