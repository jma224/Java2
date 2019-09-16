//* Jingyan Ma *//
import java.math.BigInteger;
import java.util.Iterator;

public class Polynomial
{
    private SLinkedList<Term> polynomial;
    public int size()
    {
        return polynomial.size();
    }
    private Polynomial(SLinkedList<Term> p)
    {
        polynomial = p;
    }

    public Polynomial()
    {
        polynomial = new SLinkedList<Term>();
    }

    // Returns a deep copy of the object.
    public Polynomial deepClone()
    {
        return new Polynomial(polynomial.deepClone());
    }

    /*
     * TODO: Add new term to the polynomial. Also ensure the polynomial is
     * in decreasing order of exponent.
     */
    public void addTerm(Term t)
    {
        /**** ADD CODE HERE ****/
        if(t.getExponent()<0 || t.getCoefficient().compareTo(new BigInteger("0"))==0){

        }else if (polynomial.isEmpty()) {
            polynomial.addLast(t);//
        }else{
            int i=0;
            for (Term currTerm: polynomial) {
                int expo = t.getExponent();
                int expo2 = currTerm.getExponent();
                if (expo2 == expo) {
                    BigInteger big = t.getCoefficient().add(currTerm.getCoefficient());
                    currTerm.setCoefficient(big);
                    if (big.compareTo(new BigInteger("0")) == 0) {
                        polynomial.remove(i);
                    }
                    break;
                }else if (expo2 < expo) {
                    polynomial.add(i, t);
                    break;
                }else{
                    i++;
                    if(i==this.size()){
                        polynomial.addLast(t);
                    }
                }
            }
        }

        // Hint: Notice that the function SLinkedList.get(index) method is O(n),
        // so if this method were to call the get(index)
        // method n times then the method would be O(n^2).
        // Instead, use a Java enhanced for loop to iterate through
        // the terms of an SLinkedList.
		/*
		for (Term currentTerm: polynomial)
		{
			// The for loop iterates over each term in the polynomial!!
			// Example: System.out.println(currentTerm.getExponent()) should print the exponents of each term in the polynomial when it is not empty.
		}
		*/
    }

    public Term getTerm(int index)
    {
        return polynomial.get(index);
    }

    //TODO: Add two polynomial without modifying either
    public static Polynomial add(Polynomial p1, Polynomial p2)
    {
        /**** ADD CODE HERE ****/
        if(p1.polynomial.isEmpty()){
            return p2;
        }
        if(p2.polynomial.isEmpty()){
            return p1;
        }
        Iterator<Term> iter1 = p1.polynomial.iterator();
        Iterator<Term> iter2 = p2.polynomial.iterator();
        Term curr1 = iter1.next();
        Term curr2 = iter2.next();
        Polynomial add = new Polynomial();
        int index1 = 0;
        int index2 = 0;
        while (index1<p1.size() && index2<p2.size()){
            int expo1 = curr1.getExponent();
            int expo2 = curr2.getExponent();
            if(expo1 != expo2){
                if(expo1>expo2){
                    add.polynomial.addLast(curr1.deepClone());
                    if (iter1.hasNext()) {
                        curr1 = iter1.next();
                    }
                    index1++;
            }
                if(expo1<expo2){
                    add.polynomial.addLast(curr2.deepClone());
                    if (iter2.hasNext()) {
                        curr2 = iter2.next();
                    }
                    index2++;
                }
            }else{
                BigInteger big = curr1.getCoefficient().add(curr2.getCoefficient());
                if (big.compareTo(new BigInteger("0")) == 0){

                }else{
                    Term temp = curr1.deepClone();
                    temp.setCoefficient(big);
                    add.polynomial.addLast(temp);
                }
                if (iter1.hasNext()) {
                    curr1 = iter1.next();
                }
                if (iter2.hasNext()) {
                    curr2 = iter2.next();
                }
                index1++;
                index2++;
            }
        }
        while(index1<p1.size()){
            add.polynomial.addLast(curr1.deepClone());
            if (iter1.hasNext()) {
                curr1 = iter1.next();
            }
            index1++;
        }
        while(index2<p2.size()){
            add.polynomial.addLast(curr2.deepClone());
            if (iter2.hasNext()) {
                curr2 = iter2.next();
            }
            index2++;
        }
        return add;
    }

