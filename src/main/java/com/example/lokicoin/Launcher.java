package com.example.lokicoin;

import com.example.lokicoin.blockchain.Simulation;

public class Launcher {
    public static void main(String[] args){
        Simulation sm = new Simulation();
        sm.run();
        //System.out.println(Simulation.getMinerArray());
    }
}