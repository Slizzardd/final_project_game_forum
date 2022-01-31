package ua.com.alevel.persistence.crud.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ua.com.alevel.exception.EntityNotFoundException;
import ua.com.alevel.persistence.crud.CrudRepositoryHelper;
import ua.com.alevel.persistence.datatable.DataTableRequest;
import ua.com.alevel.persistence.datatable.DataTableResponse;
import ua.com.alevel.persistence.entity.BaseEntity;
import ua.com.alevel.persistence.repository.BaseRepository;
import ua.com.alevel.persistence.specification.SearchSpecification;
import ua.com.alevel.persistence.specification.SearchSpecificationProcess;

import java.util.Optional;

@Service
public class CrudRepositoryHelperImpl<
        E extends BaseEntity,
        R extends BaseRepository<E>>
        implements CrudRepositoryHelper<E, R> {

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void create(R repository, E entity) {
        repository.save(entity);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(R repository, E entity) {
        checkExist(repository, entity.getId());
        repository.save(entity);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void delete(R repository, Long id) {
        checkExist(repository, id);
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<E> findById(R repository, Long id) {
        checkExist(repository, id);
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DataTableResponse<E> findAll(R repository, DataTableRequest dataTableRequest) {
        int size = dataTableRequest.getSize();
        int page = 0;
        String sortBy = dataTableRequest.getSort();
        String orderBy = dataTableRequest.getOrder();

        Sort sort = orderBy.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<E> pageEntity = repository.findAll(pageRequest);

        return generateDataTableResponse(pageEntity, sortBy, orderBy, dataTableRequest);
    }

    @Override//TODO ИСПРАВИТЬ НАХУ БЛЯТЬ
    @Transactional(readOnly = true)
    public DataTableResponse<E> findAll(R repository, DataTableRequest dataTableRequest, Class<E> entityClass) {
        System.out.println("CrudRepositoryHelperImpl.findAll");
        int size = dataTableRequest.getSize();
        int page = 0;
        String sortBy = dataTableRequest.getSort();
        String orderBy = dataTableRequest.getOrder();

        Sort sort = orderBy.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        SearchSpecification<E> searchSpecification = new SearchSpecificationProcess<>();
        Specification<E> specification = searchSpecification.generateSpecification(dataTableRequest, entityClass);

        Page<E> pageEntity = repository.findAll(specification, pageRequest);

        return generateDataTableResponse(pageEntity, sortBy, orderBy, dataTableRequest);
    }

    private void checkExist(R repository, Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("this entity is not found");
        }
    }

    private DataTableResponse<E> generateDataTableResponse(
            Page<E> pageEntity,
            String sortBy,
            String orderBy,
            DataTableRequest dataTableRequest) {
        DataTableResponse<E> dataTableResponse = new DataTableResponse<>();
        dataTableResponse.setItemsSize(pageEntity.getTotalElements());
        dataTableResponse.setTotalPageSize(pageEntity.getTotalPages());
        dataTableResponse.setItems(pageEntity.getContent());
        dataTableResponse.setOrder(orderBy);
        dataTableResponse.setSort(sortBy);
        dataTableResponse.setPageSize(dataTableRequest.getSize());
        return dataTableResponse;
    }
}
