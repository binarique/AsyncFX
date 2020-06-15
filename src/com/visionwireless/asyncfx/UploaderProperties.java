/*    */ package com.visionwireless.asyncfx;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UploaderProperties
/*    */ {
/*    */   private HttpURLConnection httpConn;
/*    */   private String requestURL;
/*    */   private String boundary;
/*    */   /*    */   
/* 16 */   public UploaderProperties(String requestURL) { this.requestURL = requestURL; }
/*    */ 
/*    */   
/*    */   public String requestBoundary() {
/* 20 */     this.boundary = "===" + System.currentTimeMillis() + "===";
/* 21 */     return this.boundary;
/*    */   }

/*    */   public HttpURLConnection setHttpConnection(String boundary, String method) throws IOException {
/* 24 */     URL url = new URL(this.requestURL);
/* 25 */     this.httpConn = (HttpURLConnection)url.openConnection();
/* 26 */     this.httpConn.setUseCaches(false);
/* 27 */     this.httpConn.setRequestMethod(method);
/* 28 */     this.httpConn.setDoOutput(true);
/* 29 */     this.httpConn.setDoInput(true);
/* 30 */     this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
/* 31 */     this.httpConn.setRequestProperty("User-Agent", "UploadFX");
/* 32 */     return this.httpConn;
/*    */   }
/*    */ }

