import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-blank',
  templateUrl: './blank.component.html',
  styleUrls: ['./blank.component.css']
})
export class BlankComponent implements OnInit {

  static readonly PARAM_TOKEN = 'token';

  private _router: Router;

  private _route: ActivatedRoute;

  constructor(router: Router, route: ActivatedRoute) {
    this._router = router;
    this._route = route;
  }

  ngOnInit() {
  }

  get router(): Router {
    return this._router;
  }

  get route(): ActivatedRoute {
    return this._route;
  }
}
