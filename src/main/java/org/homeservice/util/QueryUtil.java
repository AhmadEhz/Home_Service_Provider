package org.homeservice.util;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.homeservice.entity.OrderStatus;
import org.homeservice.entity.Specialist;
import org.homeservice.util.exception.CustomIllegalArgumentException;
import org.springframework.data.jpa.domain.Specification;

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

    public static<T> Specification<T> setSpecification(Map<String, String> filters) {
        Specification<T> specification = Specification.where(null); //Just for initial specification.
        for (Map.Entry<String, String> entry : filters.entrySet())
            specification = specification.and(Specification.where(
                    (root, cr, cb) -> cb.equal(root.get(entry.getKey()), entry.getValue())));

        return specification;
    }
}
