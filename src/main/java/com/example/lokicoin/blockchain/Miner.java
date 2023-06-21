package com.example.lokicoin.blockchain;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.*;

public class Miner extends Account{

    private static int prefix; //The difficulty the account is mining
    private static String prefixString; //Difficulty represented as prefix
    public Blockchain localBlockchain; //The account's local copy of the blockchain
    int numberOfBlocksMinerIsAwareOf = 0;
    public volatile static boolean minersActive = true;
    public Set<Integer> blockNetworkPositions = new HashSet<>();
    private int lastAddedTransactionPositionInNetwork = 0;
    public static int sleeptime = 10;
    public boolean validatingBlockchainFlag = false;


    public Miner(){
        super();
        this.localBlockchain = new Blockchain(Miner.prefix);
    }

    Miner(PublicKey pubKey, PrivateKey privKey){
        super(pubKey, privKey);
        this.localBlockchain = new Blockchain(Miner.prefix);
    }

    //Initializer to start a network for the first time
    public static Miner startNewNetwork(int difficulty) {
        Miner.setPrefix(1);
        Miner miner = new Miner();
        miner.mineBlock("The OG miner was 'ere", Miner.getPrefixString(), 0,-1);
        Miner.setPrefix(difficulty);
        return miner;
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
        block.debitAccount(tx.getSender(), tx.getAmount() + tx.getFee());
        block.creditAccount(tx.getRecipient(), tx.getAmount());
        block.setAccountNonce(tx.getSender(), tx.getNonce());
        if (tx.getSender() == this.getPubKey()){
            this.setNonce(tx.getNonce());
        }
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
            return block.getBlockNumber()==0;
        }
    }

    public Boolean validateBlockPair(Block prevBlock, Block nextBlock){
        String previousHash;
        try {
            previousHash = prevBlock.getHash();
        } catch (Exception e){
            previousHash = "0";
        }
        //checks if a block and it's contents are valid
        Boolean flag = nextBlock.getPreviousHash().equals(previousHash) //rehashes the block to check it was hashed correctly
                        && nextBlock.getHash().substring(0, Miner.prefix).equals(Miner.prefixString) //checks the hash is difficult enough
                        && nextBlock.getHash().equals(Miner.calculateBlockHash(nextBlock))
                        && nextBlock.getBlockNumber() == prevBlock.getBlockNumber()+1;  //checks it follows on from the block you were expecting
        //TODO: CHECK IS TIMESTAMP IS AFTER PREVIOUS BLOCK AND BEFORE CURRENT TIME
        if (!flag) {
            return false;
        }

        //Check to see if transactions are valid
        Block tempBlock = new Block(prevBlock);
        for (Transaction tx : nextBlock.getTransactions()){
            try {
                LokiTransaction lokiTx = new LokiTransaction(tx);
                boolean validTx = checkTxValidity(lokiTx, tempBlock);
                if (!validTx){
                    System.out.println("INVALID TX IN BLOCK " + nextBlock.getBlockNumber());
                    System.out.println(lokiTx.getTxPrintable());
                    System.out.println(tempBlock.returnBlockPrintable());
                    System.out.println(tempBlock.getAccount(tx.getSender()).getAccountPrintable());
                    flag = false;
                    break;
                }
                executeTransaction(lokiTx, tempBlock);
            } catch (Exception e){
                System.out.println("\n\nERROR PARSING LOKITX");
                //Try and parse tx as a different type
            }
        }
        tempBlock.creditAccount(nextBlock.getRewardRecipient(),Block.blockReward);
        for (Account account : nextBlock.getAccounts().values()){
            try {
                if (account.getBalance() != tempBlock.getAccount(account.getPubKey()).getBalance()) {
                    System.out.println(account.getPubKey().hashCode() + " found unexpected balance in block.");
                    flag = false;
                    break;
                }
            } catch (NullPointerException e) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public void executeBlock(Block block){
        this.localBlockchain.addNewBlock(new Block(block));
        this.blockNetworkPositions.add(block.getPositionInNetwork());
    }

    public void validateBlockchain() {
        boolean flag = true;
        int errorBlock = 0;
        int totalTransactions = 0;
        for (int i = 0; i < this.localBlockchain.blockchain.size(); i++) {
            Block block = this.localBlockchain.blockchain.get(i);
            totalTransactions += block.getTransactions().size();
            flag = this.validateBlock(block);
            if (!flag){
                errorBlock = i;
                break;
            }
        }
        if (flag){
            System.out.println("_____\nValid blockchain of height "+ localBlockchain.getBlockchainHeight() + " with "+ totalTransactions+" successful transactions");
        } else {
            System.out.println("_____\nERROR: Invalid blockchain. Error in Block " + errorBlock);
        }
    }

    public void mineBlock(String data, String previousHash, int blockHeight, int previousBlockPositionInNetwork){
        //Check if the data is valid
        Block newBlock;
        Random R = new Random();
        try {
            newBlock = new Block(data, previousHash, this.getPubKey(), blockHeight, previousBlockPositionInNetwork,
                    localBlockchain.getBlock(blockHeight - 1).getAccounts());
        } catch (IndexOutOfBoundsException e){
            newBlock = new Block(data, previousHash, this.getPubKey(), blockHeight, previousBlockPositionInNetwork,
                    null);
        }
        //creates a string with "0"*difficulty
        String difficultyString = new String(new char[Miner.prefix]).replace('\0', '0');
        List<Transaction> transactions = new ArrayList<>();
        int networkTxCount = Network.getNumberOfPotentialTransactions();
        if (networkTxCount > lastAddedTransactionPositionInNetwork){
            transactions = Network.getPotentialTransactions(lastAddedTransactionPositionInNetwork,networkTxCount);
            lastAddedTransactionPositionInNetwork = networkTxCount;
        }
        //This line can be uncommented which will cause more forks to happen
//        int numberOfBlocksMinerIsAwareOf = Network.getPotentialBlocks().size();

        for (Transaction tx : transactions){
            try {
                LokiTransaction lokiTx = new LokiTransaction(tx);
                if (this.checkTxValidity(lokiTx, newBlock)) {
                    executeTransaction(lokiTx, newBlock);
                    newBlock.addTransaction(tx);
                }
            } catch(Exception e){
                System.out.println("Invalid LokiTx type");
            }
        }

        //loops until it finds a block hash that meets the difficulty requirement, or finds a block on the network
        //The difficulty is the number of "0"s at the start of the hash.
        boolean validAdd = false;
        int nextNetworkCheck = R.nextInt(50);
        while (!newBlock.getHash().substring(0, Miner.prefix).equals(difficultyString)
                && minersActive) {
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
            if (this.numberOfBlocksMinerIsAwareOf != numberOfBlocksInNetwork
                && R.nextInt(70) <= 1){ //chance of miner checking the network for new blocks
                validAdd = this.addBlocksFromNetwork(numberOfBlocksInNetwork);
                this.numberOfBlocksMinerIsAwareOf = numberOfBlocksInNetwork;
                if (validAdd){
                    break;
                }
            }
        }

        if (!validAdd && minersActive) {
            newBlock.creditAccount(this.getPubKey(),Block.blockReward);
            newBlock.setBlockPositionInNetwork(Network.addPotentialBlock(new Block(newBlock)));
            this.executeBlock(newBlock);
            this.numberOfBlocksMinerIsAwareOf++;
            System.out.println("Block " + newBlock.getBlockNumber() +" generated by " + this.getPubKey().hashCode());
        }
    }

    public boolean addBlocksFromNetwork(int numberOfBlocksInNetwork){
        // loop through new potential blocks from network
        // Retrieve all new potential blocks at once

        List<Block> newPotentialBlocks = Network.getPotentialBlocks(numberOfBlocksMinerIsAwareOf, numberOfBlocksInNetwork);
        // Sort the blocks by block number in descending order
        Collections.sort(newPotentialBlocks, (b1, b2) -> Integer.compare(b2.getBlockNumber(), b1.getBlockNumber()));
        boolean validFork = false;
        for (Block networkBlock : newPotentialBlocks) {
            Block block = new Block(networkBlock);
            int blockNum = block.getBlockNumber();
            int chainHeight = localBlockchain.getBlockchainHeight();
            // check if it claims to be the next block in the miner's chain
            if (blockNum == chainHeight) {
                // execute the block if it's valid and add it to the local blockchain
                if (validateBlock(block)) {
                    executeBlock(block);
                    validFork = true;
                    break;
                }
            }
            if (blockNum >= chainHeight) {
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
                Block prevBlock = new Block(localBlockchain.getBlock(forkPoint));
                for (int i = listOfBlockPositionInNetwork.size() - 1; i >= 0; i--) {
                    Block nextBlock = new Block(Network.getBlock(listOfBlockPositionInNetwork.get(i)));

                    if (!validateBlockPair(prevBlock, nextBlock)){
                        System.out.println("Block " + nextBlock.getBlockNumber() + " from miner " + nextBlock.getRewardRecipient().hashCode()
                                + " is invalid");
                        validFork = false;
                        break;
                    }
                    prevBlock = nextBlock;
                }
                //if all the fork blocks are valid, the chain needs to reshuffle
                if (validFork){
                    validatingBlockchainFlag = true;
                    if (listOfBlockPositionInNetwork.size() > 2) {
                        System.out.println("Miner " + getPubKey().hashCode() + " is syncing with the last " +
                                (listOfBlockPositionInNetwork.size() - 1) + " blocks from another chain.");
                    }
                    //remove all blocks from forkPoint onwards
                    List<Integer> networkPositions = localBlockchain.removeBlocksAfterBlockX(forkPoint);

                    //Remove all network positions greater than position at forkPoint
                    networkPositions.forEach(blockNetworkPositions::remove);
//                    blockNetworkPositions.removeAll(networkPositions);
                    //Loop through and add the blocks to the network
                    for (int i = listOfBlockPositionInNetwork.size() - 1; i >= 0; i--) {
                        Block nextBlock = new Block(Network.getBlock(listOfBlockPositionInNetwork.get(i)));
                        executeBlock(nextBlock);
                    }
                    validatingBlockchainFlag = false;
                    break;
                }
            } else {
                //if the blocks in the network are shorter than the current chain the loop should break
                break;
            }
        }
        numberOfBlocksMinerIsAwareOf = numberOfBlocksInNetwork;
        return validFork;
    }

    //Each miner gets set to run, then should run continuously until they're told to stop
    //This means continuing to build a blockchain, both by mining blocks and adding blocks from the network
    public void run(){
        System.out.println("Miner " + this.getPubKey().hashCode() + " starting! Commencing Network synchronization");
        if (localBlockchain.getBlockchainHeight() == 0){
            this.executeBlock(Network.getBlock(0));
            this.numberOfBlocksMinerIsAwareOf++;
        }
        if (Network.getNumberOfPotentialBlocks() > this.numberOfBlocksMinerIsAwareOf){
            this.addBlocksFromNetwork(Network.getNumberOfPotentialBlocks() - this.numberOfBlocksMinerIsAwareOf);
        }
        while (minersActive) {
            //Gets the miner to mine a valid block
            this.mineBlock(  "Miner hashcode " + this.getPubKey().hashCode(), //data should go in the first field
                    this.localBlockchain.getLastHash(),
                    this.localBlockchain.getBlockchainHeight(),
                    this.localBlockchain.getLastBlock().getPositionInNetwork()
            );
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
        System.out.println("\nMiner "+getPubKey().hashCode() + " reports:");
        this.validateBlockchain();
        this.localBlockchain.getLastBlock().printAccounts();
    }

    public String getAccounts() {
        return this.localBlockchain.getLastBlock().getAccountsAsString();
    }
}