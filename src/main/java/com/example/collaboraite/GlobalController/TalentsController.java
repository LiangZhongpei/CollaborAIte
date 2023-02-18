package com.example.collaboraite.GlobalController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.collaboraite.Entity.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin
@Controller
public class TalentsController {
    @Resource
    private MongoTemplate mongoTemplate;

    @ResponseBody
    @RequestMapping("/gettalents")
    public String getTalents(){
       List<Talents> talentsList = mongoTemplate.findAll(Talents.class,"talents");
       return JSON.toJSONString(talentsList);
    }

    @ResponseBody
    @RequestMapping("/gettalentbyid")
    public String getTalentById(@Param("id") Integer id){
        Query query = new Query(Criteria.where("Tid").is(id));
        Talents talents = mongoTemplate.findOne(query,Talents.class,"talents");
        return JSON.toJSONString(talents);
    }


    @ResponseBody
    @RequestMapping("/getcases")
    public String getcases(){
        List<Cases> casesList = mongoTemplate.findAll(Cases.class,"cases");
        return JSON.toJSONString(casesList);
    }

    @ResponseBody
    @RequestMapping("/getevaluates")
    public String getevaluates() throws IOException, InvalidFormatException, JSONException {
       List<Evaluates> evaluatesList = mongoTemplate.findAll(Evaluates.class,"evaluates");
       return JSON.toJSONString(evaluatesList);
    }

