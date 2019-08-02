import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NoAuthHomeComponent} from './components/no-auth-home/no-auth-home.component';
import {WorkspaceComponent} from './components/workspace/workspace.component';
import {RegisterNewOrganizationComponent} from './components/register-new-organization/register-new-organization.component';
import {AuthenticationComponent} from './components/authentication/authentication.component';
import {ContactsComponent} from './components/contacts/contacts.component';

const routes: Routes = [
    {path: '', redirectTo: '/workspace', pathMatch: 'full'},
    {
        path: 'workspace',
        component: WorkspaceComponent,
        children: [
            {path: 'clients', component: ContactsComponent},
        ]
    },
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
