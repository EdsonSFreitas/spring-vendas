package org.freitas.vendas.domain.repositorio;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.exceptions.ObjetoNaoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */

@Repository
public class ClienteRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        entityManager.persist(cliente);
        return cliente;
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {
        entityManager.merge(cliente);
        return cliente;
    }

    @Transactional
    public void deletar(Cliente cliente) {
        if (!entityManager.contains(cliente)) {
            cliente = entityManager.merge(cliente);
        }
        entityManager.remove(cliente);
    }

    @Transactional
    public void deletarPorId(Integer id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        deletar(cliente);
    }

    @Transactional(readOnly = true)
    public Optional<List<Cliente>> buscarPorNome(String nome) {
        String jpql = "SELECT c FROM Cliente c WHERE UPPER(c.nome) like UPPER(:nome)";
        final TypedQuery<Cliente> clienteTypedQuery = entityManager.createQuery(jpql, Cliente.class)
                .setParameter("nome", "%" + nome + "%");
        return Optional.of(clienteTypedQuery.getResultList());
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Integer id) {
        try {
            return Optional.of(entityManager.find(Cliente.class, id))
                    .orElseThrow(() -> new ObjetoNaoEncontrado(String.valueOf(id), "Cliente não encontrado"));
        } catch (EmptyResultDataAccessException | IllegalStateException e) {
            throw new ObjetoNaoEncontrado(String.valueOf(id), "Ocorreu erro ao obter o cliente");
        }
    }

    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        String jpql = "SELECT c FROM Cliente c";
        final TypedQuery<Cliente> clienteTypedQuery = entityManager.createQuery(jpql, Cliente.class);
        clienteTypedQuery.setFirstResult((int) pageable.getOffset());
        clienteTypedQuery.setMaxResults(pageable.getPageSize());
        List<Cliente> resultList = clienteTypedQuery.getResultList();
        return new PageImpl<>(resultList, pageable, resultList.size());
    }
}