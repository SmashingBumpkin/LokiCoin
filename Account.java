import java.util.HashMap;
import java.util.Map;

public class Account {
    private int balance; //Account balance
    private final String address; //address of the account
    private int nonce; //The nonce of the account, must always go up
    private final String privKey; //The private key of the account, used for signing transactions

    //Every account which is mining needs it's own copy of the blockchain
    //This should probably be set up as a subclass of Account...

    Account(String address, String privKey){
        this.address = address;
        this.privKey = privKey;
        this.balance = 0;
        this.nonce = 0;
    }

    public int getBalance() { return this.balance; }
    public String getAddress() { return this.address; }
    public int getNonce() { return this.nonce; }
    private String getPrivKey() { return this.privKey; }

    public void debitBalance(int debit) { this.balance -= debit; }
    public void creditBalance(int credit) { this.balance += credit; }
    public void setNonce(int nonce) { this.nonce = nonce; }
    

    
    public static Map<String, String> generateKeys() {
        // Generate public key and private key
        Map<String, String> keys = new HashMap<>();
        
        //TODO: Joaquin
        String pubKey = "my public key";// logic to generate public key
        String privKey = "my private key";// logic to generate private key

        keys.put("pubKey", pubKey);
        keys.put("privKey", privKey);
        return keys;
    }

    public Account generateAccount(){
        //Returns a new account with automatically generated keys
        Map<String, String> keys = Account.generateKeys();
        return new Account(keys.get("pubKey"), keys.get("privKey"));
    }

    //TODO: public Transaction generateTransaction(){}
}