package com.springcomics.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcomics.api.domain.comic.ComicBody;
import com.springcomics.api.repository.comic.ComicBodyRepository;
import com.springcomics.api.repository.ImageRepository;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class ComicBodyServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ComicBodyRepository comicBodyRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("/upload 요청시 DB에 값 저장")
    @Rollback(value = false)
    public void uploadComic() throws Exception {
        //given

        ComicUpload request = ComicUpload.builder()
                .title("나혼렙")
                .authorComment("좋아요 눌러주세요")
                .imageUrls(List.of("1.png","2.png","3.png"))
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //when

        //then

        Assertions.assertEquals(1L, comicBodyRepository.count());
        Assertions.assertEquals(3L,imageRepository.count());

        ComicBody comicBody = comicBodyRepository.findAll().get(0);


        Assertions.assertEquals("나혼렙", comicBody.getTitle());
    }

}