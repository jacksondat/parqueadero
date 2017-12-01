package com.ceiba.parqueadero.ws.services;

import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Cliente;

public interface AdministradorCeldas {

	void ingresarCliente(Cliente cliente) throws TipoVehiculoException, VehiculoException, ClienteException;

	Cliente retirarCliente(Cliente cliente) throws ClienteException;

}
