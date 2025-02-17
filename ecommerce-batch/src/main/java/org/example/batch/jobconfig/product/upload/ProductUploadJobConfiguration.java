package org.example.batch.jobconfig.product.upload;

import javax.sql.DataSource;
import org.example.batch.domain.product.Product;
import org.example.batch.dto.ProductUploadCsvRow;
import org.example.batch.service.monitoring.BatchStepExecutionListener;
import org.example.batch.util.ReflectionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductUploadJobConfiguration {

  @Bean
  public Job productUploadJob(JobRepository jobRepository,
      Step productUploadStep,
      JobExecutionListener listener) {
    return new JobBuilder("productUploadJob", jobRepository)
        .listener(listener)
        .start(productUploadStep)
        .build();
  }

  @Bean
  public Step productUploadStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      BatchStepExecutionListener batchStepExecutionListener,
      ItemReader<ProductUploadCsvRow> productReader,
      ItemProcessor<ProductUploadCsvRow, Product> productProcessor,
      ItemWriter<Product> productWriter
  ) {
    return new StepBuilder("productUploadStep", jobRepository)
        .<ProductUploadCsvRow, Product>chunk(1000, transactionManager)
        .reader(productReader)
        .processor(productProcessor)
        .writer(productWriter)
        .allowStartIfComplete(true) // 개발 단계에서는 true를 넣어서 돌립니다.
        .listener(batchStepExecutionListener)
        .build();
  }

  // csv파일 읽기
  @Bean
  @StepScope
  public FlatFileItemReader<ProductUploadCsvRow> productReader(
      @Value("#{jobParameters['inputFilePath']}") String path
  ) {
    return new FlatFileItemReaderBuilder<ProductUploadCsvRow>()
        .name("productReader")
        .resource(new FileSystemResource(path))
        .delimited()
        .names(ReflectionUtils.getFieldNames(ProductUploadCsvRow.class).toArray(String[]::new))
        .targetType(ProductUploadCsvRow.class)
        .linesToSkip(1)
        .build();
  }

  // productUploadCsvRow -> Product로 매핑
  @Bean
  public ItemProcessor<ProductUploadCsvRow, Product> productProcessor() {
    return row -> Product.from(row);
  }

  @Bean
  public JdbcBatchItemWriter<Product> productWriter(DataSource dataSource) {

    String sql = "insert into products(product_id, seller_id, category, product_name, "
        + "sales_start_date, sales_end_date, product_status, "
        + "brand, manufacturer, sales_price, stock_quantity, created_at, updated_at)"
        + " values(:productId, :sellerId, :category, :productName, :salesStartDate, "
        + ":salesEndDate, :productStatus, :brand, :manufacturer, :salesPrice, "
        + ":stockQuantity, :createdAt, :updatedAt)";

    return new JdbcBatchItemWriterBuilder<Product>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();
  }
}
