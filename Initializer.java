import java.util.Date;

public class Initializer {
    public static void main(String[] args) {
        System.out.println("Initializing blockchain LFG");
        Block genesisBlock = new Block("Down with banks, up with Loki", "0", new Date().getTime());
        int prefix = 2;
        genesisBlock.mineBlock(prefix);
        Blockchain blockchain = new Blockchain(prefix);
        blockchain.blockchain.add(genesisBlock);
        blockchain.mineNewBlock("LETS GOOOO");
        blockchain.mineNewBlock("block #3");
        blockchain.mineNewBlock("Still going");
        blockchain.printBlockchain();
        blockchain.validateBlockchain();
    }
}
