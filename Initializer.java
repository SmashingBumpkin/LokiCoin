public class Initializer {
    public static void main(String[] args) {
        //Used to start a new simulation
        
        System.out.println("Initializing network LFG");

        //miner1 starts a new network
        MinerThread miner1 = MinerThread.startNewNetwork(2, "address1", "privkey1");

        //TEMPORARY - miner2 should get the data from the network and continue working on the same network
        MinerThread miner2 = MinerThread.startNewNetwork(2, "address2", "privkey2");

        miner1.start();
        miner2.start();

        //add a loop that spams transactions for the miners to pick up

        try {
            miner1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            miner2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        miner1.miner.printBlockchain();
    }
}
