package com.example.collaboraite.GlobalController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.collaboraite.Entity.*;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@CrossOrigin
@Controller
public class AdminController {
    @Resource
    private MongoTemplate mongoTemplate;

    @ResponseBody
    @RequestMapping("/getalldemand")
    public String getAllDemand(){
        List<Demand> demandList = mongoTemplate.findAll(Demand.class,"demand");
        return JSON.toJSONString(demandList);
    }

    @ResponseBody
    @RequestMapping("/getallclient")
    public String getallclient(){
        List<Client> demandList = mongoTemplate.findAll(Client.class,"client");
        return JSON.toJSONString(demandList);
    }

    @ResponseBody
    @RequestMapping("/getallorder")
    public String getallorder(){
        List<Order> orderList = mongoTemplate.findAll(Order.class,"order");
        return JSON.toJSONString(orderList);
    }

    @ResponseBody
    @RequestMapping("/addmessage")
    public void addmessage(@RequestBody String data){//Send text messages to customers
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Query query = new Query(Criteria.where("CollName").is("message"));
        Update update = new Update();
        update.inc("IncId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Info inc= mongoTemplate.findAndModify(query, update, options, Info.class,"info");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);

        Message message = new Message();
        message.setMid(inc.getIncId());
        message.setCid((Integer) obj.get("cid"));
        message.setTime(time);
        message.setContent(String.valueOf(obj.get("content")));

        mongoTemplate.insert(message,"message");
    }

    @ResponseBody
    @RequestMapping("/getdata")
    public String getdata(){
        JSONArray data = new JSONArray();
        List<Client> clientList = mongoTemplate.findAll(Client.class,"client");
        int Chinanum = 0;
        int Canadanum = 0;
        for (Client client:clientList){
            if(client.getArea().equals("China")){
                Chinanum = Chinanum + 1;
            }else if(client.getArea().equals("Canada")){
                Canadanum = Canadanum + 1;
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("name","China");
        obj.put("value",Chinanum);
        data.add(obj);

        obj = new JSONObject();
        obj.put("name","Canada");
        obj.put("value",Canadanum);
        data.add(obj);

        return data.toString();
    }

    @ResponseBody
    @RequestMapping("/getDemanddata")
    public String getDemanddata(){
        JSONArray data = new JSONArray();
        List<Demand> clientList = mongoTemplate.findAll(Demand.class,"demand");
        int startnum = 0;
        int overnum = 0;
        for (Demand demand:clientList){
            if(demand.getState()==1){
                startnum = startnum + 1;
            }else if(demand.getState()==2){
                overnum = overnum + 1;
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("name","In progress");
        obj.put("value",startnum);
        data.add(obj);

        obj = new JSONObject();
        obj.put("name","Have ended");
        obj.put("value",overnum);
        data.add(obj);

        return data.toString();
    }

    @ResponseBody
    @RequestMapping("/getOrderdata")
    public String getOrderdata(){
        JSONArray data = new JSONArray();
        List<Order> orderList = mongoTemplate.findAll(Order.class,"order");
        int[] num = new int[]{0, 0, 0, 0, 0};
        int overnum = 0;
        for (Order order:orderList){
            num[order.getState()-1] = num[order.getState()-1] + 1;
        }
        JSONObject obj = new JSONObject();
        obj.put("name","Unpaid");
        obj.put("value",num[0]);
        data.add(obj);

        obj = new JSONObject();
        obj.put("name","Cancelled");
        obj.put("value",num[1]);
        data.add(obj);

        obj = new JSONObject();
        obj.put("name","To be accepted");
        obj.put("value",num[2]);
        data.add(obj);

        obj = new JSONObject();
        obj.put("name","To be evaluated");
        obj.put("value",num[3]);
        data.add(obj);

        obj = new JSONObject();
        obj.put("name","Pending refund");
        obj.put("value",num[4]);
        data.add(obj);

        return data.toString();
    }

    @ResponseBody
    @RequestMapping("/addarticle")
    public String addArticle() throws IOException {
        Query query = new Query(Criteria.where("CollName").is("articles"));
        Update update = new Update();
        update.inc("IncId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Info inc1= mongoTemplate.findAndModify(query, update, options, Info.class,"info");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
        String time = formatter.format(date);

        File file = new File("C:\\Users\\Administrator\\Desktop\\articles\\article4.txt"); // 要读取以上路径的input。txt文件
        System.out.println(file.getName());
        byte[] bytes = new byte[1024];
        StringBuffer sb = new StringBuffer();
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, len));
        }

        Article article = new Article();
        article.setAid(inc1.getIncId());
        article.setTitle("Web Crawler Technology: An Insight into the Automated Internet Navigators");
        article.setBody(sb.toString());
        article.setTime(time);
        article.setAuthor("Links Eric");
        article.setImagefile("https://s1.ax1x.com/2023/02/11/pS4lRHO.jpg");
        article.setType("Web Crawler");

        mongoTemplate.insert(article,"articles");

        return "Success";
    }
}
