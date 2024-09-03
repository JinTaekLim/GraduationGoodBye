package com.ggb.graduationgoodbye.domain.member.common.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OAuth2InfoDto {

  private String snsId;
  private String email;
  private String profile;

  public static OAuth2InfoDto ofGoogle(Map<String, Object> attributes) {
    return OAuth2InfoDto.builder()
        .snsId(attributes.get("sub").toString())
        .email(attributes.get("email").toString())
        .profile(attributes.get("picture").toString())
        .build();
  }
}