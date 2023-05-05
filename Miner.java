@@ -0,0 +1,374 @@
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Miner extends Account{
    
    private static int prefix; //The difficulty the account is mining
    private static String prefixString; //Difficulty represented as prefix
    public Blockchain localBlockchain; //The account's local copy of the blockchain
    int numberOfBlocksMinerIsAwareOf = 0;
    public volatile static boolean minersActive = true;
    public Set<Integer> blockNetworkPositions = new HashSet<Integer>();


    Miner(){
        super();
        this.localBlockchain = new Blockchain(Miner.prefix);
    }

    Miner(PublicKey pubKey, PrivateKey privKey){
        super(pubKey, privKey);
        this.localBlockchain = new Blockchain(Miner.prefix);
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

    public void executeTransaction(LokiTransaction tx) {
        //updates nonce
        Account senderAccount = getAccount(tx.getSender());
        senderAccount.debitBalance(tx.getAmount() + tx.getFee());
        Account recipientAccount = getAccount(tx.getRecipient());
        recipientAccount.creditBalance(tx.getAmount());
        senderAccount.setNonce(tx.getNonce());
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
    
    public Boolean validateBlock(Block block){
        String previousHash;
        if (block.getBlockNumber() == 0){
            previousHash = prefixString;
        } else {
            previousHash = localBlockchain.blockchain.get(block.getBlockNumber()).getHash();
        }
        //checks if a block and it's contents are valid
        Boolean flag = block.getPreviousHash().equals(previousHash) //rehashes the block to check it was hashed correctly
        && block.getHash().substring(0, Miner.prefix).equals(Miner.prefixString) //checks the hash is difficult enough
        && block.getHash().equals(Miner.calculateBlockHash(block));  //checks it follows on from the block you were expecting
        //CHECK IS TIMESTAMP IS AFTER PREVIOUS BLOCK AND BEFORE CURRENT TIME
        if (!flag) {
            return false;
        }

        //Check to see if transactions are valid
        Set<PublicKey> senders = new HashSet<PublicKey>(); 
        for (Transaction tx : block.getTransactions()){
            boolean duplicateSender = senders.add(tx.getSender());
            if (!duplicateSender){
                System.out.println("DUPLICATE SENDER IN BLOCK:\n " + tx.getTxAsString());
                break;
            }

            try {
                LokiTransaction lokiTx = new LokiTransaction(tx);
                boolean validTx = checkTxValidity(lokiTx);
                if (!validTx){
                    System.out.println("INVALID TX IN BLOCK:\n " + lokiTx.getTxPrintable());
                    break;
                }
            } catch (Exception e){
                System.out.println("\n\nERROR PARSING LOKITX");
                //Try and parse tx as a different type
            }
        }
        return flag;
    }

    public Block mineBlock(String data, String previousHash, int blockHeight){
        //Check if the data is valid
        Block newBlock = new Block(data, previousHash, this.getPubKey(), blockHeight);

        System.out.println("Blockchain height: " + localBlockchain.blockchainHeight);
        //creates a string with "0"*diffilculty
        String difficultyString = new String(new char[Miner.prefix]).replace('\0', '0'); 

        int numberOfBlocksMinerIsAwareOf = Network.getPotentialBlocks().size();

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
            if (numberOfBlocksMinerIsAwareOf != numberOfBlocksInNetwork){
                System.out.println("The number of blocks in the pool is: " + numberOfBlocksMinerIsAwareOf);
                this.addBlocksFromNetwork(numberOfBlocksInNetwork - numberOfBlocksMinerIsAwareOf);
                //TODO: Andrea

                //Get any blocks that the miner was previously unaware of from the pool
                //Check the block is valid (can be added to this miners chain)
                //Check the contents
                //This should check the latest blocks from the pool. If they are valid and longer, stop mining and start using them
            }
        }
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

    public void addBlocksFromNetwork(int numberOfNewPotentialBlocks){
        //TODO:
        //Iterate through the last newPotentialBlocks and
        //If valid and longer add them to the localBlockchain
        /* 
         * loop through newPotentialBlocks blocks from Network
         * Sort by longest n
         * begin big loop
         * Check if it claims to be the next block in the miner's chain
         * If it does, check and utilize public boolean executeBlock(Block block){
         * Else check if it's longer than current chain if it is,
         * 
         * start listOfBlockPositionInNetwork = [positionOfBlock]
         * begin loop
         *
         *      get the previous block position in network, n
         *      remove it from newPotentialBlocks if it's in there
         *      check if it's in blockNetworkPositions
         *      if it isn't add to list and continue looping
         * if it is get height h of block in position n
         * localBlockchain.get(h)
         * loop through blocks from network and check if they're legitimate successors of block h
         * using the function public Boolean validateBlock(Block block, String previousHash){
         * 
         * if they are, replace all blocks from position h onwards with the blocks from the network
         * and keep mining from there
         * end big loop
         * 
         */

         // loop through new potential blocks from network
        // Retrieve all new potential blocks at once
        List<Block> newPotentialBlocks = Network.getPotentialBlocks(numberOfNewPotentialBlocks);

        // Sort the blocks by block number in descending order
        Collections.sort(newPotentialBlocks, (b1, b2) -> Integer.compare(b2.getBlockNumber(), b1.getBlockNumber()));

        // Loop through the new potential blocks
        for (Block block : newPotentialBlocks) {

            // check if it claims to be the next block in the miner's chain
            if (block.getBlockNumber() == localBlockchain.getBlockchainHeight() + 1) {
                // execute the block if it's valid and add it to the local blockchain
                if (validateBlock(block)) {
                    executeBlock(block);
                    localBlockchain.addNewBlock(block);
                    blockNetworkPositions.remove(block.getPositionInNetwork());
                }
            } else if (block.getBlockNumber() > localBlockchain.getBlockchainHeight()) {
                // check if the potential block is longer than the current chain
                int height = -1; // height of the previous block in the potential chain
                List<Integer> listOfBlockPositionInNetwork = new ArrayList<>();
                listOfBlockPositionInNetwork.add(block.getPositionInNetwork());

                // iterate over the blocks from the potential chain in reverse order
                while (height < 0) {
                    int prevPosition = listOfBlockPositionInNetwork.get(listOfBlockPositionInNetwork.size() - 1) - 1;
                    if (prevPosition < 0) break; // reached the beginning of the potential chain

                    Block prevBlock = Network.potentialBlocks.get(prevPosition);
                    if (localBlockchain.containsBlock(prevBlock)) {
                        // found the previous block in the local blockchain, so the potential chain is invalid
                        break;
                    } else if (blockNetworkPositions.contains(prevPosition)) {
                        // found the previous block in the potential chain, so add it to the list
                        listOfBlockPositionInNetwork.add(prevPosition);
                    } else {
                        // the previous block is not in the potential chain or the local blockchain, so the potential chain is invalid
                        break;
                    }

                    // check if the previous block is a legitimate successor of the current block
                    if (!validateBlock(prevBlock, potentialBlock.getHash())) {
                        break;
                    }

                    // update height and potentialBlock for the next iteration
                    height = localBlockchain.getHeight(prevBlock.getHash()) + 1;
                    potentialBlock = prevBlock;
                }

                if (height >= 0) {
                    // found a valid longer chain
                    for (int j = listOfBlockPositionInNetwork.size() - 1; j >= 0; j--) {
                        Block blockToAdd = Network.potentialBlocks.get(listOfBlockPositionInNetwork.get(j));
                        if (executeBlock(blockToAdd)) {
                            localBlockchain.addBlock(blockToAdd);
                            blockNetworkPositions.remove(blockToAdd.getPositionInNetwork());
                        }
                    }
                }
            }
    }

    numberOfBlocksMinerIsAwareOf += numberOfNewPotentialBlocks;
}

        numberOfBlocksMinerIsAwareOf += numberOfNewPotentialBlocks;
    }

    //Each miner gets set to run, then should run continuously until they're told to stop
    //This means continuing to build a blockchain, both by mining blocks and adding blocks from the network
    public void run(){
        System.out.println("Miner " + this.getPubKey().hashCode() + " starting!");
        // List<Block> potentialBlocks = Network.getPotentialBlocks();
        // if (potentialBlocks.size() > 1){
        //     for (Block block : potentialBlocks) {

        //         //TODO: Unify potential blocks with local blockchain
        //         //Checks height of potential blocks
        //         //Resolves to longest chain 
        //         this.executeBlock(block);
        //     }
        // }
        // while (minersActive) {
        for (int i = 0; i<3; i++){
            //Gets the miner to mine a valid block
            Block newBlock = this.mineBlock(  "Miner hashcode " + this.getPubKey().hashCode(), //data should go in the first field
                                                    this.localBlockchain.getLastHash(), 
                                                    this.localBlockchain.blockchainHeight
                                                    );
            //adds it to it's local blockchain
            this.executeBlock(newBlock)
            newBlock.setBlockPositionInNetwork(Network.addPotentialBlock(newBlock));
            
        }
    }

    //Initializer to start a network for the first time
    public static Miner startNewNetwork(int difficulty) {
        Miner.setPrefix(difficulty);
        Miner miner = new Miner();

        Block genesisBlock = miner.mineBlock("The OG miner was 'ere", Miner.getPrefixString(), 0);
        miner.executeBlock(genesisBlock);
        Network.addPotentialBlock(genesisBlock);
        return miner;
    }

    public boolean executeBlock(Block block){
        //Checks if block is legit 
        //ads it to blockchain
        this.localBlockchain.addNewBlock(block);

        //Credits rewardee
        PublicKey blockRewardee = block.getRewardRecipient();
        Account rewardee = getAccount(blockRewardee);
        rewardee.creditBalance(Block.blockReward);

        for (Transaction tx :block.getTransactions()){
            try{
                LokiTransaction lokiTx = new LokiTransaction(tx);
                executeTransaction(lokiTx);
                
                //TODO: parse transactions from block
                //TODO: Update accounts based on the transactions
            } catch (Exception e) {
                //handle other transaction types here
                System.out.println("Problem executing transaction:\n" + tx.getTxAsString());
            }
        }
        return true;
    }
}