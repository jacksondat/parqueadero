import { Component } from '@angular/core';
import {VehicleService} from './vehicle.service';
import {Vehicle} from './Vehicle';

@Component({
  selector: 'search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  resultado: Vehicle = new Vehicle();
  founded = true;
  placaInput = '';

  constructor(private vehicleService: VehicleService) {}

  limpiarResultado() {
    this.resultado = new Vehicle();
    this.founded = true;
  }

  buscar() {
    if (this.placaInput !== '') {
      const vehiculo = {placa: this.placaInput};

      this.limpiarResultado();

      this.vehicleService.buscarVehiculo(vehiculo)
         .subscribe((resultado: Vehicle) => {

            if (resultado.mensaje !== null) {
              this.founded = false;
              this.resultado.placa = this.placaInput;
            }else {
              this.resultado = resultado;
              this.founded = true;
            }
      });
    }
  }

  retirarVehiculo(vehiculo) {
    if (vehiculo.placa !== '') {

      this.limpiarResultado();

      this.vehicleService.retirarVehiculo(vehiculo)
         .subscribe((resultado: Vehicle) => {

           if (resultado.mensaje !== null) {
              console.log(resultado.mensaje);
           }else {
              this.resultado = resultado;
           }
      });
    }
  }

}
