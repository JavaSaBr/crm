import {Component} from "@angular/core";
import {PageComponent} from "../../page.component";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent extends PageComponent{

  constructor() {
    super();
  }
}
