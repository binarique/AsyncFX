/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author Walulya francis
 */
public class Pair {
    
public List<NameValuePair> formparams = formparams= new ArrayList<>();  
Charset ucharset;
public Pair(Charset ucharset){
this.ucharset = ucharset;   
}
public void add(String name, String value){
this.formparams.add(new BasicNameValuePair(name, value));    
}

public UrlEncodedFormEntity getPairs(Charset charset){
Charset mcharset = (ucharset == null) ? charset : ucharset;
return new UrlEncodedFormEntity(formparams, mcharset); 
}

public List<NameValuePair> getListNamePair(){
return formparams;    
}

public String getNamePair(Charset charset){
String mcharset = (ucharset == null) ? charset.displayName() : ucharset.displayName();
String line = "";
for(int i = 0; i < formparams.size(); i++){
    try{
        line += URLEncoder.encode(formparams.get(i).getName(), mcharset) + "=" + URLEncoder.encode(formparams.get(i).getValue(), mcharset);
 
if(i != (formparams.size()-1)){
line += "&"; 
}
}catch(UnsupportedEncodingException ex){
 System.err.println(ex.getMessage());
}
}
return line;
}
}
