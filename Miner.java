import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Date;

public class Miner extends Account{
    
    private static int prefix; //The difficulty the account is mining
    private static String prefixString; //Difficulty represented as prefix
    public Blockchain localBlockchain = new Blockchain(prefix); //The account's local copy of the blockchain

    Miner(){
        super();
    }

    Miner(PublicKey pubKey, PrivateKey privKey){
        super(pubKey, privKey);
    }

    public static String getPrefixString(){ return Miner.prefixString; }
    
    public static void setPrefix(int difficulty){
        Miner.prefix = difficulty;
        Miner.prefixString = new String(new char[Miner.prefix]).replace('\0', '0'); 
    }

    public boolean checkTxValidity(LokiTransaction tx) {
        //Check the transaction hash is valid
        Account account = getAccount(tx.getSender());
        //Checks if the sender has enough to pay the fee + amount
        //Checks the transaction's nonce is higher than the sender's
        if (account.getBalance() >= (tx.getAmount() + tx.getFee())
            && tx.getNonce() > account.getNonce()) {
            
            //Checks the tx was correctly signed
            try {
                Signature verifier;
                verifier = Signature.getInstance("SHA256withRSA");
                verifier.initVerify(tx.getSender());
                String txAsString = tx.getTxAsString();
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] messageHash = messageDigest.digest(txAsString.getBytes());
                verifier.update(messageHash);
                return verifier.verify(tx.getHash());
            } catch (Exception e) {
                // Something failed therefore invalid transaction
                e.printStackTrace();
                return false;
            }
        } else {
            return false;   
        }
    }

    public void executeTransaction(LokiTransaction tx) throws Exception {
        //updates nonce
        Account senderAccount = getAccount(tx.getSender());
        senderAccount.debitBalance(tx.getAmount() + tx.getFee());
        Account recipientAccount = getAccount(tx.getRecipient());
        recipientAccount.creditBalance(tx.getAmount());
        senderAccount.setNonce(tx.getNonce());
    }

    public boolean executeBlock(Block block){
        //Checks if block is legit 
        //ads it to blockchain
        this.localBlockchain.addNewBlock(block);

        //Credits rewardee
        PublicKey blockRewardee = block.getRewardRecipient();
        Account rewardee = getAccount(blockRewardee);
        rewardee.creditBalance(Block.blockReward);

        return true;
    }

    public Account getAccount(PublicKey pubKey){
        Account accounts = this.localBlockchain.accounts.get(pubKey);
        if (accounts == null){
            accounts = new Account(pubKey);
            this.localBlockchain.addNewAccount(accounts);
        }
        return accounts;
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
        }
        return CryptographyReencoding.bytesAsString(bytes);
    }
    
    public Boolean validateBlock(Block block, String previousHash){
        //String prefixString = new String(new char[Miner.prefix]).replace('\0', '0'); 
        //checks if a block and it's contents are valid
        Boolean flag = block.getHash().equals(Miner.calculateBlockHash(block)) //rehashes the block to check it was hashed correctly
        && block.getPreviousHash().equals(previousHash) //checks it follows on from the block you were expecting
        && block.getHash().substring(0, Miner.prefix).equals(Miner.prefixString); //checks the hash is difficult enough

        if (flag) {
            //Needs to check to see if transactions are valid
            //Every sender should appear at most one time
        }
        return flag;
    }

    public Block mineBlock(String data, String previousHash, int blockHeight){
        //Check if the data is valid
        Block newBlock = new Block(data, previousHash, this.getPubKey(), blockHeight);

        //creates a string with "0"*diffilculty
        String difficultyString = new String(new char[Miner.prefix]).replace('\0', '0'); 

        int numberOfBlocksInPool = Network.getPotentialBlocks().size();

        //loops until it finds a block hash that meets the difficulty requirement
        //The difficulty is the number of "0"s at the start of the hash. 
        while (!newBlock.getHash().substring(0, Miner.prefix).equals(difficultyString)) {
            newBlock.setNonce(newBlock.getNonce()+1); //iterates nonce to force the hash to change
            //Nerfed mining algorithm, stops it from just wasting CPU power/levels playing field
            newBlock.setTimeStamp(new Date().getTime());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            newBlock.setHash(Miner.calculateBlockHash(newBlock));
            if (numberOfBlocksInPool != Network.getNumberOfPotentialBlocks()){
                numberOfBlocksInPool = Network.getNumberOfPotentialBlocks();
                System.out.println("The number of blocks in the pool is: " + numberOfBlocksInPool);
                //This should check the latest blocks from the pool. If they are valid and longer, stop mining and start using them
            }
        }
        //TODO: parse transactions from block
        //TODO: Update accounts based on the transactions
        System.out.println("New block generated by " + this.getPubKey().hashCode());
        //returns valid block
        return newBlock;
    }

    public void validateBlockchain() {
        boolean flag = true;
        int errorBlock = 0;
        String previousHash = prefixString;
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

    public void printAccounts(){
        for (Account account : this.localBlockchain.accounts.values()) {
            System.out.println(account.returnAccountPrintable());
        }
        this.validateBlockchain();
    }
}
