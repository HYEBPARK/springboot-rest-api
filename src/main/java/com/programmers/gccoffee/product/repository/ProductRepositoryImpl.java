package com.programmers.gccoffee.product.repository;

import com.programmers.gccoffee.product.model.Category;
import com.programmers.gccoffee.product.model.Product;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product insert(Product product) {
        String insertSql = "INSERT INTO product(product_id, product_name, category, price, description, created_at, updated_at) VALUES (UNHEX(REPLACE(:productId, '-', '')),:productName, :category, :price, :description, :createdAt, :updatedAt)";

        try {
            var update = namedParameterJdbcTemplate.update(insertSql, toParamMap(product));
        } catch (DataIntegrityViolationException e) {
            log.error("duplication key error");
            throw new RuntimeException();
        }

        return product;
    }

    @Override
    public Product update(Product product) {
        String updateSql = "UPDATE product SET product_name =:productName, category =:category, price = :price, description = :description, updated_at=:updatedAt WHERE product_id=(UNHEX(REPLACE(:productId, '-', '')))";

        var update = namedParameterJdbcTemplate.update(updateSql, toParamMap(product));

        if (update == 0) {
            log.error("product update error");
            throw new RuntimeException();
        }

        return product;
    }

    @Override
    public List<Product> findAll() {
        String selectSql = "SELECT * FROM product";

        return namedParameterJdbcTemplate.query(selectSql, productRowMapper);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        String selectByIdSql = "SELECT * FROM product WHERE product_id = UNHEX(REPLACE(:productId, '-', ''))";
        try {
            var product = namedParameterJdbcTemplate.queryForObject(selectByIdSql,
                Collections.singletonMap("productId", id.toString().getBytes()), productRowMapper);

            return Optional.of(product);
        } catch (IncorrectResultSizeDataAccessException e) {
            log.error("product findById error -> {}", e);

            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        String deleteSql = "DELETE FROM product WHERE product_id = UNHEX(REPLACE(:productId,'-',''))";
        try {
            namedParameterJdbcTemplate.update(deleteSql,
                Collections.singletonMap("productId", id.toString().getBytes()));

            return true;
        } catch (InvalidDataAccessApiUsageException e) {
            log.error("product deleteById error -> {}", e);

            return false;
        }
    }

    @Override
    public void deleteAll() {
        String deleteAllSql = "DELETE FROM product";
        namedParameterJdbcTemplate.update(deleteAllSql, Collections.emptyMap());
    }

    private static Map<String, Object> toParamMap(Product product) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("productId", product.getProductId().toString().getBytes());
        paramMap.put("productName", product.getProductName());
        paramMap.put("category", product.getCategory().toString());
        paramMap.put("price", product.getPrice());
        paramMap.put("description", product.getDescription());
        paramMap.put("createdAt", product.getCreatedAt());
        paramMap.put("updatedAt", product.getUpdatedAt());

        return paramMap;
    }

    private static final RowMapper<Product> productRowMapper = (resultSet, i) -> new Product(
        toUUID(resultSet.getBytes("product_id")),
        resultSet.getString("product_name"),
        Category.valueOf(resultSet.getString("category")),
        resultSet.getLong("price"),
        resultSet.getString("description"),
        toLocalDateTime(resultSet.getTimestamp("created_at")),
        toLocalDateTime(resultSet.getTimestamp("updated_at"))
    );

    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    public static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);

        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }
}
