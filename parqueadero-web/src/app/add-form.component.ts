import { Component } from '@angular/core';
import {Vehicle} from './Vehicle';
import {VehicleService} from './vehicle.service';

@Component({
  selector: 'add-form',
  templateUrl: './add-form.component.html',
  styleUrls: ['./add-form.component.css']
})
export class AddFormComponent {
  placaInput = '';
  resultado: Vehicle = new Vehicle();
  radioCarro = true;
  radioMoto = false;
  successModal = false;
  exceptionModal = false;

  constructor(private vehicleService: VehicleService) {}

  toggleTipoVehiculo() {
    this.radioCarro = !this.radioCarro;
    this.radioMoto = !this.radioMoto;
  }

  resetModal() {
    this.successModal = false;
    this.exceptionModal = false;
  }

  agregarVehiculo() {

    this.resetModal();

    if (this.placaInput !== '') {
      const vehiculo = new Vehicle();
      vehiculo.placa = this.placaInput.toUpperCase();

      if (this.radioCarro) {
        vehiculo.tipoVehiculo = 'carro';
      }else {
        vehiculo.tipoVehiculo = 'moto';
      }

      this.vehicleService.agregarVehiculo(vehiculo)
        .subscribe((resultado: Vehicle) => {

            this.resultado = resultado;

            if (resultado.mensaje !== null) {
              this.exceptionModal = true;
            }else {
              this.successModal = true;
            }
      });
    }
  }
}
