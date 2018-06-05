package gov.epa.DecisionTreeCluster;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Tree tree = new Tree("LC50_training_set-2D.csv");
        System.out.println(tree.isEmpty());
        
        String[] dataNames = tree.getDataNames();
        String total="";
        for (int i=0; i< dataNames.length-1;i++) total+=dataNames[i]+", ";
        total+=dataNames[dataNames.length-1]+"\n";
        
        System.out.println(total);
        System.out.println(tree.first().toString());
        System.out.println(tree.last().toString());
    }
}
