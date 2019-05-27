package com.example.yandexautotask;

import java.util.ArrayList;

class RepoObject {
    private ArrayList<Repo> items;
    private int total_count;

    ArrayList<Repo> getItems(){
        return items;
    }

    int getCount(){ return total_count; }

}
