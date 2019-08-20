import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NoAuthHomeComponent} from '@app/component/no-auth-home/no-auth-home.component';
import {WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {RegisterNewOrganizationComponent} from '@app/component/register-new-organization/register-new-organization.component';
import {AuthenticationComponent} from '@app/component/authentication/authentication.component';
import {ContactsComponent} from '@app/component/contacts/contacts.component';
import {NewContactComponent} from '@app/component/contact/new/new-contact.component';

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
