import java.security.PublicKey;

public class Transaction {
    public PublicKey sender;
    public int fee; 
    public int nonce;
    public byte[] hash;//Can be generated with all of the above information
    public String data; //different transaction types can be encoded into a string to allow them to be
                        //saved as a normal transaction
    
    public Transaction(PublicKey sender, int fee, int nonce){
        //Generate a transacaction (excluding the hash)
        this.sender = sender;
        this.fee = fee;
        this.nonce = nonce;
    }

    public Transaction(PublicKey sender, int fee, int nonce, byte[] hash){
        //Generate a transacaction (including the hash)
        this(sender, fee, nonce);
        this.hash = hash;
    }

    Transaction(String txAsString){
        //TODO: Maria
        //parse data from a string
        //Check it's all valid <- this 
        //Create a new transaction based on all of that
        //this(sender, receiver, amount, fee, nonce, hash);
    }

    public PublicKey getSender() {return sender;}
    public int getNonce() {return nonce;}
    public int getFee() {return fee;}
    public byte[] getHash() {return this.hash;}
    public String getData() { return this.data; }
    public void setHash(byte[] hash) { this.hash = hash; }
    public void setData(String string) { this.data = string; }
    
    public String getTxAsString(){
        //Condenses the details of the transaction into a single string
        String output = CryptographyReencoding.pubKeyAsString(getSender()) + " " + getFee() + " " + getNonce();
        return output;
    }

    public String getTxWithHashAsString(){
        return getTxAsString() + " " + CryptographyReencoding.bytesAsString(getHash());
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
