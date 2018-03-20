package com.fangxuele.spiderproject.service;

import com.fangxuele.spiderproject.domain.TDangdangComment;
import com.fangxuele.spiderproject.dto.DangDangCommentDTO;
import com.fangxuele.spiderproject.mapper.TDangdangCommentMapper;
import com.fangxuele.spiderproject.util.HttpClientUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.mapper.JsonMapper;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * 爬取当当图书评价信息 Created by wfc on 2017/8/25.
 */
@Service
@Transactional
public class DangDangSpiderService {

	private Integer count = 0;

	private StringBuilder sb = new StringBuilder("isbn码未找到图书:");

	private String path = "C:\\Users\\wfc\\Desktop\\梵高报名图片\\";

	@Autowired
	private TDangdangCommentMapper dangdangCommentMapper;

	@PostConstruct
	public void init() {
		HttpClientUtil.init();
	}

	private static JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);

	public static String DANGDANG_SEARCH_BASE_URL = "http://search.dangdang.com";

	public static String DANGDANG_COMMENT_BASE_URL = "http://product.dangdang.com/index.php";

	public static String DANGDANG_BEST_BASE_URL = "http://bang.dangdang.com/books/bestsellers";

	public static String DANGDANG_PRODUCT_DETAIL_URL = "http://product.dangdang.com/index.php?r=callback%2Fdetail&productId=23612361&templateType=publish&describeMap=&shopId=0&categoryPath=01.41.50.03.00.00";

	public void getBookCommentInfoByIsbn(String title,String isbn) throws Exception {
		Thread.sleep(1000);
		Map<String, Object> params = Maps.newHashMap();
		params.put("key", isbn);
		params.put("art", "input");
		String html = HttpClientUtil.callUrlGet(DANGDANG_SEARCH_BASE_URL, params);

		// 获取dom并解析
		Document document = Jsoup.parse(html);

		Element element = document.getElementById("search_nature_rg");

		if(element!=null){
			// 获取详情url
			String href = element.getElementsByTag("a").get(0).attr("href");
			addBookCommentInfoByUrl(title,isbn,href,4);
		}else{
//			System.err.println("isbn码:"+isbn+"的图书未找到");
			sb.append(title).append(":").append(isbn).append("; ");
			TDangdangComment dangdangComment = new TDangdangComment();
			dangdangComment.setIsbn(isbn);
			dangdangComment.setTitle(title);
			dangdangComment.setType(4);
			dangdangComment.setUrl("无数据");
			dangdangCommentMapper.insert(dangdangComment);
			count++;
			System.err.println(count);
		}



	}

	public void addBookCommentInfoByUrl(String title,String isbn,String href,Integer type) throws Exception {
		// 解析详情页
		String detail = HttpClientUtil.callUrlGet(href);
		Document detailDocument = Jsoup.parse(detail);
		// 获取isbn
//		Elements metas = detailDocument.select("meta[name=keywords]");

//		String title = null;
//		String isbn = null;
//		if (metas != null && metas.size() > 0) {
//			String content = metas.get(0).attr("content");
//
//			String[] split = content.split("，");
//
//			title = split[0];
//
//			isbn = split[split.length - 1];
//		}

		// 获取当当产品id
		String productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

		// 获取评价信息
		StringBuilder url = new StringBuilder(DANGDANG_COMMENT_BASE_URL).append("?r=comment%2Flist")
				.append("&productId=").append(productId).append("&mainProductId=").append(productId);
		String commentInfo = HttpClientUtil.callUrlGet(url.toString());

		// 解析评价信息
		DangDangCommentDTO dangDangCommentDTO = mapper.fromJson(commentInfo, DangDangCommentDTO.class);
		DangDangCommentDTO.Summary summary = dangDangCommentDTO.getData().getSummary();

		// 获取价格信息
		Element mainPrice = detailDocument.getElementById("main_price");
		Elements del = detailDocument.getElementsByTag("del");
		String originalPrice = del.get(0).text();
		originalPrice = originalPrice.replace("¥", "");
		String price = mainPrice.text();

		TDangdangComment dangdangComment = new TDangdangComment();

		dangdangComment.setIsbn(isbn);
		dangdangComment.setTitle(title);
		dangdangComment.setUrl(href);
		dangdangComment.setTotalCommentNum(summary.getTotal_comment_num());
		dangdangComment.setTotalCrazyCount(summary.getTotal_crazy_count());
		dangdangComment.setTotalIndifferentCount(summary.getTotal_indifferent_count());
		dangdangComment.setTotalDetestCount(summary.getTotal_detest_count());
		dangdangComment.setAverageScore(summary.getAverage_score());
		dangdangComment.setGoodRate(summary.getGoodRate() + "%");
		dangdangComment.setOriginalPrice(originalPrice);
		dangdangComment.setPrice(price);
		dangdangComment.setType(type);
		dangdangCommentMapper.insert(dangdangComment);
		System.out.println("成功爬取当当《" + title + "》图书信息");
		count++;
		System.err.println(count);
	}

	public void getDangDanagBestBook() throws Exception {
		String para = "/01.41.00.00.00.00-recent30-0-0-1-";
		String url = DANGDANG_BEST_BASE_URL + para;

		for (int i = 1; i <= 25; i++) {
			String html = HttpClientUtil.callUrlGet(url + i);
			Document document = Jsoup.parse(html);
			Elements elements = document.select("div[class=name]");
			for (Element element : elements) {
				Elements a = element.getElementsByTag("a");
				if (a != null && a.size() > 0) {
					Element element1 = element.getElementsByTag("a").get(0);
//					addBookCommentInfoByUrl(element1.attr("href"),1);
				}
			}
		}

		System.out.println(count);
	}

	public void getDangDangCommentInfo() throws Exception {

		BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\wfc\\Desktop\\isbn.txt"));

		String str;
		// 判断最后一行不存在，为空结束循环
		while ((str = bufferedReader.readLine()) != null){
			String[] split = str.split(";");
//			getBookCommentInfoByIsbn(split[0],split[1]);
			getDangDangImage(split[0],split[1]);
		}
		System.err.println(sb.toString());
	}


	public void getBookInfoByIsbn(String isbn) throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		params.put("key", isbn);
		params.put("art", "input");
		String html = HttpClientUtil.callUrlGet(DANGDANG_SEARCH_BASE_URL, params);

		// 获取dom并解析
		Document document = Jsoup.parse(html);

		Element element = document.getElementById("search_nature_rg");

		if(element!=null){
			// 获取详情url
			String href = element.getElementsByTag("a").get(0).attr("href");
			// 解析详情页
			String detail = HttpClientUtil.callUrlGet(href);
			Document detailDocument = Jsoup.parse(detail);
			// 获取isbn
			Elements metas = detailDocument.select("meta[name=keywords]");

			String title = null;
			if (metas != null && metas.size() > 0) {
				String content = metas.get(0).attr("content");

				String[] split = content.split("，");

				title = split[0];

				isbn = split[split.length - 1];
			}
			Element mainPrice = detailDocument.getElementById("main_price");
			Elements del = detailDocument.getElementsByTag("del");
			String text = del.get(0).text();
			text = text.replace("¥", "");
			String price = mainPrice.text();

//			System.err.println(detail);
//			System.err.println(title);
//			System.err.println(isbn);
			System.err.println(text);
			System.err.println(price);

		}else{
			System.err.println("isbn码:"+isbn+"的图书未找到");
		}

	}

	public void getDangDangImage(String isbn,String id) throws Exception {
		Thread.sleep(1000);
		Map<String, Object> params = Maps.newHashMap();
		params.put("key", isbn);
		params.put("art", "input");
		String html = HttpClientUtil.callUrlGet(DANGDANG_SEARCH_BASE_URL, params);

		// 获取dom并解析
		Document document = Jsoup.parse(html);

		Element element = document.getElementById("search_nature_rg");

		if(element!=null){
			// 获取详情url
			String href = element.getElementsByTag("a").get(0).attr("href");
			String s = HttpClientUtil.callUrlGet(href);

			Document doucment1 = Jsoup.parse(s);
			Elements elements = doucment1.select("ul[class=top-slider]");
			Elements elementsByTag = elements.get(0).getElementsByTag("img");
			String url = elementsByTag.attr("src");
			InputStream inputStream = returnBitMap(url);
			write(id+".jpg",inputStream);
		}else{
			System.err.println(count);
		}

	}

	/**
	 * 通过图片url返回图片Bitmap
	 * @param path
	 * @return
	 */
	public InputStream returnBitMap(String path) {
		URL url = null;
		InputStream is =null;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream(); //得到网络返回的输入流

		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	private void write(String filename,InputStream in){

		File file=new File(path);
//		if(!file.exists()){
//			if(!file.mkdirs()){//若创建文件夹不成功
//				System.out.println("Unable to create external cache directory");
//			}
//		}

		File targetfile=new File(path+filename);
		OutputStream os=null;
		try{
			os=new FileOutputStream(targetfile);
			int ch=0;
			while((ch=in.read())!=-1){
				os.write(ch);
			}
			os.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				os.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}



}
