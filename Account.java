import java.util.HashMap;
import java.util.Map;

public class Account {
    public int balance;
    public final String address;
    public int nonce;
    private final String privKey;
    public static int prefix;

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
        Map<String, String> keys = Account.generateKeys();
        return new Account(keys.get("pubKey"), keys.get("privKey"));
    }

    public boolean checkTxValidity(int fee, int amount, int nonce){
        //asserts the balance is high enough 
        //asserts the nonce has increased
        if (this.balance >= fee + amount && this.nonce > nonce) {
            return true;
        } else {
            return false;
        }
    }

    public boolean debitAccount(int fee, int amount, int nonce){
        //check tx then deducts amount from balance of account
        //updates nonce
        if (this.checkTxValidity(fee, amount, nonce)) {
            this.balance -= amount;
            this.nonce = nonce;
            return true;
        } else {
            return false;
        }
    }

    public void creditAccount(int amount){
        //deducts amount from balance of account
        //updates nonce
        this.balance += amount;
    }

    public Block mineBlock(String data, String previousHash){
        Block newBlock = new Block(data, previousHash, this.address);
        newBlock.mineBlock(Account.prefix);
        return newBlock;
    }
}