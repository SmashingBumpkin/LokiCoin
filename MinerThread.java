import java.util.List;

public class MinerThread extends Thread {
    //Each miner thread is related to one miner
    //They all share a single network
    public static Network network = new Network();
    public Miner miner;

    public MinerThread(String pubKey, String privKey) {
        super(pubKey);
        this.miner = new Miner(pubKey, privKey);
        List<Block> potentialBlocks = Network.getPotentialBlocks();
        if (potentialBlocks.size() > 0){
            for (Block block : potentialBlocks){
                this.miner.executeBlock(block);
            }
        }
    }

    //Each miner gets set to run, then should run continuously until they're told to stop
    //This means continuing to build a blockchain, both by mining blocks and adding blocks from the network
    public void run(){
        System.out.println("Miner " + this.miner.getAddress() + " starting!");
        for (int i = 0; i<3; i++){
            //Gets the miner to mine a valid block
            Block newBlock = this.miner.mineBlock(  this.miner.getAddress(), //data should go in the first field
                                                    this.miner.localBlockchain.getLastHash(), 
                                                    this.miner.localBlockchain.blockchainHeight
                                                    );
            //adds it to it's local blockchain
            this.miner.localBlockchain.addNewBlock(newBlock);
            Network.addPotentialBlock(newBlock);
        }
    }


    //Initializer to start a network for the first time
    public static MinerThread startNewNetwork(int difficulty, String pubKey, String privKey){
        Miner.setPrefix(difficulty);
        MinerThread minerThread = new MinerThread( pubKey, privKey);

        Block genesisBlock = minerThread.miner.mineBlock("The OG miner was 'ere", Miner.getPrefixString(), -1);
        minerThread.miner.executeBlock(genesisBlock);
        Network.addPotentialBlock(genesisBlock);
        return minerThread;
    }   
}