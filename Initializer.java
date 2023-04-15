import java.util.Date;

public class Initializer {
    public static void main(String[] args) {
        System.out.println("Initializing blockchain LFG");
        Block genesisBlock = new Block("Down with banks, up with Loki", "", new Date().getTime());
        Blockchain blockchain = new Blockchain();
        blockchain.blockchain.add(genesisBlock);
        blockchain.givenBlockchain_whenNewBlockAdded_thenSuccess();
        blockchain.printBlockchain();
    }
}
