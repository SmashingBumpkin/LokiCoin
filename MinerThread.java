public class MinerThread extends Thread {
    public static Blockchain blockchain = new Blockchain(2);
    public static String data;
    public Account account;

    public MinerThread(String pubKey, String privKey) {
        super(pubKey);
        this.account = new Account(pubKey, privKey);
    }

    public void run(){
        System.out.println("Miner " + this.account.getAddress() + " starting!");
        Block newBlock = this.account.mineBlock(this.account.getAddress(), MinerThread.blockchain.getLastHash());
        synchronized (this) {
            MinerThread.blockchain.addNewBlock(newBlock);
        }
    }
}
