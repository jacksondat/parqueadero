package com.ceiba.parqueadero.ws.dao;

import com.ceiba.parqueadero.ws.exceptions.TipoVehiculoException;
import com.ceiba.parqueadero.ws.persistence.entities.TipoVehiculoEntity;

public interface TipoVehiculoDAO {

	TipoVehiculoEntity buscarTipoVehiculoPorDescripcion(String descripcion) throws TipoVehiculoException;

}
