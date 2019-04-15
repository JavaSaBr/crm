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
    MatMenuModule, MatRadioModule,
    MatSelectModule,
    MatSidenavModule, MatSnackBarModule,
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
import {PhoneNumberInput} from './input/phone-number/phone-number-input';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {NoAuthHomeService} from './services/no-auth-home.service';
import {PhoneNumberValidator} from './input/phone-number/phone-number-validator';
import {CountryInput} from './input/country/country-input';
import {ErrorService} from './services/error.service';
import {registerLocaleData} from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import localeEn from '@angular/common/locales/en';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

registerLocaleData(localeRu);
registerLocaleData(localeEn);

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

@NgModule({
    declarations: [
        AppComponent,
        NavBarComponent,
        SideNavComponent,
        HomeComponent,
        NoAuthHomeComponent,
        RegisterNewOrganizationComponent,
        PhoneNumberInput,
        CountryInput,
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
        HttpClientModule,
        MatRadioModule,
        MatSnackBarModule,
        TranslateModule.forRoot(
            {
                useDefaultLang: true,
                loader: {
                    provide: TranslateLoader,
                    useFactory: HttpLoaderFactory,
                    deps: [HttpClient]
                }
            }
        ),
    ],
    providers: [SideMenuService, UserService, NoAuthHomeService, ErrorService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
