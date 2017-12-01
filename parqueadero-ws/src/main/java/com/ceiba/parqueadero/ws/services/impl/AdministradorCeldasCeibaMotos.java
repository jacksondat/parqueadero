package com.ceiba.parqueadero.ws.services.impl;

import java.util.Date;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ceiba.parqueadero.ws.dao.ClienteDAO;
import com.ceiba.parqueadero.ws.dao.TipoVehiculoDAO;
import com.ceiba.parqueadero.ws.dao.VehiculoDAO;
import com.ceiba.parqueadero.ws.dao.impl.ClienteH2DAOImpl;
import com.ceiba.parqueadero.ws.dao.impl.TipoVehiculoH2DAOImpl;
import com.ceiba.parqueadero.ws.dao.impl.VehiculoH2DAOImpl;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Cliente;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.persistence.entities.ClienteEntity;
import com.ceiba.parqueadero.ws.persistence.entities.TipoVehiculoEntity;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;
import com.ceiba.parqueadero.ws.services.AdministradorCeldas;

public class AdministradorCeldasCeibaMotos implements AdministradorCeldas {

	private VehiculoDAO vehiculoDAO;
	private TipoVehiculoDAO tipoVehiculoDAO;
	private ClienteDAO clienteDAO;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final int CAPACIDAD = 10;
	public static final int MINIMO_HORAS_DIA = 9;
	public static final int MAXIMO_HORAS_DIA = 24;
	public static final int MAXIMO_CILINDRAJE = 500;
	public static final double VALOR_HORA = 500;
	public static final double VALOR_DIA = 4000;
	public static final double ADICION_VALOR_CILINDRAJE = 2000;
	
	public AdministradorCeldasCeibaMotos() {
		vehiculoDAO = new VehiculoH2DAOImpl();
		tipoVehiculoDAO = new TipoVehiculoH2DAOImpl();
		clienteDAO = new ClienteH2DAOImpl();
	}
	
	public AdministradorCeldasCeibaMotos(ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	
	@Override
	public void ingresarCliente(Cliente cliente) throws TipoVehiculoException, VehiculoException, ClienteException {
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		boolean celdasDisponibles = validarDisponibilidadCeldas();
		
		if(celdasDisponibles) {
			
			VehiculoEntity vehiculoEntity = null;
			try {
				vehiculoEntity = vehiculoDAO.buscarVehiculoPorPlaca(cliente.getVehiculo().getPlaca());
			} catch(VehiculoException e) {
				logger.info(e.getMessage());
			}
			
			if(vehiculoEntity == null) {
				vehiculoEntity = new VehiculoEntity();				
				vehiculoEntity.setPlaca(cliente.getVehiculo().getPlaca());
				
				TipoVehiculoEntity tipoVehiculoEntity = tipoVehiculoDAO.buscarTipoVehiculoPorDescripcion(TipoVehiculoEnum.MOTO.getValue());
				
				vehiculoEntity.setTipoVehiculo(tipoVehiculoEntity);
				
				vehiculoDAO.guardarVehiculo(vehiculoEntity);
			}
			
			ClienteEntity clienteEntity = new ClienteEntity();
			clienteEntity.setFechaIngreso(new Date());
			
			vehiculoEntity = vehiculoDAO.buscarVehiculoPorPlaca(cliente.getVehiculo().getPlaca());
			
			clienteEntity.setVehiculo(vehiculoEntity);
			
			clienteDAO.guardarCliente(clienteEntity);
		} else {
			throw new VehiculoException(rb.getString("parqueadero.ingresarvehiculo.moto.nodisponibilidad"));
		}
	}
	
	private boolean validarDisponibilidadCeldas() {
		boolean celdasDisponibles = false;
		
		long numCeldasOcupadas = clienteDAO.consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum.MOTO);
		
		if(numCeldasOcupadas < CAPACIDAD) {
			celdasDisponibles = true;
		}
		
		return celdasDisponibles;
	}

	@Override
	public Cliente retirarCliente(Cliente cliente) throws ClienteException {
		ClienteEntity clienteEntity = clienteDAO.buscarClienteActivoPorPlaca(cliente.getVehiculo().getPlaca());
		
		clienteEntity.setFechaSalida(new Date());
		
		double valor = calcularValorServicioParqueaderoPorTiempo(clienteEntity.getFechaIngreso(), clienteEntity.getFechaSalida());
		valor += calcularAdicionValorPorCilindraje(clienteEntity.getVehiculo().getCilindraje());
		
		clienteEntity.setValor(valor);
		
		clienteDAO.actualizarCliente(clienteEntity);
		
		Vehiculo vehiculo = new Carro();
		vehiculo.setPlaca(clienteEntity.getVehiculo().getPlaca());
		vehiculo.setCilindraje(clienteEntity.getVehiculo().getCilindraje());
		
		cliente.setVehiculo(vehiculo);
		cliente.setFechaIngreso(clienteEntity.getFechaIngreso());
		cliente.setFechaSalida(clienteEntity.getFechaSalida());
		cliente.setValor(clienteEntity.getValor());
		
		return cliente;
	}

	private double calcularAdicionValorPorCilindraje(float cilindraje) {
		double valor = 0;
		
		if(cilindraje > MAXIMO_CILINDRAJE) {
			valor = ADICION_VALOR_CILINDRAJE;
		}
		
		return valor;
	}

	private double calcularValorServicioParqueaderoPorTiempo(Date fechaIngreso, Date fechaSalida) {
		double diferenciaMilisegundosFechas = Double.valueOf(fechaSalida.getTime()) - Double.valueOf(fechaIngreso.getTime());
		double diferenciaHoras = diferenciaMilisegundosFechas / (60 * 60 * 1000);
		
		double horasParqueo = Math.ceil(diferenciaHoras);
		
		double valor;
		
		if(horasParqueo <= MINIMO_HORAS_DIA) {
			valor = horasParqueo * VALOR_HORA;
		} else if(horasParqueo < MAXIMO_HORAS_DIA){
			valor = VALOR_DIA;
		} else {
			
			int numeroDias = (int) horasParqueo / MAXIMO_HORAS_DIA;
			int numeroHorasSobrante = (int) horasParqueo % MAXIMO_HORAS_DIA;
			
			valor = numeroDias * VALOR_DIA;
			
			if(numeroHorasSobrante <= MINIMO_HORAS_DIA) {
				valor += numeroHorasSobrante * VALOR_HORA;
			}else {
				valor += VALOR_DIA;
			}			
		}
		
		return valor;
	}

}
