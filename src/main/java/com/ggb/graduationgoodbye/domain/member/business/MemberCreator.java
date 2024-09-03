package com.ggb.graduationgoodbye.domain.member.business;

import com.ggb.graduationgoodbye.domain.member.common.entity.Member;
import com.ggb.graduationgoodbye.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 멤버 Create.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MemberCreator {

  private final MemberRepository memberRepository;

  public void save(Member member) {
    memberRepository.save(member);
  }

}