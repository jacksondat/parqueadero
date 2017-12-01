package integration;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import com.ceiba.parqueadero.ws.dto.VehiculoDTO;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;

public class ParqueaderoTest extends JerseyTest{
	
	@Override
    protected Application configure() {
        return new ResourceConfig().packages("com.ceiba.parqueadero.ws.controllers");
    }
	
	@Test
	public void ingresarCarroTest(){
		//Arrange	
		VehiculoDTO vehiculo = new VehiculoDTO();
		vehiculo.setPlaca("JKM654");
		vehiculo.setTipoVehiculo(TipoVehiculoEnum.CARRO.getValue());
		
		//Act
		Response ingresarVehiculoResponse = target("/v1/vehiculos")
				.request()
				.post(Entity.entity(vehiculo, MediaType.APPLICATION_JSON));
		
		Response consultarVehiculoResponse = target("/v1/vehiculos/"+vehiculo.getPlaca()).request().get();
		VehiculoDTO vehiculoResponse = consultarVehiculoResponse.readEntity(VehiculoDTO.class);
  
		//Assert
		assertEquals(Status.CREATED.getStatusCode(), ingresarVehiculoResponse.getStatus());
		assertEquals(Status.ACCEPTED.getStatusCode(), consultarVehiculoResponse.getStatus());
		assertEquals(vehiculo.getPlaca(), vehiculoResponse.getPlaca());
	}
	
	@Test
	public void ingresarMotoTest(){
		//Arrange	
		VehiculoDTO vehiculo = new VehiculoDTO();
		vehiculo.setPlaca("JKM658");
		vehiculo.setCilindraje(100);
		vehiculo.setTipoVehiculo(TipoVehiculoEnum.MOTO.getValue());
		
		//Act
		Response ingresarVehiculoResponse = target("/v1/vehiculos")
				.request()
				.post(Entity.entity(vehiculo, MediaType.APPLICATION_JSON));
		
		Response consultarVehiculoResponse = target("/v1/vehiculos/"+vehiculo.getPlaca()).request().get();
		VehiculoDTO vehiculoResponse = consultarVehiculoResponse.readEntity(VehiculoDTO.class);
  
		//Assert
		assertEquals(Status.CREATED.getStatusCode(), ingresarVehiculoResponse.getStatus());
		assertEquals(Status.ACCEPTED.getStatusCode(), consultarVehiculoResponse.getStatus());
		assertEquals(vehiculo.getPlaca(), vehiculoResponse.getPlaca());
	}
	
	@Test
	public void retirarCarroTest(){
		//Arrange	
		VehiculoDTO vehiculo = new VehiculoDTO();
		vehiculo.setPlaca("JKM654");
		vehiculo.setTipoVehiculo(TipoVehiculoEnum.CARRO.getValue());
	    
		//Act
		target("/v1/vehiculos").request().post(Entity.entity(vehiculo, MediaType.APPLICATION_JSON));
		
	    Response retirarVehiculoResponse = target("/v1/vehiculos/"+vehiculo.getPlaca())
	            .request()
	            .delete();
	    
	    VehiculoDTO vehiculoResponse = retirarVehiculoResponse.readEntity(VehiculoDTO.class);
	    
	    //Assert
	    assertEquals(Status.ACCEPTED.getStatusCode(), retirarVehiculoResponse.getStatus());
	    assertEquals("Deberia Cobrar 1 hora de parqueadero", 1000, vehiculoResponse.getValor(), 0);
	}
	
	@Test
	public void retirarMotoTest(){
		//Arrange	
		VehiculoDTO vehiculo = new VehiculoDTO();
		vehiculo.setPlaca("JKM658");
		vehiculo.setCilindraje(100);
		vehiculo.setTipoVehiculo(TipoVehiculoEnum.MOTO.getValue());
	    
		//Act
		target("/v1/vehiculos").request().post(Entity.entity(vehiculo, MediaType.APPLICATION_JSON));
		
	    Response retirarVehiculoResponse = target("/v1/vehiculos/"+vehiculo.getPlaca())
	            .request()
	            .delete();
	    
	    VehiculoDTO vehiculoResponse = retirarVehiculoResponse.readEntity(VehiculoDTO.class);
	    
	    //Assert
	    assertEquals(Status.ACCEPTED.getStatusCode(), retirarVehiculoResponse.getStatus());
	    assertEquals("Deberia Cobrar 1 hora de parqueadero", 500, vehiculoResponse.getValor(), 0);
	}
}
