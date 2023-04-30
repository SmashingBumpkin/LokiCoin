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
        int i = 0;
        while (i < 5){
            System.out.println(i);
            miner1 = new MinerThread("address1", "privkey1");
            miner2 = new MinerThread("address2", "privkey2");

            miner1.start();
            miner2.start();
            i++;
            
            try {
                // Wait for either miner1 or miner2 to finish
                miner1.join();
                System.out.println("Miner1 finished");
            } catch (InterruptedException e) {
                // If interrupted, stop the other miner and break out of the loop
                miner2.interrupt();
                break;
            }
            
            try {
                // Wait for either miner1 or miner2 to finish
                miner2.join();
                System.out.println("Miner2 finished");
            } catch (InterruptedException e) {
                // If interrupted, stop the other miner and break out of the loop
                miner1.interrupt();
                break;
            }
        }

        try {
            Thread.sleep(1211);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        MinerThread.blockchain.printBlockchain();
        MinerThread.blockchain.validateBlockchain();
    }
}