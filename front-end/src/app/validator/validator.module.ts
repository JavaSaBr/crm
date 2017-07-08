import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {EqualsValidatorDirective} from './equal-validator';
import {EqualsBiDirectionValidatorDirective} from './equal-bidirection-validator';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    EqualsValidatorDirective,
    EqualsBiDirectionValidatorDirective
  ],
  exports: [
    EqualsValidatorDirective,
    EqualsBiDirectionValidatorDirective,
    CommonModule
  ]
})
export class ValidatorModule {
}
