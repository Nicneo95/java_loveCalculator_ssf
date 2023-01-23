package com.example.loveCalculator.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;


// 1. Create the Model/Input of the application 
// Implement Serializable Class - object will be converted to a sequence of bytes
public class Person implements Serializable {
    
    // 2. Specify the attribute required for this application 
    
    private String fname;
    private String sname;
    private String percentage;
    private String result;
    private String id;
    private String compatibility;

    public Person() {
        this.id = generateId(8);
    }
    public Person(String fname, String sname) {
        this.id = generateId(8);
        this.fname =fname;
        this.sname = sname;
    }

    // 3. Create getters and setters
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPercentage() {
        return percentage;
    }
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCompatibility() {
        return compatibility;
    }
    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    // 4. Read Json data and store in Person Object
    public static Person create(String json) throws IOException {
        Person p = new Person();
        // Create an inputstream - read the bytes of the json string
        try (InputStream is = new ByteArrayInputStream(json.getBytes())) {
            //  JsonReader is used to read JSON data from an InputStream
            JsonReader r = Json.createReader(is);
            //  parse the JSON data from the input stream and create a JsonObject
            JsonObject o = r.readObject(); 
            // JsonObject 'o' created previously to extract the values of the different keys in the JSON data. The getString() method is used to retrieve the values of the keys "fname", "sname", "percentage" and "result" from the JsonObject
            p.setFname(o.getString("fname"));
            p.setSname(o.getString("sname"));
            p.setPercentage(o.getString("percentage"));
            p.setResult(o.getString("result"));
            if (Integer.parseInt(o.getString("percentage"))>75){
                p.setCompatibility("compatible!");
            } else {
                p.setCompatibility("not compatible!");
            }

        }
        // return an instance of the object 'p' with the fields populated with values from the JSON data
        return p;
    }
    // Create a method to generate id
    private synchronized String generateId(int numChars) {
        Random r = new Random();
        StringBuilder strBuilder = new StringBuilder();
        while (strBuilder.length() < numChars) {
            strBuilder.append(Integer.toHexString(r.nextInt()));
        }
        return strBuilder.toString().substring(0, numChars);
    }
}
