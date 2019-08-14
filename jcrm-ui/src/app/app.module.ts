import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {WorkspaceNavBarComponent} from './components/workspace-nav-bar/workspace-nav-bar.component';
import {
    MatAutocompleteModule,
    MatButtonModule, MatCardModule,
    MatDividerModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule, MatPaginatorModule,
    MatRadioModule,
    MatSelectModule,
    MatSidenavModule,
    MatSnackBarModule,
    MatStepperModule, MatTableModule,
    MatToolbarModule
} from '@angular/material';
import {WorkspaceSideNavComponent} from './components/workspace-side-nav/workspace-side-nav.component';
import {WorkspaceComponent} from './components/workspace/workspace.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {SideMenuService} from './services/side-menu.service';
import {PhoneNumberInput} from './input/phone-number/phone-number-input';
import {HttpClient, HttpClientModule} from '@angular/common/http';
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
import {ContactsComponent} from './components/contacts/contacts.component';
import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FabButtonComponent} from './components/fab-button/fab-button.component';
import {RelativeHeightDirective} from './directives/relative-height.directive';

registerLocaleData(localeRu);
registerLocaleData(localeEn);

export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
};

@NgModule({
    declarations: [
        AppComponent,
        WorkspaceNavBarComponent,
        WorkspaceSideNavComponent,
        WorkspaceComponent,
        ContactsComponent,
        PhoneNumberInput,
        CountryInput,
        RegisterNewOrganizationComponent,
        NoAuthHomeComponent,
        AuthenticationComponent,
        FabButtonComponent,
        AutofocusDirective,
        RelativeHeightDirective,
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
        MatTableModule,
        MatPaginatorModule,
        PerfectScrollbarModule,
        FlexLayoutModule,
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
    providers: [
        SideMenuService,
        ErrorService,
        {
            provide: PERFECT_SCROLLBAR_CONFIG,
            useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
