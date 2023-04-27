import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import static org.junit.Assert.*;

public class Blockchain {
    public List<Block> blockchain = new ArrayList<>();
    public int prefix = 2;
    public String prefixString;
    

    public Blockchain(int prefix) {
        this.prefix = prefix;
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public void printBlockchain(){
        for (Block block : this.blockchain) {
            System.out.println(block.returnBlockPrintable());
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
}
