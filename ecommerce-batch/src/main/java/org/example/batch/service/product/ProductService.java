package org.example.batch.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final JdbcTemplate jdbcTemplate;

  public Long countProduct() {
    return jdbcTemplate.queryForObject("select count(*) from products", Long.class);
  }
}
