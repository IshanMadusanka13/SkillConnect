package com.skillconnect.server.repository;

import com.skillconnect.server.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, int> {

}
