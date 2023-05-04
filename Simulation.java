public class Simulation {
    public static void main(String[] args) {
        //Used to start a new simulation
        
        System.out.println("Initializing network LFG");

        //miner1 starts a new network
        Miner miner1 = Miner.startNewNetwork(1);


        //TEMPORARY - miner2 should get the data from the network and continue working on the same network
        Miner miner2 = Miner.startNewNetwork(1);

        // Miner miner1 = new Miner();
        // Miner miner2 = new Miner();

        miner1.start();

        
        miner2.start();

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
        miner1.printBlockchain();
        // miner1.miner.printAccounts();

        long startTime = System.currentTimeMillis();
        
        for (int i=0; i<4; i++){
            //TODO: Andrea
            //Accounts/miners get added to network as potential people to do a transaction

            //Write a loop that sends a  transaction from one random account to random another
            //Keeps looping and adding the transactions
            //Add a time so they don't get created TOO quickly

            //Relevant classes: Account, Network, Miner, LokiTransacion

            LokiTransaction tx = miner1.generateLokiTransaction(miner2.getPubKey(), 100, 5);
            System.out.println(tx.getTxPrintable());
            try {
                if (miner1.checkTxValidity(tx)){
                    miner1.executeTransaction(tx);
                }
            } catch (Exception e) {
                System.out.println("Invalid tx");
            }
        }

        for (int i=0; i<0; i++){
            LokiTransaction tx = miner2.generateLokiTransaction(miner1.getPubKey(), 20, 5);
            System.out.println(tx.getTxPrintable());
            try {
                if (miner1.checkTxValidity(tx)){
                    miner1.executeTransaction(tx);
                }
            } catch (Exception e) {
                System.out.println("Invalid tx");
            }
        }

        miner1.printAccounts();
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("The code took " + duration + " milliseconds to execute");
    }
}
