package pl.szkiladz;

public class Purchase implements Comparable<Purchase> {
    private final String name;
    private final double price;
    private final Category category;

    public Purchase(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    @Override
    public String toString() {
        return String.format("%s $%.2f", name, price);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }


    @Override
    public int compareTo(Purchase o) {
        return Double.compare(o.getPrice(), price);
    }
}
