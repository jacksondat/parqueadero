package com.ceiba.parqueadero.ws.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.ceiba.parqueadero.ws.dao.TipoVehiculoDAO;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.persistence.connection.JPAConnection;
import com.ceiba.parqueadero.ws.persistence.entities.TipoVehiculoEntity;

public class TipoVehiculoH2DAOImpl implements TipoVehiculoDAO {

	private JPAConnection jpaConnection;
	
	public TipoVehiculoH2DAOImpl(){
		jpaConnection = JPAConnection.getInstance();
	}
	
	@Override
	public TipoVehiculoEntity buscarTipoVehiculoPorDescripcion(String descripcion) throws TipoVehiculoException {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		TipoVehiculoEntity tipoVehiculo = null;
		
		try{
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<TipoVehiculoEntity> q = cb.createQuery(TipoVehiculoEntity.class);
	
			Root<TipoVehiculoEntity> tipoVehiculoRoot = q.from(TipoVehiculoEntity.class);
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(tipoVehiculoRoot.get("descripcion"),descripcion));
			
			q.select(tipoVehiculoRoot)
				.where(predicates.toArray(new Predicate[]{}));
	
			tipoVehiculo = entityManager.createQuery( q ).getSingleResult();
		}catch(Exception e){
			throw new TipoVehiculoException("No se encontraron resultados para el tipo de vehiculo "+descripcion+". "+e.getMessage());
		}finally{
			entityManager.close();
		}

		return tipoVehiculo;
	}

}
