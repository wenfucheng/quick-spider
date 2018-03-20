package com.fangxuele.spiderproject;

import com.fangxuele.spiderproject.mapper.TDangdangCommentMapper;
import com.fangxuele.spiderproject.service.JingDongSpiderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by wfc on 2017/8/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JingDongSpiderTest {

	@Autowired
	private JingDongSpiderService jingDongSpiderService;

	@Autowired
	private TDangdangCommentMapper dangdangCommentMapper;

	@Test
	public void getCommentInfo() throws Exception {

		jingDongSpiderService.getBookInfoByIsbn("9787539770222");

//		jingDongSpiderService.getDangDangCommentInfo();

	}
}
