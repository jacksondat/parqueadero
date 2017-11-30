package com.ceiba.parqueadero.ws.services;

import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Moto;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.services.impl.AdministradorCeldasCeibaCarros;
import com.ceiba.parqueadero.ws.services.impl.AdministradorCeldasCeibaMotos;

public class AdministradorCeldasCeibaFactory {

	public AdministradorCeldas createAdministradorCeldas(Vehiculo vehiculo) {
		AdministradorCeldas administradorCeldas = null;
		
		if(vehiculo instanceof Carro) {
			administradorCeldas = new AdministradorCeldasCeibaCarros();
		}else if(vehiculo instanceof Moto) {
			administradorCeldas = new AdministradorCeldasCeibaMotos();
		}
		
		return administradorCeldas;
	}

	
	
}
