public class LokiTransaction extends Transaction {
    public String receiver;
    public int amount;

    public LokiTransaction(String sender, int fee, int nonce, String receiver, int amount) {
        //Generate a LokiCoin transacaction (excluding the hash)
        super(sender, receiver, amount, fee, nonce); //Remove once Transaction.java is refactored
        //super(sender, fee, nonce); //New super!
        this.receiver = receiver;
        this.amount = amount;
    }

    public LokiTransaction(String sender, int fee, int nonce, String receiver, int amount, String hash){
        //Generate a LokiCoin transacaction (including the hash)
        super(sender, receiver, amount, fee, nonce, hash); //Remove once Transaction.java is refactored
        //super(sender, fee, nonce, hash); //New super!
        this.receiver = receiver;
        this.amount = amount;
    }

    public Transaction lokiTransactionFromString(String txAsString){
        //parse data from a string
        //Return a new transaction based on all of that
        String sender = "jeff";
        String receiver = "Steve";
        int amount = 1;
        int fee = 1;
        int nonce = 1;
        String hash = "hash";
        return new LokiTransaction(sender, fee, nonce, receiver, amount, hash);
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

    public String getTxWithHashAsString(){
        //Condenses the details of the transaction into a single string
        String output = super.getTxWithHashAsString();
        return output + " " + getReceiver() + " " + getAmount();
    }
}