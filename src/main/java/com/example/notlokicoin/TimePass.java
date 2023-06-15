package com.example.notlokicoin;
import java.util.ArrayList;

public class TimePass {
        public static ArrayList<Miner> listm = new ArrayList<>();
        public static Miner miner69 = new Miner();
        public static int getMinerInt(){return miner69.getBalance();}

        public static void run() {
            listm.add(miner69);
            System.out.println(miner69.getBalance());
            //miner69.balance = 69;
        }
        //MyObject obj = new MyObject("Initial value");

        // Modify the object
        //obj.change("hahahah", obj);

        //System.out.println(miner69.getBalance());
        // Retrieve the object from the list
        //MyObject retrievedObj = list.get(0);
        //System.out.println(retrievedObj.getValue());  // Output: "Modified value"
    }



class MyObject {
    private String value;

    public MyObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public void change(String changed, MyObject object) {
        object.setValue(changed);
    }
}
