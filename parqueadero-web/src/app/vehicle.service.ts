import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import {Vehicle} from './Vehicle';
@Injectable()
export class VehicleService {

  constructor(private http: HttpClient) {}

  buscarVehiculo(vehiculo): Observable<Vehicle> {
    return this.http.get('http://localhost:8080/parqueadero-ws/v1/vehiculos/' + vehiculo.placa);
  }

  retirarVehiculo(vehiculo): Observable<Vehicle> {
    return this.http.delete('http://localhost:8080/parqueadero-ws/v1/vehiculos/' + vehiculo.placa);
  }

  agregarVehiculo(vehiculo): Observable<Vehicle> {
    return this.http.post('http://localhost:8080/parqueadero-ws/v1/vehiculos', vehiculo);
  }
}
