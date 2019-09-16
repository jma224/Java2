public class Egg extends MarketProduct {

    private int num;
    private int price;

    public Egg(String name, int num, int price){
        super(name);
        this.num = num;
        this.price = price;
    }

    public int getCost(){
        int cost = (int)(this.price/12.0*this.num);
        return cost;
    }

    public boolean equals(Object o){
        if (o instanceof Egg && ((Egg) o).getName().equals(this.getName()) && ((Egg) o).price==this.price && ((Egg) o).num==this.num){
            return true;
        }else{
            return false;
        }
    }


}
