package kr.hs.entrydsm.husky.global.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    private String color;
    private String pretext;
    private String authorName;
    private String title;
    private String text;
    private String footer;
    private Long ts;
    private List<Field> fields;
}
