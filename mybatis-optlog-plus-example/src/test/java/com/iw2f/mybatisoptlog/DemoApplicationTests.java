package com.iw2f.mybatisoptlog;


import com.alibaba.fastjson.JSON;
import com.iw2f.mybatisoptlog.persistence.dao.CourseMapper;
import com.iw2f.mybatisoptlog.persistence.dao.ScoreMapper;
import com.iw2f.mybatisoptlog.persistence.pojo.Course;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*; //注意这里, 这是junit5引入的;  junit4引入的是org.junit.Test这样类似的包
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;//这里只写SpringBootTest这个注解; 如果是junit4的话, 就要加上@RunWith(SpringRunner.class)

import java.util.List;

@Slf4j
@ExtendWith(SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private ScoreMapper courseMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    void contextLoads() {
    }

    @BeforeAll
    public static void beforeAll() {
        log.info("beforeAll");
    }

    @BeforeEach
    public void beforeEach() {
        log.info("beforeEach");
        // mockMvc = MockMvcBuilders.standaloneSetup(new IndexController()).build();
        // mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    public void afterEach() {
        log.info("afterEach");
    }

    @AfterAll
    public static void afterAll() {
        log.info("afterAll");
    }


    public void testTwo() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/index/getData")
                .param("searchPhrase", "ABC")          //传参
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);  //请求类型 JSON
        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())     //期望的结果状态 200
                .andDo(MockMvcResultHandlers.print())                 //添加ResultHandler结果处理器，比如调试时 打印结果(print方法)到控制台
                .andReturn();                                         //返回验证成功后的MvcResult；用于自定义验证/下一步的异步处理；
        int status = mvcResult.getResponse().getStatus();                 //得到返回代码
        String content = mvcResult.getResponse().getContentAsString();    //得到返回结果
        log.info("status:" + status + ",content:" + content);
    }

    @Test
    public void testTwo3() throws Exception {
        String str = JSON.toJSONString(courseMapper.selectAll());
        log.info(str);
    }
}