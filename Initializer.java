public class Initializer {
    public static void main(String[] args) {
        //Used to start a new simulation
        
        System.out.println("Initializing network LFG");

        //miner1 starts a new network
        MinerThread miner1 = MinerThread.startNewNetwork(1);


        //TEMPORARY - miner2 should get the data from the network and continue working on the same network
        MinerThread miner2 = MinerThread.startNewNetwork(1);

        // MinerThread miner1 = new MinerThread();
        // MinerThread miner2 = new MinerThread();

        miner1.start();


        // miner2.start();

        //add a loop that spams transactions for the miners to pick up

        try {
            miner1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // try {
        //     miner2.join();
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        // miner1.miner.printBlockchain();
        // miner1.miner.printAccounts();

        long startTime = System.currentTimeMillis();

        for (int i=0; i<4; i++){
            LokiTransaction tx = miner1.miner.generateLokiTransaction(miner2.miner.getPubKey(), 100, 5);
            System.out.println(tx.getTxPrintable());
            try {
                System.out.println(miner1.miner.executeTransaction(tx));
            } catch (Exception e) {
                System.out.println("Invalid tx");
            }
        }

        for (int i=0; i<4; i++){
            LokiTransaction tx = miner2.miner.generateLokiTransaction(miner1.miner.getPubKey(), 20, 5);
            System.out.println(tx.getTxPrintable());
            try {
                System.out.println(miner1.miner.executeTransaction(tx));
            } catch (Exception e) {
                System.out.println("Invalid tx");
            }
        }

        miner1.miner.printAccounts();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("The code took " + duration + " milliseconds to execute");
    }
}
