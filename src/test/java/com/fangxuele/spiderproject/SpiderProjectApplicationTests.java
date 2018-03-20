package com.fangxuele.spiderproject;

import com.fangxuele.spiderproject.service.SpiderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderProjectApplicationTests {

	@Autowired
	SpiderService spiderService;

	@Test
	public void contextLoads() throws IOException, InterruptedException {
//		String dangdangDetail = spiderService.getDangDangBookDetailByIsbn("9787533274269");

		String url = spiderService.getMJdDetailUrlByIsbn("9787559001511");
		System.out.println(url);
	}

}
