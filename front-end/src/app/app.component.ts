import {Component, OnInit} from "@angular/core";
import {SecurityService} from "./security.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  static readonly MIN_USERNAME_LENGTH: number = 3;
  static readonly MAX_USERNAME_LENGTH: number = 25;
  static readonly MIN_PASSWORD_LENGTH: number = 6;
  static readonly MAX_PASSWORD_LENGTH: number = 25;

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
