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

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestComponent
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerTest {
    Services services;

    @Autowired
    public CustomerTest(Services services) {
        this.services = services;
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
        services.customerService.changePassword(customer1.getUsername(), customer1.getPassword(), newPassword);
        customer1 = services.loadCustomer(customer1.getId());
        assertEquals(newPassword, customer1.getPassword());
    }

    @Test
    @Order(3)
    void checkPassword() {
        String incorrectPassword = customer1.getPassword() + "in";
        assertThrows(CustomIllegalArgumentException.class, () -> services.customerService
                .changePassword(customer1.getUsername(), incorrectPassword, "newPassword"));
    }

    //Following tests fails if run this class. run MainTest.

    void addOrder() {
        services.orderService.save(order1, CustomerTest.customer1.getId(), AdminTest.subService1.getId());
        assertEquals(order1, services.loadOrder(order1.getId()));
        assertEquals(OrderStatus.WAITING_FOR_BID, services.loadOrder(order1.getId()).getStatus());
        Set<org.homeservice.entity.Order> os = new HashSet<>(Arrays.asList(order1));
    }


    void addOrderWithLowerOfferPrice() {
        int orders = services.orderService.findAll().size();
        assertThrows(CustomIllegalArgumentException.class, () ->
                services.orderService.save(order2, customer2.getId(), AdminTest.subService2.getId()));
        assertEquals(orders, services.orderService.findAll().size());
    }

    void addOrderWithNotFoundCustomerAndSubService() {
        assertThrows(NotFoundException.class, () ->
                services.orderService.save(order2, Long.MAX_VALUE, AdminTest.subService2.getId()));
        assertThrows(NotFoundException.class, () ->
                services.orderService.save(order2, customer2.getId(), Long.MAX_VALUE));
    }

    void showSubServiceForAService() {
        List<SubService> subServices = services.subServiceService.loadAllByService(AdminTest.service2.getId());
        Set<SubService> getSubServicesFromService = services.loadService(AdminTest.service2.getId()).getSubServices();
        assertTrue(subServices.containsAll(getSubServicesFromService));
        assertTrue(getSubServicesFromService.containsAll(subServices));
    }

    void selectBidForOrder() {
        assertEquals(OrderStatus.WAITING_FOR_CHOOSE_SPECIALIST, services.loadOrder(order1.getId()).getStatus());
        services.orderService.selectBidForOffer(SpecialistTest.bid1.getId(), SpecialistTest.bid1.getId());
        assertTrue(services.bidService.loadByCustomerAndSpecialist
                (customer1.getId(), SpecialistTest.specialist1.getId()).isPresent());
        assertEquals(OrderStatus.WAITING_FOR_COMING_SPECIALIST, services.loadOrder(order1.getId()).getStatus());
    }

    void showAllBidsOrderByPriceAndSpecialistScore() {
        //Initial specialist
        Specialist tempSpecialist1 = new Specialist("temp1", "temp1", "temp1",
                "temp1pass", "temp1@mail.com");
        Specialist tempSpecialist2 = new Specialist("temp2", "temp2", "temp2",
                "temp2pass", "temp2@mail.com");
        Specialist tempSpecialist3 = new Specialist("temp3", "temp3", "temp3",
                "temp3pass", "temp3@mail.com");
        Specialist tempSpecialist4 = new Specialist("temp4", "temp4", "temp4",
                "temp4pass", "temp4@mail.com");
        Specialist tempSpecialist5 = new Specialist("temp4", "temp4", "temp5",
                "temp4pass", "temp5@mail.com");
        // Initial Bids
        LocalDateTime exampleStartTime = LocalDateTime.of(2023, 2, 1, 12, 0);
        LocalDateTime exampleEndTime = exampleStartTime.plusDays(1);

        Bid tempBid1 = new Bid(500D, exampleStartTime, exampleEndTime);
        Bid tempBid2 = new Bid(300D, exampleStartTime, exampleEndTime);
        Bid tempBid3 = new Bid(320D, exampleStartTime, exampleEndTime);
        Bid tempBid4 = new Bid(275D, exampleStartTime, exampleEndTime);
        Bid tempBid5 = new Bid(260D, exampleStartTime, exampleEndTime);


        //Initial Order
        org.homeservice.entity.Order tempOrder
                = new org.homeservice.entity.Order(600D, "tempDescription",
                exampleStartTime, "tempAddress");

        //Saving specialist
        services.specialistService.save(tempSpecialist1);
        services.specialistService.save(tempSpecialist2);
        services.specialistService.save(tempSpecialist3);
        services.specialistService.save(tempSpecialist4);
        services.specialistService.save(tempSpecialist5);

        //Update specialist score
        tempSpecialist1.setScore(5D);
        tempSpecialist2.setScore(2D);
        tempSpecialist3.setScore(1D);
        tempSpecialist4.setScore(3D);
        tempSpecialist5.setScore(3.5D);
        services.specialistService.update(tempSpecialist1);
        services.specialistService.update(tempSpecialist2);
        services.specialistService.update(tempSpecialist3);
        services.specialistService.update(tempSpecialist4);
        services.specialistService.update(tempSpecialist5);

        //Verify specialist
        services.specialistService.verifySpecialist(tempSpecialist1.getId());
        services.specialistService.verifySpecialist(tempSpecialist2.getId());
        services.specialistService.verifySpecialist(tempSpecialist3.getId());
        services.specialistService.verifySpecialist(tempSpecialist4.getId());
        services.specialistService.verifySpecialist(tempSpecialist5.getId());

        //Save order
        services.orderService.save(tempOrder, customer1.getId(), AdminTest.subService1.getId());

        //Save bid
        services.bidService.save(tempBid1, tempOrder.getId(), tempSpecialist1.getId());
        services.bidService.save(tempBid2, tempOrder.getId(), tempSpecialist2.getId());
        services.bidService.save(tempBid3, tempOrder.getId(), tempSpecialist3.getId());
        services.bidService.save(tempBid4, tempOrder.getId(), tempSpecialist4.getId());
        services.bidService.save(tempBid5, tempOrder.getId(), tempSpecialist5.getId());

        //Refresh bid
        tempBid1 = services.loadBid(tempBid1.getId());
        tempBid2 = services.loadBid(tempBid2.getId());
        tempBid3 = services.loadBid(tempBid3.getId());
        tempBid4 = services.loadBid(tempBid4.getId());
        tempBid5 = services.loadBid(tempBid5.getId());

        List<Bid> bids = List.of(tempBid1, tempBid2, tempBid3, tempBid4, tempBid5);

        //Load sorted bids
        List<Bid> bidsOrderByPrice = services.bidService.loadAllByOrderSortedByPrice(tempOrder.getId());
        List<Bid> bidsOrderBySpecialistScore = services.bidService.loadAllByOrderSortedBySpecialistScore(tempOrder.getId());

        assertEquals(bidsOrderByPrice, bids.stream().sorted(Comparator.comparing(Bid::getOfferPrice)).toList());
        assertEquals(bidsOrderBySpecialistScore, bids.stream().sorted((bid1, bid2) ->
                Double.compare(bid2.getSpecialist().getScore(), bid1.getSpecialist().getScore())).toList());
    }
    void startOrder() {
        //Order time is after now.
        assertThrows(CustomIllegalArgumentException.class,() ->services.orderService.changeStatusToStarted(order1.getId(), customer1.getId()));

        order1 = services.loadOrder(order1.getId());
        Bid bid = services.bidService.loadByOrderId(order1.getId()).get();
        bid.setStartWorking(LocalDateTime.now().minusMinutes(5));
        services.bidService.update(bid);
        assertEquals(OrderStatus.WAITING_FOR_COMING_SPECIALIST,services.loadOrder(order1.getId()).getStatus());
        services.orderService.changeStatusToStarted(order1.getId(), customer1.getId());
        assertEquals(OrderStatus.STARTED,services.loadOrder(order1.getId()).getStatus());
    }
}
