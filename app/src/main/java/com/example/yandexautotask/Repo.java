package com.example.yandexautotask;

public class Repo {
    private String full_name;
    private String description;
    private String html_url;
    private Owner owner;

    public Owner getOwner() {
        return owner;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getDescription() {
        return description;
    }

    public String getHtml_url() {
        return html_url;
    }

}
