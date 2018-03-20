package com.fangxuele.spiderproject;

import com.fangxuele.spiderproject.service.DangDangCommentService;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wfc on 2017/8/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DangDangSpiderTest {

	@Autowired
	private DangDangCommentService dangDangCommentService;


	@Test
	@Rollback()
	public void getCommentInfo() throws Exception {
//		dangDangCommentService.getDangDangCommentByIsbn("9787533274269");
		dangDangCommentService.getDangDangCOmment();
//		dangDangCommentService.getProductIdByIsbn("9787533255879");
	}
}
