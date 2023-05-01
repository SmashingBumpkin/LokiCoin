import java.util.ArrayList;
import java.util.List;
//import static org.junit.Assert.*;
import java.util.Map;

public class Blockchain {

    public List<Block> blockchain = new ArrayList<>();
    public Map<String, Account> accounts;
    public int prefix = 2; //difficulty of the blockchain
    public String prefixString; //difficulty as a string prefix
    public String lastHash; //The most recent block hash
    public int blockchainHeight = 0; //height of the blockchain
    
    //creates an empty blockchain of set difficulty
    public Blockchain(int prefix) {
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public String getLastHash(){ return this.lastHash; }

    public void addNewBlock(Block newBlock){
        //Add block to chain
        this.blockchain.add(newBlock);
        this.lastHash = newBlock.getHash();
        this.blockchainHeight++;
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
