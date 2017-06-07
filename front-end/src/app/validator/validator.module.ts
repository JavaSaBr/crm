import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {EqualValidatorDirective} from "./equal-validator";

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    EqualValidatorDirective
  ],
  exports: [
    EqualValidatorDirective, CommonModule
  ]
})
export class ValidatorModule {
}
