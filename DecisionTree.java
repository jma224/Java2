//Name: Liu Jiewen ID: 260825295

import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;

public class DecisionTree implements Serializable {

    DTNode rootDTNode;
    int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split
    //Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
    public static final long serialVersionUID = 343L;

    public DecisionTree(ArrayList<Datum> datalist, int min) {
        minSizeDatalist = min;
        rootDTNode = (new DTNode()).fillDTNode(datalist);
    }

    class DTNode implements Serializable {
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


        // this method takes in a datalist (ArrayList of type datum) and a minSizeInClassification (int) and returns
        // the calling DTNode object as the root of a decision tree trained using the datapoints present in the
        // datalist variable
        // Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
        DTNode fillDTNode(ArrayList<Datum> datalist) {

            //YOUR CODE HERE
            DTNode filled_node = new DTNode();

            if (datalist.size() >= minSizeDatalist) {
                int lable_check = datalist.get(0).y;
                boolean equality_check = true;
                for (Datum abc : datalist) {
                    if (abc.y != lable_check) {
                        equality_check = false;
                        break;
                    }
                }
                if (equality_check) {
                    filled_node.label = datalist.get(0).y;
                    filled_node.leaf = true;
                    return filled_node;
                } else {
                    double etro = Double.MAX_VALUE;
                    int attri = -1;
                    int thres = -1;
                    ArrayList<Datum> l = new ArrayList<>();
                    ArrayList<Datum> r = new ArrayList<>();

                    int[] info = findGoodSplit(datalist);
                    l = splitLowArray(datalist, info[1], info[0]);
                    r = splitUpArray(datalist, info[1], info[0]);
                    attri = info[0];
                    thres = info[1];
                    filled_node.attribute = attri;
                    filled_node.threshold = thres;
                    filled_node.left = fillDTNode(l);
                    filled_node.right = fillDTNode(r);
                    return filled_node;
                }
            }
            if (datalist.isEmpty()) {
                return null;
            } else {
                filled_node.label = findMajority(datalist);
                return filled_node;
            }
        }

        ArrayList<Datum> splitLowArray(ArrayList<Datum> dataList, int split, int position) {
            ArrayList<Datum> lowDataList = new ArrayList<>();
            for (Datum abc : dataList) {
                if (abc.x[position] < split) {
                    lowDataList.add(abc);
                }
            }
            return lowDataList;
        }

        ArrayList<Datum> splitUpArray(ArrayList<Datum> dataList, int split, int position) {
            ArrayList<Datum> upDataList = new ArrayList<>();
            for (Datum abc : dataList) {
                if (abc.x[position] >= split) {
                    upDataList.add(abc);
                }
            }
            return upDataList;
        }


        int[] findGoodSplit(ArrayList<Datum> dataList) {
            double etro = Double.MAX_VALUE;
            int[] dataa = new int[2];
            int record1 = -1;
            int record2 = -1;
            ArrayList<Datum> lowDataList, upDataList;
            for (int i = 0; i < 2; i++) {
                for (Datum abc : dataList) {
                    lowDataList = splitLowArray(dataList, (int) abc.x[i], i);
                    upDataList = splitUpArray(dataList, (int) abc.x[i], i);
                    double w1 = (double) lowDataList.size() / dataList.size();
                    double w2 = (double) upDataList.size() / dataList.size();
                    double temp = calcEntropy(lowDataList) * w1 + calcEntropy(upDataList) * w2;
                    if (temp < etro) {
                        etro = temp;
                        record1 = i;
                        record2 = (int) abc.x[i];
                    }
                }
                dataa[0] = record1;
                dataa[1] = record2;
            }
            return dataa;
        }


        //This is a helper method. Given a datalist, this method returns the label that has the most
        // occurences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.

        int findMajority(ArrayList<Datum> datalist) {
            int l = datalist.get(0).x.length;
            int[] votes = new int[l];

            //loop through the data and count the occurrences of datapoints of each label
            for (Datum data : datalist) {
                votes[data.y] += 1;
            }
            int max = -1;
            int max_index = -1;
            //find the label with the max occurrences
            for (int i = 0; i < l; i++) {
                if (max < votes[i]) {
                    max = votes[i];
                    max_index = i;
                }
            }
            return max_index;
        }


        // This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
        // returns its corresponding label, as determined by the decision tree
        int classifyAtNode(double[] xQuery) {
            //YOUR CODE HEREN
            int r_label = -1;
            DTNode temp = rootDTNode;
            while (temp.label == -1) {
                if (xQuery[temp.attribute] >= temp.threshold) {
                    temp = temp.right;
                    continue;
                }
                if (xQuery[temp.attribute] < temp.threshold) {
                    temp = temp.left;
                    continue;
                }
            }
            r_label = temp.label;
            return r_label;
        }

                public boolean compareNode(DTNode a_node, DTNode b_node) {
                    if (a_node == null && b_node == null)
                        return true;
                    if (a_node != null && b_node != null) {
                        if (a_node.label == b_node.label && a_node.attribute == b_node.attribute && Math.abs(a_node.threshold - b_node.threshold) < 0.00001) {
                            return (compareNode(a_node.left, b_node.left) && compareNode(a_node.right, b_node.right));
                        }
                        return false;
                    }
                    return false;
                }
                //given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
                //at DTNode object passed as the parameter
                public boolean equals(Object dt2) {
                    //YOUR CODE HERE
                    DTNode a_node = rootDTNode;
                    DTNode b_node = (DTNode) dt2;
                    return compareNode(a_node, b_node);
                }
            }



            //Given a dataset, this retuns the entropy of the dataset
            double calcEntropy(ArrayList<Datum> datalist) {
                double entropy = 0;
                double px = 0;
                float[] counter = new float[2];
                if (datalist.size() == 0)
                    return 0;
                double num0 = 0.00000001, num1 = 0.000000001;

                //calculates the number of points belonging to each of the labels
                for (Datum d : datalist) {
                    counter[d.y] += 1;
                }
                //calculates the entropy using the formula specified in the document
                for (int i = 0; i < counter.length; i++) {
                    if (counter[i] > 0) {
                        px = counter[i] / datalist.size();
                        entropy -= (px * Math.log(px) / Math.log(2));
                    }
                }

                return entropy;
            }


            // given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
            int classify(double[] xQuery) {
                DTNode node = this.rootDTNode;
                return node.classifyAtNode(xQuery);
            }

            // Checks the performance of a DecisionTree on a dataset
            //  This method is provided in case you would like to compare your
            //results with the reference values provided in the PDF in the Data
            //section of the PDF

            String checkPerformance(ArrayList<Datum> datalist) {
                DecimalFormat df = new DecimalFormat("0.000");
                float total = datalist.size();
                float count = 0;

                for (int s = 0; s < datalist.size(); s++) {
                    double[] x = datalist.get(s).x;
                    int result = datalist.get(s).y;
                    if (classify(x) != result) {
                        count = count + 1;
                    }
                }

                return df.format((count / total));
            }


            //Given two DecisionTree objects, this method checks if both the trees are equal by
            //calling onto the DTNode.equals() method
            public static boolean equals(DecisionTree dt1, DecisionTree dt2) {
                boolean flag = true;
                flag = dt1.rootDTNode.equals(dt2.rootDTNode);
                return flag;
            }

        }