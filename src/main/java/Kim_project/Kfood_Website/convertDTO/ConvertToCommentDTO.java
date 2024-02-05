package Kim_project.Kfood_Website.convertDTO;

import Kim_project.Kfood_Website.domain.Comment;
import Kim_project.Kfood_Website.dto.CommentDTO;

import java.util.ArrayList;
import java.util.List;

public class ConvertToCommentDTO {
    public List<CommentDTO> convertToCommentDTOs(List<Comment> comments) {
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentNumber(comment.getCommentNumber());
            commentDTO.setCommentText(comment.getCommentText());
            commentDTO.setMenuNumber(comment.getMenuNumber());
            commentDTO.setMemberId(comment.getMemberId());
            commentDTOs.add(commentDTO);
        }
        return commentDTOs;
    }
}
