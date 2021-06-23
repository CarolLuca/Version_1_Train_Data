public class Trois implements Comparable {
    private int start;
    private String entity;
    private String type;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Trois(int start, String entity, String type) {
        this.start = start;
        this.entity = entity;
        this.type = type;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null)
            return 1;
        if (!(o instanceof Trois))
            return 1;
        Trois t = (Trois) o;
        if (start < t.start)
            return -1;
        if (start > t.start)
            return 1;
        return 0;
    }
}
