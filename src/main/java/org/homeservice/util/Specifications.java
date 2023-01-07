package org.homeservice.util;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.homeservice.entity.*;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Map;

public class Specifications {

    public static Specification<Order> getOrder(Map<String, String> filters) {
        if (filters == null || filters.isEmpty())
            return null;
        Specification<Order> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<Order> spec = (root, cq, cb) -> switch (entry.getKey().toLowerCase()) {
                case "customerid" -> cb.equal(root.get(Order_.specialist), toDouble(entry.getValue()));
                case "status" -> {
                    OrderStatus status = OrderStatus.valueOf(entry.getValue());
                    if (status == null)
                        throw new CustomIllegalArgumentException("Order status is not correct");
                    yield cb.equal(root.get(Order_.status), status);
                }
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

    public static Specification<Order> getOrderByAdmin(Map<String, String> filters) {
        if (filters == null || filters.isEmpty())
            return null;
        Specification<Order> specification = Specification.where(null);
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            Specification<Order> spec = (root, cq, cb) -> switch (entry.getKey().toLowerCase()) {
                case "from" -> cb.greaterThanOrEqualTo(root.get(Order_.createdAt), toDate(entry.getValue()));
                case "to" -> cb.lessThanOrEqualTo(root.get(Order_.createdAt), toDate(entry.getValue()));
                case "status" -> {
                    OrderStatus orderStatus = OrderStatus.valueOf(entry.getValue());
                    if (orderStatus == null)
                        throw new CustomIllegalArgumentException("Order status is incorrect.");
                    yield cb.equal(root.get(Order_.status), orderStatus);
                }
                case "serviceid" -> {
                    Join<SubService, Service> serviceJoin = root.join(Order_.subService).join(SubService_.service);
                    yield cb.equal(serviceJoin.get(Service_.id), toLong(entry.getValue()));
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
                        yield cb.and();
                    }
                    case "price.desc" -> {
                        cq.orderBy(cb.desc(root.get(Order_.finalPrice)));
                        yield cb.and();
                    }
                    default -> throw new CustomIllegalArgumentException("Filter is incorrect.");
                };
                default -> throw new CustomIllegalArgumentException("Filter is incorrect.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    public static Specification<Specialist> getSpecialist(Map<String, String> filters) {
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
                case "subserviceid" -> {
                    Join<SubServiceSpecialist, SubService> subServiceJoin =
                            root.join(Specialist_.subServiceSpecialist)
                                    .join(SubServiceSpecialist_.subService);
                    yield cb.equal(subServiceJoin.get(SubService_.id), toDouble(entry.getValue()));
                }
                default -> throw new CustomIllegalArgumentException("Filter is not correct.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    public static Specification<Customer> getCustomer(Map<String, String> filters) {
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
                default -> throw new CustomIllegalArgumentException("Filter is not correct.");
            };
            specification = specification.and(spec);
        }
        return specification;
    }

    static Long toLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new CustomIllegalArgumentException("Filter is not correct.");
        }
    }

    private static Double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new CustomIllegalArgumentException("Filter is not correct.");
        }
    }

    private static LocalDateTime toDate(String date) {
        try {
            return LocalDateTime.parse(date);
        } catch (Exception e) {
            throw new CustomIllegalArgumentException("Date format is invalid.");
        }
    }
}
