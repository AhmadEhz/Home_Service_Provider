package org.homeservice;

import org.homeservice.entity.*;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.homeservice.util.exception.NotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestComponent
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {
    Services services;
    private final DataSource dataSource;

    @Autowired
    public CustomerTest(Services services, DataSource dataSource) {
        this.services = services;
        this.dataSource = dataSource;
    }

    static Customer customer1 = new Customer("customer1FName", "customer1LName",
            "customer1UName", "customer1Pass", "customer1@Mail.com");
    static Customer customer2 = new Customer("customer2FName", "customer2LName",
            "customer2UName", "customer2Pass", "customer2@Mail.com");
    static Customer customer3 = new Customer("customer1FName", "customer1LName",
            "customer3UName", "customer1Pass", "customer3@Mail.com");
    static org.homeservice.entity.Order order1 = new org.homeservice.entity.Order
            (200D, "order1Description", LocalDateTime.of(2023, 12, 25, 12, 30),
                    "Tehran");
    static org.homeservice.entity.Order order2 = new org.homeservice.entity.Order(
            125D, "order2description"
            , LocalDateTime.of(2023, 12, 29, 18, 0), "order2address");

    @Test
    @Order(1)
    void save() {
        services.customerService.save(customer1);
        services.customerService.save(customer2);
        services.customerService.save(customer3);
        assertEquals(customer1, services.loadCustomer(customer1.getId()));
        assertEquals(customer2, services.loadCustomer(customer2.getId()));
        assertEquals(customer3, services.loadCustomer(customer3.getId()));
    }

    @Test
    @Order(2)
    void changePassword() {
        String newPassword = "customer1newPass";
        assertEquals(customer1.getPassword(), services.loadCustomer(customer1.getId()).getPassword());
        services.customerService.changePassword(customer1, customer1.getPassword(), newPassword);
        customer1 = services.loadCustomer(customer1.getId());
        assertEquals(newPassword, customer1.getPassword());
    }

    @Test
    @Order(3)
    void checkPassword() {
        String incorrectPassword = customer1.getPassword() + "in";
        assertThrows(CustomIllegalArgumentException.class, () -> services.customerService
                .changePassword(customer1, incorrectPassword, "newPassword"));
    }

    //Following tests fails if run this class. run MainTest.

    void addOrder() {
        services.orderService.save(order1, CustomerTest.customer1, AdminTest.subService1.getId());
        assertEquals(order1, services.loadOrder(order1.getId()));
        assertEquals(OrderStatus.WAITING_FOR_BID, services.loadOrder(order1.getId()).getStatus());
    }


    void addOrderWithLowerOfferPrice() {
        int orders = services.orderService.findAll().size();
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.orderService.save(order2, customer2, AdminTest.subService2.getId()));
        assertEquals(orders, services.orderService.findAll().size());
    }

    void addOrderWithNotFoundCustomerAndSubService() {
        Customer customer = new Customer();
        customer.setId(Long.MAX_VALUE);
        assertThrows(NotFoundException.class, () ->
                services.orderService.save(order2, customer, AdminTest.subService2.getId()));
        assertThrows(NotFoundException.class, () ->
                services.orderService.save(order2, customer2, Long.MAX_VALUE));
    }

    void showSubServiceForAService() {
        List<SubService> subServices = services.subServiceService.loadAllByService(AdminTest.service2.getId());
        Set<SubService> getSubServicesFromService = services.loadService(AdminTest.service2.getId()).getSubServices();
        assertTrue(subServices.containsAll(getSubServicesFromService));
        assertTrue(getSubServicesFromService.containsAll(subServices));
    }

    void selectBidForOrder() {
        assertEquals(OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST, services.loadOrder(order1.getId()).getStatus());
        services.orderService.selectBid(SpecialistTest.bid1.getId(), SpecialistTest.bid1.getOrder().getCustomer());
        assertTrue(services.bidService.loadByCustomerAndSpecialist
                (customer1.getId(), SpecialistTest.specialist1.getId()).isPresent());
        assertEquals(OrderStatus.WAITING_FOR_COMING_SPECIALIST, services.loadOrder(order1.getId()).getStatus());
    }

    void showAllBidsOrderByPriceAndSpecialistScore() {
        long orderId = 0;
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("prepare-show-all-bids.sql"));
            String query = "select last_value from orders_id_seq";
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (!rs.next())
                throw new RuntimeException("Cannot get order_id");
            orderId = rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<Bid> bids = services.bidService.loadAllByOrder(orderId, null);
        List<Bid> bidsOrderByPrice = services.bidService.loadAllByOrder(orderId, "price");
        List<Bid> bidsOrderBySpecialistScore = services.bidService.loadAllByOrder(orderId, "specialist");
        assertEquals(bidsOrderByPrice, bids.stream().sorted(Comparator.comparing(Bid::getOfferPrice)).toList());
        assertEquals(bidsOrderBySpecialistScore, bids.stream().sorted((bid1, bid2) ->
                Double.compare(bid2.getSpecialist().getScore(), bid1.getSpecialist().getScore())).toList());
    }

    void startOrder() {
        //Order time is after now.
        assertThrows(CustomIllegalArgumentException.class, () -> services.orderService.changeStatusToStarted
                (order1.getId(), customer1));

        order1 = services.loadOrder(order1.getId());
        Bid bid = services.bidService.loadByOrderId(order1.getId()).get();
        bid.setStartWorking(LocalDateTime.now().minusMinutes(5));
        services.bidService.update(bid);
        assertEquals(OrderStatus.WAITING_FOR_COMING_SPECIALIST, services.loadOrder(order1.getId()).getStatus());
        services.orderService.changeStatusToStarted(order1.getId(), customer1);
        assertEquals(OrderStatus.STARTED, services.loadOrder(order1.getId()).getStatus());
    }
}
