package com.ceiba.parqueadero.ws.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.ceiba.parqueadero.ws.dto.VehiculoDTO;
import com.ceiba.parqueadero.ws.enums.ParqueaderoCompany;
import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Carro;
import com.ceiba.parqueadero.ws.model.Moto;
import com.ceiba.parqueadero.ws.model.Vehiculo;
import com.ceiba.parqueadero.ws.services.ParqueaderoService;
import com.ceiba.parqueadero.ws.services.ParqueaderoServiceFactory;

@Path("/v1/vehiculos")
public class VehiculoController {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ingresarVehiculo(VehiculoDTO vehiculoDTO) {
		if(vehiculoDTO == null){
			return Response.status(Status.BAD_REQUEST).entity("Agregue un vehiculo.").build();
	    }
	     
	    if(vehiculoDTO.getPlaca() == null) {
	        return Response.status(Status.BAD_REQUEST).entity("Agregue una placa al vehiculo").build();
	    }
	    
	    ParqueaderoServiceFactory parqueaderoServiceFactory = new ParqueaderoServiceFactory();
	    ParqueaderoService parqueaderoService = parqueaderoServiceFactory.createParqueaderoService(ParqueaderoCompany.CEIBA);
	    
	    Vehiculo vehiculo = null;
	    
	    if(vehiculoDTO.getTipoVehiculo().equals(TipoVehiculoEnum.CARRO.getValue())) {
	    	vehiculo = new Carro();
	    	vehiculo.setPlaca(vehiculoDTO.getPlaca());
	    }else if(vehiculoDTO.getTipoVehiculo().equals(TipoVehiculoEnum.MOTO.getValue())) {
	    	vehiculo = new Moto();
	    	vehiculo.setCilindraje(vehiculoDTO.getCilindraje());
	    	vehiculo.setPlaca(vehiculoDTO.getPlaca());
	    }
	    
	    try {
			parqueaderoService.ingresarVehiculo(vehiculo);
			
			return Response.status(Status.CREATED).build();
		} catch (VehiculoException|TipoVehiculoException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
	
	@GET
	@Path("{placa: \\w+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarVehiculoPorPlaca(@PathParam("placa") String placa) {

		ParqueaderoServiceFactory parqueaderoServiceFactory = new ParqueaderoServiceFactory();
	    ParqueaderoService parqueaderoService = parqueaderoServiceFactory.createParqueaderoService(ParqueaderoCompany.CEIBA);
	    
	    Vehiculo vehiculo;
		try {
			vehiculo = parqueaderoService.buscarVehiculoPorPlaca(placa);
			
			VehiculoDTO vehiculoResponse = new VehiculoDTO();
		    vehiculoResponse.setPlaca(vehiculo.getPlaca());
		    vehiculoResponse.setCilindraje(vehiculo.getCilindraje());
		    
		    vehiculoResponse.setTipoVehiculo(parseTipoVehiculo(vehiculo));
			
			return Response.status(Status.ACCEPTED).entity(vehiculoResponse).build();
		} catch (VehiculoException e) {
			return Response.status(Status.ACCEPTED).entity(e.getMessage()).build();
		}
	}
	
	
	private String parseTipoVehiculo(Vehiculo vehiculo){
		String tipoVehiculo = TipoVehiculoEnum.CARRO.getValue();
		
		if(vehiculo instanceof Moto) {
			tipoVehiculo = TipoVehiculoEnum.MOTO.getValue();
		}
		
		return tipoVehiculo;
	}
	
}
