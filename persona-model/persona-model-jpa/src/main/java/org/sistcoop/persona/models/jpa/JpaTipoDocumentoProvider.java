package org.sistcoop.persona.models.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.sistcoop.persona.models.TipoDocumentoModel;
import org.sistcoop.persona.models.TipoDocumentoProvider;
import org.sistcoop.persona.models.enums.TipoPersona;
import org.sistcoop.persona.models.jpa.entities.TipoDocumentoEntity;
import org.sistcoop.persona.models.search.PagingModel;
import org.sistcoop.persona.models.search.SearchCriteriaModel;
import org.sistcoop.persona.models.search.SearchResultsModel;
import org.sistcoop.persona.models.search.filters.TipoDocumentoFilterProvider;

@Named
@Stateless
@Local(TipoDocumentoProvider.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class JpaTipoDocumentoProvider extends AbstractJpaStorage implements TipoDocumentoProvider {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private TipoDocumentoFilterProvider filterProvider;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
    }

    @Override
    public TipoDocumentoModel create(String abreviatura, String denominacion, int cantidadCaracteres,
            TipoPersona tipoPersona) {
        TipoDocumentoEntity tipoDocumentoEntity = new TipoDocumentoEntity();
        tipoDocumentoEntity.setAbreviatura(abreviatura);
        tipoDocumentoEntity.setDenominacion(denominacion);
        tipoDocumentoEntity.setCantidadCaracteres(cantidadCaracteres);
        tipoDocumentoEntity.setTipoPersona(tipoPersona);
        tipoDocumentoEntity.setEstado(true);
        em.persist(tipoDocumentoEntity);
        return new TipoDocumentoAdapter(em, tipoDocumentoEntity);
    }

    @Override
    public TipoDocumentoModel findByAbreviatura(String abreviatura) {
        TypedQuery<TipoDocumentoEntity> query = em.createNamedQuery("TipoDocumentoEntity.findByAbreviatura",
                TipoDocumentoEntity.class);
        query.setParameter("abreviatura", abreviatura);
        List<TipoDocumentoEntity> results = query.getResultList();
        if (results.size() == 0)
            return null;
        return new TipoDocumentoAdapter(em, results.get(0));
    }

    @Override
    public boolean remove(TipoDocumentoModel tipoDocumentoModel) {
        TipoDocumentoEntity tipoDocumentoEntity = em.find(TipoDocumentoEntity.class,
                tipoDocumentoModel.getAbreviatura());
        if (tipoDocumentoEntity == null)
            return false;
        em.remove(tipoDocumentoEntity);
        return true;
    }

    @Override
    public SearchResultsModel<TipoDocumentoModel> search() {
        TypedQuery<TipoDocumentoEntity> query = em.createNamedQuery("TipoDocumentoEntity.findAll",
                TipoDocumentoEntity.class);

        List<TipoDocumentoEntity> entities = query.getResultList();
        List<TipoDocumentoModel> models = new ArrayList<TipoDocumentoModel>();
        for (TipoDocumentoEntity tipoDocumentoEntity : entities) {
            if (tipoDocumentoEntity.isEstado()) {
                models.add(new TipoDocumentoAdapter(em, tipoDocumentoEntity));
            }
        }

        SearchResultsModel<TipoDocumentoModel> result = new SearchResultsModel<>();
        result.setModels(models);
        result.setTotalSize(models.size());
        return result;
    }

    @Override
    public SearchResultsModel<TipoDocumentoModel> search(SearchCriteriaModel criteria) {
        SearchResultsModel<TipoDocumentoEntity> entityResult = find(criteria, TipoDocumentoEntity.class);

        SearchResultsModel<TipoDocumentoModel> modelResult = new SearchResultsModel<>();
        List<TipoDocumentoModel> list = new ArrayList<>();
        for (TipoDocumentoEntity entity : entityResult.getModels()) {
            list.add(new TipoDocumentoAdapter(em, entity));
        }
        modelResult.setTotalSize(entityResult.getTotalSize());
        modelResult.setModels(list);
        return modelResult;
    }

    @Override
    public SearchResultsModel<TipoDocumentoModel> search(SearchCriteriaModel criteria, String filterText) {

        SearchResultsModel<TipoDocumentoEntity> entityResult = findFullText(criteria,
                TipoDocumentoEntity.class, filterText);

        // Hibernate Search
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(TipoDocumentoEntity.class).get();

        Query query = qb.keyword()
                .onFields(filterProvider.getAbreviaturaFilter(), filterProvider.getDenominacionFilter())
                .matching(filterText).createQuery();

        // set criteria
        Session session = em.unwrap(Session.class);
        // Criteria criteriaHibernate =
        // session.createCriteria(TipoDocumentoEntity.class).set;

        // wrap Lucene query in a javax.persistence.Query
        javax.persistence.Query persistenceQuery = fullTextEntityManager.createFullTextQuery(query,
                TipoDocumentoEntity.class)/*
                                           * .setCriteriaQuery(criteriaHibernate)
                                           */
        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        // paging
        PagingModel paging = criteria.getPaging();
        if (paging == null) {
            paging = new PagingModel();
            paging.setPage(1);
            paging.setPageSize(20);
        }
        int page = paging.getPage();
        int pageSize = paging.getPageSize();
        int start = (page - 1) * pageSize;
        persistenceQuery.setFirstResult(start);
        persistenceQuery.setMaxResults(pageSize + 1);

        // sorting

        // execute search
        @SuppressWarnings("unchecked")
        List<TipoDocumentoEntity> resultQuery = persistenceQuery.getResultList();

        List<TipoDocumentoModel> models = new ArrayList<>();
        for (Object object : resultQuery) {
            TipoDocumentoEntity entity = (TipoDocumentoEntity) object;
            models.add(new TipoDocumentoAdapter(em, entity));

        }
        SearchResultsModel<TipoDocumentoModel> result = new SearchResultsModel<>();
        result.setModels(models);
        result.setTotalSize(models.size());

        return result;
    }

}
