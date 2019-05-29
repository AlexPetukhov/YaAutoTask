package com.example.yandexautotask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RepoAdapter repoAdapter;
    private int pageToLoad = 1;
    private int pages;
    private final int pagesPerLoad = 30;
    private String queryBase;
    private boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setNestedScrollingEnabled(false);

        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Search repositories");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                    searchView.clearFocus();
                }
                getRepos(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    if (!loading && pageToLoad <= pages) loadRepos();
                }
            }
        });

    }

    public void loadRepos(){
        loading = true;

        showProgressBar(searchView, MainActivity.this);
        String sPage = Integer.toString(pageToLoad);

        new asyncSearch().execute(queryBase, sPage);
    }

    public void getRepos(String query){
        queryBase = query;
        loading = false;
        pageToLoad = 1;
        pages = 0;

        repoAdapter = new RepoAdapter(MainActivity.this);
        recyclerView.setAdapter(repoAdapter);

        showProgressBar(searchView, MainActivity.this);

        new asyncSearch().execute(query);

        recyclerView.setVisibility(View.VISIBLE);
    }

    public void processRepos(List<Repo> repos){
        if(repos != null ){
            if(pageToLoad == 1 && repos.size() == 0){
                Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();
            }else {
                repoAdapter.addAll(repos);
                pageToLoad++;
            }
            hideProgressBar(searchView);
        }else{
            Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();
        }
    }

    public class asyncSearch extends AsyncTask<String, Void, RepoObject > {
        private Exception exception;
        protected RepoObject doInBackground(String... query) {
            try {
                RepoObject repoObject;
                if(query.length == 2) {
                    repoObject = API.load(query[0], query[1]);
                }
                else {
                    repoObject = API.get(query[0]);
                }
                return repoObject;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(RepoObject repoObject) {
            loading = false;
            if(!(repoObject == null)) {
                pages = repoObject.getCount();
                if (pages % pagesPerLoad == 0) pages /= pagesPerLoad;
                else pages = pages / pagesPerLoad + 1;
                processRepos(repoObject.getItems());
            }
        }
    }


    public void showProgressBar(SearchView searchView, Context context) {
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        if (searchView.findViewById(id).findViewById(R.id.search_progress_bar) != null) {
            searchView.findViewById(id).findViewById(R.id.search_progress_bar).animate().setDuration(200).alpha(1).start();
        }
        else {
            View v = LayoutInflater.from(context).inflate(R.layout.loading_icon, null);
            ((ViewGroup) searchView.findViewById(id)).addView(v, 1);
        }
    }
    public void hideProgressBar(SearchView searchView) {
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        if (searchView.findViewById(id).findViewById(R.id.search_progress_bar) != null) {
            searchView.findViewById(id).findViewById(R.id.search_progress_bar).animate().setDuration(200).alpha(0).start();
        }
    }

}
