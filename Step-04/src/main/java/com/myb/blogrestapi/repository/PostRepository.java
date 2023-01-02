package com.myb.blogrestapi.repository;

import com.myb.blogrestapi.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post,Long> , PagingAndSortingRepository<Post,Long> {
}
