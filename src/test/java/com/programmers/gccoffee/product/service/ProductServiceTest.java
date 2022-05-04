package com.programmers.gccoffee.product.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import com.programmers.gccoffee.product.model.Category;
import com.programmers.gccoffee.product.model.Product;
import com.programmers.gccoffee.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);
    private final UUID id = UUID.randomUUID();
    private final String name = "커피";
    private final Category category = Category.COFFEE_BEAN_PACK;
    private final long price = 2200L;
    private final String description = "커피에용";
    private final LocalDateTime createdAt = LocalDateTime.now();
    private final LocalDateTime updatedAt = null;
    private final Product PRODUCT = new Product(id, name, category, price, description,createdAt,updatedAt);

    @Test
    @DisplayName("create함수 실행시 price가 0이하일 경우 IllegalArgumentException가 발생")
    void createFail() {
        assertThatThrownBy(
            () -> productService.create(PRODUCT.getProductId(), PRODUCT.getProductName(),
                PRODUCT.getCategory(),
                0, PRODUCT.getDescription(), PRODUCT.getCreatedAt(),
                PRODUCT.getUpdatedAt())).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("getProducts 실행시 products List가 return 되어야함")
    void getProducts() {
        //given
        List<Product> products = List.of(PRODUCT);
        when(productRepository.findAll()).thenReturn(products);
        //when
        var productList = productService.getProducts();
        //then
        assertThat(productList, equalTo(products));
    }

    @Test
    @DisplayName("findById 실행시 해당 ID의 product가 return됨")
    void findById() {
        //given
        when(productRepository.findById(PRODUCT.getProductId())).thenReturn(Optional.of(PRODUCT));
        //when
        var product = productService.findById(PRODUCT.getProductId()).get();
        //then
        assertThat(product, samePropertyValuesAs(PRODUCT));
    }

    @Test
    @DisplayName("findById에 없는 ID 입력 시 Optional.emapty가 return")
    void findByIdFail() {
        //given
        var id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        //when
        var product = productService.findById(id);
        //then
        assertThat(product, is(Optional.empty()));
    }
}
