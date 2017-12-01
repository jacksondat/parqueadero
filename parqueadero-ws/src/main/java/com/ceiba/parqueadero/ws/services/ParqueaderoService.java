package com.ceiba.parqueadero.ws.services;

import com.ceiba.parqueadero.ws.exceptions.ClienteException;
import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.exceptions.VehiculoException;
import com.ceiba.parqueadero.ws.model.Cliente;

public interface ParqueaderoService {

	void ingresarCliente(Cliente cliente) throws VehiculoException, TipoVehiculoException, ClienteException;

	Cliente buscarClienteActivoPorPlaca(String placa) throws ClienteException;

	Cliente retirarClientePorPlaca(String placa) throws ClienteException;

}
