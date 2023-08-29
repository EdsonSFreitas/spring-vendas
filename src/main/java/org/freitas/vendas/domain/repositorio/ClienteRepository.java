package org.freitas.vendas.domain.repositorio;

import org.freitas.vendas.domain.entity.Cliente;
import org.freitas.vendas.exceptions.ObjetoNaoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 29/08/2023
 * {@code @project} vendas
 */

@Repository
public class ClienteRepository {

    private static final String INSERT = "insert into cliente (nome) values (?)";
    private static final String SELECT_ALL = "SELECT * FROM CLIENTE";
    private static final String SELECT_ID = "SELECT * FROM CLIENTE WHERE ID =  ?";
    private static final String SELECT_POR_NOME = "SELECT * FROM CLIENTE WHERE UPPER(NOME) LIKE UPPER(?)";
    private static final String UPDATE = "UPDATE cliente SET nome = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM cliente WHERE id = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Cliente salvar(Cliente cliente) {
        jdbcTemplate.update(INSERT, cliente.getNome());
        return cliente;
    }

    public Cliente atualizar(Cliente cliente) {
        jdbcTemplate.update(UPDATE, cliente.getNome(), cliente.getId());
        return cliente;
    }

    public void deletar(Cliente cliente) {
        deletarPorId(cliente.getId());
    }

    public void deletarPorId(Integer id) {
        jdbcTemplate.update(DELETE, id);
    }

    public Optional<List<Cliente>> buscarPorNome(String nome) {
        String nomePesquisa = "%" + nome.toUpperCase() + "%";
        return Optional.of(jdbcTemplate.query(SELECT_POR_NOME,
                new Object[]{nomePesquisa}, new RowMapper<Cliente>() {
            @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                String nome = rs.getString("nome");
                return new Cliente(id, nome);
            }
        }));
    }


    public Cliente buscarPorId(Integer id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ID, new Object[]{id}, new RowMapper<Cliente>() {
                @Override
                public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Integer clienteId = rs.getInt("id");
                    String nome = rs.getString("nome");
                    return new Cliente(clienteId, nome);
                }
            });
        } catch (EmptyResultDataAccessException | IllegalStateException e) {
            throw new ObjetoNaoEncontrado( String.valueOf(id), "Cliente não encontrado");
        }
    }
















    public List<Cliente> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new RowMapper<Cliente>() {
            @Override
            public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = rs.getInt("id");
                String nome = rs.getString("nome");
                return new Cliente(id, nome);
            }
        });
    }
}