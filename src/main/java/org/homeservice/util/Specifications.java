package org.homeservice.util;

import jakarta.persistence.criteria.*;
import org.homeservice.entity.*;
import org.homeservice.entity.Order;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Scope("singleton")
public class Specifications {

    public Specification<Order> getOrder(Map<String, String> filters) {
        if (filters == null || filters.isEmpty())
            return null;
        Specification<Order> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<Order> spec = (root, cq, cb) -> switch (entry.getKey().toLowerCase()) {
                case "customerid" -> cb.equal(root.join(Order_.customer).get(Customer_.id), toLong(entry.getValue()));
                case "status" -> root.get(Order_.status).in(toOrderStatus(entry.getValue()));
                case "order" -> switch (entry.getValue().toLowerCase()) {
                    case "time", "time.asc" -> {
                        cq.orderBy(cb.asc(root.get(Order_.createdAt)));
                        yield cb.and();
                    }
                    case "time.desc" -> {
                        cq.orderBy(cb.desc(root.get(Order_.createdAt)));
                        yield cb.and();
                    }
                    default -> throw new CustomIllegalArgumentException("Filter is not correct.");
                };
                default -> throw new CustomIllegalArgumentException("filter is not correct.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    public Specification<Order> getOrderByAdmin(Map<String, String> filters) {
        if (filters == null || filters.isEmpty())
            return Specification.where((root, cq, cb) -> cb.and());
        Specification<Order> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<Order> spec = (root, cq, cb) -> switch (entry.getKey().toLowerCase()) {
                case "from" -> cb.greaterThanOrEqualTo(root.get(Order_.createdAt), toDate(entry.getValue()));
                case "to" -> cb.lessThanOrEqualTo(root.get(Order_.createdAt), toDate(entry.getValue()));
                case "status" -> cb.equal(root.get(Order_.status), toOrderStatus(entry.getValue()));
                case "id", "orderid" -> cb.equal(root.get(Order_.id), toLong(entry.getValue()));
                case "serviceid" -> {
                    Join<SubService, Service> serviceJoin = root.join(Order_.subService).join(SubService_.service);
                    yield cb.equal(serviceJoin.get(Service_.id), toLong(entry.getValue()));
                }
                case "servicename" -> {
                    Join<SubService, Service> serviceJoin = root.join(Order_.subService).join(SubService_.service);
                    yield cb.equal(serviceJoin.get(Service_.name), entry.getValue());
                }
                case "subserviceid" -> {
                    Join<Order, SubService> subServiceJoin = root.join(Order_.subService);
                    yield cb.equal(subServiceJoin.get(SubService_.id), toLong(entry.getValue()));
                }
                //Set order by
                case "order" -> switch (entry.getValue().toLowerCase()) {
                    case "created", "created.asc" -> {
                        cq.orderBy(cb.asc(root.get(Order_.createdAt)));
                        yield cb.and();
                    }
                    case "created.desc" -> {
                        cq.orderBy(cb.desc(root.get(Order_.createdAt)));
                        yield cb.and();
                    }
                    case "price", "price.asc" -> {
                        cq.orderBy(cb.asc(root.get(Order_.finalPrice)));
                        yield cb.isNotNull(root.get(Order_.finalPrice));
                    }
                    case "price.desc" -> {
                        cq.orderBy(cb.desc(root.get(Order_.finalPrice)));
                        yield cb.isNotNull(root.get(Order_.finalPrice));
                    }
                    default -> throw new CustomIllegalArgumentException("Filter is incorrect.");
                };
                default -> throw new CustomIllegalArgumentException("Filter is incorrect.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    public Specification<Specialist> getSpecialist(Map<String, String> filters) {
        Specification<Specialist> specification = Specification.where(null);
        if (filters == null)
            throw new CustomIllegalArgumentException("Filter is empty.");
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<Specialist> spec = (root, cq, cb) -> switch (entry.getKey().toLowerCase()) {
                case "score.gt" -> cb.ge(root.get(Specialist_.score), toDouble(entry.getValue()));
                case "score.lt" -> cb.le(root.get(Specialist_.score), toDouble(entry.getValue()));
                case "score", "score.eq" -> cb.equal(root.get(Specialist_.score), toDouble(entry.getValue()));
                case "firstname", "firstname.eq" -> cb.equal(root.get(Specialist_.firstName), entry.getValue());
                case "firstname.like" -> cb.like(root.get(Specialist_.firstName), '%' + entry.getValue() + '%');
                case "lastname", "lastname.eq" -> cb.equal(root.get(Specialist_.lastName), entry.getValue());
                case "lastname.like" -> cb.like(root.get(Specialist_.lastName), '%' + entry.getValue() + '%');
                case "username", "username.eq" -> cb.equal(root.get(Specialist_.username), entry.getValue());
                case "email", "email.eq" -> cb.equal(root.get(Specialist_.email), entry.getValue());
                case "email.like" -> cb.like(root.get(Specialist_.email), '%' + entry.getValue() + '%');
                case "subservice", "subserviceid" -> {
                    Join<SubServiceSpecialist, SubService> subServiceJoin =
                            root.join(Specialist_.subServiceSpecialist)
                                    .join(SubServiceSpecialist_.subService);
                    yield cb.equal(subServiceJoin.get(SubService_.id), toDouble(entry.getValue()));
                }
                case "orders", "orders.eq" -> {
                    cq.distinct(true); //Prevent duplicate result.
                    Subquery<Long> subquery = specialistSubqueryForCountOrder(root, cq, cb);
                    yield cb.equal(subquery, toLong(entry.getValue()));
                }
                case "orders.gt" -> {
                    cq.distinct(true);
                    Subquery<Long> subquery = specialistSubqueryForCountOrder(root, cq, cb);
                    yield cb.greaterThanOrEqualTo(subquery, toLong(entry.getValue()));
                }
                case "orders.lt" -> {
                    cq.distinct(true);
                    Subquery<Long> subquery = specialistSubqueryForCountOrder(root, cq, cb);
                    yield cb.lessThanOrEqualTo(subquery, toLong(entry.getValue()));
                }
                case "created" -> {
                    LocalDateTime date = toDate(entry.getValue());
                    LocalDateTime fromDate = timeToZero(date);
                    LocalDateTime toDate = setLastSecondOfDay(fromDate); //Set the last second of the day.
                    yield cb.between(root.get(Specialist_.createdAt), fromDate, toDate);
                }
                case "created.after" -> cb.greaterThanOrEqualTo
                        (root.get(Specialist_.createdAt), toDate(entry.getValue()));
                case "created.before" -> {
                    LocalDateTime date = toDate(entry.getValue());
                    if (isZeroTime(date)) //Inclusive date.
                        date = setLastSecondOfDay(date);
                    yield cb.lessThanOrEqualTo(root.get(Specialist_.createdAt), date);
                }
                default -> throw new CustomIllegalArgumentException("Filter is not correct.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    public Specification<Customer> getCustomer(Map<String, String> filters) {
        if (filters == null || filters.isEmpty())
            return null;
        Specification<Customer> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<Customer> spec = (root, cq, cb) -> switch (entry.getKey().toLowerCase()) {
                case "firstname", "firstname.eq" -> cb.equal(root.get(Customer_.firstName), entry.getValue());
                case "firstname.like" -> cb.like(root.get(Customer_.firstName), '%' + entry.getValue() + '%');
                case "lastname", "lastname.eq" -> cb.equal(root.get(Customer_.lastName), entry.getValue());
                case "lastname.like" -> cb.like(root.get(Customer_.lastName), '%' + entry.getValue() + '%');
                case "username", "username.eq" -> cb.equal(root.get(Customer_.username), entry.getValue());
                case "email", "email.eq" -> cb.equal(root.get(Customer_.email), entry.getValue());
                case "email.like" -> cb.like(root.get(Customer_.email), '%' + entry.getValue() + '%');
                case "orders", "orders.eq" -> {
                    cq.distinct(true);
                    Subquery<Long> subquery = customerSubqueryForCountOrder(root, cq, cb);
                    yield cb.equal(subquery, toLong(entry.getValue()));
                }
                case "orders.gt" -> {
                    cq.distinct(true);
                    Subquery<Long> subquery = customerSubqueryForCountOrder(root, cq, cb);
                    yield cb.greaterThanOrEqualTo(subquery, toLong(entry.getValue()));
                }
                case "orders.lt" -> {
                    cq.distinct(true);
                    Subquery<Long> subquery = customerSubqueryForCountOrder(root, cq, cb);
                    yield cb.lessThanOrEqualTo(subquery, toLong(entry.getValue()));
                }
                case "created.after" ->
                        cb.greaterThanOrEqualTo(root.get(Customer_.createdAt), toDate(entry.getValue()));
                case "created.before" -> cb.lessThanOrEqualTo(root.get(Customer_.createdAt), toDate(entry.getValue()));

                default -> throw new CustomIllegalArgumentException("Filter is not correct.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    private Subquery<Long> specialistSubqueryForCountOrder(Root<Specialist> root,
                                                                  CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<Order> orderRoot = subquery.from(Order.class);
        return subquery.select(cb.count(orderRoot)).where( //For counting orders of this Specialist only.
                cb.equal(orderRoot.get(Order_.specialist).get(Specialist_.id), root.get(Specialist_.id)));
    }

    private Subquery<Long> customerSubqueryForCountOrder(Root<Customer> root,
                                                                CriteriaQuery<?> cq, CriteriaBuilder cb) {
        Subquery<Long> subquery = cq.subquery(Long.class);
        Root<Order> orderRoot = subquery.from(Order.class);
        return subquery.select(cb.count(orderRoot)).where( //For counting orders of this Customer only.
                cb.equal(orderRoot.get(Order_.customer).get(Customer_.id), root.get(Customer_.id)));
    }

    private Long toLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new CustomIllegalArgumentException("Filter is not correct.");
        }
    }

    private Double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new CustomIllegalArgumentException("Filter is not correct.");
        }
    }

    private OrderStatus toOrderStatus(String orderStatus) {
        String[] statuses = orderStatus.split(",");
        List<OrderStatus> orderStatuses = new ArrayList<>();
        try {
            for (String s : statuses) {
                orderStatuses.add(OrderStatus.valueOf(s.toUpperCase()));
            }
            return orderStatuses.get(0);
        } catch (IllegalArgumentException e) {
            throw new CustomIllegalArgumentException("Order status is incorrect.");
        }
    }

    private LocalDateTime toDate(String date) {
        try {
            return LocalDateTime.parse(date);
        } catch (Exception e1) {
            try {
                return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
            } catch (Exception e2) {
                throw new CustomIllegalArgumentException("Date format is invalid.");
            }
        }
    }

    private boolean isZeroTime(LocalDateTime date) {
        return date.getHour() == 0 && date.getMinute() == 0 && date.getSecond() == 0 && date.getNano() == 0;
    }

    private LocalDateTime timeToZero(LocalDateTime date) {
        return date.minusHours(date.getHour()).minusMinutes(date.getMinute())
                .minusSeconds(date.getSecond()).minusNanos(date.getNano());
    }
    private LocalDateTime setLastSecondOfDay(LocalDateTime date) {
        return timeToZero(date).plusDays(1L).minusNanos(1L);
    }
}
