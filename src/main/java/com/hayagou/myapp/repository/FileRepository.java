package com.hayagou.myapp.repository;

import com.hayagou.myapp.entity.File;
import com.hayagou.myapp.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

//    @Query("select f from File f where :#{#post.postId} = f.post.postId")
    List<File> findAllByPost(Post post);

    @Query("select f from File f where f.post.postId  =:id")
    List<File> findAllByPostId(Long id);
}
