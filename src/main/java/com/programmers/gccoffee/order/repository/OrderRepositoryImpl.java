package com.programmers.gccoffee.order.repository;

import com.programmers.gccoffee.order.model.Order;
import com.programmers.gccoffee.order.model.OrderItem;
import com.programmers.gccoffee.order.model.OrderStatus;
import com.programmers.gccoffee.product.model.Category;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private static final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public OrderRepositoryImpl(
        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        String insertOrderSql =
            "INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) VALUES (UNHEX(REPLACE(:orderId,'-','')), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)";
        String insertOrderItemSql =
            "INSERT INTO order_item(order_id, product_id, category, price, quantity, created_at, updated_at) VALUES (UNHEX(REPLACE(:orderId, '-', '')),  UNHEX(REPLACE(:productId, '-', '')), :category, :price, :quantity, :createdAt, :updatedAt)";

        var orderUpdate = namedParameterJdbcTemplate.update(insertOrderSql, toOrderParamMap(order));

        if (orderUpdate == 0) {
            throw new RuntimeException();
        }

        for (OrderItem item : order.getOrderItems()) {
            var update = namedParameterJdbcTemplate.update(insertOrderItemSql,
                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(),
                    item));
            if (update == 0) {
                log.error("orderItem insert error -> {}", item.toString());
                throw new RuntimeException();
            }
        }

        return order;
    }

    @Override
    public List<Order> findAll() {
        String selectOrdersAllSql = "SELECT * FROM orders";
        String selectOrderItemById = "SELECT * FROM order_item WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))";
        var orders = namedParameterJdbcTemplate.query(selectOrdersAllSql, orderRowMapper);

        if (orders.isEmpty()) {
            return orders;
        }

        for (Order order : orders) {
            var items = namedParameterJdbcTemplate.query(selectOrderItemById,
                Collections.singletonMap("orderId", order.getOrderId().toString()),
                orderItemRowMapper);
        }

        return orders;
    }

    @Override
    public boolean deleteById(UUID orderId) {
        String deleteOrderByIdSql = "DELETE FROM orders WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))";
        String deleteOrderItemById = "DELETE FROM order_item WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))";
        try {
            namedParameterJdbcTemplate.update(deleteOrderItemById,
                Collections.singletonMap("orderId", orderId.toString()));
            namedParameterJdbcTemplate.update(deleteOrderByIdSql,
                Collections.singletonMap("orderId", orderId.toString()));

            return true;
        } catch (InvalidDataAccessApiUsageException e) {
            log.error("order deleteById error -> {}", e);

            return false;
        }
    }

    public List<OrderItem> findOrderItem(UUID orderId) {
        String selectOrderItem = "SELECT * FROM order_item WHERE order_id = UNHEX(REPLACE(:orderId,'-',''))";
        var orderItems = namedParameterJdbcTemplate.query(selectOrderItem,
            Collections.singletonMap("orderId", orderId.toString()), orderItemRowMapper);

        return orderItems;
    }

    private RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        var orderId = toUUID(rs.getBytes("order_id"));
        var email = rs.getString("email");
        var address = rs.getString("address");
        var postcode = rs.getString("postcode");
        var orderStatus = OrderStatus.valueOf(rs.getString("order_status"));
        var createdAt = toLocalDateTime(rs.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(rs.getTimestamp("updated_at"));
        var orderItem = findOrderItem(orderId);
        return new Order(orderId, email, address, postcode, orderItem, orderStatus, createdAt,
            updatedAt);
    };

    private RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> {
        var productId = toUUID(rs.getBytes("product_id"));
        var orderId = toUUID(rs.getBytes("order_id"));
        var category = Category.valueOf(rs.getString("category"));
        var price = rs.getLong("price");
        var quantity = rs.getInt("quantity");

        return new OrderItem(orderId, productId, category, price, quantity);
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var orderMap = new HashMap<String, Object>();
        orderMap.put("orderId", order.getOrderId().toString().getBytes());
        orderMap.put("email", order.getEmail());
        orderMap.put("address", order.getAddress());
        orderMap.put("postcode", order.getPostcode());
        orderMap.put("orderStatus", order.getOrderStatus().toString());
        orderMap.put("createdAt", order.getCreatedAt());
        orderMap.put("updatedAt", order.getUpdatedAt());

        return orderMap;
    }


    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt,
        LocalDateTime updatedAt, OrderItem item) {
        var orderItemMap = new HashMap<String, Object>();
        orderItemMap.put("orderId", orderId.toString().getBytes());
        orderItemMap.put("productId", item.productId().toString().getBytes());
        orderItemMap.put("category", item.category().toString());
        orderItemMap.put("price", item.price());
        orderItemMap.put("quantity", item.quantity());
        orderItemMap.put("createdAt", createdAt);
        orderItemMap.put("updatedAt", updatedAt);
        return orderItemMap;
    }


    private static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    private static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
