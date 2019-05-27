package com.example.yandexautotask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    RepoAdapter repoAdapter;
    Button btn;
    TextView tView;
    int pageToLoad = 1;
    int pages;
    String queryBase;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = findViewById(R.id.btn);
        tView = findViewById(R.id.textView);

        recyclerView = findViewById(R.id.search_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setNestedScrollingEnabled(false);
//        repoAdapter = new RepoAdapter(this);
//        recyclerView.setAdapter(repoAdapter);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loading && pageToLoad <= pages) loadRepos();
//                else Toast.makeText(MainActivity.this, "Loading... " , Toast.LENGTH_SHORT).show();
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
        String sPage = Integer.toString(pageToLoad);
        System.out.println("LOADING MORE ITEMS");
        System.out.println("Page number " + sPage);
        new asyncSearch().execute(queryBase, sPage);
        btn.setText(Integer.toString(pageToLoad));
//        Toast.makeText(MainActivity.this, "Loaded pages: " + pageToLoad, Toast.LENGTH_SHORT).show();
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
//            && repos.size() > 0
            System.out.println("SIZE OF LIST " + repos.size());
            System.out.println("SIZE OF LIST " + repos.size());
            System.out.println("SIZE OF LIST " + repos.size());
            System.out.println("SIZE OF LIST " + repos.size());

            repoAdapter.addAll(repos);

            System.out.println("ADDED REPOS  " + repos.size());
            System.out.println("ADDED REPOS  " + repos.size());
            System.out.println("ADDED REPOS  " + repos.size());
            System.out.println("ADDED REPOS  " + repos.size());
            pageToLoad++;
            hideProgressBar(searchView);
        }else{
            Toast.makeText(this, "No Data Found", Toast.LENGTH_LONG).show();
            recyclerView.setVisibility(View.GONE);
        }
    }


    class asyncSearch extends AsyncTask<String, Void, RepoObject > {
        private Exception exception;
        protected RepoObject doInBackground(String... query) {
            try {
                System.out.println("SEARCHING FOR REPOSITORIES ");
                RepoObject repoObject;
                System.out.println("QUERY : " + query[0]);
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
            if(repoObject == null){
                Toast.makeText(MainActivity.this, "No Data Found(repoObj == null)", Toast.LENGTH_LONG).show();

//                recyclerView.setVisibility(View.GONE);
            }else{
                pages = repoObject.getCount();
                if(pages % 30 == 0)pages /= 30;
                else pages = pages / 30 + 1;
                String s = Integer.toString(pages);
                s += " pages";
                tView.setText(s);
                processRepos(repoObject.getItems());
            }
        }
    }

//    class asyncLoad extends AsyncTask<String, Void, RepoObject > {
//        private Exception exception;
//        protected RepoObject doInBackground(String... query) {
//            try {
//                System.out.println("SEARCHING FOR REPOSITORIES " + query[1]);
//                RepoObject repoObject = API.load(query[0], query[1]);
//                return repoObject;
//            } catch (Exception e) {
//                this.exception = e;
//                return null;
//            }
//        }
//
//        protected void onPostExecute(RepoObject repoObject) {
//            if(repoObject == null){
//                Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_LONG).show();
//                recyclerView.setVisibility(View.GONE);
//            }else{
//                processRepos(repoObject.getItems());
//            }
//
//        }
//    }


    public void showProgressBar(SearchView searchView, Context context) {
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        if (searchView.findViewById(id).findViewById(R.id.search_progress_bar) != null)
            searchView.findViewById(id).findViewById(R.id.search_progress_bar).animate().setDuration(200).alpha(1).start();

        else
        {
            View v = LayoutInflater.from(context).inflate(R.layout.loading_icon, null);
            ((ViewGroup) searchView.findViewById(id)).addView(v, 1);
        }
    }
    public void hideProgressBar(SearchView searchView) {
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        if (searchView.findViewById(id).findViewById(R.id.search_progress_bar) != null)
            searchView.findViewById(id).findViewById(R.id.search_progress_bar).animate().setDuration(200).alpha(0).start();
    }

}