    @ResponseBody
    @RequestMapping("/adddemand")
    public void addDemand(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Query query = new Query(Criteria.where("CollName").is("demand"));
        Update update = new Update();
        update.inc("IncId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Info inc= mongoTemplate.findAndModify(query, update, options, Info.class,"info");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String time = formatter.format(date);

        Demand demand = new Demand();
        demand.setDid(inc.getIncId());
        demand.setCid((Integer) obj.get("cid"));
        demand.setTalentType(String.valueOf(obj.get("talenttype")));
        demand.setProjectName(String.valueOf(obj.get("projectname")));
        demand.setProjectDescription(String.valueOf(obj.get("projectdescription")));
        demand.setStartTime(String.valueOf(obj.get("starttime")));
        demand.setDuration(String.valueOf(obj.get("duration")));
        demand.setDailyRate(String.valueOf(obj.get("dailyrate")));
        demand.setName(String.valueOf(obj.get("name")));
        demand.setPhone(String.valueOf(obj.get("phone")));
        demand.setEmail(String.valueOf(obj.get("email")));
        demand.setState(1);
        demand.setTime(time);

        mongoTemplate.insert(demand,"demand");
    }


    @ResponseBody
    @RequestMapping("/updatedemandstate")
    public void updateDemandState(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Integer did = (Integer) obj.get("did");
        Integer state = (Integer) obj.get("state");
        Query query = new Query(Criteria.where("Did").is(did));
        Update update = new Update();
        update.set("State",state);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        mongoTemplate.findAndModify(query, update, options, Demand.class,"demand");

    }

    @ResponseBody
    @RequestMapping("/getalldemandbyid")
    public String getAllDemandById(@Param("id") Integer id){
        Query query = new Query(Criteria.where("Cid").is(id));
        List<Demand> demandList = mongoTemplate.find(query,Demand.class,"demand");
        return JSON.toJSONString(demandList);
    }

    @ResponseBody
    @RequestMapping("/getallmessagebyid")
    public String getAllMessageById(@Param("id") Integer id){
        Query query = new Query(Criteria.where("Cid").is(id));
        List<Message> messageList = mongoTemplate.find(query,Message.class,"message");
        return JSON.toJSONString(messageList);
    }

    @ResponseBody
    @RequestMapping("/getdemandbyid")
    public String getDemandById(@Param("id") Integer id){
        Query query = new Query(Criteria.where("Did").is(id));
        Demand demand= mongoTemplate.findOne(query,Demand.class,"demand");
        return JSON.toJSONString(demand);
    }

    @ResponseBody
    @RequestMapping("/addclient")
    public boolean addclient(@RequestBody String data) throws IOException{
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Query isregister = new Query(Criteria.where("Email").is(String.valueOf(obj.get("email"))));
        Client client1 = mongoTemplate.findOne(isregister,Client.class,"client");

        if(client1==null){
            Query query = new Query(Criteria.where("CollName").is("client"));
            Update update = new Update();
            update.inc("IncId", 1);
            FindAndModifyOptions options = new FindAndModifyOptions();
            options.upsert(true);
            options.returnNew(true);
            Info inc= mongoTemplate.findAndModify(query, update, options, Info.class,"info");

            Client  client = new Client();
            client.setFirstName(String.valueOf(obj.get("firstname")));
            client.setLastName(String.valueOf(obj.get("lastname")));
            client.setEmail(String.valueOf(obj.get("email")));
            client.setPassword(String.valueOf(obj.get("password")));
            client.setArea(String.valueOf(obj.get("area")));
            client.setCid(inc.getIncId());

            mongoTemplate.insert(client,"client");
            return true;
        }else {
            return false;
        }
    }


    @ResponseBody
    @RequestMapping("/login")
    public String login(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        String email = obj.get("email").toString();
        String password = obj.get("password").toString();

        Query query = new Query(Criteria.where("Email").is(email).and("Password").in(password));
        Client client = mongoTemplate.findOne(query,Client.class,"client");
        if(client!=null){
            return JSON.toJSONString(client);
        }else {
            return "false";
        }
    }

    @ResponseBody
    @RequestMapping("/updatepassword")
    public void updatepassword(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        String email = obj.get("email").toString();
        String newpassword = obj.get("newpassword").toString();

        Query query = new Query();
        query.addCriteria(Criteria.where("Email").is(email));
        Update update = new Update();
        update.set("Password", newpassword);
        mongoTemplate.upsert(query, update, "client");
    }

    @ResponseBody
    @RequestMapping("/addorder")
    public void addOrder(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Query query = new Query(Criteria.where("CollName").is("order"));
        Update update = new Update();
        update.inc("IncId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Info inc= mongoTemplate.findAndModify(query, update, options, Info.class,"info");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);

        Order order = new Order();
        order.setOid(inc.getIncId());
        order.setCid((Integer) obj.get("cid"));
        order.setTid((Integer) obj.get("tid"));
        order.setStartTime(String.valueOf(obj.get("starttime")));
        order.setEndTime(String.valueOf(obj.get("endtime")));
        order.setMoney(Integer.parseInt(obj.get("money").toString()));
        order.setTime(time);
        order.setState(1);
        order.setType(String.valueOf(obj.get("type")));

        mongoTemplate.insert(order,"order");
    }

    @ResponseBody
    @RequestMapping("/getallorderbyid")
    public String getAllOrderById(@Param("id") Integer id){
        Query query = new Query(Criteria.where("Cid").is(id));
        List<Order> orderList = mongoTemplate.find(query,Order.class,"order");
        return JSON.toJSONString(orderList);
    }
    @ResponseBody
    @RequestMapping("/getorderbyid")
    public String getOrderById(@Param("id") Integer id){
        Query query = new Query(Criteria.where("Oid").is(id));
        Order order = mongoTemplate.findOne(query,Order.class,"order");

        query = new Query(Criteria.where("Tid").is(order.getTid()));
        Talents talents = mongoTemplate.findOne(query,Talents.class,"talents");

        JSONArray data = new JSONArray();
        data.add(order);
        data.add(talents);

        return data.toString();
    }
    @ResponseBody
    @RequestMapping("/updateorderstate")
    public void updateOrderState(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Integer oid = (Integer) obj.get("oid");
        Integer state = (Integer) obj.get("state");
        Query query = new Query(Criteria.where("Oid").is(oid));
        Update update = new Update();
        update.set("State",state);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        mongoTemplate.findAndModify(query, update, options, Order.class,"order");

    }

    @ResponseBody
    @RequestMapping("/findtalents")
    public String findTalents(@RequestBody String data){
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        String keyWord = String.valueOf(obj.get("keyWord"));
        System.out.println(keyWord);
        Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$"); //拼接 正则表达式和查询参数
        Query query = new Query(new Criteria().orOperator(Criteria.where("ProjectExperience").regex(pattern), Criteria.where("CaseShow").regex(pattern),Criteria.where("Position").regex(pattern), Criteria.where("Unit").regex(pattern),Criteria.where("WorkExperience").regex(pattern),Criteria.where("TechnicalCapacity").regex(pattern)));
        //Query query = new Query(new Criteria().orOperator(Criteria.where("ProjectExperience").regex(pattern)));
        List<Talents> talentsList = mongoTemplate.find(query, Talents.class); //查询mongodb数据库

        return JSON.toJSONString(talentsList);
    }

    @ResponseBody
    @RequestMapping("/addinvestormessage")
    public void addInvestorMessage(@RequestBody String data) throws IOException, InvalidFormatException, JSONException {
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Query query = new Query(Criteria.where("CollName").is("investor"));
        Update update = new Update();
        update.inc("IncId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        Info inc= mongoTemplate.findAndModify(query, update, options, Info.class,"info");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);

        Investor investor = new Investor();
        investor.setIid(inc.getIncId());
        investor.setName(String.valueOf(obj.get("name")));
        investor.setEmail(String.valueOf(obj.get("email")));
        investor.setPhone(String.valueOf(obj.get("phone")));
        investor.setMessage(String.valueOf(obj.get("message")));
        investor.setTime(time);

        mongoTemplate.insert(investor,"investor");
    }

    @ResponseBody
    @RequestMapping("/getarticle")
    public String getArticle(@RequestBody String data){
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        Integer id = Integer.parseInt(String.valueOf(obj.get("id")));
        Query query = new Query(Criteria.where("Aid").is(id));
        Article article = mongoTemplate.findOne(query,Article.class,"articles");
        return JSON.toJSONString(article);
    }

    @ResponseBody
    @RequestMapping("/getallarticle")
    public String getAllArticle(){
        List<Article> articleList = mongoTemplate.findAll(Article.class,"articles");
        return JSON.toJSONString(articleList);
    }

    @ResponseBody
    @RequestMapping("/getarticlebytype")
    public String getArticleByType(@RequestBody String data){
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        String type = String.valueOf(obj.get("keyWord"));
        Query query = new Query(Criteria.where("type").is(type));
        List<Article> articleList = mongoTemplate.find(query,Article.class,"articles");
        return JSON.toJSONString(articleList);
    }

    @ResponseBody
    @RequestMapping("/getarticlebykeyword")
    public String getArticleByKeyWord(@RequestBody String data){
        Map<String,Object> obj =JSON.parseObject(data,Map.class);
        String keyWord = String.valueOf(obj.get("keyWord"));
        Pattern pattern = Pattern.compile("^.*" + keyWord + ".*$"); //拼接 正则表达式和查询参数
        Query query = new Query(new Criteria().orOperator(Criteria.where("title").regex(pattern), Criteria.where("body").regex(pattern),Criteria.where("author").regex(pattern), Criteria.where("type").regex(pattern),Criteria.where("time").regex(pattern)));

        List<Article> articleList = mongoTemplate.find(query,Article.class,"articles");
        return JSON.toJSONString(articleList);
    }
//    @ResponseBody
//    @RequestMapping("/getallcases")
//    public void getallcases() throws IOException, InvalidFormatException, JSONException {
//        JSONArray data = new JSONArray();
//
//        //excel文件路径
//        String excelPath = "D:\\python\\yuanjisong-data\\globaltalent-data\\项目案例2.xls";
//
//        //String encoding = "GBK";
//        File excel = new File(excelPath);
//
//        String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
//        Workbook wb;
//        //根据文件后缀（xls/xlsx）进行判断
//        if ( "xls".equals(split[1])){
//            FileInputStream fis = new FileInputStream(excel);   //文件流对象
//            wb = new HSSFWorkbook(fis);
//        }else if ("xlsx".equals(split[1])){
//            wb = new XSSFWorkbook(excel);
//        }else {
//            System.out.println("文件类型错误!");
//            return;
//        }
//
//        //开始解析
//        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
//
//        int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
//        int lastRowIndex = sheet.getLastRowNum();
//
//        Row rowfirst = sheet.getRow(firstRowIndex-1);
//
//        for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
//            Row row = sheet.getRow(rIndex);
//            JSONObject obj = new JSONObject();
//            if (row != null) {
//                Cases cases = new Cases();
//                cases.setCid(rIndex);
//                cases.setName(row.getCell(0).toString());
//                cases.setType(row.getCell(1).toString());
//                cases.setProjectIntroduction(row.getCell(2).toString());
//
//                mongoTemplate.insert(cases,"cases");
//            }
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/getallevaluates")
//    public void getallevaluates() throws IOException, InvalidFormatException, JSONException      {
//        JSONArray data = new JSONArray();
//
//        //excel文件路径
//        String excelPath = "D:\\python\\yuanjisong-data\\globaltalent-data\\好评人才1.xls";
//
//        //String encoding = "GBK";
//        File excel = new File(excelPath);
//
//        String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
//        Workbook wb;
//        //根据文件后缀（xls/xlsx）进行判断
//        if ( "xls".equals(split[1])){
//            FileInputStream fis = new FileInputStream(excel);   //文件流对象
//            wb = new HSSFWorkbook(fis);
//        }else if ("xlsx".equals(split[1])){
//            wb = new XSSFWorkbook(excel);
//        }else {
//            System.out.println("文件类型错误!");
//            return;
//        }
//
//        //开始解析
//        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
//
//        int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
//        int lastRowIndex = sheet.getLastRowNum();
//
//        Row rowfirst = sheet.getRow(firstRowIndex-1);
//
//        for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
//            Row row = sheet.getRow(rIndex);
//            JSONObject obj = new JSONObject();
//            if (row != null) {
//                Evaluates evaluates = new Evaluates();
//                evaluates.setEid(rIndex);
//                evaluates.setOrderName(row.getCell(0).toString());
//                evaluates.setDemandsNumber(row.getCell(1).toString());
//                evaluates.setOrdersNumber(row.getCell(2).toString());
//                evaluates.setComment(row.getCell(3).toString());
//                evaluates.setUnit(row.getCell(4).toString());
//                evaluates.setPosition(row.getCell(5).toString());
//                evaluates.setSalary(row.getCell(6).toString());
//                evaluates.setExperience(row.getCell(7).toString());
//
//                mongoTemplate.insert(evaluates,"evaluates");
//            }
//        }
//    }
//
//    @ResponseBody
//    @RequestMapping("/getalltalents")
//    public void getallTalents() throws IOException, InvalidFormatException, JSONException {
//        //excel文件路径
//        String excelPath = "D:\\python\\yuanjisong-data\\globaltalent-data\\人才展示5.xls";
//
//        //String encoding = "GBK";
//        File excel = new File(excelPath);
//
//        String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
//        Workbook wb;
//        //根据文件后缀（xls/xlsx）进行判断
//        if ( "xls".equals(split[1])){
//            FileInputStream fis = new FileInputStream(excel);   //文件流对象
//            wb = new HSSFWorkbook(fis);
//        }else if ("xlsx".equals(split[1])){
//            wb = new XSSFWorkbook(excel);
//        }else {
//            System.out.println("文件类型错误!");
//            return ;
//        }
//
//        //开始解析
//        Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
//
//        int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
//        int lastRowIndex = sheet.getLastRowNum();
//
//        Row rowfirst = sheet.getRow(firstRowIndex-1);
//
//        for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
//            Row row = sheet.getRow(rIndex);
//            JSONObject obj = new JSONObject();
//            if (row != null) {
//                Talents talents = new Talents();
//                talents.setTid(rIndex);
//                talents.setTechnicalCapacity(row.getCell(3).toString());
//                if (row.getCell(5)!=null)
//                    talents.setCaseShow(row.getCell(5).toString());
//                else
//                    talents.setCaseShow("");
//                talents.setSalary(row.getCell(6).toString());
//                talents.setWorkExperience(row.getCell(2).toString());
//                talents.setPosition(row.getCell(1).toString());
//                talents.setUnit(row.getCell(0).toString());
//                talents.setProjectExperience(row.getCell(4).toString());
//
//                mongoTemplate.insert(talents,"talents");
//            }
//        }
//    }
//

}
