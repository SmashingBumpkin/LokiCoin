import java.util.ArrayList;
import java.util.List;

public class Network {
    public List<Account> accounts = new ArrayList<>();
    //Instance of the network
    //Contains a whole bunch of miners
    //Will have accounts sending transactions and stuff
    public static void main(String[] args) {
        System.out.println("Initializing network LFG");
        MinerThread miner1 = new MinerThread("address1", "privkey1");
        MinerThread miner2 = new MinerThread("address2", "privkey2");
        Block genesisBlock = miner1.account.mineBlock("The OG miner was 'ere", "0");
        int prefix = 2;
        Account.prefix = prefix;
        genesisBlock.mineBlock(prefix);
        MinerThread.blockchain.addNewBlock(genesisBlock);
        miner1.start();
        miner2.start();
        MinerThread.blockchain.printBlockchain();
        MinerThread.blockchain.validateBlockchain();
    }
}
