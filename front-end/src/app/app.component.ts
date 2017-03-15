import {Component, NgModule} from "@angular/core";
import {MaterialModule} from "@angular/material";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app works!';
}

@NgModule({
  imports: [MaterialModule],
})
export class PizzaPartyAppModule {
}
