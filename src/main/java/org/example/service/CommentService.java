package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Comment;
import service.CommentService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

    private final CommentService commentService = CommentService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Comment comment = objectMapper.readValue(req.getInputStream(), Comment.class);

        // Timestamp-ის დამატება ავტომატურად
        comment.setTimestamp(LocalDateTime.now());

        commentService.save(comment);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"message\": \"Comment added successfully\"}");
    }
}
