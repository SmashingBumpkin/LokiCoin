package com.example.notlokicoin;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    public Miner miner1 = Miner.startNewNetwork(1);
    public Miner miner2 = new Miner();
    public Miner miner3 = new Miner();
    public Miner miner4 = new Miner();
    public Miner miner5 = new Miner();
    public Miner miner6 = new Miner();
    public Random R = new Random();
    public List <Miner> minerArray = new ArrayList<>();
    public List <Account> accArray = new ArrayList<>();

    public void run() {
        //Used to start a new simulation
        this.minerArray.add(miner1);
        this.minerArray.add(miner2);
        this.minerArray.add(miner3);
        this.minerArray.add(miner4);
        this.minerArray.add(miner5);
        this.minerArray.add(miner6);
        for (Miner m : minerArray){this.accArray.add(m);}
        //simulation parameters
        int simRuntimeMilliseconds = 1200;
        int difficulty = 1;
        Miner.sleeptime = 10;

        System.out.println("Initializing network LFG");

        //miner1 starts a new network
        /*
        Miner miner1 = Miner.startNewNetwork(difficulty);
        Miner miner2 = new Miner();
        Miner miner3 = new Miner();
        Miner miner4 = new Miner();
        Miner miner5 = new Miner();
        Miner miner6 = new Miner();
        */
        for (Miner m : minerArray){m.start();}
        /*
        miner1.start();
        miner2.start();
        miner3.start();
        miner4.start();
        miner5.start();
        miner6.start();
        */
        //add a loop that spams transactions for the miners to pick up

        /*
        for (int i=0; i<100; i++){
            try{
                Thread.sleep(100);
            } catch (Exception e){
            }
            LokiTransaction lokiTx = miner1.generateLokiTransaction(miner2.getPubKey(), 1, 0);
            Transaction tx = lokiTx.lokiToGenericTransaction();
            Network.addPotentialTransaction(tx);
         */

        Thread makeNPCs = new Thread(() -> {
            while (Miner.minersActive == true){
                Account NPC = new Account();
                accArray.add(NPC);
                //Network.addAccount(NPC.getPubKey());
                NPC.run();
                try{
                    Thread.sleep(R.nextInt(1000,5000));
                } catch (Exception e){
                }}});
        makeNPCs.start();

        Thread makeDrones = new Thread(()->{
            while (Miner.minersActive == true){
                Miner Drone = new Miner();
                minerArray.add(Drone);
                accArray.add(Drone);
                Drone.start();
                try{
                    Thread.sleep(R.nextInt(1000,5000));
                } catch (Exception e){
                }}});
        makeDrones.start();

        while (Miner.minersActive == true){
            //Random R = new Random();
            Account x = accArray.get(R.nextInt(accArray.size()));
            Account y = accArray.get(R.nextInt(accArray.size()));
            //Miner x = minerArray[R.nextInt(6)];
            //Miner y = minerArray[R.nextInt(6)];
            if (x != y ) {
                LokiTransaction lokiTx = x.generateLokiTransaction(y.getPubKey(), R.nextInt(0, 10), R.nextInt(0, 3));
                Transaction tx = lokiTx.lokiToGenericTransaction();
                Network.addPotentialTransaction(tx);
            }
            try{
                Thread.sleep(R.nextInt(0,100));
            } catch (Exception e){
            }}

        /*
        for (int i=0; i<20; i++){
            try{
                Thread.sleep(100);
            } catch (Exception e){
            }
            LokiTransaction lokiTx = miner1.generateLokiTransaction(miner2.getPubKey(), 50, 1);
            Transaction tx = lokiTx.lokiToGenericTransaction();
            Network.addPotentialTransaction(tx);
        }
        */
        try{
            Thread.sleep(simRuntimeMilliseconds);
        } catch (Exception e){

        }

        //Miner.minersActive = false;

        try {
            for (Miner m : minerArray){m.join();}
            /*
            miner1.join();
            miner2.join();
            miner3.join();
            miner4.join();
            miner5.join();
            miner6.join();
            */

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//         miner1.printBlockchain();
        for (Miner m : minerArray) {m.printStatus();}
        /*
        miner6.printStatus();
        miner5.printStatus();
        miner4.printStatus();
        miner3.printStatus();
        miner2.printStatus();
        miner1.printStatus();
         */
        miner1.validateBlockchain();
        Network.printStatus();

        miner1.printAccounts();
//        miner2.printAccounts();
//        miner3.printAccounts();
//        miner4.printAccounts();
//        miner5.printAccounts();
//        miner6.printAccounts();

        long startTime = System.currentTimeMillis();

//        for (int i=0; i<4; i++){
//            //TODO: Andrea
//            //Accounts/miners get added to network as potential people to do a transaction
//
//            //Write a loop that sends a  transaction from one random account to random another
//            //Keeps looping and adding the transactions
//            //Add a time so they don't get created TOO quickly
//
//            //Relevant classes: Account, Network, Miner, LokiTransacion
//
//            LokiTransaction tx = miner1.generateLokiTransaction(miner2.getPubKey(), 199, 5);
////            System.out.println(tx.getTxPrintable());
//            try {
//                if (miner1.checkTxValidity(tx,miner1.localBlockchain.getLastBlock())){
//                    miner1.executeTransaction(tx,miner1.localBlockchain.getLastBlock());
//                }
//            } catch (Exception e) {
//                System.out.println("Invalid tx");
//            }
//        }
//
//        for (int i=0; i<5; i++){
//            LokiTransaction tx = miner2.generateLokiTransaction(miner1.getPubKey(), 19, 5);
////            System.out.println(tx.getTxPrintable());
//            try {
//                if (miner1.checkTxValidity(tx, miner1.localBlockchain.getLastBlock())){
//                    miner1.executeTransaction(tx, miner1.localBlockchain.getLastBlock());
//                }
//            } catch (Exception e) {
//                System.out.println("Invalid tx");
//            }
//        }
//        miner1.printAccounts();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("The code took " + duration + " milliseconds to execute");
    }
}
