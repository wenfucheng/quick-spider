package com.fangxuele.spiderproject.service;

import com.fangxuele.spiderproject.domain.TDangdangComment;
import com.fangxuele.spiderproject.mapper.TDangdangCommentMapper;
import com.fangxuele.spiderproject.util.HttpClientUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.mapper.JsonMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

/**
 * Created by wfc on 2017/8/27.
 */
@Service
@Transactional
public class JingDongSpiderService {

    private static JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);

    private static String JINGDONG_BASE_URL = "https://search.jd.com/Search";

    private static String JINGDONG_COMMENT_BASE_URL = "https://club.jd.com/comment/productCommentSummaries.action";

    private static String JINGDONG_PRICE_BASE_URL = "https://p.3.cn/prices/get?skuid=J_";

    private static String PREFIX = "http:";

    private StringBuilder sb = new StringBuilder("isbn码未找到图书:");

    Integer count = 0;

    @Autowired
    private TDangdangCommentMapper dangdangCommentMapper;

    public void getBookCommentInfoByIsbn(String title, String isbn) throws Exception {
        Thread.sleep(2000);
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyword", isbn);
        params.put("enc", "utf-8");
        params.put("wq", isbn);
        String html = HttpClientUtil.callUrlGet(JINGDONG_BASE_URL, params);

        // 获取dom并解析
        Document document = Jsoup.parse(html);

        Element element = document.getElementById("J_goodsList");

        if (element != null) {
            // 获取详情url
            String href = element.getElementsByTag("a").get(0).attr("href");

            href = PREFIX + href;

            addBookCommentInfoByUrl(title, isbn, href, 5);
        } else {
//			System.err.println("isbn码:"+isbn+"的图书未找到");
            sb.append(isbn).append("; ");
            TDangdangComment dangdangComment = new TDangdangComment();
            dangdangComment.setIsbn(isbn);
            dangdangComment.setTitle(title);
            dangdangComment.setUrl("无数据");
            dangdangComment.setType(5);
            dangdangCommentMapper.insert(dangdangComment);
            count++;
            System.err.println(count);
            System.out.println("未找到京东《" + title + "》图书信息");
        }
    }


    public void addBookCommentInfoByUrl(String title, String isbn, String href, Integer type) throws Exception {
        // 解析详情页
//		String detail = HttpClientUtil.callUrlGet(href,"gbk");

//		Document detailDocument = Jsoup.parse(detail);
        // 获取isbn
//		Elements metas = detailDocument.select("meta[name=keywords]");

//		String title = null;
//		String isbn = null;
//		if (metas != null && metas.size() > 0) {
//			String content = metas.get(0).attr("content");
//
//			String[] split = content.split(",");
//
//			title = split[0];
//
//			isbn = split[3];
//		}

        // 获取京东产品id
        String productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

        // 获取评价信息
        StringBuilder url = new StringBuilder(JINGDONG_COMMENT_BASE_URL).append("?referenceIds=")
                .append(productId);

        // 解析评价信息
        Map<String, Object> commentsMap = null;
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                String commentInfo = HttpClientUtil.callUrlGet(url.toString());
                Map<String, List<Map<String, Object>>> map = mapper.fromJson(commentInfo, Map.class);
                commentsMap = map.get("CommentsCount").get(0);
                break;
            } catch (Exception e) {
            }
        }

        // 获取价格信息
        String priceUrl = JINGDONG_PRICE_BASE_URL + productId;

        List<Map<String, String>> list = null;
        Map<String, String> priceMap = null;
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                System.err.println(priceUrl);
                String priceInfo = HttpClientUtil.callUrlGet(priceUrl);
                list = mapper.fromJson(priceInfo, List.class);
                priceMap = list.get(0);
                break;
            } catch (Exception e) {
            }
        }


        TDangdangComment dangdangComment = new TDangdangComment();

        dangdangComment.setIsbn(isbn);
        dangdangComment.setTitle(title);
        dangdangComment.setUrl(href);
        dangdangComment.setTotalCommentNum(commentsMap == null ? null : (Integer) commentsMap.get("CommentCount"));
        dangdangComment.setTotalCrazyCount(commentsMap == null ? null : Integer.valueOf((Integer) commentsMap.get("GoodCount")));
        dangdangComment.setTotalIndifferentCount(commentsMap == null ? null : Integer.valueOf((Integer) commentsMap.get("GeneralCount")));
        dangdangComment.setTotalDetestCount(commentsMap == null ? null : Integer.valueOf((Integer) commentsMap.get("PoorCount")));
        dangdangComment.setAverageScore(commentsMap == null ? null : commentsMap.get("AverageScore").toString());
        dangdangComment.setGoodRate(commentsMap == null ? null : commentsMap.get("GoodRateShow") + "%");
        dangdangComment.setOriginalPrice(priceMap == null ? "无数据" : priceMap.get("m"));
        dangdangComment.setPrice(priceMap == null ? "无数据" : priceMap.get("p"));
        dangdangComment.setType(type);
        dangdangCommentMapper.insert(dangdangComment);
        System.out.println("成功爬取京东《" + title + "》图书信息");
        count++;
        System.err.println(count);
    }

    public void getDangDangCommentInfo() throws Exception {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\wfc\\Desktop\\isbn.txt"));
        String str;
        // 判断最后一行不存在，为空结束循环
        while ((str = bufferedReader.readLine()) != null) {
            String[] split = str.split(";");
            getBookCommentInfoByIsbn(split[0], split[1]);
//			getBookCommentInfoByIsbn(str.trim());
        }
        System.err.println(sb.toString());
    }


    public void getBookInfoByIsbn(String isbn) throws Exception {
        Map<String, Object> params = Maps.newHashMap();
        params.put("keyword", isbn);
        params.put("enc", "utf-8");
        params.put("wq", isbn);
        String html = HttpClientUtil.callUrlGet(JINGDONG_BASE_URL, params);

        // 获取dom并解析
        Document document = Jsoup.parse(html);

        Element element = document.getElementById("J_goodsList");

        if (element != null) {
            // 获取详情url
            String href = element.getElementsByTag("a").get(0).attr("href");

            href = PREFIX + href;

            // 解析详情页
            String detail = HttpClientUtil.callUrlGet(href, "gbk");

            Document detailDocument = Jsoup.parse(detail);

            // 获取京东产品id
            String productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

            href = JINGDONG_PRICE_BASE_URL + productId;
            String priceInfo = HttpClientUtil.callUrlGet(href);

            List<Map<String, String>> list = mapper.fromJson(priceInfo, List.class);

            System.err.println(list.get(0).get("p"));
            System.err.println(list.get(0).get("m"));

        } else {
            sb.append(isbn).append(";");
        }
    }

}
