import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NavBarComponent} from './components/nav-bar/nav-bar.component';
import {
    MatDividerModule,
    MatFormFieldModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatSidenavModule,
    MatToolbarModule
} from '@angular/material';
import {SideNavComponent} from './components/side-nav/side-nav.component';
import {HomeComponent} from './pages/home/home.component';
import {FormsModule} from '@angular/forms';
import {SideMenuService} from './services/side-menu.service';
import {UserService} from './services/user.service';

@NgModule({
    declarations: [
        AppComponent,
        NavBarComponent,
        SideNavComponent,
        HomeComponent,
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
        MatGridListModule
    ],
    providers: [SideMenuService, UserService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
