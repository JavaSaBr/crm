import {Component, OnInit} from '@angular/core';
import {BlankComponent} from '../blank.component';
import {CustomerRegisterBlank} from '../../../model/blank/customer-register-blank';
import {ParamMap} from '@angular/router';

@Component({
  selector: 'app-blank-customer-register',
  templateUrl: './blank-customer-register.component.html',
  styleUrls: ['./blank-customer-register.component.css']
})
export class BlankCustomerRegisterComponent extends BlankComponent implements OnInit {

  blank = new CustomerRegisterBlank();

  ngOnInit() {
    this.route.paramMap
      .lift((params: ParamMap) => this.blank.token = params.get('token'));
  }
}
