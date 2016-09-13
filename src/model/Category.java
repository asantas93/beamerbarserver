package model;

public class Category {

    private final Integer id;
    private final String name;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Category && ((Category) other).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
