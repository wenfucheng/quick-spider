package com.fangxuele.spiderproject.service;

import com.fangxuele.spiderproject.dto.DangDangBookDetailDTO;
import com.fangxuele.spiderproject.util.HttpClientUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.utils.base.ExceptionUtil;
import org.springside.modules.utils.mapper.JsonMapper;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 爬虫相关Service
 * Created by wfc on 2017/8/17.
 */
@Service
@Transactional
public class SpiderService {

	private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

	private static JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);

	private static final String DANGDANG_SEARCH_BASE_URL = "http://search.dangdang.com";

	private static final String DANGDANG_PRODUCT_DETAIL_URL = "http://product.dangdang.com/index.php?r=callback%2Fdetail&templateType=publish&describeMap=&shopId=0&categoryPath=01.41.50.03.00.00&productId=";

	private static final String PREFIX_URL = "https:";

	private static final String M_JD_BASE_URL = "https://so.m.jd.com/ware/search.action?keyword=";

	private static final String M_JD_INDEX_URL = "https://m.jd.com/";

	private static final String M_DD_BASE_URL = "http://search.m.dangdang.com/search.php?keyword=";

	private static final String M_DD_INDEX_URL = "http://m.dangdang.com/";

	private WebClient webClient;

	@PostConstruct
	public void init() {
		// 初始化浏览器对象
		webClient = new WebClient(BrowserVersion.CHROME);
		// 配置是否加载css和javaScript
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setTimeout(50000);
	}

	/**
	 * 根据isbn获取京东手机图书详情URL
	 * @param isbn
	 * @return
	 * @throws IOException
	 */
	public String getMJdDetailUrlByIsbn(String isbn) throws IOException {
		// 获取网页对象
		HtmlPage htmlpage = webClient.getPage(M_JD_BASE_URL + isbn);

		DomElement seachList = htmlpage.getElementById("seach_list");

		DomNodeList<HtmlElement> a = seachList.getElementsByTagName("a");

		if(!a.isEmpty()){
			String href;
			String behaviordata = a.get(0).getAttribute("behaviordata");
			if(StringUtils.isNotEmpty(behaviordata)){
				Map<String,String> map = mapper.fromJson(behaviordata, Map.class);
				href = map.get("url");
			}else{
				href = a.get(0).getAttribute("href");
			}

			return PREFIX_URL + href.substring(0, href.indexOf("?"));

		}
		return M_JD_INDEX_URL;
	}

	/**
	 * 根据isbn获取当当商品详情
	 * @param isbn
	 * @return
	 */
	public String getDangDangBookDetailByIsbn(String isbn) {
		try {
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
				// 获取当当产品id
				String productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

				String bookDetail = HttpClientUtil.callUrlGet(DANGDANG_PRODUCT_DETAIL_URL + productId);

				if(StringUtils.isNotEmpty(bookDetail)){
					DangDangBookDetailDTO dangDangBookDetailDTO = mapper.fromJson(bookDetail, DangDangBookDetailDTO.class);
					if(dangDangBookDetailDTO != null){
						return dangDangBookDetailDTO.getData().getHtml();
					}
				}
			}
		} catch (Exception e) {
			logger.error(ExceptionUtil.stackTraceText(e));
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据isbn获取当当手机图书详情URL
	 * @param isbn
	 * @return
	 * @throws IOException
	 */
	public String getMDdDetailUrlByIsbn(String isbn) throws IOException, InterruptedException {
		// 获取网页对象
		HtmlPage htmlpage = webClient.getPage(M_DD_BASE_URL + isbn);

		// 主要是这个线程的等待 因为js加载也是需要时间的
		Thread.sleep(3000);

		DomElement seachList = htmlpage.getElementById("j_list");

		DomNodeList<HtmlElement> a = seachList.getElementsByTagName("a");

		if(!a.isEmpty()){
			String href = a.get(0).getAttribute("href");
			return href.substring(0, href.indexOf("?"));
		}
		return M_DD_INDEX_URL;
	}
}
