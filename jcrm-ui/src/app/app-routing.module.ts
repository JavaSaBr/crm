import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NoAuthHomeComponent} from './components/no-auth-home/no-auth-home.component';
import {HomeComponent} from './components/home/home.component';
import {RegisterNewOrganizationComponent} from './components/register-new-organization/register-new-organization.component';
import {AuthenticationComponent} from './components/authentication/authentication.component';

const routes: Routes = [
    {path: '', component: HomeComponent},
    {
        path: 'no-auth',
        component: NoAuthHomeComponent,
        children: [
            {path: 'register', component: RegisterNewOrganizationComponent},
            {path: 'auth', component: AuthenticationComponent}
        ]
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}
