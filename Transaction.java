public class Transaction {
    public String sender;
    public String receiver;
    public int amount;
    public int nonce;
    public int fee; 
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
        return hash;
    }

    public String getTxAsString(){
        //Condenses the details of the transaction into a single string
        return "LOLLLL";
    }

    public String getTxWithHashAString(){
        return getTxAsString() + " " + getHash();
    }

    public boolean checkTransaction(){
        //Implement checker to see if a transaction is valid
        //
        return false;
    }
    
    public Transaction(String privateKey){
        //parse received transaction string and try to build a transaction from it

    }

    public void buildTransaction(){
        //Builds a tx

        ///...... maybe thise should be in the account class
    }

    public void broadcastTransaction(){
        //builds a tx and sends it to any other nodes
    }
}
