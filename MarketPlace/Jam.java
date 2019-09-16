public class Jam extends MarketProduct {

    private int num;
    private int price;

    public Jam(String name, int num, int price) {
        super(name);
        this.num = num;
        this.price = price;
    }

    public int getCost() {
        int cost = this.num * this.price;
        return cost;
    }

    public boolean equals(Object o) {
        if (o instanceof Jam && ((Jam) o).getName().equals(this.getName()) && ((Jam) o).price == this.price && ((Jam) o).num == this.num) {
            return true;
        } else {
            return false;
        }
    }
}





