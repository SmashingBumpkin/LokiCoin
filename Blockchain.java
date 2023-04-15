import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import static org.junit.Assert.*;

public class Blockchain {
    public List<Block> blockchain = new ArrayList<>();
    public int prefix = 4;
    public String prefixString;
    

    public Blockchain() {
        this.prefixString = new String(new char[prefix]).replace('\0', '0');
    }

    public void printBlockchain(){
        for (Block block : this.blockchain) {
            System.out.println(block.returnBlockPrintable());
        }
    }

    public void givenBlockchain_whenNewBlockAdded_thenSuccess() {
        Block newBlock = new Block(
            "The is a New Block.", 
            blockchain.get(blockchain.size() - 1).getHash(),
            new Date().getTime());

        newBlock.mineBlock(prefix);
        //assertTrue(newBlock.getHash().substring(0, prefix).equals(prefixString));
        blockchain.add(newBlock);
    }

    // public void givenBlockchain_whenValidated_thenSuccess() {
    //     boolean flag = true;
    //     for (int i = 0; i < blockchain.size(); i++) {
    //         String previousHash = i==0 ? "0" : blockchain.get(i - 1).getHash();
    //         flag = blockchain.get(i).getHash().equals(blockchain.get(i).calculateBlockHash())
    //         && previousHash.equals(blockchain.get(i).getPreviousHash())
    //         && blockchain.get(i).getHash().substring(0, prefix).equals(prefixString);
    //             if (!flag) break;
    //     }
    //     //assertTrue(flag);
    // }
}
