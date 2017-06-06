import {Component, OnInit} from "@angular/core";
import {SecurityService} from "./security.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private readonly security: SecurityService) {
  }

  ngOnInit(): void {
  }

  logout() {
  }

  isAuthed(): boolean {
    return this.security.isAuthed();
  }
}
