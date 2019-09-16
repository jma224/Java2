//* Jingyan Ma *//

import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

    DTNode rootDTNode;
    int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split
    //Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
    public static final long serialVersionUID = 343L;
    public DecisionTree(ArrayList<Datum> datalist , int min) {
        minSizeDatalist = min;
        rootDTNode = (new DTNode()).fillDTNode(datalist);
    }

    class DTNode implements Serializable{
        //Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
        public static final long serialVersionUID = 438L;
        boolean leaf;
        int label = -1;      // only defined if node is a leaf
        int attribute; // only defined if node is not a leaf
        double threshold;  // only defined if node is not a leaf



        DTNode left, right; //the left and right child of a particular node. (null if leaf)

        DTNode() {
            leaf = true;
            threshold = Double.MAX_VALUE;
        }


        ArrayList<Datum> leftSplit(ArrayList<Datum> datalist, int attribute, double threshold){
            ArrayList<Datum> leftList = new ArrayList<Datum>();
            for (int i=0; i<datalist.size(); i++){
                Datum data = datalist.get(i);
                if(data.x[attribute]<threshold){
                    leftList.add(data);
                }
            }
            return leftList;
        }

        ArrayList<Datum> rightSplit(ArrayList<Datum> datalist, int attribute, double threshold){
            ArrayList<Datum> rightList = new ArrayList<Datum>();
            for (int i=0; i<datalist.size(); i++){
                Datum data = datalist.get(i);
                if(data.x[attribute]>=threshold){
                    rightList.add(data);
                }
            }
            return rightList;
        }

        double[] findBestSplit(ArrayList<Datum> datalist){
            double bestAve = Double.MAX_VALUE;
            int bestAttr = -1;
            double bestThres = -1;

            for(int i=0; i<2; i++){
                for (Datum data:datalist){
                    ArrayList<Datum> leftList = leftSplit(datalist, i, data.x[i]);
                    ArrayList<Datum> rightList = rightSplit(datalist, i, data.x[i]);
                    double e1 = calcEntropy(leftList);
                    double e2 = calcEntropy(rightList);
                    double w1 = (double)leftList.size()/datalist.size();
                    double w2 = (double)rightList.size()/datalist.size();
                    double h= w1*e1+w2*e2;
                    if(h<bestAve){
                        bestAve = h;
                        bestAttr = i;
                        bestThres = data.x[i];
                    }
                }
            }
            double[] arr = {bestAttr,bestThres};
            return arr;
        }


        // this method takes in a datalist (ArrayList of type datum) and a minSizeInClassification (int) and returns
        // the calling DTNode object as the root of a decision tree trained using the datapoints present in the
        // datalist variable
        // Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
        DTNode fillDTNode(ArrayList<Datum> datalist) {

            //YOUR CODE HERE
            if(datalist.isEmpty()){
                return null;
            }
            DTNode node = new DTNode();
            int size = datalist.size();
            if(size >= minSizeDatalist){
                boolean equal = true;
                int labeltest = datalist.get(0).y;
                for (Datum data:datalist){
                    if(data.y != labeltest){
                        equal = false;
                        break;
                    }
                }
                if(equal){
                    node.label = labeltest;
                    return node;
                }else{
                    double[] arr = findBestSplit(datalist);
                    node.leaf = false;
                    node.attribute = (int)arr[0];
                    node.threshold = arr[1];
                    ArrayList<Datum> leftList = leftSplit(datalist, node.attribute, node.threshold);
                    ArrayList<Datum> rightList = rightSplit(datalist, node.attribute, node.threshold);
                    node.left = fillDTNode(leftList);
                    node.right = fillDTNode(rightList);
                    return node;
                }
            }else{
                int mlabel = findMajority(datalist);
                node.label = mlabel;
                return node;
            }
        }



        //This is a helper method. Given a datalist, this method returns the label that has the most
        // occurences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
        int findMajority(ArrayList<Datum> datalist)
        {
            int l = datalist.get(0).x.length;
            int [] votes = new int[l];

            //loop through the data and count the occurrences of datapoints of each label
            for (Datum data : datalist)
            {
                votes[data.y]+=1;
            }
            int max = -1;
            int max_index = -1;
            //find the label with the max occurrences
            for (int i = 0 ; i < l ;i++)
            {
                if (max<votes[i])
                {
                    max = votes[i];
                    max_index = i;
                }
            }
            return max_index;
        }



        // This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
        // returns its corresponding label, as determined by the decision tree
        int classifyAtNode(double[] xQuery) {
            //YOUR CODE HERE
            int label = -1;
            DTNode dt = this;
            while (dt.leaf == false){
                if(xQuery[dt.attribute] < dt.threshold){
                    dt = dt.left;
                }
                if(xQuery[dt.attribute] >= dt.threshold){
                    dt = dt.right;
                }
            }
            label = dt.label;
            return label;

        }


        private boolean compareNodes(DTNode dt1, DTNode dt2) {
            if (dt1 == null && dt2 == null) {
                return true;
            } else if (dt1 != null && dt2 != null) {
                if (dt1.label == dt2.label && dt1.attribute == dt2.attribute && dt1.threshold == dt2.threshold) {
                    return (compareNodes(dt1.left, dt2.left) && compareNodes(dt1.right, dt2.right));
                }
                return false;
            }
            return false;
        }


        //given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
        //at DTNode object passed as the parameter
        public boolean equals(Object dt2)
        {
            //YOUR CODE HERE
            if (!(dt2 instanceof DTNode)){
                System.out.println("The input must be a DTNode!");
                return false;
            }
            DTNode dtn1 = rootDTNode;
            DTNode dtn2 = (DTNode)dt2;

            return (compareNodes(dtn1,dtn2));
        }
    }



    //Given a dataset, this retuns the entropy of the dataset
    double calcEntropy(ArrayList<Datum> datalist)
    {
        double entropy = 0;
        double px = 0;
        float [] counter= new float[2];
        if (datalist.size()==0)
            return 0;
        double num0 = 0.00000001,num1 = 0.000000001;

        //calculates the number of points belonging to each of the labels
        for (Datum d : datalist)
        {
            counter[d.y]+=1;
        }
        //calculates the entropy using the formula specified in the document
        for (int i = 0 ; i< counter.length ; i++)
        {
            if (counter[i]>0)
            {
                px = counter[i]/datalist.size();
                entropy -= (px*Math.log(px)/Math.log(2));
            }
        }

        return entropy;
    }


    // given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
    int classify(double[] xQuery ) {
        DTNode node = this.rootDTNode;
        return node.classifyAtNode( xQuery );
    }

    // Checks the performance of a DecisionTree on a dataset
    //  This method is provided in case you would like to compare your
    //results with the reference values provided in the PDF in the Data
    //section of the PDF

    String checkPerformance( ArrayList<Datum> datalist)
    {
        DecimalFormat df = new DecimalFormat("0.000");
        float total = datalist.size();
        float count = 0;

        for (int s = 0 ; s < datalist.size() ; s++) {
            double[] x = datalist.get(s).x;
            int result = datalist.get(s).y;
            if (classify(x) != result) {
                count = count + 1;
            }
        }

        return df.format((count/total));
    }


    //Given two DecisionTree objects, this method checks if both the trees are equal by
    //calling onto the DTNode.equals() method
    public static boolean equals(DecisionTree dt1,  DecisionTree dt2)
    {
        boolean flag = true;
        flag = dt1.rootDTNode.equals(dt2.rootDTNode);
        return flag;
    }

}
