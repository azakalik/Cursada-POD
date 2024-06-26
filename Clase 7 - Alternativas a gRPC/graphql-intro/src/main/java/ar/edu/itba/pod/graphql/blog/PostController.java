package ar.edu.itba.pod.graphql.blog;

import ar.edu.itba.pod.graphql.GraphqlConfiguration;
import ar.edu.itba.pod.graphql.blog.dao.AuthorDao;
import ar.edu.itba.pod.graphql.blog.dao.PostDao;
import ar.edu.itba.pod.graphql.blog.model.Author;
import ar.edu.itba.pod.graphql.blog.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class PostController {
    private final PostDao postDao;
    public PostController(PostDao postDao){
        this.postDao = postDao;
    }

    @QueryMapping
    public List<Post> recentPosts(@Argument int count, @Argument int offset) {
        return postDao.getRecentPosts(count, offset);
    }
}