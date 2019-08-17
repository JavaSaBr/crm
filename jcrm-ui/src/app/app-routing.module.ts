import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NoAuthHomeComponent} from './components/no-auth-home/no-auth-home.component';
import {WorkspaceComponent} from './components/workspace/workspace.component';
import {RegisterNewOrganizationComponent} from './components/register-new-organization/register-new-organization.component';
import {AuthenticationComponent} from './components/authentication/authentication.component';
import {ContactsComponent} from './components/contacts/contacts.component';
import {NewContactComponent} from './components/contact/new-contact.component';

const routes: Routes = [
    {path: '', redirectTo: '/' + WorkspaceComponent.COMPONENT_PATH, pathMatch: 'full'},
    {
        path: WorkspaceComponent.COMPONENT_PATH,
        component: WorkspaceComponent,
        children: [
            {path: 'contacts', component: ContactsComponent},
            {path: 'contact/new', component: NewContactComponent},
        ]
    },
    {
        path: NoAuthHomeComponent.COMPONENT_PATH,
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
