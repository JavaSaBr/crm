import {NgModule} from '@angular/core';
import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {CommonModule} from '@angular/common';
import {LoginComponent} from '../component/page/login/login.component';
import {CustomerRegisterComponent} from '../component/page/customer-register/customer-register.component';
import {DashboardComponent} from '../component/page/dashboard/dashboard.component';
import {RouteList} from '../util/route-list';
import {BlankCustomerRegisterComponent} from '../component/blank/blank-customer-register/blank-customer-register.component';
import {BlankComponent} from '../component/blank/blank.component';

/**
 * The list of routes of this application.
 */
const routes: Routes = [
  {path: '', redirectTo: '/' + RouteList.PAGE_DASHBOARD, pathMatch: 'full'},
  {path: RouteList.PAGE_DASHBOARD, component: DashboardComponent},
  {path: RouteList.PAGE_LOGIN, component: LoginComponent},
  {path: RouteList.PAGE_CUSTOMER_REGISTER, component: CustomerRegisterComponent},
  {path: RouteList.BLANK_CUSTOMER_REGISTER + '/:' + BlankComponent.PARAM_TOKEN, component: BlankCustomerRegisterComponent},
];

/**
 * The configuration of the router module.
 */
const routerOptions: ExtraOptions = {
  useHash: true
};

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes, routerOptions)
  ],
  exports: [RouterModule],
  declarations: []
})
export class AppRoutingModule {
}
