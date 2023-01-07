package org.homeservice.util;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.homeservice.entity.*;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class QueryUtil {
    private QueryUtil() {
    }

    //When typedQuery.getSingleResult() not found any entity,
    // This method cached NoResultException was thrown and return null.
    public static <T> T getSingleResult(TypedQuery<T> query) {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public static <X extends Throwable> void checkUpdate(int update, Supplier<? extends X> exception) throws X {
        if (update < 1)
            throw exception.get();
    }

    public static <T> Specification<T> setSpecification(Map<String, String> filters, Class<T> entity) {
        if (filters == null)
            return null;
        Specification<T> specification = Specification.where(null); //Just for initial specification.
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String[] filter = entry.getKey().split("\\.");//firstame.like
            String key = Values.getFilter(filter[0]);
            String method = filter.length == 2 ? filter[1] : "eq";
            //Check this filter is valid. (For example, user can't get to entity with password.)
            specification = specification.and(Specification.where
                    (Values.getSpecification(key, entry.getValue(), method, entity)));
        }
        return specification;
    }

    public static Sort sortBy(String sort) {
        if (sort == null)
            return null;
        return Values.getSort(sort);
    }

    private static class Values {
        static final Map<String, String> filterValues;
        static final Map<String, Sort> sortValues;

        static {
            filterValues = new HashMap<>();
            sortValues = new HashMap<>();
            filterValues.put("firstname", "firstName");
            filterValues.put("lastname", "lastName");
            filterValues.put("score", "score");
            filterValues.put("email", "email");
            filterValues.put("subserviceid", "subServiceId");
            sortValues.put("price", Sort.by(Sort.Direction.ASC, "offerPrice"));
            sortValues.put("specialist", Sort.by(Sort.Direction.DESC, "specialist.score"));
        }

        static <T> Specification<T> getSpecification(String key, String value, String method, Class<T> entity) {
            if (key == null)
                throw new CustomIllegalArgumentException("Filter is not found.");
            return (root, cq, cb) -> {
                switch (key) {
                    case "score" -> {
                        //score only work when filter on Specialist.
                        if (entity.equals(Specialist.class)) {
                            return switch (method) {
                                case "gt" -> cb.gt(root.get(key), toDouble(value));
                                case "lt" -> cb.lt(root.get(key), toDouble(value));
                                case "eq" -> cb.equal(root.get(key), toDouble(value));
                                default -> throw new CustomIllegalArgumentException("Filter is not correct.");
                            };
                        } else throw new CustomIllegalArgumentException("Filter not found.");
                    }
                    case "subServiceId" -> {
                        //subServiceId only work when filter on Specialist.
                        if (entity.equals(Specialist.class)) {
                            Root<Specialist> specialistRoot = cq.from(Specialist.class);
                            Join<SubServiceSpecialist, SubService> subServiceJoin =
                                    specialistRoot.join(Specialist_.subServiceSpecialist)
                                            .join(SubServiceSpecialist_.subService);
                            return cb.equal(subServiceJoin.get(SubService_.id), toDouble(value));
                        } else throw new CustomIllegalArgumentException("Filter not found.");
                    }
                    default -> {
                        return switch (method) {
                            case "like" -> cb.like(root.get(key), '%' + value + '%');
                            case "eq" -> cb.equal(root.get(key), value);
                            default -> throw new CustomIllegalArgumentException("Filter is not correct or not found.");
                        };
                    }
                }
            };
        }

        public static Specification<Order> getOrderSpecification(Map<String, String> filters) {
            Specification<Order> specification = Specification.where(null);
            if (filters == null || filters.isEmpty())
                return null;
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

        static String getFilter(String key) {
            return filterValues.get(key.toLowerCase());
        }

        static Double toDouble(String value) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new CustomIllegalArgumentException("Filter is not correct.");
            }
        }

        static Sort getSort(String key) {
            return sortValues.get(key.toLowerCase());
        }
    }
}
