/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.knittech.elasticsearchexample.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Sumit
 */
@Controller
@RequestMapping("/")
public class DefaultController {

    TransportClient client = null;
    public DefaultController() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY)
        .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    public String index() throws UnknownHostException, IOException{        
        IndexResponse response = client.prepareIndex("twitter", "tweet", "5")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("user", "Sudan")
                        .field("postDate", new Date())
                        .field("message", "trying out LinkedIn")
                    .endObject()
                  )
        .get();
        return "index";
    }
    @RequestMapping(path = "/get",method = RequestMethod.GET)
    @ResponseBody
    public String getAll (){
        SearchResponse response = client.prepareSearch("twitter").get();
        return response.toString();
        
    }
    @RequestMapping(path = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getById (@PathVariable("id") String id){
        GetResponse response = client.prepareGet("twitter", "tweet", id).get();
        return response.getSourceAsString();
        
    }
    
    @RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteById(@PathVariable("id") String id){
        DeleteResponse response = client.prepareDelete("twitter", "tweet", id).get();
        return response.getResult().toString();
    }
    @RequestMapping(path = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateById(@PathVariable("id")String id) throws IOException{
        UpdateResponse response = client.prepareUpdate("twitter","tweet",id).setDoc(jsonBuilder()
        .startObject()
            .field("age",25)
        .endObject()).get();
        return response.getResult().toString();
    }
}
