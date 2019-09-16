public class Customer {
    private String name;
    private int balance;
    private Basket basket;

    public Customer(String name, int initialb){
       this.name = name;
       this.balance = initialb;
       this.basket = new Basket();
    }

    public String getName(){
        return this.name;
    }

    public int getBalance(){
        return this.balance;
    }

    public Basket getBasket(){
        return this.basket;
    }

    public int addFunds(int add){
        if (add<0){
            throw new IllegalArgumentException ("You cannot add an negative amount to the balance!");
        }else{
            this.balance += add;
            return this.balance;
        }
    }

    public void addToBasket(MarketProduct mp){
        this.basket.add(mp);
    }

    public boolean removeFromBasket(MarketProduct mp){
        boolean remove = this.basket.remove(mp);
        return remove;
    }

    public String checkOut(){
        if (this.balance<this.basket.getTotalCost()){
            throw new IllegalStateException("Can't afford this basket!");
        }else{
            this.balance -= this.basket.getTotalCost();
            String receipt = this.basket.toString();
            this.basket.clear();
            return receipt;
        }
    }

}
