package org.sistcoop.persona.models.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.persona.models.search.OrderByModel;
import org.sistcoop.persona.models.search.PagingModel;
import org.sistcoop.persona.models.search.SearchCriteriaFilterModel;
import org.sistcoop.persona.models.search.SearchCriteriaFilterOperator;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;

/**
 * A base class that JPA storage impls can extend.
 *
 * @author eric.wittmann@redhat.com
 */
public abstract class AbstractJpaStorage {

    /**
     * Constructor.
     */
    public AbstractJpaStorage() {

    }

    protected abstract EntityManager getEntityManager();

    protected <T> SearchResultsModel<T> findFullText(SearchCriteriaModel criteria, Class<T> type,
            String filterText) {
        SearchResultsModel<T> results = new SearchResultsModel<>();
        EntityManager entityManager = getEntityManager();

        // Set some default in the case that paging information was not
        // included in the request.
        PagingModel paging = criteria.getPaging();
        if (paging == null) {
            paging = new PagingModel();
            paging.setPage(1);
            paging.setPageSize(20);
        }
        int page = paging.getPage();
        int pageSize = paging.getPageSize();
        int start = (page - 1) * pageSize;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        applySearchCriteriaToQuery(criteria, builder, criteriaQuery, from, false);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(pageSize + 1);
        boolean hasMore = false;

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(type).get();
        Query query = qb.keyword().onFields("").matching(filterText).createQuery();
        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(query, type)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        persistenceQuery.setFirstResult(start);
        persistenceQuery.setMaxResults(pageSize + 1);
        boolean hasMore = false;
        
        // Now query for the actual results
        @SuppressWarnings("unchecked")
        List<T> resultList = persistenceQuery.getResultList();

        // Check if we got back more than we actually needed.
        if (resultList.size() > pageSize) {
            resultList.remove(resultList.size() - 1);
            hasMore = true;
        }

        // If there are more results than we needed, then we will need to do
        // another
        // query to determine how many rows there are in total
        int totalSize = start + resultList.size();
        if (hasMore) {
            totalSize = executeCountQuery(criteria, entityManager, type);
        }
        results.setTotalSize(totalSize);
        results.setModels(resultList);
        return results;
    }

    /**
     * Get a list of entities based on the provided criteria and entity type.
     * 
     * @param criteria
     * @param type
     * @throws StorageException
     *             if a storage problem occurs while storing a bean
     */
    protected <T> SearchResultsModel<T> find(SearchCriteriaModel criteria, Class<T> type) {
        SearchResultsModel<T> results = new SearchResultsModel<>();
        EntityManager entityManager = getEntityManager();

        // Set some default in the case that paging information was not
        // included in the request.
        PagingModel paging = criteria.getPaging();
        if (paging == null) {
            paging = new PagingModel();
            paging.setPage(1);
            paging.setPageSize(20);
        }
        int page = paging.getPage();
        int pageSize = paging.getPageSize();
        int start = (page - 1) * pageSize;

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = builder.createQuery(type);
        Root<T> from = criteriaQuery.from(type);
        applySearchCriteriaToQuery(criteria, builder, criteriaQuery, from, false);
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(start);
        typedQuery.setMaxResults(pageSize + 1);
        boolean hasMore = false;

        // Now query for the actual results
        List<T> resultList = typedQuery.getResultList();

        // Check if we got back more than we actually needed.
        if (resultList.size() > pageSize) {
            resultList.remove(resultList.size() - 1);
            hasMore = true;
        }

        // If there are more results than we needed, then we will need to do
        // another
        // query to determine how many rows there are in total
        int totalSize = start + resultList.size();
        if (hasMore) {
            totalSize = executeCountQuery(criteria, entityManager, type);
        }
        results.setTotalSize(totalSize);
        results.setModels(resultList);
        return results;
    }

    /**
     * Gets a count of the number of rows that would be returned by the search.
     * 
     * @param criteria
     * @param entityManager
     * @param type
     */
    protected <T> int executeCountQuery(SearchCriteriaModel criteria, EntityManager entityManager,
            Class<T> type) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<T> from = countQuery.from(type);
        countQuery.select(builder.count(from));
        applySearchCriteriaToQuery(criteria, builder, countQuery, from, true);
        TypedQuery<Long> query = entityManager.createQuery(countQuery);
        return query.getSingleResult().intValue();
    }

    /**
     * Applies the criteria found in the {@link SearchCriteriaModel} to the JPA
     * query.
     * 
     * @param criteria
     * @param builder
     * @param query
     * @param from
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected <T> void applySearchCriteriaToQuery(SearchCriteriaModel criteria, CriteriaBuilder builder,
            CriteriaQuery<?> query, Root<T> from, boolean countOnly) {

        List<SearchCriteriaFilterModel> filters = criteria.getFilters();
        if (filters != null && !filters.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchCriteriaFilterModel filter : filters) {
                if (filter.getOperator() == SearchCriteriaFilterOperator.eq) {
                    Path<Object> path = from.get(filter.getName());
                    Class<?> pathc = path.getJavaType();
                    if (pathc.isAssignableFrom(String.class)) {
                        predicates.add(builder.equal(path, filter.getValue()));
                    } else if (pathc.isEnum()) {
                        predicates.add(builder.equal(path, Enum.valueOf((Class) pathc, filter.getValue())));
                    } else if (pathc.isAssignableFrom(Date.class)) {
                        predicates.add(builder.equal(from.<Date> get(filter.getName()), filter.getValue()));
                    }
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.bool_eq) {
                    predicates.add(builder.equal(from.<Boolean> get(filter.getName()),
                            Boolean.valueOf(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.gt) {
                    predicates.add(builder.greaterThan(from.<Long> get(filter.getName()),
                            new Long(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.gte) {
                    predicates.add(builder.greaterThanOrEqualTo(from.<Long> get(filter.getName()), new Long(
                            filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.lt) {
                    predicates.add(builder.lessThan(from.<Long> get(filter.getName()),
                            new Long(filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.lte) {
                    predicates.add(builder.lessThanOrEqualTo(from.<Long> get(filter.getName()), new Long(
                            filter.getValue())));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.neq) {
                    predicates.add(builder.notEqual(from.get(filter.getName()), filter.getValue()));
                } else if (filter.getOperator() == SearchCriteriaFilterOperator.like) {
                    predicates.add(builder.like(builder.upper(from.<String> get(filter.getName())), filter
                            .getValue().toUpperCase().replace('*', '%')));
                }
            }
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        OrderByModel orderBy = criteria.getOrderBy();
        if (orderBy != null && !countOnly) {
            if (orderBy.isAscending()) {
                query.orderBy(builder.asc(from.get(orderBy.getName())));
            } else {
                query.orderBy(builder.desc(from.get(orderBy.getName())));
            }
        }
    }

}