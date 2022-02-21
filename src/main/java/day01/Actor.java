package day01;

public class Actor {
    private int id;
    private String actorName;

    public Actor(int id, String actorName) {
        this.id = id;
        this.actorName = actorName;
    }

    public int getId() {
        return id;
    }

    public String getActorName() {
        return actorName;
    }
}
