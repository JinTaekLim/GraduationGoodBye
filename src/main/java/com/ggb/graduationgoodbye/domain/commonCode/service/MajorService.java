package com.ggb.graduationgoodbye.domain.commonCode.service;


import com.ggb.graduationgoodbye.domain.commonCode.entity.CommonCode;
import com.ggb.graduationgoodbye.domain.commonCode.exception.NotFoundMajorException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 학과 Service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MajorService {

  private MajorReader majorReader;

  public List<CommonCode> findMajorAll() {
    return majorReader.findMajorAll();
  }

  public CommonCode findByMajor(String name) {
    return majorReader.findByMajor(name)
        .orElseThrow(NotFoundMajorException::new);
  }
}
