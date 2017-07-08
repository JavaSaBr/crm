import {Component} from '@angular/core';
import {BlankComponent} from '../blank.component';
import {CustomerRegisterBlank} from '../../../model/blank/customer-register-blank';
import {ParamMap} from '@angular/router';

@Component({
  selector: 'app-blank-customer-register',
  templateUrl: './blank-customer-register.component.html',
  styleUrls: ['./blank-customer-register.component.css']
})
export class BlankCustomerRegisterComponent extends BlankComponent {

  blank = new CustomerRegisterBlank();

  ngOnInit() {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.blank.token = params.get('token'));
  }
}
