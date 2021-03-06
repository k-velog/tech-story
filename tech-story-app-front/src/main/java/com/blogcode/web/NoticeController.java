package com.blogcode.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/notice")
public class NoticeController {

    //페이지 조회
    @GetMapping("/{page}")
    public ModelAndView getNoticeList(ModelAndView modelAndView, @PathVariable Integer page){
        return modelAndView;
    }

    //검색
    @GetMapping("/noticeSearch/{noticeSearch}")
    public ModelAndView searchNotice(ModelAndView modelAndView, @PathVariable String noticeSearch){
        return modelAndView;
    }

    //세부내용 조회
    @GetMapping("/{noticeId}")
    public ModelAndView getNotice(ModelAndView modelAndView, @PathVariable Integer noticeId){
        return modelAndView;
    }

    //출간

    //임시저장
}
