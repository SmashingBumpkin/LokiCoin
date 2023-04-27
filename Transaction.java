import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Transaction {
    public String sender;
    public String receiver;
    public int amount;
    public int fee; 
    public int nonce;
    public String hash;//Can be generated with all of the above information

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getAmount() {
        return amount;
    }

    public int getNonce() {
        return nonce;
    }

    public int getFee() {
        return fee;
    }

    public String getHash() {
        return "Hashed transaction";
    }

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

    public String getTxWithHashAString(){
        return getTxAsString() + " " + getHash();
    }

    public boolean checkTransaction(){
        //TODO: Joaquin to implement
        //Implement checker to see if a transaction is valid
        //
        return false;
    }
    
    public Transaction(String sender, String receiver){
        //Generate a transacaction (excluding the hash)

    }

    public void buildTransaction(){
        //Builds a tx

        ///...... maybe thise should be in the account class
    }

    public void broadcastTransaction(){
        //builds a tx and sends it to any other nodes
    }
}
