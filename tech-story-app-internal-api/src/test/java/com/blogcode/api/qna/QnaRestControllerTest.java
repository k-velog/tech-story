package com.blogcode.api.qna;

import com.blogcode.InternalApiApplication;
import com.blogcode.common.RestDocsConfiguration;
import com.blogcode.member.domain.Member;
import com.blogcode.member.repository.MemberRepository;
import com.blogcode.posts.domain.HashTag;
import com.blogcode.posts.domain.PostType;
import com.blogcode.posts.domain.Posts;
import com.blogcode.posts.dto.HashTagDTO;
import com.blogcode.posts.dto.QnaDTO;
import com.blogcode.posts.repository.HashTagRepository;
import com.blogcode.posts.repository.QnaRepository;
import com.blogcode.posts.service.QnaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = InternalApiApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
class QnaRestControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    QnaService qnaService;

    @Autowired
    QnaRepository qnaRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("Qna ??????")
    public void createQna() throws Exception{
        Member member = getRandomMember();
        System.out.println("member = " + member.getId());

        HashTagDTO hashTag = HashTagDTO.builder()
                .keyword("?????????")
                .build();

        QnaDTO qnaDTO = QnaDTO.builder()
                .title("????????? ???")
                .content("?????????")
                .thumbnailPath("/")
                .tempSaveStatus("N")
                .dType(PostType.QNA)
                .memberId(member.getId())
                .build();

        this.mockMvc.perform(post("/api/qna")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Forwarded-Proto", "http")
                .header("X-Forwarded-Host","localhost")
                .header("X-Forwarded-Port", "8084")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(qnaDTO))
        )
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("dtype").exists())
                .andExpect(jsonPath("createId").exists())
                .andDo(document("create-qna",
                        links(
                                linkWithRel("self").description("????????? qna ??????"),
                                linkWithRel("query-qna").description("qna ?????? ??????"),
                                linkWithRel("update-qna").description("????????? qna ??????"),
                                linkWithRel("profile").description("?????? Rest API profile ??????")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept ??????"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                        ),
                        requestFields(
                                fieldWithPath("title").description("????????? qna ??????"),
                                fieldWithPath("content").description("????????? qna ??????"),
                                fieldWithPath("tempSaveStatus").description("????????? qna??? ???????????? ??????"),
                                fieldWithPath("memberId").description("????????? qna ????????? Id"),
                                fieldWithPath("dtype").description("????????? qna??? ??????(?????????/Q&A)"),
                                fieldWithPath("thumbnailPath").description("????????? qna??? ????????? ??????(??????)")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type - Hal Json"),
                                headerWithName(HttpHeaders.LOCATION).description("?????? Location")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("????????? qna??? ?????????"),
                                fieldWithPath("title").description("????????? qna??? ??????"),
                                fieldWithPath("content").description("????????? qna??? ??????"),
                                fieldWithPath("writerId").description("????????? qna??? ????????? ?????????"),
                                fieldWithPath("writerEmail").description("????????? qna ????????? ?????????"),
                                fieldWithPath("writerName").description("????????? qna??? ????????? ??????"),
                                fieldWithPath("views").description("????????? qna??? ?????????"),
                                fieldWithPath("thumbnailPath").description("????????? qna??? ????????? ??????"),
                                fieldWithPath("tempSaveStatus").description("????????? qna??? ??????????????????"),
                                fieldWithPath("dtype").description("????????? qna??? ??????(?????????/Q&A)"),
                                fieldWithPath("_links.self").description("????????? qna ?????? URI"),
                                fieldWithPath("_links.query-qna").description("qna ?????? ??????"),
                                fieldWithPath("_links.update-qna").description(")????????? qna ?????? URI"),
                                fieldWithPath("_links.profile").description("?????? Rest API profile ??????"),

                                fieldWithPath("createId").description("qna??? ????????? ?????? Id"),
                                fieldWithPath("createDateTime").description("qna??? ????????? ??????"),
                                fieldWithPath("modifyId").description("qna??? ??????????????? ????????? ?????? Id"),
                                fieldWithPath("modifyDateTime").description("qna??? ??????????????? ????????? ??????")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("Qna ?????? ?????? - ?????? ??????")
    public void qnaCreateFail_member() throws Exception{
        QnaDTO qnaDTO = QnaDTO.builder()
                .title("????????? ???")
                .content("?????????")
                .thumbnailPath("/")
                .tempSaveStatus("N")
                .dType(PostType.QNA)
                .build();

        this.mockMvc.perform(post("/api/qna")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(qnaDTO))
        )
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("Qna ?????? ?????? - ??? ?????? ??????")
    public void qnaCreateFail_All() throws Exception{
        QnaDTO qnaDTO = QnaDTO.builder()
                .build();

        this.mockMvc.perform(post("/api/qna")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(qnaDTO))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors.[0].field").exists())
                .andExpect(jsonPath("errors.[1].field").exists())
                .andExpect(jsonPath("errors.[2].field").exists())
                .andExpect(jsonPath("errors.[3].field").exists())
                .andExpect(jsonPath("errors.[4].field").exists())
                .andExpect(jsonPath("_links.index").exists())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("Qna ?????? ?????? - ?????? X")
    public void getQnaList() throws Exception {
        String postsList = "_embedded.postsList";

        this.mockMvc.perform(get("/api/qna")
                .param("page", "0")
                .param("size","6")
                .param("sort","createDateTime,DESC")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-Forwarded-Proto", "http")
                .header("X-Forwarded-Host","localhost")
                .header("X-Forwarded-Port", "8084")
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("get-qna-list",
                        links(
                                linkWithRel("self").description("????????? qna ??????"),
                                linkWithRel("trend").description("?????? ??? ?????? ???????????? ????????? qna ??????"),
                                linkWithRel("trend-week").description("????????? ??? ?????? ???????????? ????????? qna ??????"),
                                linkWithRel("trend-month").description("?????? ??? ?????? ???????????? ????????? qna ??????"),
                                linkWithRel("trend-year").description("?????? ??? ?????? ???????????? ????????? qna ??????"),
                                linkWithRel("recent").description("?????? ????????? qna ??????"),
                                linkWithRel("search").description("?????? ???????????? qna ??????"),
                                linkWithRel("blog").description("blog ?????? ??????"),
                                linkWithRel("profile").description("?????? Rest API profile ??????"),
                                linkWithRel("first").description("????????? qna ??? ?????? ??? ?????? ?????????"),
                                linkWithRel("last").description("????????? qna ??? ?????? ????????? ?????????"),
                                //linkWithRel("prev").description("?????? qna ????????? ??????"),
                                linkWithRel("next").description("?????? qna ????????? ??????")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept ??????"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                        ),
                        requestParameters(
                                parameterWithName("page").description("qna??? ????????? ????????? ??????"),
                                parameterWithName("size").description("????????? qna??? ????????? ????????? ??????"),
                                parameterWithName("sort").description("????????? ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type - Hal Json")
                        ),
                        relaxedResponseFields(
                                fieldWithPath(postsList).description("????????? qna ?????????"),
                                fieldWithPath(postsList+"[0].createId").description("qna??? ????????? ?????? ?????????"),
                                fieldWithPath(postsList+"[0].createDateTime").description("qna ?????? ??????"),
                                fieldWithPath(postsList+"[0].id").description("????????? qna ?????????"),
                                fieldWithPath(postsList+"[0].title").description("????????? qna ??????"),
                                fieldWithPath(postsList+"[0].content").description("????????? qna ??????"),
                                fieldWithPath(postsList+"[0].writerName").description("????????? qna ????????? ?????????"),
                                fieldWithPath(postsList+"[0].writerEmail").description("????????? qna ????????? ?????????"),
                                fieldWithPath(postsList+"[0].views").description("????????? qna ?????????"),
                                fieldWithPath(postsList+"[0].likes").description("????????? qna ????????? ???"),
                                fieldWithPath(postsList+"[0].thumbnailPath").description("????????? qna ????????? ??????"),
                                fieldWithPath(postsList+"[0].countScripting").description("????????? qna??? ???????????? ??????")
                        )

                ));
    }


    @Test
    @DisplayName("Qna ??????")
    public void getQnaDetail() throws Exception {
        this.mockMvc.perform(get("/api/qna/1")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-Forwarded-Proto", "http")
                .header("X-Forwarded-Host","localhost")
                .header("X-Forwarded-Port", "8084")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("useYn").isNotEmpty())
                .andExpect(jsonPath("createDateTime").exists())
                .andExpect(jsonPath("writerName").exists())
                .andDo(print())
                .andDo(document("get-qna-detail",
                        links(
                                linkWithRel("self").description("?????? qna ??????"),
                                linkWithRel("update-qna").description("?????? qna ??????"),
                                linkWithRel("delete-qna").description("?????? qna ??????"),
                                linkWithRel("likes").description("?????? qna ????????? ??????"),
                                linkWithRel("scripting").description("?????? qna ???????????? ??????"),
                                linkWithRel("hashTag").description("?????? ???????????? ?????? ??????"),
                                linkWithRel("profile").description("?????? Rest API profile ??????"),
                                linkWithRel("create-reply").description("?????? post ??????"),
                                linkWithRel("create-re-reply").description("????????? post ??????"),
                                linkWithRel("like-reply").description("?????? ????????? ??????")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept ??????")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type - Hal Json")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("????????? qna id"),
                                fieldWithPath("writerName").description("qna ????????? name"),
                                fieldWithPath("writerId").description("qna ????????? id"),
                                fieldWithPath("title").description("qna ??????"),
                                fieldWithPath("content").description("qna ??????"),
                                fieldWithPath("views").description("qna ?????????"),
                                fieldWithPath("likes").description("qna ????????? ???"),
                                fieldWithPath("replyList").description("?????? ?????????"),
                                fieldWithPath("hashTags").description("???????????? ?????????"),
                                fieldWithPath("createDateTime").description("qna ????????????")
                        )
                ));

        ;
    }


    @Test
    @DisplayName("Qna ?????? ?????? - trend")
    public void getQnaList_trend() throws Exception {
        String postsList = "_embedded.postsList";


        this.mockMvc.perform(get("/api/qna")
                .param("page", "0")
                .param("size","6")
                .param("sort","createDateTime,DESC")
                .param("interval","day")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .header("X-Forwarded-Proto", "http")
                .header("X-Forwarded-Host","localhost")
                .header("X-Forwarded-Port", "8084")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath(postsList+"[0].createId").exists())
                .andExpect(jsonPath(postsList+"[0].createDateTime").exists())
                .andExpect(jsonPath(postsList+"[0].id").exists())
                .andExpect(jsonPath(postsList+"[0].title").exists())
                .andExpect(jsonPath(postsList+"[0].content").exists())
                .andExpect(jsonPath(postsList+"[0].writerName").exists())
                .andExpect(jsonPath(postsList+"[0].writerEmail").exists())
                .andExpect(jsonPath(postsList+"[0].views").exists())
                .andExpect(jsonPath(postsList+"[0].likes").exists())
                .andExpect(jsonPath(postsList+"[0].thumbnailPath").exists())
                .andExpect(jsonPath(postsList+"[0].countScripting").exists())
                .andDo(print())
                ;

    }

    @Test
    @DisplayName("Qna ?????? ?????? - trend/recent/search")
    public void getQnaList_TODO() throws Exception {

    }


    @Test
    @DisplayName("Qna ??????")
    public void qnaUpdate_OK() throws Exception {
        QnaDTO qnaDTO = QnaDTO.builder()
                .title("????????? ??? - ??????")
                .content("????????? - ??????2")
                .thumbnailPath("/ - ??????2")
                .tempSaveStatus("N - ??????")
                .memberId(1L)
                .dType(PostType.QNA)
                .build();

        this.mockMvc.perform(put("/api/qna/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Forwarded-Proto", "http")
                .header("X-Forwarded-Host","localhost")
                .header("X-Forwarded-Port", "8084")
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(qnaDTO))
        )
                .andDo(print())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("dtype").exists())
                .andExpect(jsonPath("createId").exists())
                .andDo(document("update-qna",
                        links(
                                linkWithRel("self").description("????????? qna ??????"),
                                linkWithRel("query-qna").description("qna ?????? ??????"),
                                linkWithRel("profile").description("?????? Rest API profile ??????")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept ??????"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type ??????")
                        ),
                        requestFields(
                                fieldWithPath("title").description("????????? qna ??????"),
                                fieldWithPath("content").description("????????? qna ??????"),
                                fieldWithPath("tempSaveStatus").description("????????? qna??? ???????????? ??????"),
                                fieldWithPath("dtype").description("????????? qna??? ??????(?????????/Q&A)"),
                                fieldWithPath("thumbnailPath").description("????????? qna??? ????????? ??????(??????)"),
                                fieldWithPath("memberId").description("????????? qna??? ????????? id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type - Hal Json")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").description("qna??? ?????????"),
                                fieldWithPath("title").description("????????? qna??? ??????"),
                                fieldWithPath("content").description("????????? qna??? ??????"),
                                fieldWithPath("thumbnailPath").description("????????? qna??? ????????? ??????"),
                                fieldWithPath("tempSaveStatus").description("????????? qna??? ??????????????????"),
                                fieldWithPath("dtype").description("????????? qna??? ??????(?????????/Q&A)"),
                                fieldWithPath("_links.self").description("????????? qna ?????? URI"),
                                fieldWithPath("_links.query-qna").description("qna ?????? ??????"),
                                fieldWithPath("_links.profile").description("?????? Rest API profile ??????"),

                                fieldWithPath("createId").description("qna??? ????????? ?????? Id"),
                                fieldWithPath("createDateTime").description("qna??? ????????? ??????"),
                                fieldWithPath("modifyId").description("qna??? ??????????????? ????????? ?????? Id"),
                                fieldWithPath("modifyDateTime").description("qna??? ??????????????? ????????? ??????")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("Qna ??????")
    public void deleteQna() throws Exception {
        Member member = getRandomMember();

        Posts posts = Posts.builder()
                .title("?????? ?????????")
                .content("?????? ?????????")
                .thumbnailPath("/")
                .dType(PostType.QNA)
                .build();
        posts.setMemberData(member);

        Posts save = this.qnaRepository.save(posts);
        Long id = save.getId();


        this.mockMvc.perform(delete("/api/qna/"+id)
                .header("X-Forwarded-Proto", "http")
                .header("X-Forwarded-Host","localhost")
                .header("X-Forwarded-Port", "8084")
        )
                .andExpect(status().isOk())
                .andDo(document("delete-qna",
                        links(
                                linkWithRel("self").description("????????? qna ??????"),
                                linkWithRel("query-qna").description("qna ?????? ??????"),
                                linkWithRel("profile").description("?????? Rest API profile ??????")
                        )
                ))
        ;
    }




    public Member getRandomMember(){
        Integer ints = new Random().nextInt(100);
        String email = "zee12"+ ints +"@gstim.com";
        String password = "itm@6700";

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name("?????????")
                .build();

        this.memberRepository.save(member);

        return member;
    }

    public QnaDTO stackRandomPosts(){
        Member randomMember = getRandomMember();
        Integer ints = new Random().nextInt(6);

        QnaDTO qnaDTO = QnaDTO.builder()
                .title("????????? ???" + ints)
                .content("?????????" + ints)
                .thumbnailPath("/" + ints)
                .tempSaveStatus("N" + ints)
                .dType(PostType.QNA)
                .memberId(randomMember.getId())
                .build();
        return qnaDTO;
    }

}