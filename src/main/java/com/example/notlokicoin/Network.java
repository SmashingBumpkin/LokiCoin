package com.example.notlokicoin;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Network extends Thread {
    // all of the blocks that have been submitted and can be mined
    private static List<Block> potentialBlocks = new ArrayList<>();
    private static int numberOfPotentialBlocks = 0;
    //all of the transactions that have been submitted and can be mined
    private static List<Transaction> potentialTransactions = new ArrayList<>();
    private static int numberOfPotentialTransactions = 0;

    private static List<PublicKey> accountPubKeys = new ArrayList<>();
    private static int numberOfAccounts = 0;

    Network(){}

    // Getter for potentialBlocks list
    public static List<Block> getPotentialBlocks() {return Network.potentialBlocks;}
    public static List<Block> getPotentialBlocks(int start, int finish) {
        return new ArrayList<>(Network.potentialBlocks.subList(start, finish));
    }
    public static Block getBlock(Integer blockPosition) {
        return potentialBlocks.get(blockPosition);
    }
    public static int getNumberOfPotentialBlocks() {return Network.numberOfPotentialBlocks;}

    // Adder for potentialBlocks list
    public synchronized static int addPotentialBlock(Block block) {
        Network.potentialBlocks.add(block);
        block.setBlockPositionInNetwork(numberOfPotentialBlocks);
        return numberOfPotentialBlocks++;
    }

    // Getter for potentialTransactions list
    public static List<Transaction> getPotentialTransactions() {
        return Network.potentialTransactions;
    }
    public static int getNumberOfPotentialTransactions() { return Network.numberOfPotentialTransactions; }
    public static List<Transaction> getPotentialTransactions(int start, int finish) {
        return new ArrayList<>(Network.potentialTransactions.subList(start, finish));
    }
    // Adder for potentialTransactions list
    public synchronized static void addPotentialTransaction(Transaction transaction) {
        Network.potentialTransactions.add(transaction);
        Network.numberOfPotentialTransactions++;
    }

    public synchronized static void addAccount(PublicKey pubKey){
        Network.accountPubKeys.add(pubKey);
        Network.numberOfAccounts++;
    }

    public static List<PublicKey> getAccountPubKeys(){
        return Network.accountPubKeys;
    }

    public static PublicKey getAPubKey(){
        int randomIndex = (int) (Math.random() * Network.numberOfAccounts);
        return Network.accountPubKeys.get(randomIndex);
    }
    public static void printStatus(){
        System.out.println("There have been " + Network.numberOfPotentialBlocks + " blocks submitted and");
        System.out.println(Network.numberOfPotentialTransactions + " transactions from " + Network.numberOfAccounts + " accounts.");
    }
}