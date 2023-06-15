package com.example.lokicoin.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    int difficulty = 2;
    public Miner miner1 = Miner.startNewNetwork(difficulty);
    public Miner miner2 = new Miner();
    public Miner miner3 = new Miner();
    public Miner miner4 = new Miner();
    public Miner miner5 = new Miner();
    public Miner miner6 = new Miner();
    public Random R = new Random();
    public List<Miner> minerArray = new ArrayList<>();
    public List <Account> accArray = new ArrayList<>();
    public void run() {
        //Used to start a new simulation
        Miner.sleeptime = 10;

        //Used to start a new simulation
        this.minerArray.add(miner1);
        this.minerArray.add(miner2);
        this.minerArray.add(miner3);
        this.minerArray.add(miner4);
        this.minerArray.add(miner5);
        this.minerArray.add(miner6);

        for (Miner m : minerArray){this.accArray.add(m);
                                    Network.addAccount(m.getPubKey());}

        System.out.println("Initializing network LFG");

        for (Miner m : minerArray){m.start();}


        Thread makeNPCs = new Thread(() -> {
            while (Miner.minersActive == true){
                Account NPC = new Account();
                accArray.add(NPC);
                Network.addAccount(NPC.getPubKey());
                try{
                    Thread.sleep(R.nextInt(500,2000));
                } catch (Exception e){
                }}});
        makeNPCs.start();

        Thread makeDrones = new Thread(()->{
            while (Miner.minersActive == true){
                Miner drone = new Miner();
                minerArray.add(drone);
                accArray.add(drone);
                drone.start();
                try{
                    Thread.sleep(R.nextInt(1000,5000));
                } catch (Exception e){
                }}});
        makeDrones.start();

        while (Miner.minersActive){
//        for (int i = 0; i<100; i++){
            try{
                Thread.sleep(100);
            } catch (Exception e){
            }
            Account sender = accArray.get(R.nextInt(accArray.size()));
            Account receiver = accArray.get(R.nextInt(accArray.size()));
            LokiTransaction lokiTx = sender.generateLokiTransaction(receiver.getPubKey(), R.nextInt(100), R.nextInt(5));
            Transaction tx = lokiTx.lokiToGenericTransaction();
            Network.addPotentialTransaction(tx);
        }

        try {
            for (Miner m : minerArray){m.join();}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Miner m : minerArray){m.printStatus();}
        miner1.validateBlockchain();
        Network.printStatus();

        miner1.printAccounts();

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