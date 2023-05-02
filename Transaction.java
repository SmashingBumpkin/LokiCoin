public class Transaction {
    public String sender;
    public String receiver;
    public int amount;
    public int fee; 
    public int nonce;
    public String hash;//Can be generated with all of the above information
    
    public Transaction(String sender, String receiver, int amount, int fee, int nonce){
        //Generate a transacaction (excluding the hash)
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.fee = fee;
        this.nonce = nonce;
    }

    public Transaction(String sender, String receiver, int amount, int fee, int nonce, String hash){
        //Generate a transacaction (including the hash)
        this(sender, receiver, amount, fee, nonce);
        this.hash = hash;
    }

    Transaction(String txAsString){
        //TODO: Maria
        //parse data from a string
        //Check it's all valid <- this 
        //Create a new transaction based on all of that
        //this(sender, receiver, amount, fee, nonce, hash);
    }

    public String getSender() {return sender;}
    public String getReceiver() {return receiver;}
    public int getAmount() {return amount;}
    public int getNonce() {return nonce;}
    public int getFee() {return fee;}
    public String getHash() {return "Hashed transaction";}
    
    public String getTxAsString(){
        //Condenses the details of the transaction into a single string
        String output = getSender() + " " + getReceiver() + " " + getAmount() +
            " " + getFee() + " " + getNonce();
        return output;
    }

    public String generateHash(String privateKey) {
        //TODO: Joaquin to implement
        String txAsString = getTxAsString();
        this.hash = "hashed tx";
        return "Hashed transaction";
    }

    public String getTxWithHashAsString(){
        return getTxAsString() + " " + getHash();
    }

    public boolean checkTransaction(){
        //TODO: DEPRECATE and move to Miner
        //TODO: Joaquin to implement
        //Implement checker to see if a transaction is valid
        //
        return false;
    }

    public void broadcastTransaction(){
        //TODO: DEPRECATE and move to Miner or MinerThread
        //Sends the completed and hashed transaction to the miners
    }

    public void exportTransaction(){
        //TODO: Elena
        //Exports a transaction for broadcasting
    }
    
    public void importTransaction(){
        //TODO: Elena
        //Imports a transaction and constructs it
        //Adds it to the mempool?
    }
}
