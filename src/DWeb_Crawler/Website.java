package DWeb_Crawler;

public class Website {
    private String title;
    private String link;
    private String description;

    public Website(String title, String link, String description){
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }


    @Override
    public String toString() {
        return "[Title]: " +this.title + " \n   [Link]: " + this.link;
    }
}
