package com.ceiba.parqueadero.ws.persistence.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAConnection {
	private static final String H2 = "parqueadero-h2";
	private EntityManagerFactory entityManagerFactory;
	
	private static JPAConnection jpaConnection;
	
	private JPAConnection() {
		initEntityManagerFactory();
	}
	
	public static JPAConnection getInstance(){
		if(jpaConnection == null){
			jpaConnection = new JPAConnection();
		}
		
		return jpaConnection;
	}
	
	public EntityManager createEntityManager() {
		return this.entityManagerFactory.createEntityManager();
	}
	
	public void initEntityManagerFactory(){
		entityManagerFactory = Persistence.createEntityManagerFactory(H2);
	}
}
