import {Component, NgModule} from "@angular/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app works!';

  logout() {}
}

@NgModule({
})
export class PizzaPartyAppModule {
}

