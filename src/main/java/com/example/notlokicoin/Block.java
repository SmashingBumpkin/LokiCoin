package com.example.notlokicoin;

import java.security.PublicKey;
import java.util.*;

public class Block {
    private String hash = "initial"; //the hash generated by this block
    private String previousHash; //hash from last block
    private String data; //All of the transactions and stuff being written to the blockchain
    private long timeStamp; //time the block is mined
    private int nonce; //used to randomize the block hash
    private PublicKey rewardRecipient; //The miner who gets the block reward
    //(the prize for being the successfull miner)
    // private static int longestChain = 0; //Needs removing
    private int blockNumber; //must be one greater than the previous block
    private int blockPositionInNetwork;
    private int previousBlockPositionInNetwork;
    private List<Transaction> transactions = new ArrayList<>();
    public static final int blockReward = 100;
    private Map<PublicKey, Account> accounts = new HashMap<>();

    //EACH BLOCK SHOULD HOLD THE STATE OF ACCOUNTS AT THAT POINT IT TIME
    //THIS CAN ALLOW A MAJOR REORDER TO BE PROPERLY CHECKED

    public Block(String data, String previousHash, PublicKey rewardRecipient, int blockHeight) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.blockNumber = blockHeight;
        this.rewardRecipient = rewardRecipient;
    }

    public Block(String data, String previousHash, PublicKey rewardRecipient, int blockHeight, int previousBlockPositionInNetwork) {
        this(data, previousHash, rewardRecipient, blockHeight);
        this.previousBlockPositionInNetwork = previousBlockPositionInNetwork;
    }

    //Getters & setters
    public String getHash() { return this.hash;}
    public String getPreviousHash() {return this.previousHash;}
    public String getData() {return this.data;}
    public long getTimeStamp(){return this.timeStamp;}
    public int getNonce(){return this.nonce;}
    public int getBlockNumber(){return this.blockNumber;}
    public int getPositionInNetwork(){return this.blockPositionInNetwork;}
    public int getPreviousPositionInNetwork(){return this.previousBlockPositionInNetwork;}
    public PublicKey getRewardRecipient(){return this.rewardRecipient;}
    public List<Transaction> getTransactions(){ return this.transactions; }
    public Map<PublicKey, Account> getAccounts(){ return this.accounts; }
    public Account getAccount(PublicKey pubKey){ return this.accounts.get(pubKey); }
    public void setNonce(int nonce) { this.nonce = nonce; }
    public void setHash(String hash) { this.hash = hash; }
    public void setTimeStamp(long timeStamp){ this.timeStamp = timeStamp; }
    public void addTransaction(Transaction tx){ this.transactions.add(tx); }
    public void setBlockPositionInNetwork(int blockPositionInNetwork){ this.blockPositionInNetwork = blockPositionInNetwork; }
    public void setAccounts(Map<PublicKey, Account> accounts) {this.accounts = accounts;}

    public String returnBlockPrintable(){ //I'm pretty sure this should be a function like //def __repr__ in python
        String output = "__________\nBLOCK " + this.blockNumber + "\n\n"
                + this.data + "\n\n" + "Hash:\n" + this.hash + "\nlast block posn:"
                + this.getPreviousPositionInNetwork() + "\n__________";
        return output;
    }

    public String returnBlockAsStringForHashing(){ //Turns the block into a string that can be hashed
        StringBuffer transactionStrings = new StringBuffer();
        for (Transaction tx : getTransactions()){
            transactionStrings.append(tx.getTxWithHashAsString() + "\n");
        }
        String output = this.getPreviousHash() + " "
                + Long.toString(this.getTimeStamp()) + " "
                + Integer.toString(this.getNonce()) + " "
                + transactionStrings.toString() + " "
                + this.getRewardRecipient() + " ";
        return output;
    }

    public void exportBlock(){
        //TODO: Elena
        //Export the current block
    }

    public void importBlock(){
        //TODO: Elena
        //Import the current block
        //Should extract the data and call the "Block(.." constructor
    }

    public void addAccount(Account account) {
        this.accounts.put(account.getPubKey(), account);
    }
}