import java.util.ArrayList;
import java.util.List;

public class Network {
    // all of the blocks that have been submitted and can be mined
    private static List<Block> potentialBlocks = new ArrayList<>();
    private static int numberOfPotentialBlocks = 0;
    //all of the transactions that have been submitted and can be mined
    private static List<Transaction> potentialTransactions = new ArrayList<>();
    private static int numberOfPotentialTransactions = 0;

    Network(){}

    // Getter for potentialBlocks list
    public static List<Block> getPotentialBlocks() {return Network.potentialBlocks;}
    public static int getNumberOfPotentialBlocks() {return Network.numberOfPotentialBlocks;}

    // Adder for potentialBlocks list
    public static void addPotentialBlock(Block block) {
        Network.potentialBlocks.add(block);
        Network.numberOfPotentialBlocks++;
    }

    // Getter for potentialTransactions list
    public static List<Transaction> getPotentialTransactions() {
        return Network.potentialTransactions;
    }
    public static int numberOfPotentialTransactions() {return Network.numberOfPotentialTransactions;}

    // Adder for potentialTransactions list
    public static void addPotentialTransaction(Transaction transaction) {
        Network.potentialTransactions.add(transaction);
        Network.numberOfPotentialTransactions++;
    }
}