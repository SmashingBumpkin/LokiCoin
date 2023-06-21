package com.example.lokicoin;

import com.example.lokicoin.blockchain.Miner;
import com.example.lokicoin.blockchain.Simulation;

public class Launcher{
    public static void main(String[] args){
        Thread simThread = new Thread(() -> {
                Simulation sm = new Simulation();
                sm.run();
        });
        simThread.start();
        try{
            System.out.println("____________________Starting sim__________________________________________");
            Thread.sleep(10000);
            System.out.println("____________________Stopping sim__________________________________________");
        } catch (Exception e){
        }
        Miner.minersActive = false;
    }
}