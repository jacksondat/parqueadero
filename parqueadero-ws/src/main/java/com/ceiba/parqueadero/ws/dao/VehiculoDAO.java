package com.ceiba.parqueadero.ws.dao;

import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.persistence.entities.VehiculoEntity;

public interface VehiculoDAO {

	void guardarVehiculo(VehiculoEntity carro) throws VehiculoException;

	VehiculoEntity buscarVehiculoPorPlaca(String placa) throws VehiculoException;

}
