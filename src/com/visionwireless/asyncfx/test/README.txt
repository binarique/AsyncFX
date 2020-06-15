The libray requires
the following dependencies
		
commons-codec-1.10.jar		
commons-logging-1.2.jar		
gson-2.8.0.jar		
httpclient-4.5.5.jar		
httpcore-4.4.9.jar


//EXAMPLE 1

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx.test;

import com.visionwireless.asyncfx.AsyncFX;
import com.visionwireless.asyncfx.Header;
import com.visionwireless.asyncfx.Pair;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;

/**
 *
 * @author walulya francis 
 * @year 2020
 */

public class AjaxTest  extends  AsyncFX<String> {
    
    private FXMLDocumentController controller;
    private String URL;
    private String DATA;
    
    public AjaxTest(FXMLDocumentController controller, String URL, String DATA){
    this.controller = controller;
    this.URL = URL;
    this.DATA = DATA;    
    }
    
    @Override
    public String doInBackground() {
    Pair pair = new Pair(null); 
    pair.add("q", "sony");
    pair.add("client", "webapp");
    pair.add("category", "2");
    pair.add("page", "3");
    pair.add("perpage", "1");
    Header header = new Header();
    header.add("lang", "lug");
    final String response = Post(URL, null, header, pair, null);
    return response;
    }

    @Override
    public void onError(String error) {
    System.out.print("Opps an error:" + error);
    }

    @Override
    public void onBeforeSend() {
    System.out.println("Posting request please wait.....");
    controller.updateLabel("Posting request please wait.....");
    }

    @Override
    public void onSuccess(String response) {
     System.out.println("Posting request success.....");
    System.out.println("RESPONSE CODE:" +  getResponseCode());
    System.out.println("RESPONSE STATUS:" + getResponseStatus());
    System.out.println("RESULTS:" + response);
    controller.updateLabel(response);
    }
    
}


//UPLOAD EXAMPLE 2
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx.test;

import com.visionwireless.asyncfx.Header;
import com.visionwireless.asyncfx.Pair;
import com.visionwireless.asyncfx.UploadFX;
import java.io.File;

/**
 *
 * @author ICY media
 */
public class FxUploadTest extends UploadFX<String> {
    
    private String FilePath;
    private FXMLDocumentController controller;
    public FxUploadTest(FXMLDocumentController controller, String RequestURL, String FilePath,  String RequestMethod,  String charset) {
    super(RequestURL, RequestMethod, charset);
    this.FilePath = FilePath;
    this.controller = controller;
    }

    @Override
    public void onProgress(double args1, int args2) {
         this.controller.updateProgress(args1);
    System.out.println("progress:" + args2);
    }

    @Override
    public String doInBackground() {
    File file = new File(FilePath);
    Pair pair = new Pair(null);
    pair.add("UPLOADING_FILES", "MY_PHOTOS");
    Header header = new Header();
    header.add("User-Agent", "Uploader4J");
    if(file.exists()){
    final String response = Upload(file, "uploadedfile", pair, header);
    return response;
    }else{
    return "File doesn't exist"; 
    }
    }

    @Override
    public void onError(String error) {
    System.out.println("Error:" + error);
    }

    @Override
    public void onStart() {
    System.out.println("Upload started....");
    this.controller.updateLabel("Uploading: " + new File(FilePath).getName());
    }

    @Override
    public void onFinish(String response) {
    System.out.println("Upload finished");
    System.out.println("Response Code:" + getResponseCode());
        System.out.println("Result:" + response);
    }
//String path = "D:\\FingerPrintPic\\Sample2.bmp";    
}


//INITIALIZATION
String path = "D:\\demo\\movie.mp4";
    FxUploadTest test = new FxUploadTest(this, uploadURL, path, null, null);
    test.setConcurrent(false);
    test.execute();
    
    String json = RequestModel.getRequest("walulyafrancis@gmail.com", "1234567");
    AjaxTest test1 = new AjaxTest(this, Login_form_request, json);
    test1.setConcurrent(false);
    test1.execute();
  

//POSTING FORM DATA
Post(URL, NULL, headers, pairs, ContentType.APPLICATION_JSON)

//JSON POST
Post(URL, JSON, headers, null, null)

//GET REQUEST DATA
Get(String URL, Header header, Pair pairs)

//UPLOAD EXAMPLE
Upload(File, FILE_UPLOAD_NAME, pairs, headers);