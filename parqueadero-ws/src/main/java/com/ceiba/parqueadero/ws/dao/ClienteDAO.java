package com.ceiba.parqueadero.ws.dao;

import com.ceiba.parqueadero.ws.enums.TipoVehiculoEnum;
import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.persistence.entities.ClienteEntity;

public interface ClienteDAO {

	void guardarCliente(ClienteEntity cliente) throws ClienteException;

	ClienteEntity buscarClienteActivoPorPlaca(String placa) throws ClienteException;

	void actualizarCliente(ClienteEntity cliente) throws ClienteException;

	long consultarOcupacionParqueaderoPorTipoVehiculo(TipoVehiculoEnum tipoVehiculo);

}
