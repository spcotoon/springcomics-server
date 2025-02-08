package com.springcomics.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcomics.api.domain.comic.ComicBody;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.request.ComicUpload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(ComicController.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class ComicBodyControllerTest {

    @Autowired
    MockMvc mockMvc;



    @Autowired
    private ComicBodyRepository comicBodyRepository;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("업로드시 제목은 필수")
    public void 웹툰업로드() throws Exception {
        //given
        ComicUpload request = ComicUpload.builder()
                .title(" ")
                .authorComment("조석")
                .build();


        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        //when

        mockMvc.perform(post("/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andDo(print());

        
        //then
     }

     @Test
     @DisplayName("/upload 요청시 DB에 값 저장")
     @Rollback(value = false)
     public void uploadComic() throws Exception {
         //given

         ComicUpload request = ComicUpload.builder()
                 .title("조의 영역")
                 .authorComment("조석")
                 .build();

         ObjectMapper objectMapper = new ObjectMapper();
         String json = objectMapper.writeValueAsString(request);

         mockMvc.perform(post("/upload")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(json))
                 .andExpect(status().isBadRequest())
                 .andDo(print());

         //when

         //then

         Assertions.assertEquals(1L, comicBodyRepository.count());

         ComicBody comicBody = comicBodyRepository.findAll().get(0);

         Assertions.assertEquals("조의 영역", comicBody.getTitle());
     }

     @Test
     public void test() throws Exception {
         //given

         //when

         //then
      }


}