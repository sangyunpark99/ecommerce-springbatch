package org.example.batch.domain.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.batch.dto.ProductUploadCsvRow;
import org.example.batch.util.DateTimeUtil;
import org.example.batch.util.RandomUtils;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 정적 생성자만 사용하도록 세팅
public class Product {

  private String productId;
  private Long sellerId;

  private String category;
  private String productName;
  private LocalDate salesStartDate;
  private LocalDate salesEndDate;
  private String productStatus;
  private String brand;
  private String manufacturer;

  private int salesPrice;
  private int stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static Product from(ProductUploadCsvRow row) {
    LocalDateTime now = LocalDateTime.now();
    return new Product(
        RandomUtils.generateRandomId(),
        row.getSellerId(),
        row.getCategory(),
        row.getProductName(),
        DateTimeUtil.toLocalDate(row.getSalesStartDate()),
        DateTimeUtil.toLocalDate(row.getSalesEndDate()),
        row.getProductStatus(),
        row.getBrand(),
        row.getManufacturer(),
        row.getSalesPrice(),
        row.getStockQuantity(),
        now,
        now
    );
  }
}
