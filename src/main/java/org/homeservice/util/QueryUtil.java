package org.homeservice.util;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.homeservice.entity.OrderStatus;
import org.homeservice.util.exception.CustomIllegalArgumentException;

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
}
