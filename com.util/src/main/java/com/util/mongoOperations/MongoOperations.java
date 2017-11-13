package com.util.mongoOperations;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.util.ArrayList;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoOperations {
	private MongoClient mongo = null;
	private MongoDatabase db = null;
	CodecRegistry pojoCodecRegistry;

	public static MongoOperations instance = null;

	public static MongoOperations getInstance(boolean runOnCf) {
		if (instance == null) {
			instance = new MongoOperations(runOnCf);
		}
		return instance;
	}

	public MongoOperations(boolean runOnCf) {

		if (runOnCf == true) {

			String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
			JSONParser parser = new JSONParser();
			JSONObject o;
			try {
				o = (JSONObject) parser.parse(VCAP_SERVICES);

				JSONArray j = (JSONArray) o.get("mongodb");
				JSONObject cred = (JSONObject) ((JSONObject) ((JSONObject) j.get(0)).get("credentials"));

				String textUri = cred.get("uri").toString();
				String dbName = cred.get("dbname").toString();
				bindToDb(textUri, dbName);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void bindToDb(String textUri, String dbName) {
		pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		try {
			MongoClientURI uri = new MongoClientURI(textUri);
			mongo = new MongoClient(uri);
			db = mongo.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void createCollection(String collectionName) {

		boolean collectionExists = db.listCollectionNames().into(new ArrayList<String>()).contains(collectionName);
		if (!collectionExists) {
			db.createCollection(collectionName);
		}
	}

	public String showAllCollections() {
		StringBuilder totalString = new StringBuilder();
		MongoIterable<String> colls = db.listCollectionNames();

		for (String s : colls) {
			totalString.append(s + ",");
		}
		return totalString.toString();
	}

	public MongoDatabase getDb() {
		return db;
	}
}
