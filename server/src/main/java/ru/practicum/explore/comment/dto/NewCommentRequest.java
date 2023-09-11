package ru.practicum.explore.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.comment.Comment;
import ru.practicum.explore.error.ErrorMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentRequest {

    @NotNull
    @NotBlank
    @Size(max = 7000, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String content;

    public Comment toEntity() {
        var comment = new Comment();
        comment.setContent(content);
        return comment;
    }

}
