import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NoAuthHomeComponent} from './components/no-auth-home/no-auth-home.component';
import {HomeComponent} from './components/home/home.component';

const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'no-auth', component: NoAuthHomeComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {

}
