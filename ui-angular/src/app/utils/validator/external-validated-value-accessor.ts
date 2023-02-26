import {AbstractControl, ValidationErrors} from '@angular/forms';

export interface ExternalValidatedValueAccessor {

    externalValidate(control: AbstractControl): ValidationErrors | null
}
