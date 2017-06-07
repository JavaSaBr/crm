import {Directive, Input} from "@angular/core";
import {AbstractControl, NG_VALIDATORS, Validator} from "@angular/forms";

@Directive({
  selector: '[equalsBiDirection]',
  providers: [{
    provide: NG_VALIDATORS,
    useExisting: EqualsBiDirectionValidatorDirective,
    multi: true
  }]
})
export class EqualsBiDirectionValidatorDirective implements Validator {

  @Input()
  private equalsBiDirection: string;

  validate(control: AbstractControl): { [key: string]: any } {

    let value = control.value;
    let parent = control.parent;
    let targetControl = parent.get(this.equalsBiDirection);

    if (targetControl == null) return null;
    let result = value === targetControl.value;

    if (result !== targetControl.valid) {
      targetControl.reset(targetControl.value);
    }

    return result ? null : {this: {value}};
  }
}


