package com.blogcode.blog.rest;

import com.blogcode.domain.Posts;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/api/blog", produces = MediaTypes.HAL_JSON_VALUE)
public class BlogRestController {
    // TODO blog 목록 조회
    @GetMapping
    public ResponseEntity queryBlogs(Posts posts){


        return ResponseEntity.ok().build();
    }

    // TODO blog 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity getBlog(@PathVariable Long id){


        return ResponseEntity.ok().build();
    }

    // TODO blog 생성
    @PostMapping
    public ResponseEntity createBlog(Posts posts){


        return ResponseEntity.ok().build();
    }

    // TODO blog 수정
    @PutMapping("/{id}")
    public ResponseEntity createBlog(@PathVariable Long id){


        return ResponseEntity.ok().build();
    }

    // TODO blog 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBlog(@PathVariable Long id){


        return ResponseEntity.ok().build();
    }
}
