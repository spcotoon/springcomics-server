package com.springcomics.api.repository.comic;

import com.springcomics.api.admin.adminDto.ComicHeadDto;
import com.springcomics.api.domain.comic.ComicHead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ComicHeadRepositoryCustom {

    Slice<ComicHead> getComicHeadList(String searchWord, String genre, Pageable  pageable);

    List<ComicHead> findAllByPenName(String penName);
    Page<ComicHeadDto> findComicHeadListByAdminPage(Pageable pageable);
}
