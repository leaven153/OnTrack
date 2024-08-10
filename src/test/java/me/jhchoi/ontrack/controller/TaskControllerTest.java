package me.jhchoi.ontrack.controller;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.dto.TaskBinRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    
    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test @DisplayName("그냥 List 안의 Map을 접근해보자")
    void testListMap(){
        // given
        List<Map<Long, Long>> testList = new ArrayList<>();
        Map<Long, Long> testMap1 = new HashMap<>();
        Map<Long, Long> testMap2 = new HashMap<>();
        testMap1.put(9L, 14L);
        testMap2.put(10L, 25L);

        testList.add(testMap1);
        testList.add(testMap2);

        log.info("List<Map<Long, Long>> testList: {}", testList);
        // List<Map<Long, Long>> testList: [{9=14}, {10=25}]

        log.info("List<Map<Long, Long>> testList: {}", testList.get(0));
        // List<Map<Long, Long>> testList: {9=14}

        log.info("List<Map<Long, Long>> testList: {}", testList.get(0).keySet());
        // List<Map<Long, Long>> testList: [9]

        // 빈 map이 들어있을 때
        List<Map<Long, Long>> testList2 = new ArrayList<>();
        Map<Long, Long> testMap3 = new HashMap<>();
        testList2.add(testMap3);
        log.info("List<Map<Long, Long>> testList2: {}", testList2);
        // List<Map<Long, Long>> testList2: [{}]
        log.info("List<Map<Long, Long>> testList2.size(): {}", testList2.size());
        // List<Map<Long, Long>> testList2.size(): 1
        log.info("List<Map<Long, Long>> testList2.get(0): {}", testList2.get(0));
        // List<Map<Long, Long>> testList2.get(0): {}

    }
//    @Test @DisplayName("ECMA에서 map을 보냈을 때")
//    void mapperTest() throws JacksonException {
//        String jsonInput = "{\"testId\": \"value\"}";
//        ObjectMapper mapper = new ObjectMapper();
//
//        Map<String, String> map = (Map<String, String>) mapper.readValue(jsonInput, TaskBinRequest.class);
//        log.info("뭘까 이건: {}", map);
//    }
}