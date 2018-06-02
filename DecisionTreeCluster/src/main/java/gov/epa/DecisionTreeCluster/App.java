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
        
        System.out.println(tree.first().toString());
        System.out.println(tree.last().toString());
    }
}
