/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public abstract class AsyncFX<T1> extends Thread {
    
private Thread backGroundThread;

private HttpPost httpPost = null;    

private HttpGet httpGet = null;

private HttpResponse response = null;

private final  CloseableHttpClient client = HttpClients.createDefault();

private HttpEntity RequestEntity = null;

private HttpEntity ResponseEntity = null;

private boolean concurrent = true;

private NetworkLock lock = null;

public AsyncFX(){
this.lock = new NetworkLock();
}

@Override
public void run(){
//locking the rexecution to the current thread
if(lock != null){
while (lock.getRunningThread()  > 0){
synchronized (lock) {
try {
lock.wait();
} catch (InterruptedException ex){ 
}
}
}
lock.registerThread();  
}

    
final T1 result =  doInBackground();
Platform.runLater(() -> onSuccess((result)));

//notifying another waiting thread to continue with execution
 if(lock != null){
 lock.removeThread();
 synchronized(lock) {
 lock.notify();
 }
 }
}



public void execute() {
Platform.runLater(() -> {
onBeforeSend();
this.backGroundThread = new Thread(this);
if (this.backGroundThread != null) {
this.backGroundThread.start();
if(!concurrent){
try {
this.backGroundThread.join();
} catch (InterruptedException ex) {}
}      
}
});
}
protected abstract T1 doInBackground();

protected abstract void onError(String error);

protected String Post(String URL, String data, Header header, Pair pairs, ContentType contentType){
//initialize a post object
this.httpPost = new HttpPost(URL);
//request headers
if(header != null){
Map<String, String> headers = header.getHeaders();
Set<String> keys = headers.keySet();
Iterator keysItr = keys.iterator();
while(keysItr.hasNext()){
String key = (String)keysItr.next();
String value = headers.get(key);
httpPost.addHeader(key, value);
}
}

if(pairs != null){
 httpPost.setEntity(pairs.getPairs(Consts.UTF_8));   
}

if(data != null && contentType != null){
//create a new string entity request
this.RequestEntity = new StringEntity(data, contentType);
httpPost.setEntity(RequestEntity);
}

StringBuilder sb = null;
try {
this.response = client.execute(httpPost);
this.ResponseEntity = response.getEntity();
BufferedReader reader = new BufferedReader(new InputStreamReader(ResponseEntity.getContent()));
sb = new StringBuilder();
String line = "";
while((line = reader.readLine()) != null){
sb.append(line);
}
} catch (IOException ex) {
Platform.runLater(() -> onError(ex.getMessage()));
}
if(sb != null){
return sb.toString();    
}else{
return null;  
}
}

public void setConcurrent(boolean concurrent){
this.concurrent = concurrent;   
}

protected int getResponseCode(){
if(response != null) return response.getStatusLine().getStatusCode();
return 0;
}

protected String getResponseStatus(){
if(response != null) return response.getStatusLine().getReasonPhrase();
return null;
}

protected HttpEntity getResponseEntity(){
return ResponseEntity;   
}


protected String Get(String URL, Header header, Pair pairs){
String newUrl = (pairs == null) ? URL : URL + "?" + pairs.getNamePair(Consts.UTF_8);
this.httpGet = new HttpGet(newUrl);
if(header != null){
 Map<String, String> headers = header.getHeaders();
Set<String> keys = headers.keySet();
Iterator keysItr = keys.iterator();
while(keysItr.hasNext()){
String key = (String)keysItr.next();
String value = headers.get(key);
httpGet.addHeader(key, value);
}
}
StringBuffer sb = null;
try{
this.response = client.execute(httpGet);
InputStream in = response.getEntity().getContent();
BufferedReader reader = new BufferedReader(new InputStreamReader(in));
sb = new StringBuffer();
String line = "";
while((line = reader.readLine()) != null){
sb.append(line);
}
}catch(IOException ex){
Platform.runLater(() -> onError(ex.getMessage()));
}
if(sb != null){
return sb.toString();    
}else{
return null;  
}
}

protected abstract void onBeforeSend();

protected abstract void onSuccess(T1 response);

}
