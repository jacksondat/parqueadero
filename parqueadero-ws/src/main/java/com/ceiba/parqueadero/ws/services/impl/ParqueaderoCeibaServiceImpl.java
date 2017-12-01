package com.ceiba.parqueadero.ws.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ceiba.parqueadero.ws.dao.ClienteDAO;
import com.ceiba.parqueadero.ws.dao.impl.ClienteH2DAOImpl;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Cliente;
import com.ceiba.parqueadero.ws.model.Moto;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.persistence.entities.ClienteEntity;
import com.ceiba.parqueadero.ws.services.AdministradorCeldas;
import com.ceiba.parqueadero.ws.services.AdministradorCeldasCeibaFactory;
import com.ceiba.parqueadero.ws.services.ParqueaderoService;

public class ParqueaderoCeibaServiceImpl implements ParqueaderoService {

	private ClienteDAO clienteDAO;
	private AdministradorCeldas administradorCeldas;
	private LocalDate localDate;
	
	
	public static final char INICIAL_PLACA_CONDICIONAL = 'A';
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public ParqueaderoCeibaServiceImpl() {
		clienteDAO = new ClienteH2DAOImpl();
	}
	
	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}
	
	public LocalDate getLocalDate() {
		if(localDate == null) {
			localDate = LocalDate.now();
		}
		return localDate;
	}
	
	public AdministradorCeldas getAdministradorCeldas(Vehiculo vehiculo) {
		if (administradorCeldas == null) {
			AdministradorCeldasCeibaFactory administradorCeldasFactory = new AdministradorCeldasCeibaFactory();
			administradorCeldas = administradorCeldasFactory.createAdministradorCeldas(vehiculo);
		}
		return administradorCeldas;
	}

	@Override
	public void ingresarCliente(Cliente cliente) throws VehiculoException, TipoVehiculoException, ClienteException {
		
		boolean validarPlacaVehiculo = validarPlacaVehiculo(cliente.getVehiculo());
		boolean validarVehiculoDuplicado = validarVehiculoConPlacaDuplicada(cliente.getVehiculo());
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		if (validarPlacaVehiculo && validarVehiculoDuplicado) {
			
			getAdministradorCeldas(cliente.getVehiculo()).ingresarCliente(cliente);
			
		} else if(!validarPlacaVehiculo) {
			throw new VehiculoException(rb.getString("parqueadero.ingresarvehiculo.carro.placainvalida"));
		} else {
			throw new VehiculoException(rb.getString("parqueadero.ingresarvehiculo.placaduplicada"));
		}
	}
	
	private boolean validarPlacaVehiculo(Vehiculo vehiculo) {
		DayOfWeek actualDay = getLocalDate().getDayOfWeek();
		
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
			ClienteEntity clienteEntity = clienteDAO.buscarClienteActivoPorPlaca(vehiculo.getPlaca());
			
			vehiculoPermitido = !vehiculo.getPlaca().equals(clienteEntity.getVehiculo().getPlaca());
			
		} catch (ClienteException e) {
			logger.info(e.getMessage());
		}
		
		return vehiculoPermitido;
	}

	@Override
	public Cliente buscarClienteActivoPorPlaca(String placa) throws ClienteException {
		ClienteEntity clienteEntity = clienteDAO.buscarClienteActivoPorPlaca(placa);
		
		Cliente cliente = null;
		Vehiculo vehiculo = null;
		
		if(clienteEntity.getVehiculo().getTipoVehiculo().getDescripcion().equals(TipoVehiculoEnum.CARRO.getValue())) {
			vehiculo = new Carro();
			vehiculo.setPlaca(clienteEntity.getVehiculo().getPlaca());
		} else if(clienteEntity.getVehiculo().getTipoVehiculo().getDescripcion().equals(TipoVehiculoEnum.MOTO.getValue())) {
			vehiculo = new Moto();
			vehiculo.setCilindraje(clienteEntity.getVehiculo().getCilindraje());
			vehiculo.setPlaca(clienteEntity.getVehiculo().getPlaca());
		}
		
		cliente = new Cliente();
		cliente.setFechaIngreso(clienteEntity.getFechaIngreso());
		cliente.setVehiculo(vehiculo);
		
		return cliente;
	}

	@Override
	public Cliente retirarClientePorPlaca(String placa) throws ClienteException {
		
		Cliente clienteResult = null;
		
		ClienteEntity clienteEntity = clienteDAO.buscarClienteActivoPorPlaca(placa);
		
		Vehiculo vehiculo = null;
		
		if(clienteEntity.getVehiculo().getTipoVehiculo().getDescripcion().equals(TipoVehiculoEnum.CARRO.getValue())) {
			vehiculo = new Carro();
			vehiculo.setPlaca(clienteEntity.getVehiculo().getPlaca());
		} else if(clienteEntity.getVehiculo().getTipoVehiculo().getDescripcion().equals(TipoVehiculoEnum.MOTO.getValue())) {
			vehiculo = new Moto();
			vehiculo.setCilindraje(clienteEntity.getVehiculo().getCilindraje());
			vehiculo.setPlaca(clienteEntity.getVehiculo().getPlaca());
		}
		
		clienteResult = new Cliente();
		clienteResult.setVehiculo(vehiculo);
		
		clienteResult = getAdministradorCeldas(vehiculo).retirarCliente(clienteResult);
		
		return clienteResult;
	}

}
