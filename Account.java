public class Account {
    public int balance;
    public String address;
    public int nonce;

    Account(String address){
        this.address = address;
        this.balance = 0;
        this.nonce = 0;
    }

    
}