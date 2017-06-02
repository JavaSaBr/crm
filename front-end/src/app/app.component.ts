import {Component, NgModule} from "@angular/core";
import {AlertModule} from "ngx-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
@NgModule({
  imports: [AlertModule.forRoot()]
})
export class AppComponent {
  logout() {
  }
}
