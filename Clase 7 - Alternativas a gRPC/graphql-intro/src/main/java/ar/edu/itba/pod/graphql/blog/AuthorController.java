package ar.edu.itba.pod.graphql.blog;


import ar.edu.itba.pod.graphql.blog.dao.AuthorDao;
import ar.edu.itba.pod.graphql.blog.model.Author;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController {
    private final AuthorDao authorDao;
    public AuthorController(AuthorDao authorDao){
        this.authorDao = authorDao;
    }
    @QueryMapping
    public Author getAuthor(String authorId) {
        return authorDao.getAuthor(authorId);
    }
}