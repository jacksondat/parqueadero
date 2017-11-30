package com.ceiba.parqueadero.ws.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ceiba.parqueadero.ws.dao.VehiculoDAO;
import com.ceiba.parqueadero.ws.dao.impl.VehiculoH2DAOImpl;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Moto;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;
import com.ceiba.parqueadero.ws.services.AdministradorCeldas;
import com.ceiba.parqueadero.ws.services.AdministradorCeldasCeibaFactory;
import com.ceiba.parqueadero.ws.services.ParqueaderoService;

public class ParqueaderoCeibaServiceImpl implements ParqueaderoService {

	private VehiculoDAO vehiculoDAO;
	private LocalDate localDate;
	
	private final char INICIAL_PLACA_CONDICIONAL = 'A';
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ParqueaderoCeibaServiceImpl() {
		vehiculoDAO = new VehiculoH2DAOImpl();
	}
	
	public ParqueaderoCeibaServiceImpl(LocalDate localDate) {
		this();
		this.localDate = localDate;
	}

	@Override
	public void ingresarVehiculo(Vehiculo vehiculo) throws VehiculoException, TipoVehiculoException {
		
		boolean validarPlacaVehiculo = validarPlacaVehiculo(vehiculo);
		boolean validarVehiculoDuplicado = validarVehiculoConPlacaDuplicada(vehiculo);
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		if (validarPlacaVehiculo && validarVehiculoDuplicado) {
			AdministradorCeldasCeibaFactory administradorCeldasFactory = new AdministradorCeldasCeibaFactory();
			AdministradorCeldas administradorCeldas = administradorCeldasFactory.createAdministradorCeldas(vehiculo);
			
			administradorCeldas.ingresarVehiculo(vehiculo);
		} else if(!validarPlacaVehiculo) {
			throw new VehiculoException(rb.getString("parqueadero.ingresarvehiculo.carro.placainvalida"));
		} else {
			throw new VehiculoException(rb.getString("parqueadero.ingresarvehiculo.placaduplicada"));
		}
	}
	
	private boolean validarPlacaVehiculo(Vehiculo vehiculo) {
		DayOfWeek actualDay = localDate != null ? localDate.getDayOfWeek() : LocalDate.now().getDayOfWeek();
		
		boolean permitirIngreso = true;
		
		if(vehiculo.getPlaca().charAt(0) == INICIAL_PLACA_CONDICIONAL) {
			switch(actualDay) {
			case TUESDAY:
			case WEDNESDAY:
			case THURSDAY:
			case FRIDAY:
			case SATURDAY:
				permitirIngreso = false;
				break;
			default:
				permitirIngreso = true;
				break;
			}
		}
		
		return permitirIngreso;
	}
	
	private boolean validarVehiculoConPlacaDuplicada(Vehiculo vehiculo) {
		
		boolean vehiculoPermitido = true;
		
		try {
			Vehiculo vehiculoResult = buscarVehiculoPorPlaca(vehiculo.getPlaca());
			
			vehiculoPermitido = !vehiculo.getPlaca().equals(vehiculoResult.getPlaca());
			
		} catch (VehiculoException e) {
			logger.info(e.getMessage());
		}
		
		return vehiculoPermitido;
	}

	@Override
	public Vehiculo buscarVehiculoPorPlaca(String placa) throws VehiculoException {
		VehiculoEntity vehiculoEntity = vehiculoDAO.buscarVehiculoPorPlaca(placa);
		
		Vehiculo vehiculo = null;
		
		if(vehiculoEntity.getTipoVehiculo().getDescripcion().equals(TipoVehiculoEnum.CARRO.getValue())) {
			vehiculo = new Carro();
			vehiculo.setPlaca(vehiculoEntity.getPlaca());
		} else if(vehiculoEntity.getTipoVehiculo().getDescripcion().equals(TipoVehiculoEnum.MOTO.getValue())) {
			vehiculo = new Moto();
			vehiculo.setCilindraje(vehiculoEntity.getCilindraje());
			vehiculo.setPlaca(vehiculoEntity.getPlaca());
		}
		
		return vehiculo;
	}

}
