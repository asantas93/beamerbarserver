package model;

public class Ingredient {

    private final Long id;
    private final String name;
    private final Boolean inStock;
    private final Double pricePerUnit;

    public Ingredient(Long id, String name, Boolean inStock, Double pricePerUnit) {
        this.id = id;
        this.name = name;
        this.inStock = inStock;
        this.pricePerUnit = pricePerUnit;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean inStock() {
        return inStock;
    }

    public Double getCostForQuantity(double units) {
        return pricePerUnit * units / 1.5;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Ingredient && ((Ingredient) other).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
