import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from '@app/component/app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {WorkspaceNavBarComponent} from '@app/component/workspace-nav-bar/workspace-nav-bar.component';
import {WorkspaceSideNavComponent} from '@app/component/workspace-side-nav/workspace-side-nav.component';
import {WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {WorkspaceSideMenuService} from '@app/service/workspace-side-menu.service';
import {PhoneNumberInput} from '@app/input/phone-number/phone-number-input';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {CountryInput} from '@app/input/country/country-input';
import {ErrorService} from '@app/service/error.service';
import {DatePipe, registerLocaleData} from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import localeEn from '@angular/common/locales/en';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {RegisterNewOrganizationComponent} from '@app/component/register-new-organization/register-new-organization.component';
import {NoAuthHomeComponent} from '@app/component/no-auth-home/no-auth-home.component';
import {AuthenticationComponent} from '@app/component/authentication/authentication.component';
import {AutofocusDirective} from '@app/directive/autofocus.directive';
import {ContactsComponent} from '@app/component/contacts/contacts.component';
import {PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule} from 'ngx-perfect-scrollbar';
import {FlexLayoutModule} from '@angular/flex-layout';
import {FabButtonComponent} from '@app/component/fab-button/fab-button.component';
import {RelativeHeightDirective} from '@app/directive/relative-height.directive';
import {ContactWorkspaceComponent} from '@app/component/contact/workspace-component/contact-workspace.component';
import {ContactViewComponent} from '@app/component/contact/view/contact-view.component';
import {UserInput} from '@app/input/user/user-input';
import {UsersInput} from '@app/input/users/users-input';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatNativeDateModule} from '@angular/material/core';
import {ContactEmailsInput} from '@app/input/contact-emails/contact-emails-input';
import {ContactSitesInput} from '@app/input/contact-sites-input/contact-sites-input';
import {UserProfileComponent} from '@app/component/user-profile/user-profile.component';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatSortModule} from '@angular/material/sort';
import {SettingsComponent} from '@app/component/settings/settings.component';
import {GlobalLoadingComponent} from '@app/component/global-loading/global-loading.component';
import {UsersComponent} from '@app/component/users/users.component';
import {UserViewComponent} from '@app/component/user/view/user-view.component';
import {MatToolbarModule} from '@app/node-modules/@angular/material/toolbar';
import {MatSidenavModule} from '@app/node-modules/@angular/material/sidenav';
import {MatFormFieldModule} from '@app/node-modules/@angular/material/form-field';
import {MatInputModule} from '@app/node-modules/@angular/material/input';
import {MatDividerModule} from '@app/node-modules/@angular/material/divider';
import {MatIconModule} from '@app/node-modules/@angular/material/icon';
import {MatListModule} from '@app/node-modules/@angular/material/list';
import {MatGridListModule} from '@app/node-modules/@angular/material/grid-list';
import {MatStepperModule} from '@app/node-modules/@angular/material/stepper';
import {MatMenuModule} from '@app/node-modules/@angular/material/menu';
import {MatButtonModule} from '@app/node-modules/@angular/material/button';
import {MatAutocompleteModule} from '@app/node-modules/@angular/material/autocomplete';
import {MatSelectModule} from '@app/node-modules/@angular/material/select';
import {MatRadioModule} from '@app/node-modules/@angular/material/radio';
import {MatSnackBarModule} from '@app/node-modules/@angular/material/snack-bar';
import {MatCardModule} from '@app/node-modules/@angular/material/card';
import {MatPaginatorModule} from '@app/node-modules/@angular/material/paginator';
import {MatTableModule} from '@app/node-modules/@angular/material/table';
import {MatChipsModule} from '@app/node-modules/@angular/material/chips';
import {MatTabsModule} from '@app/node-modules/@angular/material/tabs';
import {MatDialogModule} from '@app/node-modules/@angular/material/dialog';
import {MatTooltipModule} from '@app/node-modules/@angular/material/tooltip';
import {UserWorkspaceComponent} from '@app/component/user/workspace/user-workspace.component';
import {EntityFieldsViewBlockComponent} from '@app/component/entity-view/block/entity/fields/entity-fields-vew-block.component';
import {PhoneNumbersInput} from '@app/input/phone-numbers/phone-numbers-input';
import {MessengersInput} from '@app/input/messengers/messengers-input';

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
        RegisterNewOrganizationComponent,
        NoAuthHomeComponent,
        AuthenticationComponent,
        FabButtonComponent,
        ContactWorkspaceComponent,
        ContactViewComponent,
        AutofocusDirective,
        RelativeHeightDirective,
        PhoneNumberInput,
        CountryInput,
        UserInput,
        UsersInput,
        ContactEmailsInput,
        ContactSitesInput,
        UserProfileComponent,
        SettingsComponent,
        GlobalLoadingComponent,
        UsersComponent,
        UserViewComponent,
        UserWorkspaceComponent,
        EntityFieldsViewBlockComponent,
        PhoneNumbersInput,
        MessengersInput,
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
        MatTooltipModule,
        MatDialogModule,
        MatTabsModule,
        MatChipsModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatProgressSpinnerModule,
        MatSortModule,
        MatCheckboxModule,
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
        WorkspaceSideMenuService,
        ErrorService,
        {
            provide: PERFECT_SCROLLBAR_CONFIG,
            useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
        },
        DatePipe
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
