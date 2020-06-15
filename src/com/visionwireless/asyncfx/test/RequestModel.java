/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx.test;

import com.google.gson.GsonBuilder;

/**
 *
 * @author ICY media
 */
public class RequestModel {
public String email;
public String password;
public static RequestModel req = null;
public RequestModel(String email, String password){
this.email = email;
this.password = password;
}
public static String getRequest(String email, String password){
 req = new RequestModel(email, password);
 return new GsonBuilder().setPrettyPrinting().create().toJson(req);
}

}
