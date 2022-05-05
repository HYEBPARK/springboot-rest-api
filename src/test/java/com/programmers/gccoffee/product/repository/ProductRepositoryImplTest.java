package com.programmers.gccoffee.product.repository;

import static com.wix.mysql.EmbeddedMysql.*;
import static com.wix.mysql.config.MysqldConfig.*;
import static com.wix.mysql.distribution.Version.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import com.programmers.gccoffee.product.model.Category;
import com.programmers.gccoffee.product.model.Product;
import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.ScriptResolver;
import com.wix.mysql.config.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

@SpringBootTest
class ProductRepositoryImplTest {

    static EmbeddedMysql embeddedMysql;
    @Autowired
    private ProductRepository productRepository;
    private final static Product PRODUCT = new Product(UUID.randomUUID(), "커피",
        Category.COFFEE_BEAN_PACKAGE, 3300L, "커피에용");

    @BeforeAll
    static void setup() {
        var config = aMysqldConfig(v5_7_latest)
            .withCharset(Charset.UTF8)
            .withPort(2215)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();
        embeddedMysql = anEmbeddedMysql(config)
            .addSchema("test-product_mgmt", ScriptResolver.classPathScript("schema.sql"))
            .start();
    }

    @AfterAll
    static void cleanup() {
        embeddedMysql.stop();
    }

    @BeforeEach
    void setting() {
        productRepository.deleteAll();
        productRepository.insert(PRODUCT);
    }

    @Test
    @DisplayName("insert 실행시 중복 키 에외가 발생되어야함")
    void DuplicateInsert() {
        //given
        //when
        //then
        assertThatThrownBy(() -> productRepository.insert(PRODUCT)).isInstanceOf(
            DuplicateKeyException.class);
    }

    @Test
    @DisplayName("update() 실행시 product의 price가 수정되어야함")
    void update() {
        //given
        var price = 300L;
        var product = PRODUCT.updateProduct(PRODUCT.getProductName(), PRODUCT.getCategory(),
            price, PRODUCT.getDescription());
        //when
        var updateProduct = productRepository.update(product);
        //then
        assertThat(updateProduct.getPrice()).isEqualTo(300L);

    }

    @Test
    @DisplayName("findAll 실행시 product List가 return 되어야함")
    void findAll() {
        //given
        List<Product> products = new ArrayList<>();
        products.add(PRODUCT);
        //when
        var findAll = productRepository.findAll();
        //then
        assertThat(products).isEqualTo(findAll);
    }

    @Test
    @DisplayName("findById 실행시 해당 product가 return 되어야함")
    void findById() {
        //given
        var id = PRODUCT.getProductId();
        //when
        var product = productRepository.findById(id);
        //then
        assertThat(product).isNotEmpty();
    }

    @Test
    @DisplayName("findById 실행시 없는 Id를 넣을 경우 Optional.empty return")
    void findByIdFail() {
        //given
        var id = PRODUCT.getProductId();
        //when
        var product = productRepository.findById(id);
        //then
        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("deleteById 실행시 해당 Id product가 삭제되어야함")
    void deleteById() {
        //given
        var id = PRODUCT.getProductId();
        //when
        var isDelete = productRepository.deleteById(id);
        //then
        assertThat(isDelete).isTrue();
    }
}