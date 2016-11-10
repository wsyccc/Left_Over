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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Siyuan on 2016/10/30.
 */

public class MongoDB {

    private static ArrayList<ServerAddress> addresses = new ArrayList<>();
    private static ArrayList<MongoCredential> credentials = new ArrayList<>();
    public MongoDatabase db;


    public boolean mongoConnect(){
        try{
            ServerAddress address = new ServerAddress("198.199.102.182", 27017);
            addresses.add(address);
            MongoCredential credential = MongoCredential.createCredential("wsyccc", "leftover", "67971127w".toCharArray());
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
            MongoCollection coll = db.getCollection(collection);
            DBObject dbObject = (DBObject)JSON.parse(json);
            coll.insertOne(dbObject);
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
            BasicDBObject field = new BasicDBObject();
            field.put(email,value);
            DBCollection coll = (DBCollection) db.getCollection(collection);
            DBCursor cursor = coll.find(field);
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
    public JSONObject find(String collection, String key, String value){
        try{
            BasicDBObject field = new BasicDBObject();
            field.put(key,value);
            MongoCollection coll = db.getCollection(collection);
            MongoCursor<Document> cursor = coll.find(field).iterator();
            Bson result= cursor.next();
            JSONObject output = new JSONObject(JSON.serialize(result));
            return output;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(e.getClass().getName(), e.getMessage());
            return null;
        }
    }

}
