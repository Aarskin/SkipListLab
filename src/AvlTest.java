import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AvlTest {

static AvlTree tree;
/**
* @param args
*/
public static void main(String[] args) {
// TODO Auto-generated method stub
tree = new AvlTree(10);


ExecutorService es = Executors.newCachedThreadPool();
        es.execute(t1);
        es.execute(t2);
        es.execute(t3);
        es.execute(t4);

        es.shutdown();
        try {
boolean finished = es.awaitTermination(1, TimeUnit.MINUTES);
} catch (InterruptedException e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
		
tree.inorderPrint();
int count = tree.getCount();
System.out.println(count);


}


private static Runnable t1 = new Runnable() {
        public void run() {
            try{
                for(int i=0; i<4000; i++){
                 tree.insert(i);
                }
            } catch (Exception e){}

        }
    };

    private static Runnable t2 = new Runnable() {
        public void run() {
            try{
                for(int i=0; i<4000; i++){
                    tree.insert(i);
                }
            } catch (Exception e){}
       }
    };
    
    private static Runnable t3 = new Runnable() {
        public void run() {
            try{
                for(int i=0; i<4000; i++){
                    tree.insert(i);
                }
            } catch (Exception e){}
       }
    };
    
    private static Runnable t4 = new Runnable() {
        public void run() {
            try{
                for(int i=0; i<4000; i++){
                    tree.insert(i);
                }
            } catch (Exception e){}
       }
    };
    

}