package org.homeservice.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import org.homeservice.entity.*;
import org.homeservice.entity.Order;
import org.homeservice.util.Specifications;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private final Specifications specifications;
    @PersistenceContext
    private EntityManager em;

    public OrderRepositoryCustomImpl(Specifications specifications) {
        this.specifications = specifications;
    }

    @Override
    public List<Order> findAllWithDetails(Map<String, String> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Order> orderRoot = cq.from(Order.class);
        SetJoin<Order, Bid> bidJoin = orderRoot.join(Order_.bids, JoinType.LEFT);
        Predicate orderPredicate = specifications.getOrder(filters).toPredicate(orderRoot, cq, cb);

        cq.multiselect(orderRoot, bidJoin).where(orderPredicate);
        List<Tuple> resultList = em.createQuery(cq).getResultList();
        List<Bid> bids = resultList.stream().map(tuple -> tuple.get(1, Bid.class)).toList();
        return resultList.stream().map(tuple -> tuple.get(0, Order.class))
                //Set accepted bid for orders.
                .peek(order -> order.setAcceptedBid(bids.stream().filter(Objects::nonNull)
                        .filter(bid -> order.getId().equals(
                        bid.getOrder().getId())).findFirst().orElse(null))).toList();
    }
}
