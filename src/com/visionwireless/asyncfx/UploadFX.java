/*     */ package com.visionwireless.asyncfx;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URLConnection;
          import java.util.Iterator;
          import java.util.Map;
          import java.util.Set;
          import javafx.application.Platform;
          import org.apache.http.NameValuePair;
/*     */ 
/*     */ public abstract class UploadFX<T1> extends Thread
/*     */ {
/*     */   private Thread backgroundThread = null;
/*     */   
/*     */   private final String boundary;
/*     */   
/*     */   private UploaderProperties uploaderProperties;

            private String charset;
            
            private boolean concurrent = true;
            
            private UploadLock lock = null;

/*     */   private HttpURLConnection httpConn;
/*     */   
/*     */   private OutputStream outputStream;
/*     */   
/*     */   private PrintWriter writer;
            
            private int BUFFER_SIZE = 1024;
 
/*     */    public UploadFX(String RequestURL, String RequestMethod, String charset){
              this.lock = new UploadLock();
/*     */     String ncharset = (charset == null)?  "UTF-8" : charset;
              this.charset = ncharset;
              String nmethod = (RequestMethod == null)? "POST" : RequestMethod;
/*     */     this.uploaderProperties = new UploaderProperties(RequestURL);
/*     */     this.boundary = this.uploaderProperties.requestBoundary();
              try{
/*     */     this.httpConn = this.uploaderProperties.setHttpConnection(this.boundary, nmethod);
/*     */     this.outputStream = this.httpConn.getOutputStream();
/*     */     this.writer = new PrintWriter(new OutputStreamWriter(this.outputStream, ncharset), true);
              }catch(IOException ex){
               Platform.runLater(() -> onError(ex.getMessage()));
              }
/*     */     }
            
            public void setConcurrent(boolean concurrent){
             this.concurrent = concurrent;   
            }
             
            public void setBufferLength(int BUFFER_LENGTH){
            this.BUFFER_SIZE = BUFFER_LENGTH;    
            }
            public abstract void onStart();

/*     */    public abstract void onProgress(long totalSize, long progressSize);
/*     */    
             public abstract T1 doInBackground();
             
             public abstract void onError(String error);
             
/*     */   
/*     */    public abstract void onFinish(T1 response);

            public int getResponseCode(){
            if(httpConn != null) try {
                return httpConn.getResponseCode();
            } catch (IOException ex) {
            Platform.runLater(() -> onError(ex.getMessage()));
            }
            return 0;
            }
            
/*     */   protected String Upload(File file, String UploadName, Pair pairs, Header header){
            if(header != null){
            Map<String, String> headers = header.getHeaders();
            Set<String> keys = headers.keySet();
           Iterator keysItr = keys.iterator();
           while(keysItr.hasNext()){
           String key = (String)keysItr.next();
           String value = headers.get(key);
           addHeaderField(key, value);
           }
           }
            
            if(pairs != null){
            for(NameValuePair pair : pairs.getListNamePair()){
             addFormField(pair.getName(), pair.getValue());   
            }
            }
            int progress = 0;
            String response = "";
/*     */     try {
/*    */       String fileName = file.getName();
/*    */       this.writer.append("--" + this.boundary).append("\r\n");
/*     */       
/*    */       this.writer.append("Content-Disposition: form-data; name=\"" + UploadName + "\"; filename=\"" + fileName + "\"")
/*    */         .append("\r\n");
/*     */       
/*  83 */       this.writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName))
/*  84 */         .append("\r\n");
/*     */       
/*  86 */       this.writer.append("Content-Transfer-Encoding: binary").append("\r\n");
/*  87 */       this.writer.append("\r\n");
/*  88 */       this.writer.flush();
/*     */       
/*  90 */       FileInputStream inputStream = new FileInputStream(file);
/*     */       
/*  92 */       long totalSize = inputStream.available();
/*     */ 
/*  96 */       byte[] buffer = new byte[BUFFER_SIZE];
/*     */       
/*  98 */       int bytesRead = -1;
/*     */       
/* 100 */       long recFileSize = 0L;
/*     */       
/* 102 */       while ((bytesRead = inputStream.read(buffer)) != -1) {
/* 103 */         recFileSize += bytesRead;
/* 105 */         
/* 106 */         publishProgress(totalSize, recFileSize);
/* 107 */         this.outputStream.write(buffer, 0, bytesRead);
/*     */       } 
/* 109 */       this.outputStream.flush();
/* 110 */       inputStream.close();
/*     */       
/* 112 */       this.writer.append("\r\n");
/* 113 */       this.writer.flush();
/*     */        response += finish();
/* 115 */     //finished
/*     */     }
/* 117 */     catch (IOException ex) {
/* 118 */     Platform.runLater(() -> onError(ex.getMessage()));
/*     */     } 
            return response;    
            }
     
          
             private void addHeaderField(String name, String value) {
             this.writer.append(String.valueOf(name) + ": " + value).append("\r\n");
             this.writer.flush();
/*     */   }
/*     */ 
/*  
/*     */   private void addFormField(String name, String value) {
/*  63 */     this.writer.append("--" + this.boundary).append("\r\n");
/*  64 */     this.writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append("\r\n");
/*  65 */     this.writer.append("Content-Type: text/plain; charset=" + this.charset).append("\r\n");
/*  66 */     this.writer.append("\r\n");
/*  67 */     this.writer.append(value).append("\r\n");
/*  68 */     this.writer.flush();
/*     */   }
/*     */ 
/*     */   
/*     */    @Override
             public void run() {
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
             
             final T1 results = doInBackground();
             Platform.runLater(() -> onFinish(results));
             
            if(lock != null){
           lock.removeThread();
	   synchronized(lock) {
	   lock.notify();
	     }
            }
/*     */    }


/*     */     private String finish() throws IOException {
/* 139 */     StringBuilder sb = new StringBuilder();
/* 140 */     this.writer.append("\r\n").flush();
/* 141 */     this.writer.append("--" + this.boundary + "--").append("\r\n");
/* 142 */     this.writer.close();  
/* 144 */     int status = this.httpConn.getResponseCode();
/* 146 */     if (status == 200) {
/* 147 */       BufferedReader reader = new BufferedReader(new InputStreamReader(
/* 148 */             this.httpConn.getInputStream()));
/* 149 */       String line = null;
/* 150 */       while ((line = reader.readLine()) != null) {
/* 151 */         sb.append(line);
/*     */       }
/* 153 */       reader.close();
/* 154 */       this.httpConn.disconnect();
/*     */     } else {
              //get error stream
/* 156 */      BufferedReader reader = new BufferedReader(new InputStreamReader(
/* 148 */             this.httpConn.getErrorStream())); 
              String line = null;
              while ((line = reader.readLine()) != null) {
/* 151 */         sb.append(line);
/*     */       }
/* 153 */       reader.close();
/* 154 */       this.httpConn.disconnect();
/*     */     } 
/*     */     
/* 159 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void publishProgress(long totalSize, long ProgressSize) {
             Platform.runLater(() ->{ 
/* 164 */     onProgress(totalSize, ProgressSize);
             });
/*     */   }

/*     */   public void execute() {
              Platform.runLater(() ->{ 
                  onStart();
                  this.backgroundThread = new Thread(this);
/* 173 */        if (this.backgroundThread != null){
/* 174 */       this.backgroundThread.start();
                      if(!concurrent){
                      try {
                      this.backgroundThread.join();
                      } catch (InterruptedException ex) {}
                      }       
              }
              });
              }

 }
