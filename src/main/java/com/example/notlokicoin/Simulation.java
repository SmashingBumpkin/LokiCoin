package com.example.notlokicoin;

public class Simulation {
    public static void run() {
        //Used to start a new simulation

        System.out.println("Initializing network LFG");

        //miner1 starts a new network
        Miner miner1 = Miner.startNewNetwork(1);
        Miner miner2 = new Miner();
        Miner miner3 = new Miner();
        Miner miner4 = new Miner();
        Miner miner5 = new Miner();
        Miner miner6 = new Miner();

        miner1.start();
        miner2.start();
        miner3.start();
        miner4.start();
        miner5.start();
        miner6.start();

        //add a loop that spams transactions for the miners to pick up

        try{
            Thread.sleep(12000);
        } catch (Exception e){

        }

        Miner.minersActive = false;

        try {
            miner1.join();
            miner2.join();
            miner3.join();
            miner4.join();
            miner5.join();
            miner6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // miner1.printBlockchain();
        miner6.printStatus();
        miner5.printStatus();
        miner4.printStatus();
        miner3.printStatus();
        miner2.printStatus();
        miner1.printStatus();
        miner1.validateBlockchain();
        Network.printStatus();

        // miner1.printAccounts();

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
//            LokiTransaction tx = miner1.generateLokiTransaction(miner2.getPubKey(), 100, 5);
//            System.out.println(tx.getTxPrintable());
//            try {
//                if (miner1.checkTxValidity(tx)){
//                    miner1.executeTransaction(tx);
//                }
//            } catch (Exception e) {
//                System.out.println("Invalid tx");
//            }
//        }
//
//        for (int i=0; i<0; i++){
//            LokiTransaction tx = miner2.generateLokiTransaction(miner1.getPubKey(), 20, 5);
//            System.out.println(tx.getTxPrintable());
//            try {
//                if (miner1.checkTxValidity(tx)){
//                    miner1.executeTransaction(tx);
//                }
//            } catch (Exception e) {
//                System.out.println("Invalid tx");
//            }
//        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("The code took " + duration + " milliseconds to execute");
    }
}