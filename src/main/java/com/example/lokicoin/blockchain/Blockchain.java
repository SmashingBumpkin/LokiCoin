package com.example.lokicoin.blockchain;
import java.util.ArrayList;
import java.util.List;
//import static org.junit.Assert.*;


public class Blockchain {

    public List<Block> blockchain = new ArrayList<>();
    public int prefix; //difficulty of the blockchain
    public String prefixString; //difficulty as a string prefix
    public String lastHash; //The most recent block hash
    public int blockchainHeight = 0; //height of the blockchain

    //creates an empty blockchain of set difficulty
    public Blockchain(int prefix) {
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public int getBlockchainHeight(){ return this.blockchainHeight;}

    public String getLastHash(){ return this.lastHash; }

    public Block getBlock(int n){return blockchain.get(n);}

    public Block getLastBlock(){return blockchain.get(this.blockchainHeight-1);}

    public void addNewBlock(Block newBlock){
        //Add block to chain
        this.blockchain.add(newBlock);
        this.lastHash = newBlock.getHash();
        this.blockchainHeight = newBlock.getBlockNumber()+1;
    }

    public List<Integer> removeBlocksAfterBlockX(int x){
        List<Integer> networkPositions = new ArrayList<>();
        for (int i = this.getBlockchainHeight(); i > x+1; i--) {
            networkPositions.add(blockchain.get(--this.blockchainHeight).getPositionInNetwork());
            blockchain.remove(this.blockchainHeight);
        }
        return networkPositions;
    }

    public void exportBlockchain(){
        //TODO: Elena
        //Export the whole blockchain
    }

    public void importBlockchain(){
        //TODO: Elena
        //Import a previously initialized blockchain
        //Should import the data and call the Blockchain constructor,
        //might need a new constructor
        //Also MUST validate the blockchain
    }
}