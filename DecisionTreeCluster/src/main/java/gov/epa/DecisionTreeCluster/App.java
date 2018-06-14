package gov.epa.DecisionTreeCluster;

import java.util.Iterator;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Tree tree = new Tree(new ToxComp(), "LC50_training_set-2D.csv");
        System.out.println(tree.isEmpty());
        
        Iterator<Node> iter = tree.iterator();
        while (iter.hasNext()) {
        	Node node = iter.next();
        	System.out.println(node.getMean()+", "+node.getEntropy()+", "+node.getRecords().size());
        }
//        String[] dataNames = tree.getDataNames();
//        String total="";
//        for (int i=0; i< dataNames.length-1;i++) total+=dataNames[i]+", ";
//        total+=dataNames[dataNames.length-1]+"\n";
//        System.out.println(total);
//        System.out.println(iter.next().toString()); // first node
//        System.out.println(iter.next().toString()); // second node
//        System.out.println(iter.next().toString()); // third node
//        System.out.println(tree.last().toString()); // last node
    }
}
