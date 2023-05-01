import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Miner extends Account{
    
    private static int prefix; //The difficulty the account is mining
    private static String prefixString;

    public Blockchain localBlockchain = new Blockchain(prefix); //The account's local copy of the blockchain

    Miner(String pubKey, String privKey){
        super(pubKey, privKey);
    }

    public Miner generateAccount(){
        //Returns a new miner with automatically generated keys
        Map<String, String> keys = Account.generateKeys();
        return new Miner(keys.get("pubKey"), keys.get("privKey"));
    }

    public static String getPrefixString(){ return Miner.prefixString;}
    public static void setPrefix(int difficulty){
        Miner.prefix = difficulty;
        Miner.prefixString = new String(new char[Miner.prefix]).replace('\0', '0'); 
    }

    public boolean checkTxValidity(Transaction tx, String senderAddress){
        Account senderAccount = this.localBlockchain.accounts.get(senderAddress);
        //Check if the sender has enough to pay the fee + amount
        //Check the transaction hash is valid
        //Check the transaction's nonce is higher than the account's
        //else return false
        return true;
    }

    public boolean executeTransaction(Transaction tx, String senderAddress){
        //checks tx then deducts amount from balance of account
        //updates nonce
        if (this.checkTxValidity(tx, senderAddress)) {
            Account senderAccount = this.localBlockchain.accounts.get(senderAddress);
            senderAccount.debitBalance(tx.getAmount() + tx.getFee());
            Account receiverAccount = localBlockchain.accounts.get(tx.getReceiver());
            receiverAccount.creditBalance(tx.getAmount());
            senderAccount.setNonce(tx.getNonce());
            return true;
        } else {
            return false;
        }
    }

    public boolean executeBlock(Block block){
        //Checks if block is legit 
        //executes it (ie completes all the transacions and updates accounts)
        //ads it to blockchain
        this.localBlockchain.addNewBlock(block);
        //sends it to 
        return true;
    }

    public static String calculateBlockHash(Block block) { //hashes the block
        String dataToHash = block.returnBlockAsStringForHashing();
        MessageDigest digest = null;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            bytes = digest.digest(dataToHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
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
    
    public Boolean validateBlock(Block block, String previousHash){
        //String prefixString = new String(new char[Miner.prefix]).replace('\0', '0'); 
        //checks if a block and it's contents are valid
        Boolean flag = block.getHash().equals(Miner.calculateBlockHash(block)) //rehashes the block to check it was hashed correctly
        && block.getPreviousHash().equals(previousHash) //checks it follows on from the block you were expecting
        && block.getHash().substring(0, Miner.prefix).equals(Miner.prefixString); //checks the hash is difficult enough
        //Doesn't check to see if transactions are valid
        return flag;
    }

    public Block mineBlock(String data, String previousHash, int previousBlockHeight){
        //Check if the data is valid
        Block newBlock = new Block(data, previousHash, this.getAddress(), previousBlockHeight);
        // newBlock.mineBlock(Miner.prefix);
        //loops until it finds a block hash that meets the difficulty requirement
        //The difficulty is the number of "0"s at the start of the hash. 

        //creates a string with "0"*diffilculty
        String difficultyString = new String(new char[Miner.prefix]).replace('\0', '0'); 

        int numberOfBlocksInPool = Network.getPotentialBlocks().size();

        while (!newBlock.getHash().substring(0, Miner.prefix).equals(difficultyString)) {
            newBlock.setNonce(newBlock.getNonce()+1); //iterates nonce to force the hash to change
            //Nerfed mining algorithm, stops it from just wasting CPU power/levels playing field
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            newBlock.setHash(Miner.calculateBlockHash(newBlock));
            if (numberOfBlocksInPool != Network.getPotentialBlocks().size()){
                numberOfBlocksInPool = Network.getPotentialBlocks().size();
                System.out.println("The number of blocks in the pool is: " + numberOfBlocksInPool);
                //This should check the latest blocks from the pool. If they are valid and longer, stop mining and start using them
            }
        }
        //TODO: parse transactions from block
        //TODO: Update accounts based on the transactions
        System.out.println("New block generated by " + this.getAddress());
        //returns valid block
        return newBlock;
    }

    public void validateBlockchain() {
        boolean flag = true;
        int errorBlock = 0;
        String previousHash = "00";
        for (int i = 0; i < this.localBlockchain.blockchain.size(); i++) {
            flag = this.validateBlock(this.localBlockchain.blockchain.get(i), previousHash);
            previousHash = this.localBlockchain.blockchain.get(i).getHash();
            if (!flag){
                errorBlock = i;
                break;
            }
        }
        if (flag){
            System.out.println("_____\nValid blockchain");
        } else {
            System.out.println("_____\nERROR: Invalid blockchain. Error in Block " + errorBlock);
        }
    }

    public void printBlockchain(){
        for (Block block : this.localBlockchain.blockchain) {
            System.out.println(block.returnBlockPrintable());
        }
        this.validateBlockchain();
    }
}
