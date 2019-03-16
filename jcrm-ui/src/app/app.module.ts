import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {
    MatAutocompleteModule,
    MatButtonModule,
    MatDividerModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatSelectModule,
    MatSidenavModule,
    MatStepperModule,
    MatToolbarModule
} from '@angular/material';
import {SideNavComponent} from './components/side-nav/side-nav.component';
import {HomeComponent} from './pages/home/home.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SideMenuService} from './services/side-menu.service';
import {UserService} from './services/user.service';
import {NoAuthHomeComponent} from './pages/no-auth-home/no-auth-home.component';
import {RegisterNewOrganizationComponent} from './components/register-new-organization/register-new-organization.component';
import {PhoneNumberInput} from './input/phone-number/phone-number-input.component';
import {HttpClientModule} from '@angular/common/http';
import {NoAuthHomeService} from './services/no-auth-home.service';
import {PhoneNumberValidator} from './input/phone-number/phone-number-validator';

@NgModule({
    declarations: [
        AppComponent,
        NavBarComponent,
        SideNavComponent,
        HomeComponent,
        NoAuthHomeComponent,
        RegisterNewOrganizationComponent,
        PhoneNumberInput,
        PhoneNumberValidator
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        MatToolbarModule,
        MatSidenavModule,
        MatDividerModule,
        MatFormFieldModule,
        FormsModule,
        MatInputModule,
        MatIconModule,
        MatListModule,
        MatGridListModule,
        MatButtonModule,
        MatMenuModule,
        MatStepperModule,
        ReactiveFormsModule,
        MatAutocompleteModule,
        MatSelectModule,
        HttpClientModule
    ],
    providers: [SideMenuService, UserService, NoAuthHomeService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
