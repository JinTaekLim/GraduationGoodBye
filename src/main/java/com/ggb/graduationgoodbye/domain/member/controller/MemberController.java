package com.ggb.graduationgoodbye.domain.member.controller;

import com.ggb.graduationgoodbye.domain.auth.common.dto.TokenDto;
import com.ggb.graduationgoodbye.domain.auth.service.TokenService;
import com.ggb.graduationgoodbye.domain.artist.entity.Artist;
import com.ggb.graduationgoodbye.domain.member.entity.Member;
import com.ggb.graduationgoodbye.domain.member.exception.NotFoundMemberException;
import com.ggb.graduationgoodbye.domain.member.service.MemberService;
import com.ggb.graduationgoodbye.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 멤버 Controller.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

  private final MemberService memberService;
  private final TokenService tokenService;

  /**
   * 회원 가입/로그인.
   */
  @PostMapping("/signup")
  public ApiResponse<TokenResponse> signup(@RequestBody MemberJoinRequest memberJoinRequest) {
    TokenDto token = memberService.join(memberJoinRequest);
    TokenResponse tokenResponse = new TokenResponse(token.getAccessToken(),
        token.getRefreshToken());
    return ApiResponse.ok(tokenResponse);
  }

  /**
   * Member 정보 반환.
   */
  @GetMapping("/info")
  public ApiResponse<Member> info() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User u = (User) authentication.getPrincipal();
    Long id = Long.valueOf(u.getUsername());
    Member member = memberService.findById(id);
    return ApiResponse.ok(member);
  }

  /**
   * Token 재발급.
   */
  @PostMapping("/reissue")
  public ApiResponse<TokenResponse> reissue(@RequestBody TokenReissueRequest tokenReissueRequest) {
    TokenDto token = tokenService.reissueToken(tokenReissueRequest);
    TokenResponse tokenResponse = new TokenResponse(token.getAccessToken(),
        token.getRefreshToken());
    return ApiResponse.ok(tokenResponse);
  }


  /**
   * 작가 등업 신청.
   */
  @PostMapping("promote-artist")
  @PreAuthorize("hasAuthority('MEMBER')")
  public ApiResponse<PromoteArtistDto.Response> promoteArtist(
      @RequestPart("request") PromoteArtistDto.Request request,
      @RequestPart("certificate") MultipartFile certificate) {

    Artist artist = memberService.promoteArtist(request, certificate);

    PromoteArtistDto.Response response = new PromoteArtistDto.Response(artist);

    return ApiResponse.ok(response);
  }
}
