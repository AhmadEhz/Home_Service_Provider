package org.homeservice.util;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
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

    public static <T> Specification<T> setSpecification(Map<String, String> filters) {
        if (filters == null)
            return null;
        Specification<T> specification = Specification.where(null); //Just for initial specification.
        for (Map.Entry<String, String> entry : filters.entrySet())
            //Check this filter is valid. (For example, user can't get to entity with password.)
            if (Values.containsFilter(entry.getKey())) {
                specification = specification.and(Specification.where(
                        (root, cr, cb) -> cb.equal(root.get(Values.getFilter(entry.getKey())), entry.getValue())));
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
            sortValues.put("price", Sort.by(Sort.Direction.ASC, "offerPrice"));
            sortValues.put("specialist", Sort.by(Sort.Direction.DESC, "specialist.score"));
        }

        static String getFilter(String key) {
            return filterValues.get(key.toLowerCase());
        }

        static boolean containsFilter(String key) {
            return filterValues.containsKey(key.toLowerCase());
        }

        static Sort getSort(String key) {
            return sortValues.get(key.toLowerCase());
        }
    }
}
