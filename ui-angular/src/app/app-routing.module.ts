import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NoAuthHomeComponent} from '@app/component/no-auth-home/no-auth-home.component';
import {WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {RegisterNewOrganizationComponent} from '@app/component/register-new-organization/register-new-organization.component';
import {AuthenticationComponent} from '@app/component/authentication/authentication.component';
import {ContactsComponent} from '@app/component/contacts/contacts.component';
import {ContactWorkspaceComponent} from '@app/component/contact/workspace-component/contact-workspace.component';
import {SettingsComponent} from '@app/component/settings/settings.component';
import {UserWorkspaceComponent} from '@app/component/user/workspace/user-workspace.component';
import {UserGroupWorkspaceComponent} from '@app/component/user-group/workspace/user-group-workspace.component';

const routes: Routes = [
    {path: '', redirectTo: '/' + WorkspaceComponent.COMPONENT_NAME, pathMatch: 'full'},
    {
        path: WorkspaceComponent.COMPONENT_NAME,
        component: WorkspaceComponent,
        children: [
            {
                path: ContactsComponent.COMPONENT_NAME,
                component: ContactsComponent
            },
            {
                path: `${ContactWorkspaceComponent.COMPONENT_NAME}/${ContactWorkspaceComponent.NEW_MODE}`,
                component: ContactWorkspaceComponent
            },
            {
                path: `${ContactWorkspaceComponent.COMPONENT_NAME}/${ContactWorkspaceComponent.VIEW_MODE}/:id`,
                component: ContactWorkspaceComponent
            },
            {
                path: SettingsComponent.componentName,
                component: SettingsComponent,
            },
            {
                path: `${SettingsComponent.componentName}/:selectedTab`,
                component: SettingsComponent
            },
            // user
            {
                path: `${SettingsComponent.componentName}/${UserWorkspaceComponent.componentName}/${UserWorkspaceComponent.modeNew}`,
                component: UserWorkspaceComponent
            },
            {
                path: `${SettingsComponent.componentName}/${UserWorkspaceComponent.componentName}/${UserWorkspaceComponent.modeView}/:id`,
                component: UserWorkspaceComponent
            },
            // user-group
            {
                path: `${SettingsComponent.componentName}/${UserGroupWorkspaceComponent.componentName}/${UserGroupWorkspaceComponent.modeNew}`,
                component: UserGroupWorkspaceComponent
            },
            {
                path: `${SettingsComponent.componentName}/${UserGroupWorkspaceComponent.componentName}/${UserGroupWorkspaceComponent.modeView}/:id`,
                component: UserGroupWorkspaceComponent
            },
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
    imports: [RouterModule.forRoot(routes, { relativeLinkResolution: 'legacy' })],
    exports: [RouterModule]
})
export class AppRoutingModule {

}
