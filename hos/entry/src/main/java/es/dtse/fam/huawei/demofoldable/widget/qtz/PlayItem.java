package es.dtse.fam.huawei.demofoldable.widget.qtz;

public class PlayItem {
    private static PlayItem instance;
    private String title;
    private String name;
    private String duration;
    private PlayerDelegate delegate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public PlayerDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(PlayerDelegate delegate) {
        this.delegate = delegate;
    }

    public static PlayItem getInstance(){
        if (instance == null){
            instance = new PlayItem();
        }
        return instance;
    }
}
