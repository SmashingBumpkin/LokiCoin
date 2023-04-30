import java.util.ArrayList;
import java.util.List;
public class Mempool {
    public List<LokiTransaction> mempool = new ArrayList<>();

    public String getValidTransactionPool(){
        String output = "";
        List<String> senders = new ArrayList<>();
        for (LokiTransaction tx : this.mempool) {
            if (senders.contains(tx.getSender())) {
            } else {
                // add the sender to the list of senders
                senders.add(tx.getSender());
                // add the transaction ID to the output string
                output += tx.getTxWithHashAsString() + "\n";
            }
        }
        return output;
    }

    public void checkAndRemoveTx(Transaction tx){
        //TODO
        //Checks mempool for transaction and removes it if found
    }

    public void validateMempool(){
        //TODO
        //Iterates through mempool to check if all transactions are valid
        //Should be called when a new block is found
    }

    public void addAndBroadcastNewTx(Transaction tx){
        //TODO
        //Checks mempool for tx
        //if it's not present,
            //adds it to the mempool
            //Sends it to other nodes
    }
}
