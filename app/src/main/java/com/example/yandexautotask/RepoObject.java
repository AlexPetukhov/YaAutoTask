package com.example.yandexautotask;

import java.util.ArrayList;

class RepoObject {
    private ArrayList<Repo> items;
    private int total_count;

    public ArrayList<Repo> getItems(){
        return items;
    }

    public int getCount(){ return total_count; }

}
