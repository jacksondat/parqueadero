import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import {SearchComponent} from './search.component';
import {AddFormComponent} from './add-form.component';
import {HomeComponent} from './home.component';

import {VehicleService} from './vehicle.service';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    AddFormComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule, FormsModule, HttpClientModule,
    RouterModule.forRoot([
      {path: '', redirectTo: 'inicio', pathMatch: 'full'},
      {path: 'inicio', component: HomeComponent},
      {path: 'search/:placa', component: SearchComponent},
      {path: 'agregar', component: AddFormComponent}
    ])
  ],
  providers: [VehicleService],
  bootstrap: [AppComponent]
})
export class AppModule { }
