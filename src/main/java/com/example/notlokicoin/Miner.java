package com.example.notlokicoin;

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
    public Set<Integer> blockNetworkPositions = new HashSet<>();

    public static int sleeptime = 10;
    boolean validFork;


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

    public boolean checkTxValidity(LokiTransaction tx, Block txBlock) {
        //Check the transaction hash is valid
        Account account = txBlock.getAccount(tx.getSender());
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

    public void executeTransaction(LokiTransaction tx, Block block) {
        //updates nonce
        Account senderAccount = block.getAccount(tx.getSender());
        senderAccount.debitBalance(tx.getAmount() + tx.getFee());
        Account recipientAccount = block.getAccount(tx.getRecipient());
        recipientAccount.creditBalance(tx.getAmount());
        senderAccount.setNonce(tx.getNonce());
    }

    public Account getAccount(PublicKey pubKey){
        Account account = this.localBlockchain.getLastBlock().getAccount(pubKey);
        if (account == null){
            account = new Account(pubKey);
            this.localBlockchain.addNewAccount(account);
        }
        return account;
    }

    public static String calculateBlockHash(Block block) { //hashes the block
        String dataToHash = block.returnBlockAsStringForHashing();
        MessageDigest digest;
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
        try {
            return validateBlockPair(localBlockchain.getBlock(block.getBlockNumber() - 1), block);
        } catch (IndexOutOfBoundsException e) {
            if (block.getBlockNumber()==0) return true;
            else return false;
        }
    }

    public Boolean validateBlockPair(Block prevBlock, Block nextBlock){
        String previousHash;

        try {
            previousHash = prevBlock.getHash();
        } catch (Exception e){
            previousHash = prefixString;
        }
        //checks if a block and it's contents are valid
        Boolean flag = nextBlock.getPreviousHash().equals(previousHash) //rehashes the block to check it was hashed correctly
                && nextBlock.getHash().substring(0, Miner.prefix).equals(Miner.prefixString) //checks the hash is difficult enough
                && nextBlock.getHash().equals(Miner.calculateBlockHash(nextBlock))
                && nextBlock.getBlockNumber() == prevBlock.getBlockNumber()+1;  //checks it follows on from the block you were expecting
        // System.out.println("Block "+ nextBlock.getBlockNumber() + " is "
        //                     + nextBlock.getPreviousHash().equals(previousHash) //rehashes the block to check it was hashed correctly
        //                     + nextBlock.getHash().substring(0, Miner.prefix).equals(Miner.prefixString) //checks the hash is difficult enough
        //                     + nextBlock.getHash().equals(Miner.calculateBlockHash(nextBlock))
        //                     + nextBlock.getBlockNumber() + (prevBlock.getBlockNumber()+1));

        //TODO: CHECK IS TIMESTAMP IS AFTER PREVIOUS BLOCK AND BEFORE CURRENT TIME
        if (!flag) {
            return flag;
        }

        //Check to see if transactions are valid
        nextBlock.setAccounts(prevBlock.getAccounts());
        for (Transaction tx : nextBlock.getTransactions()){
            try {
                LokiTransaction lokiTx = new LokiTransaction(tx);
                boolean validTx = checkTxValidity(lokiTx, nextBlock);
                if (!validTx){
                    System.out.println("INVALID TX IN BLOCK:\n " + lokiTx.getTxPrintable());
                    break;
                }
                executeTransaction(lokiTx, nextBlock);
            } catch (Exception e){
                System.out.println("\n\nERROR PARSING LOKITX");
                //Try and parse tx as a different type
            }
        }
        nextBlock.setAccounts(prevBlock.getAccounts());
        return flag;
    }

    // public Block mineBlock(String data, String previousHash, int blockHeight,int previousBlockPositionInNetwork){
    public void mineBlock(String data, String previousHash, int blockHeight,int previousBlockPositionInNetwork){
        //Check if the data is valid
        Block newBlock = new Block(data, previousHash, this.getPubKey(), blockHeight, previousBlockPositionInNetwork);
        //creates a string with "0"*difficulty
        String difficultyString = new String(new char[Miner.prefix]).replace('\0', '0');

        int numberOfBlocksMinerIsAwareOf = Network.getPotentialBlocks().size();
        boolean validAdd = false;
        //loops until it finds a block hash that meets the difficulty requirement
        //The difficulty is the number of "0"s at the start of the hash.
        while (!newBlock.getHash().substring(0, Miner.prefix).equals(difficultyString)) {
            newBlock.setNonce(newBlock.getNonce()+1); //iterates nonce to force the hash to change
            //Nerfed mining algorithm, stops it from just wasting CPU power/levels playing field
            newBlock.setTimeStamp(new Date().getTime());
            try {
                Thread.sleep(Miner.sleeptime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            newBlock.setHash(Miner.calculateBlockHash(newBlock));
            int numberOfBlocksInNetwork = Network.getNumberOfPotentialBlocks();
            if (numberOfBlocksMinerIsAwareOf != numberOfBlocksInNetwork){
                validAdd = this.addBlocksFromNetwork(numberOfBlocksInNetwork);
                numberOfBlocksMinerIsAwareOf = numberOfBlocksInNetwork;
                if (validAdd){
                    break;
                }
            }
        }

        if (!validAdd) {
            newBlock.setBlockPositionInNetwork(Network.addPotentialBlock(newBlock));
            numberOfBlocksMinerIsAwareOf++;
            this.executeBlock(newBlock);
            System.out.println("Block " + newBlock.getBlockNumber() +" generated by " + this.getPubKey().hashCode());
        }
        //returns valid block
        // return newBlock;
    }

    public boolean addBlocksFromNetwork(int numberOfBlocksInNetwork){
        // loop through new potential blocks from network
        // Retrieve all new potential blocks at once

        List<Block> newPotentialBlocks = Network.getPotentialBlocks(numberOfBlocksMinerIsAwareOf, numberOfBlocksInNetwork);
        // Sort the blocks by block number in descending order
        Collections.sort(newPotentialBlocks, (b1, b2) -> Integer.compare(b2.getBlockNumber(), b1.getBlockNumber()));
        // System.out.println(getPubKey().hashCode() + " last " + localBlockchain.getLastBlock().getBlockNumber());
        // for (Block blk : newPotentialBlocks){
        //     System.out.println(getPubKey().hashCode() + "  " + blk.getBlockNumber());
        // }
        // Loop through the new potential blocks
        validFork = false;
        for (Block block : newPotentialBlocks) {
            int blockNum = block.getBlockNumber();
            int chainHeight = localBlockchain.getBlockchainHeight();
            System.out.println("Miner "+getPubKey().hashCode()+" checking block # "+blockNum+" from "+block.getRewardRecipient().hashCode());
            // check if it claims to be the next block in the miner's chain
            if (blockNum == chainHeight) {
                System.out.println("and it's the next block in "+ getPubKey().hashCode() + "'s chain");
                // execute the block if it's valid and add it to the local blockchain
                if (validateBlock(block)) {
                    executeBlock(block);
                    validFork = true;
                    break;
                }
            } else if (blockNum > chainHeight) {
                System.out.println("and it's ahead of "+ getPubKey().hashCode() + "'s chain");
                // check if the potential block is longer than the current chain
                // height of the previous block in the potential chain
                List<Integer> listOfBlockPositionInNetwork = new ArrayList<>();
                listOfBlockPositionInNetwork.add(block.getPositionInNetwork());
                Integer lastBlockPosition = block.getPreviousPositionInNetwork();


                // iterate over the blocks from the potential chain in reverse order
                while (!blockNetworkPositions.contains(lastBlockPosition)) {
                    Block prevBlock = Network.getBlock(lastBlockPosition);
                    listOfBlockPositionInNetwork.add(prevBlock.getPositionInNetwork());
                    lastBlockPosition = prevBlock.getPreviousPositionInNetwork();
                }

                //gets fork point from network
                int forkPoint = Network.getBlock(lastBlockPosition).getBlockNumber();
                validFork = true;

                //iterates over blocks from the fork point and checks if they are valid
                Block prevBlock = localBlockchain.getBlock(forkPoint);
                for (int i = listOfBlockPositionInNetwork.size() - 1; i >= 0; i--) {
                    Block nextBlock = Network.getBlock(listOfBlockPositionInNetwork.get(i));

                    if (!validateBlockPair(prevBlock, nextBlock)){
                        System.out.println("Block " + prevBlock.getBlockNumber() + " from miner " + prevBlock.getRewardRecipient().hashCode()
                                + " is invalid");
                        validFork = false;
                        break;
                    }
                    prevBlock = nextBlock;
                }
                //if all the fork blocks are valid, the chain needs to reshuffle
                if (validFork){
                    System.out.println("Miner "+getPubKey().hashCode()+" found a valid fork and is reshuffling it's blockchain");
                    //remove all blocks from forkPoint onwards
                    List<Integer> networkPositions = localBlockchain.removeBlocksAfterBlockX(forkPoint);

                    //Remove all network positions greater than position at forkPoint
                    blockNetworkPositions.removeAll(networkPositions);
                    //Loop through and add the blocks to the network
                    for (int i = listOfBlockPositionInNetwork.size() - 1; i >= 0; i--) {
                        Block nextBlock = Network.getBlock(listOfBlockPositionInNetwork.get(i));
                        executeBlock(nextBlock);
                    }
                    break;
                }
            } else {
                System.out.println("and it's behind this chain");
                //if the blocks in the network are shorter than the current chain the loop should break
                break;
            }
        }
        System.out.println("Miner " + this.getPubKey().hashCode() + " added blocks: " + validFork);
        numberOfBlocksMinerIsAwareOf = numberOfBlocksInNetwork;
        return validFork;
    }

    //Each miner gets set to run, then should run continuously until they're told to stop
    //This means continuing to build a blockchain, both by mining blocks and adding blocks from the network
    public void run(){
        System.out.println("Miner " + this.getPubKey().hashCode() + " starting!");
        if (localBlockchain.getBlockchainHeight() == 0){
            this.executeBlock(Network.getBlock(0));
            this.numberOfBlocksMinerIsAwareOf++;
        }
        if (Network.getNumberOfPotentialBlocks() > this.numberOfBlocksMinerIsAwareOf){
            System.out.println("Synchronizing with network");
            this.addBlocksFromNetwork(Network.getNumberOfPotentialBlocks() - this.numberOfBlocksMinerIsAwareOf);
        }
        while (minersActive) {
            // for (int i = 0; i<30; i++){
            //Gets the miner to mine a valid block
            // Block newBlock = this.mineBlock(  "Miner hashcode " + this.getPubKey().hashCode(), //data should go in the first field
            this.mineBlock(  "Miner hashcode " + this.getPubKey().hashCode(), //data should go in the first field
                    this.localBlockchain.getLastHash(),
                    this.localBlockchain.getBlockchainHeight(),
                    this.localBlockchain.getLastBlock().getPositionInNetwork()
            );
            //adds it to it's local blockchain
            // newBlock.setBlockPositionInNetwork(Network.addPotentialBlock(newBlock));
        }
        // this.validateBlockchain();
    }

    //Initializer to start a network for the first time
    public static Miner startNewNetwork(int difficulty) {
        Miner.setPrefix(difficulty);
        Miner miner = new Miner();

        // Block genesisBlock = miner.mineBlock("The OG miner was 'ere", Miner.getPrefixString(), 0,-1);
        miner.mineBlock("The OG miner was 'ere", Miner.getPrefixString(), 0,-1);
        // genesisBlock.setBlockPositionInNetwork(Network.addPotentialBlock(genesisBlock));
        // miner.executeBlock(genesisBlock);
        miner.numberOfBlocksMinerIsAwareOf++;
        return miner;
    }

    public boolean executeBlock(Block block){
        //Checks if block is legit
        //ads it to blockchain
        this.localBlockchain.addNewBlock(block);
        this.blockNetworkPositions.add(block.getPositionInNetwork());

        //Credits rewardee
        PublicKey blockRewardee = block.getRewardRecipient();
        Account rewardee = getAccount(blockRewardee);
        rewardee.creditBalance(Block.blockReward);

        for (Transaction tx :block.getTransactions()){
            try{
                LokiTransaction lokiTx = new LokiTransaction(tx);
                executeTransaction(lokiTx, block);

                //TODO: parse transactions from block
                //TODO: Update accounts based on the transactions
            } catch (Exception e) {
                //handle other transaction types here
                System.out.println("Problem executing transaction:\n" + tx.getTxAsString());
            }
        }
        return true;
    }
    boolean flag = true;
    public void validateBlockchain() {

        int errorBlock = 0;
        for (int i = 0; i < this.localBlockchain.blockchain.size(); i++) {
            flag = this.validateBlock(this.localBlockchain.blockchain.get(i));

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
        System.out.println("\n\nMiner " + this.getPubKey().hashCode() + " printing blockchain:\n");
        for (Block block : this.localBlockchain.blockchain) {
            System.out.println(block.returnBlockPrintable());
        }
        this.validateBlockchain();
    }

    public void printStatus(){
        System.out.print("Miner " + this.getPubKey().hashCode());
        System.out.println( " has finished on block " + this.localBlockchain.getLastBlock().getBlockNumber()
                + " mined by " + this.localBlockchain.getLastBlock().getRewardRecipient().hashCode());
    }

    public void printAccounts(){
        for (Account account : this.localBlockchain.getLastBlock().getAccounts().values()) {
            System.out.println(account.returnAccountPrintable());
        }
        this.validateBlockchain();
    }
    public String printAccounts2(){
        String y = "";
        for (Account account : this.localBlockchain.getLastBlock().getAccounts().values()) {
            y = y + System.lineSeparator() + (account.returnAccountPrintable());
        }
        //this.validateBlockchain();
        return y;
    }
}