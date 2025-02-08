package com.springcomics.api.repository.comic;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcomics.api.admin.adminDto.ComicHeadDto;
import com.springcomics.api.admin.adminDto.QComicHeadDto;
import com.springcomics.api.domain.comic.ComicHead;
import com.springcomics.api.domain.comic.Genre;
import com.springcomics.api.domain.comic.QComicBody;
import com.springcomics.api.domain.comic.QComicHead;
import com.springcomics.api.domain.member.QArtist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ComicHeadRepositoryImpl implements ComicHeadRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    private final QComicHead comicHead = QComicHead.comicHead;
    private final QArtist artist = QArtist.artist;
    private final QComicBody comicBody = QComicBody.comicBody;

    @Override
    public Slice<ComicHead> getComicHeadList(String searchWord, String genre,Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchWord != null && !searchWord.isBlank()) {
            String sanitizedSearchWord = searchWord.replaceAll("\\s+", "");

            builder.and(Expressions.stringTemplate("REPLACE({0}, ' ', '')", comicHead.title).containsIgnoreCase(sanitizedSearchWord)
                    .or(Expressions.stringTemplate("REPLACE({0}, ' ', '')", comicHead.artist.penName).containsIgnoreCase(sanitizedSearchWord)));
        }

        if (genre != null && !genre.isBlank()) {
            try {
                builder.and(comicHead.genre.eq(Genre.valueOf(genre)));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid genre value: " + genre);
            }
        }

        List<ComicHead> content = jpaQueryFactory
                .selectFrom(comicHead)
                .where(builder)
                .join(comicHead.artist, QArtist.artist)
                .orderBy(comicHead.lastModifiedDate.desc()) // 정렬
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .fetch();

        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1);
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public List<ComicHead> findAllByPenName(String penName) {

        return jpaQueryFactory
                .selectFrom(comicHead)
                .join(comicHead.artist, artist).fetchJoin()
                .where(artist.penName.eq(penName))
                .orderBy(comicHead.lastModifiedDate.desc())
                .fetch();

    }

    @Override
    public Page<ComicHeadDto> findComicHeadListByAdminPage(Pageable pageable) {
        List<ComicHeadDto> comicHeadDtos = jpaQueryFactory
                .select(new QComicHeadDto(
                        comicHead.id,
                        comicHead.title,
                        comicHead.genre,
                        artist.penName,
                        artist.email,
                        comicBody.count(),
                        comicHead.createdDate,
                        JPAExpressions
                                .select(comicBody.createdDate.max())
                                .from(comicBody)
                                .where(comicBody.comicHead.eq(comicHead))
                ))
                .from(comicHead)
                .leftJoin(comicHead.artist, artist)
                .leftJoin(comicHead.comicBodies, comicBody)
                .groupBy(comicHead.id, artist.penName, artist.email, comicHead.createdDate)
                .orderBy(comicHead.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(comicHead.count())
                .from(comicHead);


        return PageableExecutionUtils.getPage(comicHeadDtos, pageable, countQuery::fetchOne);
    }
}