    //TODO: multiply this polynomial by a given term.
    private void multiplyTerm(Term t)
    {
        /**** ADD CODE HERE ****/

        if (t.getCoefficient().compareTo(new BigInteger("0"))==0){
            polynomial.clear();
        }else {
            for (Term t2: polynomial) {
                t2.setCoefficient(t2.getCoefficient().multiply(t.getCoefficient()));
                t2.setExponent(t2.getExponent() + t.getExponent());
            }
        }
    }

    //TODO: multiply two polynomials
    public static Polynomial multiply(Polynomial p1, Polynomial p2)
    {
        /**** ADD CODE HERE ****/
        Polynomial multiply = new Polynomial();
        if(p1.polynomial.isEmpty() || p2.polynomial.isEmpty()){
            return multiply;
        }

        for (Term term: p2.polynomial){
            Polynomial dc = p1.deepClone();
            dc.multiplyTerm(term);
            multiply = add(multiply, dc);

        }
        return multiply;
    }

    //TODO: evaluate this polynomial.
    // Hint:  The time complexity of eval() must be order O(m),
    // where m is the largest degree of the polynomial. Notice
    // that the function SLinkedList.get(index) method is O(m),
    // so if your eval() method were to call the get(index)
    // method m times then your eval method would be O(m^2).
    // Instead, use a Java enhanced for loop to iterate through
    // the terms of an SLinkedList.

    public BigInteger eval(BigInteger x)
    {
        /**** ADD CODE HERE ****/
        if (polynomial.isEmpty()){
            return new BigInteger("0");
        }else{
            Iterator<Term> iter = polynomial.iterator();
            Term curr = iter.next();
            int highest = curr.getExponent();
            int expo = highest;
            BigInteger[] poly = new BigInteger[highest+1];
            int index = 0;
            while(expo>=0){

                if(curr.getExponent()==expo){

                    poly[index] = curr.getCoefficient();

                    if (iter.hasNext()) {
                       curr = iter.next();
                    }
                }else{
                    poly[index] = new BigInteger("0");

                }
                index++;
                expo--;
            }


            BigInteger result = poly[0];
            for (int i=1; i<highest+1; i++){
                result = (result.multiply(x)).add(poly[i]);
            }
            return result;
        }
    }

    // Checks if this polynomial is same as the polynomial in the argument.
    // Used for testing whether two polynomials have same content but occupy disjoint space in memory.
    // Do not change this code, doing so may result in incorrect grades.
    public boolean checkEqual(Polynomial p)
    {
        // Test for null pointer exceptions!!
        // Clearly two polynomials are not same if they have different number of terms
        if (polynomial == null || p.polynomial == null || size() != p.size())
            return false;

        int index = 0;
        // Simultaneously traverse both this polynomial and argument.
        for (Term term0 : polynomial)
        {
            // This is inefficient, ideally you'd use iterator for sequential access.
            Term term1 = p.getTerm(index);

            if (term0.getExponent() != term1.getExponent() || // Check if the exponents are not same
                    term0.getCoefficient().compareTo(term1.getCoefficient()) != 0 || // Check if the coefficients are not same
                    term1 == term0) // Check if the both term occupy same memory location.
                return false;

            index++;
        }
        return true;
    }

    // This method blindly adds a term to the end of LinkedList polynomial.
    // Avoid using this method in your implementation as it is only used for testing.
    // Do not change this code, doing so may result in incorrect grades.
    public void addTermLast(Term t)
    {
        polynomial.addLast(t);
    }

    // This is used for testing multiplyTerm.
    // Do not change this code, doing so may result in incorrect grades.
    public void multiplyTermTest(Term t)
    {
        multiplyTerm(t);
    }

    @Override
    public String toString()
    {
        if (polynomial.size() == 0) return "0";
        return polynomial.toString();
    }
}
