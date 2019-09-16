public class Fruit extends MarketProduct {

    private double weight;
    private int price;

    public Fruit(String name, double weight, int price){
        super(name);
        this.weight = weight;
        this.price = price;
    }

    public int getCost(){
        int cost = (int) (this.weight*this.price);
        return cost;
    }

    public boolean equals(Object o){
        if (o instanceof Fruit && ((Fruit) o).getName().equals(this.getName()) && ((Fruit) o).price==this.price && ((Fruit) o).weight==this.weight){
            return true;
        }else{
            return false;
        }
    }



}
