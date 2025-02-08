package com.springcomics.api.domain.member;

import com.springcomics.api.domain.comic.ComicHead;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseUser{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String penName;
    private String selfIntroduction;
    private int salesAmount = 0;

    @OneToMany(mappedBy = "artist")
    private List<ComicHead> comicHeads = new ArrayList<>();

    @Builder
    public Artist(String email, String password, String penName, String selfIntroduction) {
        this.email = email;
        this.password = password;
        this.penName = penName;
        this.selfIntroduction = selfIntroduction;
    }

    public void addSalesAmount(int salesAmount) {
        this.salesAmount += salesAmount;
    }

}
