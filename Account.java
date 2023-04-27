public class Account {
    public int balance;
    public String address;
    public int nonce;

    Account(String address){
        this.address = address;
        this.balance = 0;
        this.nonce = 0;
    }

    public boolean checkTxValidity(int balance, int nonce){
        //asserts the balance is high enough 
        //asserts the nonce has increased
        return false;
    }

    public void updateAccount(int balance, int nonce){
        this.checkTxValidity(balance, nonce);
        //deducts amount from balance of account
        //updates nonce
    }
}