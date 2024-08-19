package com.ggb.graduationgoodbye.domain.member.service;

import com.ggb.graduationgoodbye.domain.artist.entity.Artist;
import com.ggb.graduationgoodbye.domain.artist.repository.ArtistRepository;
import com.ggb.graduationgoodbye.domain.auth.dto.TokenDto;
import com.ggb.graduationgoodbye.domain.auth.service.TokenService;
import com.ggb.graduationgoodbye.domain.commonCode.entity.CommonCode;
import com.ggb.graduationgoodbye.domain.commonCode.exception.NotFoundMajorException;
import com.ggb.graduationgoodbye.domain.commonCode.exception.NotFoundUniversityException;
import com.ggb.graduationgoodbye.domain.commonCode.service.MajorReader;
import com.ggb.graduationgoodbye.domain.commonCode.service.UniversityReader;
import com.ggb.graduationgoodbye.domain.member.controller.MemberJoinRequest;
import com.ggb.graduationgoodbye.domain.member.controller.PromoteArtistDto;
import com.ggb.graduationgoodbye.domain.member.dto.OAuth2MemberInfo;
import com.ggb.graduationgoodbye.domain.member.entity.Member;
import com.ggb.graduationgoodbye.domain.member.enums.SnsType;
import com.ggb.graduationgoodbye.domain.member.exception.NotFoundMemberException;
import com.ggb.graduationgoodbye.domain.member.repository.MemberRepository;
import com.ggb.graduationgoodbye.domain.s3.utils.S3Util;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 멤버 Service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

  private final MemberRepository memberRepository;
  private final ArtistRepository artistRepository;
  private final TokenService tokenService;
  private final MemberInfoProvider memberInfoProvider;
  private final UniversityReader universityReader;
  private final MajorReader majorReader;
  private final S3Util s3Util;

  /**
   * 회원 가입.
   */
  public TokenDto join(MemberJoinRequest request) {

    OAuth2MemberInfo memberInfo = memberInfoProvider.getInfo(request.snsType(),
        request.accessToken());

    log.info("OAuth2 Server Response >> {}", memberInfo);

    Member member = Member.builder()
        .snsType(SnsType.valueOf(request.snsType().toUpperCase()))
        .snsId(memberInfo.getSnsId())
        .email(memberInfo.getEmail())
        .profile(memberInfo.getProfile())
        .nickname(request.nickname())
        .address(request.address())
        .gender(request.gender())
        .age(request.age())
        .phone(request.phone())
        .build();

    memberRepository.save(member);

    Authentication authentication = tokenService.getAuthenticationByMember(member);

    return tokenService.getToken(authentication);
  }

  /**
   * 작가 회원 전환 요청.
   */
  public Artist promoteArtist(PromoteArtistDto.Request request,
      MultipartFile certificate) {

    /* Authentication 관련 코드 추후 작성 위치 변경 필요 */
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    Long id = Long.parseLong(user.getUsername());
    Member member = findById(id).orElseThrow(NotFoundMemberException::new);

    CommonCode university = universityReader.findUniversity(request.getUniversity())
        .orElseThrow(NotFoundUniversityException::new);
    CommonCode major = majorReader.findByMajor(request.getMajor())
        .orElseThrow(NotFoundMajorException::new);
    String certificateUrl = s3Util.upload(certificate);
    String createId = String.valueOf(member.getId());

    Artist artist = Artist.builder()
        .memberId(member)
        .universityId(university)
        .majorId(major)
        .name(request.getName())
        .certificateUrl(certificateUrl)
        .createdId(createId)
        .createdAt(LocalDateTime.now())
        .updatedId(createId)
        .build();

    return artistRepository.save(artist);
  }

  public Optional<Member> findByEmail(String email) {
    return memberRepository.findByEmail(email);
  }

  public Optional<Member> findById(Long id) {
    return memberRepository.findById(id);
  }

  public boolean existsByEmail(String email) {
    return memberRepository.findByEmail(email).isPresent();
  }
}
