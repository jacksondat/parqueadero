package unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.ceiba.parqueadero.ws.dao.ClienteDAO;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Cliente;
import com.ceiba.parqueadero.ws.model.Moto;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.persistence.entities.ClienteEntity;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;
import com.ceiba.parqueadero.ws.services.AdministradorCeldas;
import com.ceiba.parqueadero.ws.services.impl.AdministradorCeldasCeibaCarros;
import com.ceiba.parqueadero.ws.services.impl.AdministradorCeldasCeibaMotos;
import com.ceiba.parqueadero.ws.services.impl.ParqueaderoCeibaServiceImpl;

public class ParqueaderoCeibaTest {

	private Vehiculo carro;
	private Vehiculo moto;
	
	public static final double VALOR_HORA_MOTO = 500;
	public static final double VALOR_HORA_CARRO = 1000;
	public static final double VALOR_DIA_MOTO = 4000;
	public static final double VALOR_DIA_CARRO = 8000;
	public static final double VALOR_CILINDRAJE_MOTO = 2000;
	
	
	@Before
	public void configuracionBefore() {		
		carro = new Carro();
		carro.setPlaca("TMG900");
		
		moto = new Moto();
		moto.setPlaca("JKM64D");
		moto.setCilindraje(100);
	}
	
	@Test
	public void ingresarVehiculosConPlacasIguales() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		
		try {
			//Act
			parqueaderoService.ingresarCliente(cliente);
			parqueaderoService.ingresarCliente(cliente);
		} catch (VehiculoException e) {
			//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.placaduplicada"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
		
	}
	
	@Test
	public void ingresar21Carros(){
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		ClienteDAO clienteDAO = mock(ClienteDAO.class);
		when(clienteDAO.consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum.CARRO)).thenReturn(new Long(20));
		
		AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			administradorCeldas.ingresarCliente(cliente);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.carro.nodisponibilidad"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
		
	}
	
	@Test
	public void ingresar11Motos(){
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		ClienteDAO clienteDAO = mock(ClienteDAO.class);
		when(clienteDAO.consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum.MOTO)).thenReturn(new Long(10));
		
		AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			administradorCeldas.ingresarCliente(cliente);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.moto.nodisponibilidad"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarCarroPlacaIniciaConLetraAelJueves(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("AJK686");
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		parqueaderoService.setLocalDate(LocalDate.of(2017, 11, 30));
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			parqueaderoService.ingresarCliente(cliente);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.carro.placainvalida"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarCarroPlacaIniciaConLetraAelDomingo(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("AJK687");
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		parqueaderoService.setLocalDate(LocalDate.of(2017, 11, 26));
		
		//Act
		try {
			parqueaderoService.ingresarCliente(cliente);
		//Assert
			Cliente result = parqueaderoService.buscarClienteActivoPorPlaca(carro.getPlaca());
			assertEquals(carro.getPlaca(), result.getVehiculo().getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarCarroPlacaIniciaConLetraAelLunes(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("AJK688");
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		parqueaderoService.setLocalDate(LocalDate.of(2017, 11, 27));
		
		//Act
		try {
			parqueaderoService.ingresarCliente(cliente);
		//Assert
			Cliente result = parqueaderoService.buscarClienteActivoPorPlaca(carro.getPlaca());
			assertEquals(carro.getPlaca(), result.getVehiculo().getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarMotoPlacaIniciaConLetraAelJueves(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("AJK689");
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		parqueaderoService.setLocalDate(LocalDate.of(2017, 11, 30));
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			parqueaderoService.ingresarCliente(cliente);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.carro.placainvalida"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarMotoPlacaIniciaConLetraAelDomingo(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("AJK690");
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		parqueaderoService.setLocalDate(LocalDate.of(2017, 11, 26));
		
		//Act
		try {
			parqueaderoService.ingresarCliente(cliente);
		//Assert
			Cliente result = parqueaderoService.buscarClienteActivoPorPlaca(moto.getPlaca());
			assertEquals(moto.getPlaca(), result.getVehiculo().getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarMotoPlacaIniciaConLetraAelLunes(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("AJK691");
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		ParqueaderoCeibaServiceImpl parqueaderoService = new ParqueaderoCeibaServiceImpl();
		parqueaderoService.setLocalDate(LocalDate.of(2017, 11, 27));
		
		//Act
		try {
			parqueaderoService.ingresarCliente(cliente);
		//Assert
			Cliente result = parqueaderoService.buscarClienteActivoPorPlaca(moto.getPlaca());
			assertEquals(moto.getPlaca(), result.getVehiculo().getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroMenorAUnaHora() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//59min
		int unaHoraEnMilisegundos = 59*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 hora de parqueadero", VALOR_HORA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroA1Hora() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//1hora
		int unaHoraEnMilisegundos = 60*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 hora de parqueadero", VALOR_HORA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroMenorA9Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//6horas con 45min
		int unaHoraEnMilisegundos = 6*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 7 horas de parqueadero", 7*VALOR_HORA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroMayorA9Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//9horas con 45min
		int unaHoraEnMilisegundos = 9*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 dia de parqueadero", VALOR_DIA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroMayorA9Menor24Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//22horas con 45min
		int unaHoraEnMilisegundos = 22*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 dia de parqueadero", VALOR_DIA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroMayorA24HorasMenorA33Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//32horas con 45min
		int unaHoraEnMilisegundos = 32*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 dia y 9 horas de parqueadero", VALOR_DIA_CARRO+9*VALOR_HORA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarCarroMayorA24HorasMenorA48Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(carro);
		
		//40horas con 45min
		int unaHoraEnMilisegundos = 40*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(carro.getPlaca());
		vehiculoEntity.setCilindraje(carro.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(carro.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 2 dias de parqueadero", 2*VALOR_DIA_CARRO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMenorAUnaHora() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//59min
		int unaHoraEnMilisegundos = 59*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 hora de parqueadero", VALOR_HORA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMenorAUnaHoraCilindrajeMayorA500() {
		//Arrange
		moto.setCilindraje(600);
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//59min
		int unaHoraEnMilisegundos = 59*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 hora de parqueadero mas recargo cilidraje", VALOR_HORA_MOTO + VALOR_CILINDRAJE_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoA1Hora() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//1hora
		int unaHoraEnMilisegundos = 60*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 hora de parqueadero", VALOR_HORA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMenorA9Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//6horas con 45min
		int unaHoraEnMilisegundos = 6*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 7 horas de parqueadero", 7*VALOR_HORA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMayorA9Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//9horas con 45min
		int unaHoraEnMilisegundos = 9*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 dia de parqueadero", VALOR_DIA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMayorA9Menor24Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//22horas con 45min
		int unaHoraEnMilisegundos = 22*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 dia de parqueadero", VALOR_DIA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMayorA24HorasMenorA33Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//32horas con 45min
		int unaHoraEnMilisegundos = 32*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 1 dia y 9 horas de parqueadero", VALOR_DIA_MOTO+9*VALOR_HORA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	@Test
	public void retirarMotoMayorA24HorasMenorA48Horas() {
		//Arrange
		Cliente cliente = new Cliente();
		cliente.setVehiculo(moto);
		
		//40horas con 45min
		int unaHoraEnMilisegundos = 40*60*60*1000 + 45*60*1000;
		
		Date fechaIngreso = new Date();
		fechaIngreso.setTime(fechaIngreso.getTime() - unaHoraEnMilisegundos);
		
		VehiculoEntity vehiculoEntity = new VehiculoEntity();
		vehiculoEntity.setPlaca(moto.getPlaca());
		vehiculoEntity.setCilindraje(moto.getCilindraje());
		
		ClienteEntity clienteEntity = new ClienteEntity();
		clienteEntity.setFechaIngreso(fechaIngreso);
		clienteEntity.setVehiculo(vehiculoEntity);
		
		try {
			ClienteDAO clienteDAO = mock(ClienteDAO.class);
			when(clienteDAO.buscarClienteActivoPorPlaca(moto.getPlaca())).thenReturn(clienteEntity);
			
			AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(clienteDAO);
			
			//Act
			cliente = administradorCeldas.retirarCliente(cliente);
			
			//Assert
			assertEquals("Deberia Cobrar 2 dias de parqueadero", 2*VALOR_DIA_MOTO, cliente.getValor(), 0);
		} catch (ClienteException e) {
			fail();
		}
	}
	
	
}
