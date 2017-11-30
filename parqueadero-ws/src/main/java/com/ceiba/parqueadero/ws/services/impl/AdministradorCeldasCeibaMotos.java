package com.ceiba.parqueadero.ws.services.impl;

import java.util.Date;
import java.util.ResourceBundle;

import com.ceiba.parqueadero.ws.dao.TipoVehiculoDAO;
import com.ceiba.parqueadero.ws.dao.VehiculoDAO;
import com.ceiba.parqueadero.ws.dao.impl.TipoVehiculoH2DAOImpl;
import com.ceiba.parqueadero.ws.dao.impl.VehiculoH2DAOImpl;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.persistence.entities.TipoVehiculoEntity;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;
import com.ceiba.parqueadero.ws.services.AdministradorCeldas;

public class AdministradorCeldasCeibaMotos implements AdministradorCeldas {

	private VehiculoDAO vehiculoDAO;
	private TipoVehiculoDAO tipoVehiculoDAO;
	
	public AdministradorCeldasCeibaMotos() {
		vehiculoDAO = new VehiculoH2DAOImpl();
		tipoVehiculoDAO = new TipoVehiculoH2DAOImpl();
	}
	
	public AdministradorCeldasCeibaMotos(VehiculoDAO vehiculoDAO) {
		this.vehiculoDAO = vehiculoDAO;
	}
	
	@Override
	public void ingresarVehiculo(Vehiculo vehiculo) throws TipoVehiculoException, VehiculoException {
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		boolean celdasDisponibles = validarDisponibilidadCeldas();
		
		if(celdasDisponibles) {
			VehiculoEntity vehiculoEntity = new VehiculoEntity();
			vehiculoEntity.setPlaca(vehiculo.getPlaca());
			vehiculoEntity.setCilindraje(vehiculo.getCilindraje());
			vehiculoEntity.setFechaIngreso(new Date());
			
			TipoVehiculoEntity tipoVehiculoEntity = tipoVehiculoDAO.buscarTipoVehiculoPorDescripcion(TipoVehiculoEnum.MOTO.getValue());
			
			vehiculoEntity.setTipoVehiculo(tipoVehiculoEntity);
			
			vehiculoDAO.guardarVehiculo(vehiculoEntity);
		} else {
			throw new VehiculoException(rb.getString("parqueadero.ingresarvehiculo.moto.nodisponibilidad"));
		}
	}
	
	private boolean validarDisponibilidadCeldas() {
		boolean celdasDisponibles = false;
		
		long numCeldasOcupadas = vehiculoDAO.consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum.MOTO);
		
		if(numCeldasOcupadas < 10) {
			celdasDisponibles = true;
		}
		
		return celdasDisponibles;
	}

}
