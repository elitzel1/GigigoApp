package com.cert.eli.gigigoapp.Utils;

/**
 * Created by eli on 05/08/15.
 */
public class Photo {

    String title;
    String type;
    String url;
    String description;
    String ups;
    String downs;
    String score;



    public Photo(String title,String url,String description,String ups,String downs,String score){
        this.title = title;
        this.url = url;
        this.description = description;
        this.ups=ups;
        this.downs = downs;
        this.score =score;

    }

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getDowns() {
        return downs;
    }

    public void setDowns(String downs) {
        this.downs = downs;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
