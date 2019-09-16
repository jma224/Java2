public class SeasonalFruit extends Fruit {

    public SeasonalFruit(String name, double weight, int price){
        super(name, weight, price);
    }

    public int getCost(){
        int cost = (int) (super.getCost()*0.85);
        return cost;
    }


}
