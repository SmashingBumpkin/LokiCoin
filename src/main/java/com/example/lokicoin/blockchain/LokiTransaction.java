package com.example.lokicoin.blockchain;

import com.example.lokicoin.CryptographyReencoding;

import java.security.PublicKey;

public class LokiTransaction extends Transaction {
    public PublicKey recipient;
    public int amount;

    public LokiTransaction(PublicKey sender, int fee, int nonce, PublicKey recipient, int amount) {
        //Generate a LokiCoin transacaction (excluding the hash)
        super(sender, fee, nonce);
        this.recipient = recipient;
        this.amount = amount;
    }

    public LokiTransaction(PublicKey sender, int fee, int nonce, PublicKey recipient, int amount, byte[] hash){
        //Generate a LokiCoin transacaction (including the hash)
        super(sender, fee, nonce, hash);
        this.recipient = recipient;
        this.amount = amount;
    }

    public LokiTransaction(Transaction tx){
        super(tx.getSender(), tx.getFee(), tx.getNonce(), tx.getHash());
        String data = tx.getData();
        String[] dataAsList = data.split(" ");
        this.amount = Integer.parseInt(dataAsList[0]);
        this.recipient = CryptographyReencoding.stringAsPubKey(dataAsList[1]);
    }

    // public Transaction lokiTransactionFromString(String txAsString){
    //     //parse data from a string
    //     //Return a new transaction based on all of that
    //     PublicKey sender = "jeff";
    //     PublicKey recipient = "Steve";
    //     int amount = 1;
    //     int fee = 1;
    //     int nonce = 1;
    //     String hash = "hash";
    //     return new LokiTransaction(sender, fee, nonce, recipient, amount, hash);
    // }

    public PublicKey getRecipient() { return this.recipient; }
    public int getAmount() { return amount; }

    public String getTxAsString(){
        //Condenses the details of the transaction into a single string
        String output = super.getTxAsString();
        output = output + " " + CryptographyReencoding.pubKeyAsString(getRecipient()) + " " + getAmount();
        return output;
    }

    public String getTxWithHashAsString(){
        //Condenses the details of the transaction into a single string
        String output = super.getTxWithHashAsString();
        return output + " " + CryptographyReencoding.pubKeyAsString(getRecipient()) + " " + getAmount();
    }

    public String getTxPrintable(){
        return "\nSENDER:\n" + CryptographyReencoding.pubKeyAsString(getSender()) + "\nFee: " + getFee()
                + "   Nonce: " + getNonce() + "\nRecipient:\n" + CryptographyReencoding.pubKeyAsString(getRecipient())
                + "\nAmount: " + getAmount() + "\n";
    }

    public Transaction lokiToGenericTransaction(){
        Transaction outputTx = new Transaction(this.sender, this.fee, this.nonce, this.hash);
        outputTx.setData("" + this.amount + " " + CryptographyReencoding.pubKeyAsString(this.recipient));
        return outputTx;
    }
}
