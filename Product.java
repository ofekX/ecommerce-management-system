public class Product {

    private String name;
    private double price;
    private int serialNum;
    private Category category;
    private double specialboxPrice;

    public enum Category {
        KIDS, ELECTRICITY, OFFICE, CLOTHING
    }

    public Product(String name, double price, int serialNum, Category category, double specialBoxPrice) {
        this.name = name;
        this.price = price;
        this.serialNum = serialNum;
        this.category = category;
        this.specialboxPrice = specialBoxPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getSpecialboxPrice() {
        return specialboxPrice;
    }

    public void setSpecialboxPrice(double specialboxPrice) {
        this.specialboxPrice = specialboxPrice;
    }

    public Product(Product other) {
        this.name = other.name;
        this.price = other.price;
        this.serialNum = other.serialNum;
        this.category = other.category;
        this.specialboxPrice = other.specialboxPrice;
    }
}
