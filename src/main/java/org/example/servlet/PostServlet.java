package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.request.PostRequest;
import org.example.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@WebServlet("/post")
@MultipartConfig
public class PostServlet extends HttpServlet {

    private final PostService postService = PostService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String imageUploadDir = "uploaded_images";

    @Override
    public void init() throws ServletException {
        super.init();
        File uploadDir = new File(imageUploadDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        String text = req.getParameter("text");
        Part imagePart = req.getPart("image");
        String fileName = null;

        if (imagePart != null && imagePart.getSize() > 0) {
            fileName = System.currentTimeMillis() + "_" + Paths.get(imagePart.getSubmittedFileName()).getFileName();
            File file = new File(imageUploadDir, fileName);
            try (InputStream input = imagePart.getInputStream()) {
                Files.copy(input, file.toPath());
            }
        }

        PostRequest postRequest = new PostRequest(text, fileName);
        postService.addPost(postRequest);

        // Success response
        mapper.writeValue(resp.getWriter(), postRequest);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        req.setCharacterEncoding("UTF-8");

        var posts = postService.getAllPosts();
        mapper.writeValue(resp.getWriter(), posts);
    }
}
