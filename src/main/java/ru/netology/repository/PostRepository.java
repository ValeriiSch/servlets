package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final AtomicLong postID;
    private final ConcurrentHashMap<Long, Post> posts;

    public PostRepository() {
        postID = new AtomicLong(0);
        posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        long postExistID = post.getId();
        if (postExistID > 0 && posts.containsKey(postExistID)) {
            posts.replace(postExistID, post);
        } else {
            long newPostID = postExistID;
            if (postExistID == 0) {
                newPostID = postID.incrementAndGet();
                while (posts.containsKey(newPostID)) {
                    newPostID = postID.incrementAndGet();
                }
            }
            post.setId(newPostID);
            posts.put(newPostID, post);
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
