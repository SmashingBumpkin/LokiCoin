import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import static org.junit.Assert.*;

public class Blockchain {
    public List<Block> blockchain = new ArrayList<>();
    public int prefix = 2;
    public String prefixString;
    public String lastHash;
    public int blockchainHeight = 0;
    

    public Blockchain(int prefix) {
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public String getLastHash(){ return this.lastHash; }

    public void printBlockchain(){
        for (Block block : this.blockchain) {
            System.out.println(block.returnBlockPrintable());
        }
    }

    public void addNewBlock(Block newBlock){
        //Validate all transactions in the block
        //Add block to chain
        if (newBlock.getBlockNumber() == this.blockchainHeight){
            this.blockchain.add(newBlock);
            this.lastHash = newBlock.getHash();
            this.blockchainHeight++;
        }
    }

    public void mineNewBlock(String data) {
        Block newBlock = new Block(
            data, 
            blockchain.get(blockchain.size() - 1).getHash(),
            new Date().getTime());

        newBlock.mineBlock(prefix);
        blockchain.add(newBlock);
    }

    public void mineNewBlock(String data, String rewardRecipient) {
        Block newBlock = new Block(
            data,
            blockchain.get(blockchain.size() - 1).getHash(),
            new Date().getTime());

        newBlock.mineBlock(prefix);
    }

    public void validateBlockchain() {
        boolean flag = true;
        int errorBlock = 0;
        for (int i = 0; i < blockchain.size(); i++) {
            String previousHash = i==0 ? "0" : blockchain.get(i - 1).getHash();
            flag = blockchain.get(i).validateBlock(previousHash, prefix, prefixString);
            if (!flag){
                errorBlock = i;
                break;
            }
        }
        if (flag){
            System.out.println("_____\nValid blockchain");
        } else {
            System.out.println("_____\nERROR: Invalid blockchain. Error in Block " + errorBlock);
        }
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
