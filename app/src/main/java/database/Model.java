package database;

public class Model {

    private int key;
    private String image, title, description, source, guid, pub_date;

    public void setKey(int key){
        this.key = key;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public int getKey(){
        return key;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getGuid() {
        return guid;
    }

    public String getPub_date() {
        return pub_date;
    }
}
