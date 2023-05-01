import java.util.ArrayList;
import java.util.List;

public class Network {
    // all of the blocks that have been submitted and can be mined
    private static List<Block> potentialBlocks = new ArrayList<>();
    //all of the transactions that have been submitted and can be mined
    private static List<Transaction> potentialTransactions = new ArrayList<>();

    Network(){}

    // Getter for potentialBlocks list
    public static List<Block> getPotentialBlocks() {return Network.potentialBlocks;}

    // Adder for potentialBlocks list
    public static void addPotentialBlock(Block block) {Network.potentialBlocks.add(block);}

    // Getter for potentialTransactions list
    public static List<Transaction> getPotentialTransactions() {
        return Network.potentialTransactions;
    }

    // Adder for potentialTransactions list
    public static void addPotentialTransaction(Transaction transaction) {
        Network.potentialTransactions.add(transaction);
    }
}