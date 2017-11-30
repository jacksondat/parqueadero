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
	public void ingresarVehiculoTest(){
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
}
