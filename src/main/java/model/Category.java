package model;

public class Category {

    private final Long id;
    private final String name;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
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
