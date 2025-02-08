package com.springcomics.api.repository.comic;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.springcomics.api.admin.adminDto.*;
import com.springcomics.api.domain.comic.*;
import com.springcomics.api.exception.ComicNotFound;
import com.springcomics.api.response.OneComicBodyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ComicBodyRepositoryImpl implements ComicBodyRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final QComicBody comicBody = QComicBody.comicBody;
    private final QComicHead comicHead = QComicHead.comicHead;
    private final QImage image = QImage.image;
    private final QComicComment comment = QComicComment.comicComment;

    @Override
    public Page<ComicBody> findOneComicListByComicHeadId(Long comicHeadId, Pageable pageable) {

        List<ComicBody> content = jpaQueryFactory
                .selectFrom(comicBody)
                .where(comicBody.comicHead.id.eq(comicHeadId))
                .join(comicBody.comicHead, comicHead)
                .fetchJoin()
                .orderBy(comicBody.createdDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(comicBody.count())
                .from(comicBody)
                .where(comicBody.comicHead.id.eq(comicHeadId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    @Override
    public Long findMaxNoByComicHeadId(Long comicHeadId) {


        return jpaQueryFactory
                .select(comicBody.no.max())
                .from(comicBody)
                .where(comicBody.comicHead.id.eq(comicHeadId))
                .fetchOne();
    }

    @Override
    public OneComicBodyResponse findOneComicByComicHeadIdAndNo(Long comicHeadId, Long no) {

        ComicBody comicBodyEntity = jpaQueryFactory
                .selectFrom(comicBody)
                .where(comicBody.comicHead.id.eq(comicHeadId)
                        .and(comicBody.no.eq(no)))
                .leftJoin(comicBody.images, image).fetchJoin()
                .fetchOne();

        if (comicBodyEntity == null) {
            throw new ComicNotFound();
        }

        List<String> imageUrls = comicBodyEntity.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());

        return new OneComicBodyResponse(
                comicBodyEntity.getId(),
                comicBodyEntity.getTitle(),
                comicBodyEntity.getAuthorComment(),
                comicBodyEntity.getThumbnail(),
                imageUrls,
                comicBodyEntity.getCreatedDate(),
                comicBodyEntity.getNo()
        );
    }

    @Override
    public Page<ComicBodyDto> findComicBodyWithCommentByComicHeadId(Pageable pageable, Long comicHeadId) {
        List<ComicBodyDto> content = jpaQueryFactory
                .select(new QComicBodyDto(
                        comicBody.id,
                        comicBody.title,
                        comment.count()
                ))
                .from(comicBody)
                .leftJoin(comment).on(comment.comicBody.id.eq(comicBody.id))
                .where(comicBody.comicHead.id.eq(comicHeadId))
                .groupBy(comicBody.id)
                .orderBy(comicBody.createdDate.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(comicBody.count())
                .from(comicBody)
                .where(comicBody.comicHead.id.eq(comicHeadId));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }
}






