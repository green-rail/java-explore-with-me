package ru.practicum.explore.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.comment.Comment;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private Long eventId;
    private UserShortDto author;

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime postedOn;

    private String content;

    public static CommentDto toDto(Comment commentEntity) {

        return new CommentDto(
                commentEntity.getId(),
                commentEntity.getEvent().getId(),
                UserShortDto.toDto(commentEntity.getAuthor()),
                commentEntity.getPostedOn(),
                commentEntity.getContent());
    }
}
