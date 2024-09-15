package com.iasi.iasi.repository;

import com.iasi.iasi.model.Empresa;

import java.util.List;

public interface EmpresaRepository {
    int save(Empresa book);

    int update(Empresa book);

    Empresa findById(Long id);

    int deleteById(Long id);

    List<Empresa> findAll();

    List<Empresa> findByNameContaining(String nome);

    int deleteAll();
}
