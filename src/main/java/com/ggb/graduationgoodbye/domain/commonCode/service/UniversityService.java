package com.ggb.graduationgoodbye.domain.commonCode.service;

import com.ggb.graduationgoodbye.domain.commonCode.business.UniversityReader;
import com.ggb.graduationgoodbye.domain.commonCode.common.entity.CommonCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 대학 Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UniversityService {

  private final UniversityReader universityReader;

  /**
   * 대학 전체 조회.
   */
  public List<CommonCode> findUniversityAll() {
    return universityReader.findUniversityAll();
  }

  /**
   * 대학 단일 조회.
   */
  public CommonCode findByUniversity(String name) {
    return universityReader.findUniversity(name);
  }


}