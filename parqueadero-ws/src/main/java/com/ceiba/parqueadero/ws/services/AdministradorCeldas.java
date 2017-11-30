package com.ceiba.parqueadero.ws.services;

import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Vehiculo;

public interface AdministradorCeldas {

	void ingresarVehiculo(Vehiculo vehiculo) throws TipoVehiculoException, VehiculoException;

}
