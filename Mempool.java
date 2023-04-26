import java.util.ArrayList;
import java.util.List;
public class Mempool {
    public List<Transaction> blockchain = new ArrayList<>();

    public void checkAndRemoveTx(Transaction tx){
        //Checks mempool for transaction and removes it if found
    }

    public void validateMempool(){
        //Iterates through mempool to check if all transactions are valid
        //Should be called when a new block is found
    }

    public void addAndBroadcastNewTx(Transaction tx){
        //Checks mempool for tx
        //if it's not present,
            //adds it to the mempool
            //Sends it to other nodes
    }
}
