public class Account {
    public int balance;
    public int address;
    public int nonce;

    public Transaction generateHash(){
        return new Transaction("SuperDuperSecret");
    }
}
