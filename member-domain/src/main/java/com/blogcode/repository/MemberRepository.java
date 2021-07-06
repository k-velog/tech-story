package com.blogcode.repository;

import com.blogcode.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <pre>
 * com.blogcode.com.blogcode.repository
 *
 * Description :
 * </pre>
 *
 * @author leejinho
 * @version 1.0
 * @see <pre>
 * == 개정이력(Modification Information) ==
 *
 * 수정일     수정자   수정내용
 * ---------- -------- -------------------
 * 2021.07.05 leejinho 최초 생성
 * </pre>
 * @since 2021.07.05
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}