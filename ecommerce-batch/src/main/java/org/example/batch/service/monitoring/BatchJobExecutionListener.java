package org.example.batch.service.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchJobExecutionListener implements JobExecutionListener { // Job 실행 시작, 종료 부분만 가져옵니다.

  @Override
  public void beforeJob(JobExecution jobExecution) { // Job 시작 전
    log.info("listener: before job");
  }

  @Override
  public void afterJob(JobExecution jobExecution) { // Job 시작 후
    log.info("listener: after job {}", jobExecution.getExecutionContext());
  }
}
