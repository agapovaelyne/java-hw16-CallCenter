package callcenter;

public class Call implements Comparable<Call> {
    private int id;

    public Call(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Call №%d", id);
    }

    @Override
    public int compareTo(Call call) {
        return Integer.compare(id, call.id);
    }
}
