package com.ceiba.parqueadero.ws.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ceiba.parqueadero.ws.dao.VehiculoDAO;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.persistence.connection.JPAConnection;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;

public class VehiculoH2DAOImpl implements VehiculoDAO {
	
	private JPAConnection jpaConnection;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public VehiculoH2DAOImpl(){
		jpaConnection = JPAConnection.getInstance();
	}

	@Override
	public void guardarVehiculo(VehiculoEntity carro) throws VehiculoException {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(carro);
			entityManager.getTransaction().commit();		
		}catch(Exception e){
			throw new VehiculoException("Ocurrio un error al guardar el carro "+carro.getPlaca()+". "+e.getMessage());
		}finally{
			entityManager.close();
		}		
	}

	@Override
	public VehiculoEntity buscarVehiculoPorPlaca(String placa) throws VehiculoException {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		VehiculoEntity vehiculo = null;
		
		try{
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<VehiculoEntity> q = cb.createQuery(VehiculoEntity.class);
	
			Root<VehiculoEntity> vehiculoRoot = q.from(VehiculoEntity.class);
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(vehiculoRoot.get("placa"),placa));
			
			q.select(vehiculoRoot)
				.where(predicates.toArray(new Predicate[]{}));
	
			vehiculo = entityManager.createQuery( q ).getSingleResult();
		}catch(Exception e){
			throw new VehiculoException("No se encontraron vehiculos con placa "+placa+". "+e.getMessage());
		}finally{
			entityManager.close();
		}

		return vehiculo;
	}

}
