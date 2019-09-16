public class Basket {
    private MarketProduct[] mps;

    public Basket(){
        this.mps = new MarketProduct[0];
    }

    public MarketProduct[] getProducts(){
        MarketProduct[] mps2 = new MarketProduct[this.mps.length];
        for(int i=0; i<this.mps.length; i++){
            mps2[i] = this.mps[i];
        }
        return mps2;
    }

    public void add(MarketProduct mp){
        MarketProduct[] mps2 = new MarketProduct[this.mps.length+1];
        for (int i=0; i<this.mps.length; i++){
            mps2[i] =this.mps[i];
        }
        mps2[this.mps.length] = mp;
        this.mps = mps2;
    }

    public boolean remove(MarketProduct remove){
        for (int i=0; i<this.mps.length; i++){
            MarketProduct mp = this.mps[i];
            if (mp.equals(remove)){
                MarketProduct[] mp2 = new MarketProduct[this.mps.length-1];
                for (int j=0; j<i; j++){
                    mp2[j]=this.mps[j];
                }
                for (int k=i; k<this.mps.length-1;k++) {
                    mp2[k] = this.mps[k + 1];
                }
                this.mps=mp2;
                return true;
            }
        }
        return false;
    }

    public void clear(){
        this.mps=new MarketProduct[0];
    }

    public int getNumOfProducts(){
        return this.mps.length;
    }

    public int getSubTotal(){
        int total = 0;
        for (int i=0; i<this.mps.length; i++){
            MarketProduct mp = this.mps[i];
            total = total+mp.getCost();
        }
        return total;
    }

    public int getTotalTax(){
        double pre = 0;
        int tax = 0;
        for (int i=0; i<this.mps.length; i++){
            MarketProduct mp = this.mps[i];
            if (mp instanceof Jam){
                pre += mp.getCost()*0.15;
            }
        }
        tax = (int) pre;
        return tax;
    }

    public int getTotalCost(){
        return this.getSubTotal()+this.getTotalTax();
    }

    private String display(int i){
        String str = "-";
        if (i>0){
            double d = i/100.00;
            str = String.format("%.2f", d);
        }
        return str;
    }

    public String toString(){
        String str = "";
        for (int i=0; i<this.mps.length; i++){
            str += this.mps[i].getName()+"\t"+display(this.mps[i].getCost())+"\n";
        }
        str += "\n";
        str += "Subtotal \t"+display(this.getSubTotal()) +"\n";
        str += "Total Tax \t"+display(this.getTotalTax())+"\n";
        str += "\n";
        str += "Total Cost \t"+display(this.getTotalCost());
        return str;
    }

}
