package org.example.batch.domain.product;

public enum ProductStatus {

  AVALIABLE("판매 중"),
  OUT_OF_STOCK("품절"),
  DISCONTINUED("판매 종료");

  final String desc;

  ProductStatus(String desc) {
    this.desc = desc;
  }
}
