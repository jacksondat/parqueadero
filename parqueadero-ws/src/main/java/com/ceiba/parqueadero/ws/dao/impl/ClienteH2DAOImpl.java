package com.ceiba.parqueadero.ws.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ceiba.parqueadero.ws.dao.ClienteDAO;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.persistence.connection.JPAConnection;
import com.ceiba.parqueadero.ws.persistence.entities.ClienteEntity;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;

public class ClienteH2DAOImpl implements ClienteDAO {

	private JPAConnection jpaConnection;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ClienteH2DAOImpl(){
		jpaConnection = JPAConnection.getInstance();
	}
	
	@Override
	public void guardarCliente(ClienteEntity cliente) throws ClienteException {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(cliente);
			entityManager.getTransaction().commit();		
		}catch(Exception e){
			throw new ClienteException("Ocurrio un error al guardar el registro del cliente con placa "+cliente.getVehiculo().getPlaca()+". "+e.getMessage());
		}finally{
			entityManager.close();
		}

	}

	@Override
	public ClienteEntity buscarClienteActivoPorPlaca(String placa) throws ClienteException {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		ClienteEntity cliente = null;
		
		CriteriaBuilder cb;
		CriteriaQuery<ClienteEntity> q;
		try{
			cb = entityManager.getCriteriaBuilder();
			q = cb.createQuery(ClienteEntity.class);
			
			Root<ClienteEntity> clienteRoot = q.from(ClienteEntity.class);
			Join<ClienteEntity, VehiculoEntity> vehiculoJoin = clienteRoot.join("vehiculo");
	
			
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.equal(vehiculoJoin.get("placa"),placa));
			predicates.add(cb.isNull(clienteRoot.get("fechaSalida")));
			
			q.select(clienteRoot)
				.where(predicates.toArray(new Predicate[]{}));
	
			cliente = entityManager.createQuery( q ).setMaxResults(1).getSingleResult();
		}catch(Exception e) {
			throw new ClienteException("No se encontraron vehiculos activos con placa "+placa+". "+e.getMessage());
		}finally{
			entityManager.close();
		}
		
		return cliente;
	}

	@Override
	public void actualizarCliente(ClienteEntity cliente) throws ClienteException {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(cliente);
			entityManager.getTransaction().commit();		
		}catch(Exception e){
			throw new ClienteException("Ocurrio un error al actualizar el registro del cliente con placa "+cliente.getVehiculo().getPlaca()+". "+e.getMessage());
		}finally{
			entityManager.close();
		}
		
	}

	@Override
	public long consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum tipoVehiculo) {
		EntityManager entityManager = jpaConnection.createEntityManager();
		
		long ocupacion = 0;
		
		try{
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> q = cb.createQuery(Long.class);
	
			Root<ClienteEntity> clienteRoot = q.from(ClienteEntity.class);
			Join<ClienteEntity, VehiculoEntity> vehiculoJoin = clienteRoot.join("vehiculo");
			
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cb.isNull(clienteRoot.get("fechaSalida")));
			predicates.add(cb.equal(vehiculoJoin.get("tipoVehiculo").get("descripcion"), tipoVehiculo.getValue()));
			
			q.select(cb.count(clienteRoot))
				.where(predicates.toArray(new Predicate[]{}));
	
			ocupacion = entityManager.createQuery( q ).getSingleResult();
		}catch(Exception e){
			logger.warn("Ocurrio un error consultando la disponibilidad del parqueadero. "+e.getMessage());
		}finally{
			entityManager.close();
		}
		
		return ocupacion;
	}

}
