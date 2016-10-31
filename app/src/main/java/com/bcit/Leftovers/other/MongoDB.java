package com.bcit.Leftovers.other;

import android.util.Log;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;

/**
 * Created by Siyuan on 2016/10/30.
 */

public class MongoDB {

    public ArrayList<ServerAddress> addresses;
    public ArrayList<MongoCredential> credentials;

    public boolean mongoConnect(){
        try{
            ServerAddress address = new ServerAddress("198.199.102.182", 27017);
            addresses.add(address);

            MongoCredential credential = MongoCredential.createScramSha1Credential("admin", "king", "67971127w".toCharArray());
            credentials.add(credential);
            MongoClient mongoClient = new MongoClient(addresses, credentials);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("king");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage());
            return false;
        }
    }
}
