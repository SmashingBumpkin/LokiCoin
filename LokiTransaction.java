public class LokiTransaction extends Transaction {
    public String receiver;
    public int amount;

    public LokiTransaction(String sender, String receiver, int amount, int fee, int nonce) {
        //Generate a LokiCoin transacaction (excluding the hash)
        super(sender, receiver, amount, fee, nonce); //Remove once Transaction.java is refactored
        //super(sender, fee, nonce); //New super!
        this.receiver = receiver;
        this.amount = amount;
    }

    public LokiTransaction(String sender, String receiver, int amount, int fee, int nonce, String hash){
        //Generate a LokiCoin transacaction (including the hash)
        super(sender, receiver, amount, fee, nonce, hash); //Remove once Transaction.java is refactored
        //super(sender, fee, nonce, hash); //New super!
        this.receiver = receiver;
        this.amount = amount;
    }

    public LokiTransaction(String txAsString){
        //TODO: Maria
        //parse data from a string
        //Create a new transaction based on all of that
        //LokiTransaction(sender, receiver, amount, fee, nonce, hash);
        //Check it's all valid <- this 
    }

    public String getReceiver() {return receiver;}

    public int getAmount() {return amount;}

    public boolean checkTransaction(){
        super.checkTransaction();
        return true;
    }

    public String getTxAsString(){
        //Condenses the details of the transaction into a single string
        String output = super.getTxAsString();
        output = output + " " + getReceiver() + " " + getAmount();
        return output;
    }
}