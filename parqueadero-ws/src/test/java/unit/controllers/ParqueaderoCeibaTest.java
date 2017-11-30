package unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import com.ceiba.parqueadero.ws.dao.VehiculoDAO;
import com.ceiba.parqueadero.ws.enums.ParqueaderoCompany;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Moto;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.services.AdministradorCeldas;
import com.ceiba.parqueadero.ws.services.ParqueaderoService;
import com.ceiba.parqueadero.ws.services.ParqueaderoServiceFactory;
import com.ceiba.parqueadero.ws.services.impl.AdministradorCeldasCeibaCarros;
import com.ceiba.parqueadero.ws.services.impl.AdministradorCeldasCeibaMotos;
import com.ceiba.parqueadero.ws.services.impl.ParqueaderoCeibaServiceImpl;

public class ParqueaderoCeibaTest {

	private ParqueaderoService parqueaderoService;
	
	@Before
	public void configuracion() {
		ParqueaderoServiceFactory parqueaderoServiceFactory = new ParqueaderoServiceFactory();
		parqueaderoService = parqueaderoServiceFactory.createParqueaderoService(ParqueaderoCompany.CEIBA);
	}
	
	@Test
	public void ingresarVehiculosConPlacasIguales() {
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("TMG900");
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		try {
			//Act
			parqueaderoService.ingresarVehiculo(carro);
			parqueaderoService.ingresarVehiculo(carro);
		} catch (VehiculoException e) {
			//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.placaduplicada"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		}
		
	}
	
	@Test
	public void ingresar21Carros(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("TMG900");
		
		VehiculoDAO vehiculoDAO = mock(VehiculoDAO.class);
		when(vehiculoDAO.consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum.CARRO)).thenReturn(new Long(20));
		
		AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaCarros(vehiculoDAO);
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			administradorCeldas.ingresarVehiculo(carro);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.carro.nodisponibilidad"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		}
		
	}
	
	@Test
	public void ingresar11Motos(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("JKM64D");
		
		VehiculoDAO vehiculoDAO = mock(VehiculoDAO.class);
		when(vehiculoDAO.consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum.MOTO)).thenReturn(new Long(10));
		
		AdministradorCeldas administradorCeldas = new AdministradorCeldasCeibaMotos(vehiculoDAO);
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			administradorCeldas.ingresarVehiculo(moto);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.moto.nodisponibilidad"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarCarroPlacaIniciaConLetraAelJueves(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("AJK686");
		
		parqueaderoService = new ParqueaderoCeibaServiceImpl(LocalDate.of(2017, 11, 30));
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			parqueaderoService.ingresarVehiculo(carro);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.carro.placainvalida"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarCarroPlacaIniciaConLetraAelDomingo(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("AJK687");
		
		parqueaderoService = new ParqueaderoCeibaServiceImpl(LocalDate.of(2017, 11, 26));
		
		//Act
		try {
			parqueaderoService.ingresarVehiculo(carro);
		//Assert
			Vehiculo result = parqueaderoService.buscarVehiculoPorPlaca(carro.getPlaca());
			assertEquals(carro.getPlaca(), result.getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarCarroPlacaIniciaConLetraAelLunes(){
		//Arrange
		Vehiculo carro = new Carro();
		carro.setPlaca("AJK688");
		
		parqueaderoService = new ParqueaderoCeibaServiceImpl(LocalDate.of(2017, 11, 27));
		
		//Act
		try {
			parqueaderoService.ingresarVehiculo(carro);
		//Assert
			Vehiculo result = parqueaderoService.buscarVehiculoPorPlaca(carro.getPlaca());
			assertEquals(carro.getPlaca(), result.getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarMotoPlacaIniciaConLetraAelJueves(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("AJK689");
		
		parqueaderoService = new ParqueaderoCeibaServiceImpl(LocalDate.of(2017, 11, 30));
		
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		//Act
		try {
			parqueaderoService.ingresarVehiculo(moto);
			fail();
		}catch (VehiculoException e) {
		//Assert
			assertEquals(rb.getString("parqueadero.ingresarvehiculo.carro.placainvalida"), e.getMessage());
		} catch (TipoVehiculoException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarMotoPlacaIniciaConLetraAelDomingo(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("AJK690");
		
		parqueaderoService = new ParqueaderoCeibaServiceImpl(LocalDate.of(2017, 11, 26));
		
		//Act
		try {
			parqueaderoService.ingresarVehiculo(moto);
		//Assert
			Vehiculo result = parqueaderoService.buscarVehiculoPorPlaca(moto.getPlaca());
			assertEquals(moto.getPlaca(), result.getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		}
	}
	
	@Test
	public void ingresarMotoPlacaIniciaConLetraAelLunes(){
		//Arrange
		Vehiculo moto = new Moto();
		moto.setPlaca("AJK691");
		
		parqueaderoService = new ParqueaderoCeibaServiceImpl(LocalDate.of(2017, 11, 27));
		
		//Act
		try {
			parqueaderoService.ingresarVehiculo(moto);
		//Assert
			Vehiculo result = parqueaderoService.buscarVehiculoPorPlaca(moto.getPlaca());
			assertEquals(moto.getPlaca(), result.getPlaca());
		}catch (VehiculoException e) {
			fail();
		} catch (TipoVehiculoException e) {
			fail();
		}
	}	
	
}
