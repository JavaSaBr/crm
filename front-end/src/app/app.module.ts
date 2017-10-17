import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AlertModule} from 'ngx-bootstrap';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing/app-routing.module';
import {LoginComponent} from './component/page/login/login.component';
import {CustomerRegisterComponent} from './component/page/customer-register/customer-register.component';
import {DashboardComponent} from './component/page/dashboard/dashboard.component';
import {SecurityService} from './service/security.service';
import {ValidatorModule} from './validator/validator.module';
import {MatButtonModule, MatInputModule, MatToolbarModule} from '@angular/material';

import 'hammerjs';
import {BlankComponent} from './component/blank/blank.component';
import {BlankCustomerRegisterComponent} from './component/blank/blank-customer-register/blank-customer-register.component';
import {BlankService} from './service/blank.service';
import {BlankTokenService} from './service/blank-token.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CustomerRegisterComponent,
    DashboardComponent,
    BlankComponent,
    BlankCustomerRegisterComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    AlertModule.forRoot(),
    BrowserAnimationsModule,
    MatButtonModule,
    MatInputModule,
    MatToolbarModule,
    ValidatorModule
  ],
  providers: [SecurityService, BlankService, BlankTokenService, LoginComponent],
  bootstrap: [AppComponent]
})
export class AppModule {
}

