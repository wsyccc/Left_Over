package com.bcit.Leftovers.other;

import android.util.Log;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;


import java.util.ArrayList;

/**
 * Created by Siyuan on 2016/10/30.
 */

public class MongoDB {

    private ArrayList<ServerAddress> addresses;
    private ArrayList<MongoCredential> credentials;
    public MongoDatabase db;

    public boolean mongoConnect(){
        try{
            ServerAddress address = new ServerAddress("198.199.102.182", 27017);
            addresses.add(address);

            MongoCredential credential = MongoCredential.createScramSha1Credential("leftover", "wsyccc", "67971127w".toCharArray());
            credentials.add(credential);
            MongoClient mongoClient = new MongoClient(addresses, credentials);
            db = mongoClient.getDatabase("leftover");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage());
            return false;
        }
    }
    public boolean insertValue(String collection,String json){
        try{
            DBCollection coll = (DBCollection) db.getCollection(collection);
            DBObject dbObject = (DBObject)JSON.parse(json);
            coll.insert(dbObject);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage());
            return false;
        }
    }
    public boolean update(String collection, String email, String value,
                          String updateItem, String updateValue){
        try{
            WriteResult result = null;
            BasicDBObject query = new BasicDBObject();
            BasicDBObject field = new BasicDBObject();
            field.put(email,value);
            DBCollection coll = (DBCollection) db.getCollection(collection);
            DBCursor cursor = coll.find(query,field);
            while (cursor.hasNext()){
                DBObject updateDocument = cursor.next();
                updateDocument.put(updateItem,updateValue);
                result = coll.update(field,updateDocument);
            }
            if (result.getN() > 0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage());
            return false;
        }
    }

}
