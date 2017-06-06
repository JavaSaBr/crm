import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {AlertModule} from "ngx-bootstrap";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app-routing/app-routing.module";
import {LoginComponent} from "./component/dialog/login/login.component";
import {RegisterComponent} from "./component/dialog/register/register.component";
import {DashboardComponent} from "./component/page/dashboard/dashboard.component";
import {SecurityService} from "./security.service";

import {MdButtonModule, MdInputModule, MdToolbarModule} from "@angular/material";

import "hammerjs";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    DashboardComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
    AlertModule.forRoot(),
    BrowserAnimationsModule,
    MdButtonModule,
    MdInputModule,
    MdToolbarModule
  ],
  providers: [SecurityService],
  bootstrap: [AppComponent]
})
export class AppModule {
}

