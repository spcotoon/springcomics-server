package com.springcomics.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComicApproval {
    private String title;
    private String synopsis;
    private String genre;
    private String thumbnailUrl;
}
