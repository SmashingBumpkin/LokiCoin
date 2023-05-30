package com.example.notlokicoin;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class Account extends Thread  {
    public int balance = 0; //Account balance
    private final PublicKey pubKey; //address of the account
    private int nonce = 0; //The nonce of the account, must always go up
    private PrivateKey privKey; //The private key of the account, used for signing transactions

    //They all share a single network
    public static Network network = new Network();

    Account(PublicKey pubKey, PrivateKey privKey){
        super();
        this.pubKey = pubKey;
        this.privKey = privKey;
        Network.addAccount(this.pubKey);


    }
    Account(PublicKey pubKey){
        super();
        this.pubKey = pubKey;

        //if (!Network.NPCs.contains(this)){
        //    Network.NPCs.add(this);
        //}
    }

    Account(){
        super();
        //Returns a new account with automatically generated keys
        KeyPair keys = Account.generateKeyPair();
        this.pubKey = keys.getPublic();
        this.privKey = keys.getPrivate();
        Network.addAccount(this.pubKey);


    }

    public int getBalance() { return this.balance; }
    public PublicKey getPubKey() { return this.pubKey; }
    public int getNonce() { return this.nonce; }
    public void debitBalance(int debit) { this.balance -= debit; }
    public void creditBalance(int credit) { this.balance += credit; }
    public void setNonce(int nonce) { this.nonce = nonce; }

    public static KeyPair generateKeyPair(){
        // Generate public key and private key
        KeyPairGenerator keyPairGen;
        KeyPair pair = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            pair = keyPairGen.generateKeyPair();
        } catch (Exception e) {
            System.out.println("ERROR GENERATING KEYS");
            e.printStackTrace();
        }
        return pair;
    }

    public LokiTransaction generateLokiTransaction(PublicKey recipient, int amount, int fee) {
        this.nonce++;
        LokiTransaction lokiTransaction =  new LokiTransaction(this.getPubKey(), fee, this.nonce, recipient, amount);
        String txAsString = lokiTransaction.getTxAsString();

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] messageHash = messageDigest.digest(txAsString.getBytes());
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(this.privKey);
            signature.update(messageHash);
            byte[] digitalSignature = signature.sign();
            lokiTransaction.setHash(digitalSignature);
        } catch (Exception e) {
            System.out.println("Error generating loki transaction");
            e.printStackTrace();
        }
        return lokiTransaction;
    }

    public void broadcastTransaction(Transaction tx){
        Network.addPotentialTransaction(tx);
    }

    public String returnAccountPrintable(){
        return "Address " + this.pubKey.hashCode() + " has balance " + this.balance;
    }
}