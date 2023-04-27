//All these imports need to be properly understood
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.logging.Level;

public class Block {
    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;
    private static int longestChain = 0;
    private int blockNumber;
 
    public Block(String data, String previousHash, long timeStamp) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = timeStamp;
        this.hash = calculateBlockHash();
        Block.longestChain++;
        this.blockNumber = Block.longestChain;
    }

    public Boolean validateBlock(String previousHash, int prefix, String prefixString){
        //checks if a block and it's contents are valid
        Boolean flag = this.getHash().equals(this.calculateBlockHash())
        && previousHash.equals(this.getPreviousHash())
        && this.getHash().substring(0, prefix).equals(prefixString);
        return flag;
    }

    public String getHash() {
        return this.hash;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public String getData() {
        return this.data;
    }

    public long getTimeStamp(){
        return this.timeStamp;
    }

    public int nonce(){
        return this.nonce;
    }
    
    public int blockNumber(){
        return this.blockNumber;
    }

    public String returnBlockPrintable(){
        String output = "__________\nBLOCK " + this.blockNumber + "\n\n"
                         + this.data + "\n\n" + "Hash:\n" + this.hash + "\n__________";
        return output;
    }

    public String calculateBlockHash() {
        String dataToHash = previousHash 
        + Long.toString(timeStamp) 
        + Integer.toString(nonce) 
        + data;
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            /*The line logger.log(Level.SEVERE, ex.getMessage()); is giving an error because
            the logger instance variable "logger" has not been initialized or defined within 
            the Block class. It seems that this code was taken from a larger project where the 
            logger was already defined and imported in some other file.

            To fix this error, you need to define a logger instance variable within the Block 
            class or import the logger from another file where it was previously defined. Without 
            knowing more about the context of this code snippet, it's difficult to suggest a 
            specific solution for resolving this issue. */
            System.out.println("ERROR");
            System.out.println(ex.getMessage());
            //logger.log(Level.SEVERE, ex.getMessage());
        }
        StringBuffer buffer = new StringBuffer();
        for (byte b : bytes) {
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }

    public String mineBlock(int prefix) {
        String prefixString = new String(new char[prefix]).replace('\0', '0');
        while (!hash.substring(0, prefix).equals(prefixString)) {
            nonce++;
            //Nerfed mining algorithm, stops it from just wasting CPU power/levels playing field
            try {
                Thread.sleep(1);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
            hash = calculateBlockHash();
        }
        //TODO: parse transactions from block
        //TODO: Update accounts based on the transactions
        System.out.println("New block generated");
        this.broadcastBlock();
        return hash;
    }

    public void broadcastBlock(){
        //Broadcasts the newly mined block to the blockchain
        System.out.println("Broadcasting block");
    }
}