package team10.smartbell;

public class OrderedMenu {
    enum Type {
        Coffee, Tea, Dessert, Ice
    }

    private int     type;
    private String  name;
    private float   price;
    private String  description;
    private int count;

    public OrderedMenu (int type, String name, float price, String description) {
        this.type = type;
        this.name = name;
        this.price = price;
        this.description = description;
        this.count = 1;
    }

    public void addCount() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public OrderedMenu.Type getType() {
        return OrderedMenu.Type.values()[type - 1];
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

}
