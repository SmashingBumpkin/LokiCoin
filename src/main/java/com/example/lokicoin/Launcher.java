package com.example.lokicoin;

import com.example.lokicoin.blockchain.Miner;
import com.example.lokicoin.blockchain.Simulation;

public class Launcher {
    public static void main(String[] args){
        Simulation sm = new Simulation();
        sm.run();
//        try{
//            Thread.sleep(3000);
//        } catch (Exception e){
//        }
//        Miner.minersActive = false;
    }
}