package ru.yandex.practicum.catsgram.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class Image {
    private Long id;
    private long postId;
    private String originalFileName;
    private String filePath;
}
