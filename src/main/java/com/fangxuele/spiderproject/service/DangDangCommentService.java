package com.fangxuele.spiderproject.service;

import com.fangxuele.spiderproject.cache.RedisCacheConstant;
import com.fangxuele.spiderproject.cache.RedisCacheService;
import com.fangxuele.spiderproject.domain.TBook;
import com.fangxuele.spiderproject.domain.TBookComment;
import com.fangxuele.spiderproject.mapper.TBookCommentMapper;
import com.fangxuele.spiderproject.mapper.TBookMapper;
import com.fangxuele.spiderproject.util.HttpClientUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.mapper.JsonMapper;
import org.springside.modules.utils.time.DateFormatUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 爬取当当评价信息
 *
 * @author wfc
 * @date 2017/12/28
 */
@Service
@Transactional
public class DangDangCommentService {

    @Autowired
    private TBookCommentMapper bookCommentMapper;

    @Autowired
    TBookMapper bookMapper;

    @Autowired
    private RedisCacheService redisCacheService;

    private static JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);

    public static String DANGDANG_COMMENT_DETAIL_BASE_URL = "http://product.dangdang.com/index.php?r=comment%2Flist&productId=23248697&mainProductId=23248697";


    public void getDangDangCOmment() throws Exception {
        List<TBook> bookList = bookMapper.findAll();

        int count = 0;

        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\wfc\\Desktop\\爬取结果.txt"));

        //List<String> bookIds = redisCacheService.getValueOfRangeByList(RedisCacheConstant.DANG_DANG_SUCCESS_BOOK_ID, 0, Long.MAX_VALUE);
        out:for (TBook book : bookList) {
            /*if(bookIds.contains(book.getId().toString())){
                continue out;
            }*/
            try {
                String isbn = book.getIsbn();
                Map<String, Object> params = Maps.newHashMap();
                params.put("key", isbn);
                params.put("art", "input");
                String searchHtml = HttpClientUtil.callUrlGet(DangDangSpiderService.DANGDANG_SEARCH_BASE_URL, params,"GB2312");

                // 获取dom并解析
                Document searchDocument = Jsoup.parse(searchHtml);

                Element searchElement = searchDocument.getElementById("search_nature_rg");
                String productId = null;
                if(searchElement!=null){
                    // 获取详情url
                    Element a = searchElement.getElementsByTag("a").get(0);
                    String title = a.attr("title");
                    writer.write(book.getIsbn()+"  云宝书名: "+book.getTitle()+"  当当书名: "+title);
                    String href = a.attr("href");
                    // 获取当当产品id
                    productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));
                }else{
                    writer.write(book.getIsbn()+"  云宝书名: "+book.getTitle()+"  当当书名: 未找到");
                    System.err.println("isbn码:"+isbn+"的图书未找到");
                }
                writer.newLine();
                writer.flush();

                // 拼接评价url
                String commentUrl = DANGDANG_COMMENT_DETAIL_BASE_URL+"&productId="+productId+"&mainProductId="+productId;

                // 获取返回值
                String html = HttpClientUtil.callUrlGet(commentUrl);

                // 解析json
                Map<String,Map<String,Map<String,String>>> map = mapper.fromJson(html, Map.class);

                // 获取html
                String commentHtml = map.get("data").get("list").get("html");

                // 解析
                Document document = Jsoup.parse(commentHtml);

                // 获取评价内容处理
                Elements itemWrap = document.getElementsByClass("item_wrap");

                Elements elements = itemWrap.get(0).getElementsByClass("comment_items clearfix");

                in:for (Element element : elements) {
                    try {
                        String comment = element.getElementsByClass("describe_detail").get(0).getElementsByTag("a").text();
                        //comment = comment.replace("当当","**");
                        if(comment.length()>150 || comment.contains("当当")){
                            continue in;
                        }
                        String score = element.getElementsByTag("em").text();
                        score = score.replace("分", "");
                        int star = (int) (Math.ceil(Integer.parseInt(score)/2));
                        Elements imageelement = element.getElementsByClass("items_left_pic").get(0).getElementsByTag("img");
                        String customerName = imageelement.attr("alt");
                        String cutomerImage = imageelement.attr("src");
                        String createTime = element.getElementsByClass("starline clearfix").get(0).getElementsByTag("span").get(0).text();
                        String time = new StringBuilder("2017").append(createTime.substring(createTime.indexOf("-"), createTime.length())).toString();
                        Date date = DateFormatUtil.pareDate("yyyy-MM-dd HH:mm:ss", time);
                        TBookComment bookComment = new TBookComment();
                        bookComment.setBookId(book.getId());
                        bookComment.setContent(comment);
                        bookComment.setStar((byte) star);
                        bookComment.setSource((byte) 2);
                        bookComment.setStatus((byte) 20);
                        bookComment.setCustomerName(customerName);
                        bookComment.setCustomerImage(cutomerImage);
                        bookComment.setCreateTime(date);
                        bookComment.setUpdateTime(date);
                        bookCommentMapper.insert(bookComment);
                        System.err.println(customerName+"=="+comment+"=="+star+"=="+time);
                    } catch (Exception e) {
                        //redisCacheService.addValuesBySet(RedisCacheConstant.DANG_DANG_PART_SUCCESS_BOOK_ID,book.getId().toString());
                        e.printStackTrace();
                    }
                }
                /*Set<String> partSuccessBookIds = redisCacheService.getValuesBySet(RedisCacheConstant.DANG_DANG_PART_SUCCESS_BOOK_ID);
                if(!partSuccessBookIds.contains(book.getId().toString())){
                    //redisCacheService.leftPushAllValueByList(RedisCacheConstant.DANG_DANG_SUCCESS_BOOK_ID,book.getId().toString());
                }*/
            } catch (Exception e) {
                //redisCacheService.leftPushAllValueByList(RedisCacheConstant.DANG_DANG_FAIL_BOOK_ID,book.getId().toString());
                e.printStackTrace();
            }
            count++;
        }
        writer.close();
        System.out.println(count);
    }



    public void getDangDangCommentByIsbn(String isbn) throws Exception {
        List<TBook> bookList = bookMapper.selectByIsbn(isbn);
        TBook tBook = bookList.get(0);
//        String isbn = "9787533274269";
        String productId = getProductIdByIsbn(isbn);
        String commentUrl = DANGDANG_COMMENT_DETAIL_BASE_URL+"&productId="+productId+"&mainProductId="+productId;

        String html = HttpClientUtil.callUrlGet(commentUrl);

        Map<String,Map<String,Map<String,String>>> map = mapper.fromJson(html, Map.class);

        String commentHtml = map.get("data").get("list").get("html");

        Document document = Jsoup.parse(commentHtml);

        Elements itemWrap = document.getElementsByClass("item_wrap");

        Elements elements = itemWrap.get(0).getElementsByClass("comment_items clearfix");
        for (Element element : elements) {
            String comment = element.getElementsByClass("describe_detail").get(0).getElementsByTag("a").text();
            comment = comment.replace("当当","**");
            String score = element.getElementsByTag("em").text();
            score = score.replace("分", "");
            int star = (int) (Math.ceil(Integer.parseInt(score)/2));
            Elements imageelement = element.getElementsByClass("items_left_pic").get(0).getElementsByTag("img");
            String customerName = imageelement.attr("alt");
            String cutomerImage = imageelement.attr("src");
            String createTime = element.getElementsByClass("starline clearfix").get(0).getElementsByTag("span").get(0).text();

            String time = new StringBuilder("2017").append(createTime.substring(createTime.indexOf("-"), createTime.length())).toString();

            Date date = DateFormatUtil.pareDate("yyyy-MM-dd HH:mm:ss", time);

        }

    }


    public String getProductIdByIsbn(String isbn) throws Exception {
        Map<String, Object> params = Maps.newHashMap();
        params.put("key", isbn);
        params.put("art", "input");
        String html = HttpClientUtil.callUrlGet(DangDangSpiderService.DANGDANG_SEARCH_BASE_URL, params,"GB2312");

        // 获取dom并解析
        Document document = Jsoup.parse(html);

        Element element = document.getElementById("search_nature_rg");
        String productId = null;
        if(element!=null){
            // 获取详情url
            Element a = element.getElementsByTag("a").get(0);
            String title = a.attr("title");
            System.err.println(title);
            String href = a.attr("href");
            // 获取当当产品id
            productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));
        }else{
            System.err.println("isbn码:"+isbn+"的图书未找到");
        }
        return productId;
    }

    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\wfc\\Desktop\\爬取结果.txt"));
        for (int i =1; i<=6;i++){
            writer.write("你好"+i);
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }
}
