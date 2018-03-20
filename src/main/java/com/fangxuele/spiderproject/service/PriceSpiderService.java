package com.fangxuele.spiderproject.service;

import com.fangxuele.spiderproject.util.HttpClientUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springside.modules.utils.mapper.JsonMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by wfc on 2017/9/1.
 */
@Service
public class PriceSpiderService {

	private static JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);

	private static String JINGDONG_PRICE_BASE_URL = "https://p.3.cn/prices/get?pduid=15029696759471947422260&skuid=J_";

	public static void main(String[] args) throws Exception {
		HttpClientUtil.init();
		String filePath = "C:\\Users\\wfc\\Desktop\\价格爬取需求.xlsx";
		File excelFile = new File(filePath); // 创建文件对象
		FileInputStream is = new FileInputStream(excelFile); // 文件流
		Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel 2003/2007/2010 都是可以处理的
		Sheet sheet = workbook.getSheetAt(0);

		XSSFWorkbook wb = new XSSFWorkbook();

		wb.createSheet("价格信息");
		int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数
		// 遍历每一行
		for (int r = 1; r < rowCount; r++) {
			Row row = sheet.getRow(r);
			// 遍历每一列
			Cell urlCell = row.getCell(2);
			String url = urlCell.getStringCellValue();
			if(url.contains("?")){
				url = url.substring(0,url.lastIndexOf("?"));
			}
			String price = null;
			if(url.contains("jd")){
				price = getJingDongPrice(url);
			}else{
				try {
					price = getDangDangPrice(url);
				} catch (Exception e) {
					price = "无数据";
				}
			}
			Cell priceCell = row.getCell(6);
			priceCell.setCellValue(price);

		}
		workbook.write(new FileOutputStream(filePath));

	}


	public static String getDangDangPrice(String href) throws Exception {
		System.err.println(href);
//		Thread.sleep(1000);
		// 解析详情页
		String detail = HttpClientUtil.callUrlGet(href);
		Document detailDocument = Jsoup.parse(detail);
		// 获取当当产品id
		String productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

		// 获取价格信息
		Element mainPrice = detailDocument.getElementById("main_price");
		Elements del = detailDocument.getElementsByTag("del");
//		String originalPrice = del.get(0).text();
//		originalPrice = originalPrice.replace("¥", "");
		String price = mainPrice.text();
		return price;
	}


	public static String getJingDongPrice(String href) throws Exception {
		// 获取京东产品id
		String productId = href.substring(href.lastIndexOf('/') + 1, href.lastIndexOf('.'));

		// 获取价格信息
		String priceUrl = JINGDONG_PRICE_BASE_URL+productId;

		List<Map<String,String>> list = null;
		Map<String, String> priceMap = null;
		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(1000);
				System.err.println(priceUrl);
				String priceInfo = HttpClientUtil.callUrlGet(priceUrl);
				list  = mapper.fromJson(priceInfo, List.class);
				priceMap = list.get(0);
				break;
			} catch (Exception e) {
			}
		}
		return priceMap==null?"无数据":priceMap.get("p");
	}
}
