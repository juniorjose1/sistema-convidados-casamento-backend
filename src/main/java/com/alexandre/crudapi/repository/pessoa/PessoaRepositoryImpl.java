package com.alexandre.crudapi.repository.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.alexandre.crudapi.model.Pessoa;
import com.alexandre.crudapi.model.Pessoa_;
import com.alexandre.crudapi.repository.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	public List<Pessoa> pesquisar(PessoaFilter pessoaFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery criteria = builder.createQuery(Pessoa.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);

		Predicate[] predicates = criarRestricoes(pessoaFilter, builder, root);
		criteria.where(predicates);

		TypedQuery<Pessoa> query = manager.createQuery(criteria);
		return query.getResultList();
	}


	private Predicate[] criarRestricoes(PessoaFilter pessoaFilter, CriteriaBuilder builder, Root<Pessoa> root) {

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (!StringUtils.isEmpty(pessoaFilter.getNome())) {
			predicates.add(builder.like(builder.lower(root.get(Pessoa_.NOME)),
					"%" + pessoaFilter.getNome().toLowerCase() + "%"));
		}

		if (pessoaFilter.getIdade() != null) {
			predicates.add(builder.equal(root.get(Pessoa_.IDADE), pessoaFilter.getIdade()));
		}
		
		if (!StringUtils.isEmpty(pessoaFilter.getSexo())) {
			predicates.add(builder.like(builder.lower(root.get(Pessoa_.SEXO)), 
					"%" + pessoaFilter.getSexo().toLowerCase() + "%"));
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
