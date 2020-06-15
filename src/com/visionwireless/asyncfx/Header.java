/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visionwireless.asyncfx;

import java.util.HashMap;
import java.util.Map;

public class Header {
    
Map<String, String> map = new HashMap();

public void add(String name, String value){
map.put(name, value);    
}

public Map<String, String> getHeaders(){
return map;   
}
}
