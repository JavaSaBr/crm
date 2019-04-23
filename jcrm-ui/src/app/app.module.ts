import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {
    MatAutocompleteModule,
    MatButtonModule, MatCardModule,
    MatDividerModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatSnackBarModule,
    MatStepperModule,
    MatToolbarModule
} from '@angular/material';
import {SideNavComponent} from './components/side-nav/side-nav.component';
import {WorkspaceComponent} from './components/workspace/workspace.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SideMenuService} from './services/side-menu.service';
import {PhoneNumberInput} from './input/phone-number/phone-number-input';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {NoAuthHomeService} from './services/no-auth-home.service';
import {CountryInput} from './input/country/country-input';
import {ErrorService} from './services/error.service';
import {registerLocaleData} from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import localeEn from '@angular/common/locales/en';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {RegisterNewOrganizationComponent} from './components/register-new-organization/register-new-organization.component';
import {NoAuthHomeComponent} from './components/no-auth-home/no-auth-home.component';
import {AuthenticationComponent} from './components/authentication/authentication.component';
import {AutofocusDirective} from './directives/autofocus.directive';

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
        WorkspaceComponent,
        PhoneNumberInput,
        CountryInput,
        RegisterNewOrganizationComponent,
        NoAuthHomeComponent,
        AuthenticationComponent,
        AutofocusDirective
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
        MatCardModule,
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
    providers: [SideMenuService, NoAuthHomeService, ErrorService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
